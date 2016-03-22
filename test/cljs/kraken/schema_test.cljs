(ns kraken.schema-test
  (:require-macros [cljs.test :refer [deftest testing is are]]
                   )
  (:require [cljs.test :as t]
            [kraken.schema :as schema]
            [schema.core :as s])
  )

(def mongo-connection
  {:type :MongoDB
   :label "Mongo1"
   :host "localhost"
   :port "1234"
   :customProperty "test"
   :custom2 "sdgfpise"})

(def invalid-connection1
  {:type :MongoDB
   :host "localhost"
   :port "1234"})

(def invalid-connection2
  {:type :MongoDB
   :label "Mongo1"
   :host "localhost"})

(def invalid-connection3
  {:type :MongoDB
   :label ""
   :host "localhost"
   :port "1234"})

(deftest validate-mongo-schema
  (is (s/validate schema/Connection mongo-connection))
  (is (thrown? js/Error (s/validate schema/Connection {:type :MongoDB
                                           :label "Mongo1"
                                                             :host "localhost"})))
  (is (thrown? js/Error (s/validate schema/Connection invalid-connection1)))
  (is (thrown? js/Error (s/validate schema/Connection invalid-connection2)))
  (is (thrown? js/Error (s/validate schema/Connection invalid-connection3)))
  )
