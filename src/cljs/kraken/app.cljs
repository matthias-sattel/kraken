(ns kraken.app
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [dommy.core :refer-macros [sel1]]
            [kraken.connection-component :as connection-component]))

                                        ;[sablono.core :as html]))

(defonce app-state
  (atom {:connections
         [{:label "MyPostgresqlConnection"
           :type "PostgreSql"
           :version "9.4"
           :database "users"
           :user "sa"
           :password "#231342"
           :state "active"
           :host "192.168.11.2"
           :port "5432"
           :ui {:mode :view}
           }
          {:label "QSSqlServer"
           :type "SqlServer"
           :version "2012"
           :database "invoices"
           :user "sa"
           :password "#fsapfeof"
           :state "error"
           :host "sql.mydomain.me"
           :port "2341"
           :ui {:mode :view}
           }
          {:label "MySqlDemo"
           :type "MySqlServer"
           :version "7.4"
           :database "customer"
           :user "admin"
           :password "#cdewif2"
           :state "warning"
           :host "mysql.mydomain.me"
           :port "4532"
           :ui {:mode :view}
           }]
         :dbms
         [{:type "MongoDB"
           :versions [{:version "3.2"}]
           :connection-properties [:host :port :uri :user :password]}
          {:type "PostgreSQL"
           :versions [{:version "9.5"}]
           :connection-properties [:host :port :database :uri :jndi :user :password]}
          ]
         :common-properties [:label :type :state]

         }))



(defonce app-state (atom {}))

(swap! app-state assoc :header "Hello again!" :text "I like Clojurescript")


(defn init []
    (om/root
     connection-component/connections
     app-state
     {:target (sel1 :#container)})
)
