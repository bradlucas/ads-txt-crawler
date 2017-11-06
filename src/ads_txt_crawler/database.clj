(ns ads-txt-crawler.database
  (:require [clojure.java.jdbc :as jdbc]))


(defn db-spec [dbname]
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     dbname
   })

(defn save-record [domain record dbname]
  (let [db-spec (db-spec dbname)]
    (let [{:keys [exchange-domain account-id account-type tag-id comment]} record]
      ;; some ads.txt files have duplicates
      ;; for these we can ignore the exception causes by trying to insert a duplicate
      (try
        (jdbc/insert! db-spec :adstxt {:site_domain domain
                                       :exchange_domain exchange-domain
                                       :seller_account_id account-id
                                       :account_type account-type
                                       :tag_id tag-id
                                       :entry_comment comment})
        (catch org.sqlite.SQLiteException e
          ;; Uncomment if you want to see the duplicates
          ;; (.println *err* (format "Duplicate: %s,%s,%s,%s,%s" domain exchange-domain account-id account-type tag-id))
          )))))



