(ns ads-txt-crawler.client
  (:require [org.httpkit.client :as http]))

(def url "http://cubshq.com/ads.txt")

(def options  {:follow-redirects true
               :insecure? true
               :max-redirects 100})

(defn example []
  (http/get url options
            (fn [{:keys [status headers body error]}] ;; asynchronous response handling
              (if error
                (println "Failed, exception is " error)
                (println "Async HTTP GET: " status)))))



(def foo {:url url
 :method :get             ; :post :put :head or other
 :user-agent "User-Agent string"
 :oauth-token "your-token"
 :headers {"X-header" "value"
           "X-Api-Version" "2"}
 :query-params {"q" "foo, bar"} ;"Nested" query parameters are also supported
 :form-params {"q" "foo, bar"} ; just like query-params, except sent in the body
 ;; :body (json/encode {"key" "value"}) ; use this for content-type json
 :basic-auth ["user" "pass"]
 :keepalive 3000          ; Keep the TCP connection for 3000ms
 :timeout 1000      ; connection timeout and reading timeout 1000ms
 :filter (http/max-body-filter (* 1024 100)) ; reject if body is more than 100k
 :insecure? true ; Need to contact a server with an untrusted SSL cert?
 
 ;; File upload. :content can be a java.io.File, java.io.InputStream, String
 ;; It read the whole content before send them to server:
 ;; should be used when the file is small, say, a few megabytes
 :multipart [{:name "comment" :content "httpkit's project.clj"}
             {:name "file" :content (clojure.java.io/file "project.clj") :filename "project.clj"}]
 
 :max-redirects 10 ; Max redirects to follow
 ;; whether follow 301/302 redirects automatically, default to true
 ;; :trace-redirects will contain the chain of the redirections followed.
 :follow-redirects false
 })



(defn example-test []
  @(http/get "https://zoeandmorgan.com/media/catalog/product/b/a/base-chakra-n-s-detail.jpg" {:insecure? true}))
