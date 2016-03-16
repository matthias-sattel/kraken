(ns kraken.component-test-support
  (:require-macros [cljs.test :refer [is]])
  (:require  [dommy.core :refer-macros [sel sel1]]
             [dommy.core :as dommy])
  )

(defn new-id 
  ([]
   (str "container-" (gensym)))
  ([id]
   (str "container-" id)))

(defn new-node [id]
  (-> (dommy/create-element "div")
      (dommy/set-attr! "id" id)))

(defn append-node [node]
  (dommy/append! (sel1 js/document :body) node))

(defn container!
  ([]
   (container! (new-id)))
  ([id]
   (-> id
       new-node
       append-node)))
