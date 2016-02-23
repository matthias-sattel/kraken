(ns kraken.app
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [dommy.core :refer-macros [sel sel1]]
            [dommy.core :as dommy]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :refer [put! chan <! ]]
            [cljs.reader :refer [read-string]]))

                                        ;[sablono.core :as html]))

(defonce app-state
  (atom {:connections
         [{:label "MyPostgresqlConnection"
           :type "PostgreSql"
           :version "9.4"
           :database "users"
           :user "sa"
           :password "#231342"
           },
          {:label "QSSqlServer"
           :type "SqlServer"
           :version "2012"
           :database "invoices"
           :user "sa"
           :password "#fsapfeof"
           }]}))



(defonce app-state (atom {}))

(swap! app-state assoc :header "Hello again!" :text "I like Clojurescript")

(defn connection-view [data owner]
  (reify
    om/IRenderState
    (render-state [this {:keys [delete]}]
      (html [:li (str (:label data)) [:button {:onClick (fn [e] (put! delete @data))}"Delete it"]])
                                        ;(dom/li nil (str (:label data))
                                        ;        (dom/button nil "Delete it"))
      )))

(defn add-connection [data owner new-connection]
  (om/transact! data :connections
                (fn [xs] (vec (conj xs new-connection)))))

(defn connections-view [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:delete (chan)})
    om/IWillMount
    (will-mount [_]
      (let [delete (om/get-state owner :delete)]
        (go (loop []
              (let [connection (<! delete)]
                (om/transact! data :connections
                              (fn [xs] (vec (remove #(= connection %) xs))))
                              (recur))))))
    om/IRenderState
    (render-state [this {:keys [delete]}]
      (dom/div nil
               (dom/h2 nil "Connections list")
               (apply dom/ul nil
                      (om/build-all connection-view (:connections data)
                                    {:init-state {:delete delete}}))
               (html [:div
                      [:input {:type "text" :id "new-connection"}]
                      [:button {:onClick (fn [e]
                                           (let [new-connection (read-string (dommy/value (sel1 :#new-connection)))]
                                             (add-connection data owner new-connection)
                                           ))
                                } "Add Connection"]
                      ])
               ))))


(defn init []
    (om/root
     connections-view
     app-state
     {:target (sel1 :#container)})
)
