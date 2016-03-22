(ns kraken.connection-tile
  (:require [om.core :as om :include-macros true]
            [dommy.core :refer-macros [sel1]]
            [dommy.core :as dommy]
            [sablono.core :as html :refer-macros [html]]
            [cljs.core.async :refer [put!]]
            ))

;; ## Class selector used to mark the availability of a connection
;; Depending on the state a connection tile will have a availability class
;; This class is used to format the connection according to its availability


(defn availability-class [state]
  (if (= state "active")
    "connection-active"
    (if (= state "error")
      "connection-error"
      "connection-warning"))
  )

;; Unique id for an input property of the connection tile.

(defn input-id [label property]
  (str label "-" property))

;; Unique id for a button of the connection tile.

(defn button-id [label action]
  (str "btn-" label "-" action))

;; ## The REACT-Component that can display a single connection tile

(defn ui-component
  "Generates a new react component. It shows a connection as a tile."
  [data owner]
  (reify
    om/IRenderState
                                        ; We need several async channels
    (render-state [this {:keys [delete save edit update]}]
                                        ; Now we create some lexical bindings that we need to setup the component.
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
                                        ; The tile is rendered now as html div using sablono
        (html
         [:div {:class (str "pure-u-1 pure-u-md-1-3 pure-u-lg-1-5 connection-tile " class-label " " activity-label)}
          [:div {:class "pure-u-g connection-list-item"}
           [:div {:class "pure-u-1 pure-u-md-1-2"}
            [:h3 {:class "connection-list-item-header"}
             label]]
           [:div {:class "pure-u-1 pure-u-md-1-2 connection-menu"}
            [:div {:class "pure-u-g"}
             [:div {:class "pure-u-1-5 connection-menu-item"}
              [:button {:class "pure-button" :id (button-id label "delete") :onClick (fn [e] (put! delete @data))}
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
                                                                              :type (dommy/value (sel1 (keyword (str "#" (input-id label "type")))))
                                                                              :host (dommy/value (sel1 (keyword (str "#" (input-id label "host")))))
                                                                              :port (dommy/value (sel1 (keyword (str "#" (input-id label "port")))))
                                                                              :user (dommy/value (sel1 (keyword (str "#" (input-id label "user")))))
                                                                              :password (dommy/value (sel1 (keyword (str "#" (input-id label "password")))))
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
              [:input (merge {:type "text" :value type :id (input-id label "type") :onChange (fn [e] ())} mode) ]]
             [:div {:class "pure-u-1-2"}
              "Hostname: "]
             [:div {:class "pure-u-1-2"}
              [:input (merge {:type "text" :value host :id (input-id label "host") :onChange (fn [e] ())} mode)]]
             [:div {:class "pure-u-1-2"}
              "Port: "]
             [:div {:class "pure-u-1-2"}
              [:input (merge {:type "text" :value port :id (input-id label "port") :onChange (fn [e] ())} mode)]]
             [:div {:class "pure-u-1-2"}
              "User: "]
             [:div {:class "pure-u-1-2"}
              [:input (merge {:type "text" :value user :id (input-id label "user") :onChange (fn [e] ())} mode)]]
             [:div {:class "pure-u-1-2"}
              "Password: "]
             [:div {:class "pure-u-1-2"}
              [:input (merge {:type "password" :value password :id (input-id label "password") :onChange (fn [e] ())} mode)]]
           ;(om/build connection-form {} {:init-state {:save save}})
           ]]]]
         ))
      )))
