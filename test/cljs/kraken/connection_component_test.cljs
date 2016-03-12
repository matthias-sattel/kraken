(ns kraken.connection-component-test
  (:require-macros [cljs.test :refer [deftest testing is]
                    ])
  (:require [cljs.test :as t]
            [kraken.connection-component :as cc]
            [kraken.component-test-support :as ct]
            [kraken.testdata :as testdata]
            [dommy.core :refer-macros [sel sel1]]
            [dommy.core :as dommy]
            [om.core :as om :include-macros true]
            [cljs.core.async :refer [put! chan <! ]]))

(deftest connections-view-label-test
  (let [c (ct/container!)]
    (om/root cc/connections {:connections testdata/connections} {:target c})
    (let [connections-label (map dommy/text (dommy/sel ".connection-list-item-header"))]
      (print (str "Connections : " connections-label))
      
      (is (= 3 (count connections-label)))
      (is (some #(= "MyPostgresqlConnection" %)
                connections-label)))
    
    ))




