(ns ads-txt-crawler.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [ads-txt-crawler.crawl :as c])
  (:gen-class))


;; default 
(def targets-file "./doc/target-domains.txt")


(def cli-options
  ;; domain list file
  [["-t" "--targets FILE" "List of domains to crawl ads.txt files from"]])


(defn -main
  ""
  [& args]
  (let [opts (parse-opts args cli-options)]
    (let [fname (or (:targets (:options opts)) targets-file)
          domain (:domain (:options opts))]
          (if domain
            (c/crawl-domain domain)
            (c/crawl fname)))))
