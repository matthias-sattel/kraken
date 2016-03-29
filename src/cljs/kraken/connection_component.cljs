(ns kraken.connection-component
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [dommy.core :refer-macros [sel1]]
            [dommy.core :as dommy]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :refer [put! chan <! ]]
            [cljs.reader :refer [read-string]]
            [kraken.connection-tile :as tile]))

(defn connection-form [data owner]
  (reify
    om/IRenderState
    (render-state [this {:keys [save]}]
      (html [:div
             [:form {:class "pure-form pure-form-aligned"}
              [:div {:class "pure-control-group"}
               [:label {:for="type"} "Type"]
               [:select {:id "type" :onChange (fn [e] (let [el (sel1 :#type)]
                                                        (.log js/console
                                                              (str (.-value (aget (.-options el) (.-selectedIndex el)))))
                                                        ))}
                [:option "PostgreSql"]
                [:option "SqlServer"]
                [:option "Mongo"]]]
              [:div {:class "pure-control-group"}
               [:label {:for="label"} "Label"]
               [:input {:type "text" :id "new-connection-label"}]]
              [:div {:class "pure-control-group"}
               [:label {:for="host"} "Hostname"]
               [:input {:type "text" :id "new-connection-host"}]]
              [:div {:class "pure-control-group"}
               [:label {:for="port"} "Port"]
               [:input {:type "text" :id "new-connection-port"}]]
              [:div {:class "pure-controls"}
               [:button {:class "pure-button" :type "button"
                         :onClick (fn [e]
                                    (let [el (sel1 :#type)
                                          new-connection {:label (str (dommy/value (sel1 :#new-connection-label)))
                                                          :host (str (dommy/value (sel1 :#new-connection-host)))
                                                          :port (str (dommy/value (sel1 :#new-connection-port)))
                                                          :type (str (.-value (aget (.-options el) (.-selectedIndex el))))}]
                                      
                                      (put! save new-connection)))
                         } "Save"]]]
             ])
      )
    )
  )




(defn add-connection [data new-connection]
  (om/transact! data :connections
                (fn [xs] (vec (conj xs new-connection)))))

(defn update-connection [data old new]
  (om/transact! data :connections
                (fn [xs]
                  (let [index (.indexOf (to-array xs) old)]
                    (assoc xs index new)))))

(defn delete-connection [data connection]
  (om/transact! data :connections
                              (fn [xs] (vec (remove #(= connection %) xs)))))

(defn connections [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:delete (chan)
       :save (chan)
       :edit (chan)
       :update (chan)})
    om/IWillMount
    (will-mount [_]
      (let [delete (om/get-state owner :delete)
            save (om/get-state owner :save)
            edit (om/get-state owner :edit)
            update (om/get-state owner :update)]
        (go (loop []
              (let [connection (<! delete)]
                (delete-connection data connection)
                (recur))))
        (go (loop []
              (let [connection (<! save)]
                (add-connection data connection)
                (recur))))
        (go (loop []
              (let [connection (<! edit)]
                (om/transact! data :connections
                              (fn [xs] (
                                        ;(update-in (:ui (filter #(= (:label connection) %) xs)) [:mode] :edit)
                                        vec (assoc xs (.indexOf (to-array xs) connection) (update-in connection [:ui] {:mode :edit})))))
                (recur))))
        (go (loop []
              (let [connection (<! update)
                    old (:old connection)
                    new (:new connection)]
                (update-connection data old new)
                (recur))))
        ))
    om/IRenderState
    (render-state [this {:keys [delete save edit update]}]
      (let [backend-connection-state (if (= :offline (:state (:backend data)))
                                       "offline"
                                       "online")]
        (html [:div {:class (str "connections-list-" backend-connection-state) 
                     :tabIndex "0"
                                        ;:onKeyDown (fn [e] (.log js/console (str e)))
                     }
               (om/build-all tile/ui-component
                             (:connections data)
                             {:init-state {:delete delete
                                           :save save
                                           :edit edit
                                           :update update}})
               (om/build connection-form {} {:init-state {:save save}})]
              )
        )
      )
    )
  )
