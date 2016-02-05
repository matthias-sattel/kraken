(ns kraken.app
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defonce app-state (atom {}))

(swap! app-state assoc :header "Hello World!" :text "I like Clojurescript")

(defn widget [data owner]
  (reify
    om/IRender
    (render [this]
      (dom/h1 nil
              (:text data)))))
              
(defn init []
  (om/root widget app-state
           {:target (. js/document (getElementById "container"))})
  (om/root
   (fn [data owner]
     (om/component (dom/h2 nil (:header data))))
     app-state
     {:target (. js/document (getElementById "header"))}))
