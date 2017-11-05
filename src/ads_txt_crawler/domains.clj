(ns ads-txt-crawler.domains
  (:require [clojurewerkz.urly.core :refer [url-like as-map]])
  (:gen-class))

(defn strip-www
  "Remove preceeding www. from url"
  [domain]
  (let [[a b] (clojure.string/split domain #"^www.")]
    (if b
      b
      a)))

(defn hostname
  "Parse url into components and return hostname"
  [url]
  (:host (as-map (url-like url))))


(defn ignore-line
  "Lines to ignore are comments starting with # or blank lines"
  [line]
  (not   (or
   (clojure.string/starts-with? line "#")
   (clojure.string/blank? line))))

(defn clean-name
  "Clean a domain name into a proper format:

  - Trim leading and trailing whitespace
  - Remove http[s]:// prefixes
  - Remove www. prefixes
  - Lower case"
  [domain]
  (-> domain
      (clojure.string/lower-case)
      (clojure.string/trim)
      (hostname)
      (strip-www)))

(defn read-file
  "Read file of domaain names. Ignore blank lines and commentted lines. 
  Clean remaining domain names into the proper format"
  [fname]
  (with-open [r (clojure.java.io/reader fname)]
    (doall
     (->> (line-seq r)
          (filter ignore-line)
          (map clean-name)))))

(defn clean-list
  "For a given list of domains clean them into the proper format"
  [domains]
  (map clean-name domains))
