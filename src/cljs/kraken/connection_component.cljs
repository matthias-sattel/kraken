(ns kraken.connection-component
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [dommy.core :refer-macros [sel1]]
            [dommy.core :as dommy]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :refer [put! chan <! ]]
            [cljs.reader :refer [read-string]]))

(defn view [data owner]
  (reify
    om/IRenderState
    (render-state [this {:keys [delete]}]
      (html [:div {:class "pure-u-1 connection-list-item"} [:h3 {:class "connection-list-item-header"} (str (:label data))] [:button {:onClick (fn [e] (put! delete @data))}"Delete it"]])
      )))


(defn add-connection [data owner new-connection]
  (om/transact! data :connections
                (fn [xs] (vec (conj xs new-connection)))))

(defn connections [data owner]
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
      (html [:div
               (html [:h1 "Connections list"])
               (html [:div {:class "pure-g"}
                      (om/build-all view (:connections data)
                                    {:init-state {:delete delete}})])
               (html [:div
                      [:input {:type "text" :id "new-connection"}]
                      [:button {:onClick (fn [e]
                                           (let [new-connection (read-string (dommy/value (sel1 :#new-connection)))]
                                             (add-connection data owner new-connection)
                                           ))
                                } "Add Connection"]
                      ])]
               ))))
