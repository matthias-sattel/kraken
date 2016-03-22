(ns kraken.connection-test
  (:require-macros [cljs.test :refer [deftest testing is are]]
                   )
  (:require [cljs.test :as t]
            [kraken.connection :as connection])
  )

(deftest label-and-type-are-mandatory
  (is (not (connection/is-connection? {})))
  (is (connection/is-connection? {:label "MyConnection"
                                  :type :MongoDB
                                  :host "localhost"
                                  :port "1234"}))
  (is (not (connection/is-connection? {:type :MongoDB})))
  (is (not (connection/is-connection? {:label "MyConnection"})))
  )
