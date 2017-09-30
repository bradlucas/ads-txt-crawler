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

(defn read-domain-file [fname]
  ;; read file and return list of non-commented lines
  ;; - remove commented lines
  ;; - trim leading and trailing whitespace
  ;; - remove http[s]:// prefixes
  ;; - remove www. prefixes
  ;; - lower case
  (with-open [r (clojure.java.io/reader fname)]
    (doall
     (->> (line-seq r)
          (filter ignore-line)
          (map #(-> %
                   (clojure.string/lower-case)
                   (clojure.string/trim)
                   (hostname)
                   (strip-www)
                   ))))))

(defn domains
  "For a given file read it's list of domain names and return them"
  [fname]
  (read-domain-file fname))
