(ns kraken.app-test
  (:require-macros [cljs.test :refer [deftest testing is]
                    ])
  
  (:require [cljs.test :as t]
            [kraken.app :as app]
            [kraken.testdata :as testdata]
            [dommy.core :refer-macros [sel sel1]]
            [dommy.core :as dommy]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            ))


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

(deftest test-container
  (let [c (container! "container-1")]
    (is (sel1 :#container-1))))

(defn ce [tag & [text]]
  (let [el (dommy/create-element tag)]
    (when text
      (dommy/set-text! el text))
    el))

(deftest widget-element
  (let [c (container!)]
    (om/root app/widget {:text "Hello World!"} {:target c})
                                        ;(print (str "teste" (sel :h1)))
                                        ;(is (sel :h1))
                                        ;(is (= 1
                                        ;       (count (sel :h1))))
    (is (= "Hello World!" (dommy/text (first (sel :h1)))))
    ))

(deftest connections-list
  (let [c (container!)]
    (om/root app/connections-list {:data "test"} {:target c})
    (is (= 2 (dommy/text (dommy/sel1 :div))))
    ;(is (= 2 (count (dommy/sel :li))))
    ))

(deftest access-testdata
  (is (= "PostgreSql" (:type (first testdata/connections)))))
