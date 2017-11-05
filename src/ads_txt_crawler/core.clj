(ns ads-txt-crawler.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [ads-txt-crawler.crawl :as c]
            [ads-txt-crawler.domains :as domains]
            [ads-txt-crawler.database :as d]
            )
  (:gen-class))


(def cli-options
  [["-t" "--targets FILE" "List of domains to crawl ads.txt files from"]
   ["-d" "--database FILE" "Database to dump crawled data into"]])

(defn -main [& args]
  (let [opts (parse-opts args cli-options)]
    (let [targets-file (or (:targets (:options opts)))
          database-file (:database (:options opts))
          domains (if targets-file (domains/read-file targets-file) (domains/clean-list args))
          output-fnc (if database-file (c/save-results database-file) c/print-results)]
      (c/crawl domains output-fnc))))

