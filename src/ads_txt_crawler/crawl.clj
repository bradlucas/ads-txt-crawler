(ns ads-txt-crawler.crawl
  (:require [ads-txt-crawler.domains :as d]
            [ads-txt-crawler.process :as p]))

(defn print-url [domain url]
  (let [{:keys [exchange-domain account-id account-type tag-id]} url]
    (println (format "%s,%s,%s,%s,%s" domain exchange-domain account-id account-type tag-id))))

(defn crawl-domain [domain]
  (let [urls (p/process domain)]
    (doseq [url urls]
      (print-url domain, url))))

(defn crawl
  "Read the fname for domains and read each for it's ads.txt file and load the contents into dbname"
  ([fname] (crawl fname nil))
  ([fname dbname]
   (let [domains (d/domains fname)]
     (doseq [domain domains]
       (crawl-domain domain)))))
