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
           },
          {:label "QSSqlServer"
           :type "SqlServer"
           :version "2012"
           :database "invoices"
           :user "sa"
           :password "#fsapfeof"
           :state "error"
           }]}))



(defonce app-state (atom {}))

(swap! app-state assoc :header "Hello again!" :text "I like Clojurescript")


(defn init []
    (om/root
     connection-component/connections
     app-state
     {:target (sel1 :#container)})
)
