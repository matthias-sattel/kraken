(ns kraken.app-test
  (:require-macros [cljs.test :refer [deftest testing is]])
  (:require [cljs.test :as t]
            [kraken.app :as app]))

(deftest test-arithmetic []
  (is (= (+ 0.1M 0.2M) 0.3M) "Something foul is a float.")
  (is (= (- 3 2) 1)))

(deftest app-has-container []
  (is (= (+ 1 2) 3)))
