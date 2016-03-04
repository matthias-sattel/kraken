(ns kraken.connection-component
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [dommy.core :refer-macros [sel1]]
            [dommy.core :as dommy]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :refer [put! chan <! ]]
            [cljs.reader :refer [read-string]]))

(defn availability-class [state]
  (if (= state "active")
    "connection-active"
    (if (= state "error")
      "connection-error"
      "connection-warning"))
  )

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

(defn connection-tile [data owner]
  (reify
    om/IRenderState
    (render-state [this {:keys [delete save]}]
      (let [label (str (:label data))
            class-label (str "connection-" label)
            activity-label (availability-class (:state data))
            ]
        (html
         [:div {:class (str "pure-u-1 pure-u-md-1-3 pure-u-lg-1-5 connection-tile " class-label " " activity-label)}
          [:div {:class "pure-u-g connection-list-item"}
           [:div {:class "pure-u-1 pure-u-md-1-2"}
            [:h3 {:class "connection-list-item-header"}
             label]]
           [:div {:class "pure-u-1 pure-u-md-1-2"}
            [:button {:class "pure-button" :onClick (fn [e] (put! delete @data))}
             [:i {:class "fa fa-trash"}]]]
           [:div {:class "pure-u-1"}
            "Type: "
            [:input {:type "text" :readonly "true" :disabled "true" :value (str (:type data))} ]]
           ;(om/build connection-form {} {:init-state {:save save}})
           ]]
         ))
      )))


(defn add-connection [data owner new-connection]
  (om/transact! data :connections
                (fn [xs] (vec (conj xs new-connection)))))



(defn connections [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:delete (chan)
       :save (chan)})
    om/IWillMount
    (will-mount [_]
      (let [delete (om/get-state owner :delete)
            save (om/get-state owner :save)]
        (go (loop []
              (let [connection (<! delete)]
                (om/transact! data :connections
                              (fn [xs] (vec (remove #(= connection %) xs))))
                (recur))))
        (go (loop []
              (let [connection (<! save)]
                (add-connection data owner connection)
                (recur))))
        ))
    om/IRenderState
    (render-state [this {:keys [delete save]}]
      (html [:div {:class "connections-list"
                   :tabIndex "0"
                   ;:onKeyDown (fn [e] (.log js/console (str e)))
                   }
             (om/build-all connection-tile
                           (:connections data)
                           {:init-state {:delete delete
                                         :save save}})
             (om/build connection-form {} {:init-state {:save save}})]
            )
      
      )
    )
  )
