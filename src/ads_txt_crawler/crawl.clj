(ns ads-txt-crawler.crawl
  (:require [ads-txt-crawler.domains :as d]
            [ads-txt-crawler.database :as data]
            [ads-txt-crawler.process :as p]))

(defn print-record [domain record]
  (let [{:keys [exchange-domain account-id account-type tag-id]} record]
    (println (format "%s,%s,%s,%s,%s" domain exchange-domain account-id account-type tag-id))))

;; (defn print-data [domain data]
;;   (doseq [m data]
;;     (print-url domain m)))

;; (defn crawl-domain [domain dbname]
;;   (let [data (p/process domain)]
;;     (if dbname 
;;       (data/save domain data dbname)
;;       (print-data domain data))))

;; (defn crawl-file-opt-database
;;   "Read the fname for domains and read each for it's ads.txt file and load the contents into dbname"
;;   ([fname] (crawl-file-opt-database fname nil))
;;   ([fname dbname]
;;    (let [domains (d/domains fname)]
;;      (doseq [domain domains]
;;        (crawl-domain domain dbname)))))

;; (defn crawl-single-domain
;;   [domain]
;;   (print-data domain (p/process (d/clean-domain-name domain))))


(defn crawl
  "Crawl a list of domains and process them with the output-fn.
  The output-fn should accept a single parameter which is the return map from process/process-to-map

  The domains list can be generated from a file from the domains/domains function.
  Domains from other sources should be pre-processed with domains/clean-domain-name.
  "
  [domains output-fn]
  (doseq [d domains]
    (let [data (p/process-to-map d)]
      (if-let [err (:error data)]
        (println "ERROR: " (:error-message data) (:domain data))
        (output-fn data)))))

(defn print-results
  "Print the results to stdout"
  [data]
  (let [domain (:domain data)
        records (:records data)]
    (doseq [record records]
      (print-record domain record))))

(defn save-results
  "Returns a function that can be called from crawl to save the results to a database."
  [dbname]
  (fn
    [data]
    (let [domain (:domain data)
          records (:records data)]
      (doseq [record records]
        (data/save-record domain record dbname)))))
