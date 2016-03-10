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
    (render-state [this {:keys [delete save edit update]}]
      (let [label (str (:label data))
            state (str (:state data))
            mode (if (= :view (:mode (:ui data))) {:readOnly "true" :disabled "true"})
            class-label (str "connection-" label)
            activity-label (availability-class (:state data))
            type (str (:type data))
            host (str (:host data))
            port (str (:port data))
            user (str (:user data))
            password (str (:password data))
            ]
        (html
         [:div {:class (str "pure-u-1 pure-u-md-1-3 pure-u-lg-1-5 connection-tile " class-label " " activity-label)}
          [:div {:class "pure-u-g connection-list-item"}
           [:div {:class "pure-u-1 pure-u-md-1-2"}
            [:h3 {:class "connection-list-item-header"}
             label]]
           [:div {:class "pure-u-1 pure-u-md-1-2 connection-menu"}
            [:div {:class "pure-u-g"}
             [:div {:class "pure-u-1-5 connection-menu-item"}
              [:button {:class "pure-button" :onClick (fn [e] (put! delete @data))}
               [:i {:class "fa fa-trash"}]]]
             [:div {:class "pure-u-1-5 connection-menu-item"}
              [:button {:class "pure-button" :onClick (fn [e]
                                        ;(.log js/console (str e))
                                                        (put! edit @data)
                                                        )}
               [:i {:class "fa fa-pencil-square-o"}]]]
             [:div {:class "pure-u-1-5 connection-menu-item"}
              [:button {:class "pure-button" :onClick (fn [e]
                                                        (let [new-connection {:label label
                                                                              :type (dommy/value (sel1 (keyword (str "#" class-label "-type"))))
                                                                              :host (dommy/value (sel1 (keyword (str "#" class-label "-host"))))
                                                                              :port (dommy/value (sel1 (keyword (str "#" class-label "-port"))))
                                                                              :user (dommy/value (sel1 (keyword (str "#" class-label "-user"))))
                                                                              :password (dommy/value (sel1 (keyword (str "#" class-label "-password"))))
                                                                              :state state
                                                                              :ui {:mode :view}
                                                                             }]
                                                          (put! update {:old @data :new new-connection})))}
               [:i {:class "fa fa-floppy-o"}]]]
             ]]
           [:div {:class "pure-u-1"}
            [:div {:class "pure-u-g"}
             [:div {:class "pure-u-1-2"}
              "Type: "]
             [:div {:class "pure-u-1-2"}
              [:input (merge {:type "text" :value type :id (str class-label "-type") :onChange (fn [e] ())} mode) ]]
             [:div {:class "pure-u-1-2"}
              "Hostname: "]
             [:div {:class "pure-u-1-2"}
              [:input (merge {:type "text" :value host :id (str class-label "-host") :onChange (fn [e] ())} mode)]]
             [:div {:class "pure-u-1-2"}
              "Port: "]
             [:div {:class "pure-u-1-2"}
              [:input (merge {:type "text" :value port :id (str class-label "-port") :onChange (fn [e] ())} mode)]]
             [:div {:class "pure-u-1-2"}
              "User: "]
             [:div {:class "pure-u-1-2"}
              [:input (merge {:type "text" :value user :id (str class-label "-user") :onChange (fn [e] ())} mode)]]
             [:div {:class "pure-u-1-2"}
              "Password: "]
             [:div {:class "pure-u-1-2"}
              [:input (merge {:type "password" :value password :id (str class-label "-password") :onChange (fn [e] ())} mode)]]
           ;(om/build connection-form {} {:init-state {:save save}})
           ]]]]
         ))
      )))


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
      (html [:div {:class "connections-list"
                   :tabIndex "0"
                   ;:onKeyDown (fn [e] (.log js/console (str e)))
                   }
             (om/build-all connection-tile
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
