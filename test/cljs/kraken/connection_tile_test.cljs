(ns kraken.connection-tile-test
  (:require-macros [cljs.test :refer [deftest testing is are]]
                   [cljs.core.async.macros :refer [go]])
  (:require [cljs.test :as t]
            [kraken.connection-tile :as tile]
            [kraken.component-test-support :as ct]
            [kraken.testdata :as testdata]
            [dommy.core :refer-macros [sel sel1]]
            [dommy.core :as dommy]
            [om.core :as om :include-macros true]
            [cljs.core.async :refer [put! chan <! timeout close!]]
            [kraken.async-test-support :as async-test]
            ))

(deftest availability-class-test []
  (is (= "connection-active" (tile/availability-class "active")))
  (is (= "connection-error" (tile/availability-class "error")))
  (is (= "connection-warning" (tile/availability-class "warning"))))

(deftest delete-test []
  (let [c (ct/container!)
        delete-chan (chan)
        delete-done-chan (chan)]
    (om/root tile/ui-component
             (first testdata/connections)
             {:target c
              :init-state {:delete delete-chan
                           :save (chan)
                           :edit (chan)
                           :update (chan)}})
    
    (go
      (.click (dommy/sel1 (str "#" (tile/button-id "MyPostgresqlConnection" "delete")))))
    (async-test/test-async
     (async-test/test-within 1000
                             (go
                               (is
                                (= {:label "MyPostgresqlConnection"
                                    :type "PostgreSql"
                                    :version "9.4"
                                    :database "users"
                                    :user "sa"
                                    :password "#231342"
                                    :state "active"
                                    :host "192.168.11.2"
                                    :port "5432"
                                    } (<! delete-chan))))
                             ))
    )
  )

(deftest delete-chan-test
  (let [delete-chan (chan)
        delete-done-chan (chan)]
    (go
      (>! delete-chan "test"))
    (async-test/test-async
     (async-test/test-within 1000
                             (go (is (= "test" (<! delete-chan))))
                             ))
    ))

(deftest input-id-test
  (let [label "MyPostgreConnection"]
    (are [x y] (= x y)
      (tile/input-id label "type") "MyPostgreConnection-type"
      (tile/input-id label "host") "MyPostgreConnection-host"
      (tile/input-id label "port") "MyPostgreConnection-port"
      )
    ))

(deftest button-id-test
  (let [label "MongoConnectionDemo"]
    (are [x y] (= x y)
      (tile/button-id label "delete") "btn-MongoConnectionDemo-delete"
      )
    ))
