(ns kraken.app
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [dommy.core :refer-macros [sel sel1]]
            [dommy.core :as dommy]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :refer [pub sub put! chan <! >!]]))

                                        ;[sablono.core :as html]))

(def connections
  [{:label "MyPostgresqlConnection"
    :type "PostgreSql"
    :version "9.4"
    :database "users"
    :user "sa"
    :password "#231342"
    },
   {:label "QASqlServer"
    :type "SqlServer"
    :version "2012"
    :database "invoices"
    :user "sa"
    :password "#fsapfeof"
    }])



(defonce app-state (atom {}))

(swap! app-state assoc :header "Hello again!" :text "I like Clojurescript")

(defn connection-view [data owner]
  (reify
    om/IRender
    (render [this]
      (html [:li (str (:label data)) [:button "Delete it"]])
                                        ;(dom/li nil (str (:label data))
                                        ;        (dom/button nil "Delete it"))
      )))


(defn connections-view [data owner]
  (reify
    om/IRender
    (render [this]
      (dom/div nil
               (dom/h2 nil "Connections list")
               (apply dom/ul nil
                      (om/build-all connection-view (:connections data)))))))


(defn init []
  (let [pub-chan (chan)
        notif-chan (pub pub-chan :selection)]
    (om/root
     connections-view
     {:connections connections}
     {:target (sel1 :#container)})
))
