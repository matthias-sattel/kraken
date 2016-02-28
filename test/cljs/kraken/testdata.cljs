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
    },
   {:label "QASqlInstance"
    :type "SqlServer"
   :version "2012"
   :database "invoices"
   :user "sa"
   :password "#fsapfeof"
   }])
   
(is (some #(= {:label "QASqlInstance"
                   :type "SqlServer"
                   :version "2012"
                   :database "invoices"
                   :user "sa"
                   :password "#fsapfeof"
                   } %)
              connections))
