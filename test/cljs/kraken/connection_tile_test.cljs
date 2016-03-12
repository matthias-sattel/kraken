(ns kraken.connection-tile-test
  (:require-macros [cljs.test :refer [deftest testing is are]
                    ])
  (:require [cljs.test :as t]
            [kraken.connection-tile :as tile]
            [kraken.component-test-support :as ct]
            [kraken.testdata :as testdata]
            [dommy.core :refer-macros [sel sel1]]
            [dommy.core :as dommy]
            [om.core :as om :include-macros true]
            [cljs.core.async :refer [put! chan <! ]]))

(deftest availability-class-test []
  (is (= "connection-active" (tile/availability-class "active")))
  (is (= "connection-error" (tile/availability-class "error")))
  (is (= "connection-warning" (tile/availability-class "warning"))))

(deftest delete-test []
  (let [c (ct/container!)]
    (om/root tile/ui-component
             (first testdata/connections)
             {:target c
              :init-state {:delete (chan)
                           :save (chan)
                           :edit (chan)
                           :update (chan)}})
    (is (= 1 1))
    (are [x y] (= x y)
      (nil? (dommy/sel1 ".connection-MyPostgresqlConnection")) false
      (dommy/value (dommy/sel1 "#MyPostgresqlConnection-type")) "PostgreSql"
      )
    ))

(deftest input-id-test
  (let [label "MyPostgreConnection"]
    (are [x y] (= x y)
      (tile/input-id label "type") "MyPostgreConnection-type"
      (tile/input-id label "host") "MyPostgreConnection-host"
      (tile/input-id label "port") "MyPostgreConnection-port"
      )
    ))
