(ns ads-txt-crawler.process
  (:require [ads-txt-crawler.domains :as d]
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

(defn is-text [url headers]
  (try
    (clojure.string/starts-with? (:content-type headers) "text/plain")
    (catch java.lang.NullPointerException e
      (.println *err* (format "Error: content-type is not text/plain for %s" url)))))

(defn is-error [status]
  (>= status 400))
  
;; (defn read-ads-txt-url [url]
;;   ;; read the contents of url
;;   ;; - ignore non-text returns
;;   ;; - ignore commented lines
;;   ;; - parse lines into map of values
;;   ;; - ignore 
;;   (let [{:keys [status headers body error] :as resp} (h/get-url url)]
;;     (if error
;;       (.println *err* (format "Error: %s for %s" (.toString error) url))
;;       (if status
;;         ;; if 4xx or 5xx status then there is no ads.txt file
;;         (if (is-error status) 
;;           (.println *err* (format "Error: 400/500 level error for %s" url))
;;           ;; if there are no headers then there is an issue
;;           (if (not headers)
;;             (.println *err* (format "Error: headers are blank for %s" url))
;;             ;; the content-type needs to be text/plain
;;             (if (is-text url headers)
;;               (remove nil? (map process-line (clojure.string/split-lines body)))
;;               (.println *err* (format "Error: non-text result for %s" url)))))
;;         (.println *err* (format "Error: Unknown issue calling %s" url))))))
  
;; (defn build-url [domain]
;;   (format "http://%s/ads.txt" domain))

;; (defn process
;;   "For a given domain build it's ads.txt url and return the urls it contains"
;;   [domain]
;;   (read-ads-txt-url (build-url domain)))

(defn process-to-map
  "Return results in a map including a status value"
  [domain]
  (let [url (build-url domain)]
    ;; read the contents of url
    ;; - ignore non-text returns
    ;; - ignore commented lines
    ;; - parse lines into map of values
    ;; - ignore
    (let [rtn {:domain domain}
          {:keys [status headers body error] :as resp} (h/get-url url)]
      (if error
        (assoc rtn :error true :message (format "Error: %s for %s" (.toString error) url))
        (if status
          ;; Error returned from server
          (if (is-error status)
            (assoc rtn :error true :status status :message (format "Error: 400/500 level error for %s" url))
            ;; Issue without having headers
            (if (not headers)
              (assoc rtn :error true :status status (format "Error: headers are blank for %s" url))
              ;; Non-text returned
              (if (not (is-text url headers))
                (assoc rtn :error true :status status :message (format "Error: non-text result for %s" url))
                ;; Valid data. Process and return as data
                (assoc rtn :error false :status status :records (remove nil? (map process-line (clojure.string/split-lines body))))
                )))
          ;; No status, No error
          (assoc rtn :error true :message (format "Error: Unknown issue calling %s" url))
          )))))



