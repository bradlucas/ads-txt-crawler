(ns ads-txt-crawler.crawl
  (:require [ads-txt-crawler.database :as d]
            [ads-txt-crawler.httpkit :as h]))

(defn clean
  "Clean values by trimming strings and return a blank string to prevent nils from printing"
  [s]
  (if s
    (clojure.string/trim s)
    ""))

(defn parse-line [line]
  ;; Examples
  ;; google.com, pub-1037373295371110, DIRECT #video, banner, native, app
  ;; pubmatic.com, 120658, Direct, 5d62403b186f2ace
  (let [[data comment] (clojure.string/split line #"#")]
    (let [[exchange-domain account-id account-type tag-id] (clojure.string/split (clean data) #",")]
      {:exchange-domain (clean exchange-domain)
       :account-id (clean account-id)
       :account-type (clean account-type)
       :tag-id (clean tag-id)
       :comment (clean comment)
       :data data
       })))

(defn comment-line [line]
  (or
   (clojure.string/starts-with? line "#")
   (clojure.string/blank? line)))

(defn process-line [line]
  ;; ignore if commented out line
  (if (not (comment-line line))
    (parse-line line)))

(defn is-text-content-type
  "Do we have a content-type == text/plain

Note: It was observed that some companies are returning text but not including a Content-Type value.
So, we assume if there is no content-type header that we have text
If there is a content-type header we'll check as before to ensure that it is set to text/plain/."
  [headers]
  (and (contains? headers :content-type)
       (clojure.string/starts-with? (:content-type headers) "text/plain")))


(defn no-content-type-header [headers]
  (not (contains? headers :content-type)))

(defn is-error [status]
  (>= status 400))
  
(defn build-url [domain]
  (format "http://%s/ads.txt" domain))

(defn get-data
  "Read the Ads.txt file for a given domain and return it's data"
  [domain]
  (let [url (build-url domain)]
    ;; read the contents of url
    ;; - ignore non-text returns1
    ;; - ignore commented lines
    ;; - parse lines into map of values
    ;; - ignore
    (let [rtn {:domain domain :url url}     
          {:keys [status headers body error] :as resp} (h/fixup-stream (h/get-url url))]   ;; TODO Consider returning the body to save elsewhere
      (if error
        (assoc rtn :error true :message (format "Error: %s for %s" (.toString error) url))
        (if status
          ;; Error returned from server
          (if (is-error status)
            (assoc rtn :error true :status status :message (format "Error: 400/500 level error for %s" url))
            ;; Issue without having headers
            (if (not headers)
              (assoc rtn :error true :status status (format "Error: headers are blank for %s" url))
              ;; If we have a content-type it needs to be text-plain
              ;; Else if we are missing a content-type let's assume/hope that we have a text
              ;; Note: without a content-type httpkit returns a stream so we need to call fixup-stream after get-url 
              (if (or (is-text-content-type headers) (no-content-type-header headers))
                ;; Valid data. Process and return as data
                (assoc rtn :error false :status status :records (remove nil? (map process-line (clojure.string/split-lines body))))
                ;; Non-text returned
                (assoc rtn :error true :status status :message (format "Error: non-text result for %s" url))
                )))
          ;; No status, No errorbhtt
          (assoc rtn :error true :message (format "Error: Unknown issue calling %s" url)))))))

(defn print-results
  "Print the results to stdout"
  [data]
  (let [domain (:domain data)
        records (:records data)]
    (doseq [record records]
      (let [{:keys [exchange-domain account-id account-type tag-id]} record]
        (println (format "%s,%s,%s,%s,%s" domain exchange-domain account-id account-type tag-id))))))

(defn save-results
  "Returns a function that can be called from crawl to save the results to a database."
  [dbname]
  (fn
    [data]
    (let [domain (:domain data)
          records (:records data)]
      (doseq [record records]
        (d/save-record domain record dbname)))))

(defn crawl
  "Crawl a list of domains to crawl and to then out the results with the output-fnc.
  The output-fn should accept a single parameter which is the return map from process/process-to-map function
  The domains list can be generated from a file from the domains/domains function.
  Domains from other sources should be pre-processed with domains/clean-domain-name."
  [domains output-fnc]
  (doseq [domain domains]
    (let [data (get-data domain)]
      (if-let [err (:error data)]
        (.println *err* (:message data))
        (output-fnc data)))))

