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

(defn widget [data owner]
  (reify
    om/IRender
    (render [this]
      (dom/h1 nil
              (:text data)))))

(defn click []
  (.log js/alert "test")
  )

(def connection-chan (chan))

(defn connections-list [data owner]
  (reify
    om/IRender
    (render [this]
      (html [:div "Connection List"
             [:ul
              (map #(let [label (:label %)]
                      (html [:li [:button {:type "button" :onClick (fn [e] 
                                                                     (put! (:pub-chan (om/get-shared owner)) {:topic :selection :data label}))} (str label)]])) (:connections data))
              ]
             ])
      )))

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
                                        ;(om/root
                                        ;widget
                                        ;connections-list
                                        ;app-state
                                        ;{:connections connections}
                                        ;{:shared {:pub-chan pub-chan
                                        ;         :notif-chan notif-chan}
                                        ;:target (. js/document (getElementById "container"))})

    (om/root
     (fn [data owner]
       (reify
         om/IInitState
         (init-state [_]
           {:message nil})
         om/IDidMount
         (did-mount [_]
           (let [events (sub (:notif-chan (om/get-shared owner)) :selection (chan))]
             (go
               (loop [e (<! events)]
                 (om/set-state! owner :message (:data e))
                 (recur (<! events))))))
         om/IRenderState
         (render-state [_ {:keys [message]}]
           (if message
             (dom/p nil message)
             (dom/p nil "Waiting")))))
     
                                        ;(om/component (dom/h2 nil
                                        ;                      (str
                                        ;                       (go
                                        ;                         (<! connection-chan)))
                                        ;(:header data)
                                        ;                      )))

     app-state
     {:shared {:pub-chan pub-chan
               :notif-chan notif-chan}
      :target (. js/document (getElementById "header"))})))
