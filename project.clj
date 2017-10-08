(defproject com.bradlucas/ads-txt-crawler "0.0.5"
  :description "An implementation of a crawler for Ads.txt files written in Clojure"
  :url "https://github.com/bradlucas/ads-txt-crawler"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]
                 [clojurewerkz/urly "1.0.0"]
                 [http-kit "2.3.0-alpha4"]
                 [org.clojure/java.jdbc "0.7.2"]
                 [org.xerial/sqlite-jdbc "3.20.0"]]
  :target-path "target/%s"
  :profiles {:uberjar {:uberjar-name "ads-txt-crawler-standalone.jar" :aot :all}}
  :main ^:skip-aot ads-txt-crawler.core)
