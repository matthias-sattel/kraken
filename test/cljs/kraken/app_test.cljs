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
            [kraken.component-test-support :as test-support]
            ))


(deftest test-container
  (let [c (test-support/container! "container-1")]
    (is (sel1 :#container-1))))

;; (deftest connections-list
;;   (let [c (container!)]
;;     (om/root app/connections-list {:data "test"} {:target c})
;;     (is (= 2 (dommy/text (dommy/sel1 :div))))
;;     ;(is (= 2 (count (dommy/sel :li))))
;;     ))

(deftest access-testdata
  (is (= "PostgreSql" (:type (first testdata/connections)))))


