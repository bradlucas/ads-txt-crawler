(ns ads-txt-crawler.httpkit
  (:require [org.httpkit.client :as http])
  (:import [java.net URI]
           [javax.net.ssl SNIHostName SNIServerName SSLEngine SSLParameters]))


(defn sni-configure
  [^SSLEngine ssl-engine ^URI uri]
  (let [^SSLParameters ssl-params (.getSSLParameters ssl-engine)]
    (.setServerNames ssl-params [(SNIHostName. (.getHost uri))])
    (.setSSLParameters ssl-engine ssl-params)))

(def client (http/make-client {:ssl-configurer sni-configure}))

(defn fixup-stream [m]
  (if (instance? org.httpkit.BytesInputStream (:body m))
    (let [body (slurp (:body m))]
      (assoc m :body body))
    m))

;; This produces the following error if called directly with http://elpais.com/ads.txt
;; When it redirects to https://elpais.com/ads.txt the following error happens
;; Error: javax.net.ssl.SSLException: Received fatal alert: handshake_failure for http://elpais.com/ads.txt
;; (defn get-url [url]
;;   (try
;;     @(http/get url {:follow-redirects true
;;                     :insecure? true
;;                     :max-redirects 10
;;                     })
;;     (catch java.net.UnknownHostException e1
;;       (.println *err* (format "Invalid host %s" (:message (first (:via e1))))))))

;; To fix the issue
;; Pass the :client above in
;; So this routine tries normally and if there is an error tries the SNI supported call
(defn get-url [url]
  (let [opts {:follow-redirects true :insecure? true :max-redirects 10 :timeout 5000}]
    (let [{:keys [status headers body error] :as resp} @(http/get url opts)]
      (if error
        @(http/get url (assoc opts :client client))
        resp))))
  
