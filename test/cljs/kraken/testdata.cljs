(ns kraken.testdata
  (:require-macros [cljs.test :refer [deftest testing is]
                    ])
  (:require [cljs.test :as t])
  )

(def connections
  [{:label "MyPostgresqlConnection"
    :type "PostgreSql"
    :version "9.4"
    :database "users"
    :user "sa"
    :password "#231342"
    :state "active"
    :host "192.168.11.2"
    :port "5432"
    },
   {:label "QSSqlServer"
    :type "SqlServer"
    :version "2012"
    :database "invoices"
    :user "sa"
    :password "#fsapfeof"
    :state "error"
    :host "sql.mydomain.me"
    :port "2341"
    }
   {:label "MySqlDemo"
    :type "MySqlServer"
    :version "7.4"
    :database "customer"
    :user "admin"
    :password "#cdewif2"
    :state "warning"
    :host "sql.mydomain.me"
    :port "2341"
    }
   ])

(is (some #(= {:label "QSSqlServer"
               :type "SqlServer"
               :version "2012"
               :database "invoices"
               :user "sa"
               :password "#fsapfeof"
               :state "error"
               :host "sql.mydomain.me"
               :port "2341"
               } %)
          connections))
