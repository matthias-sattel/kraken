(ns kraken.async-test-support
  #?(:cljs(:require-macros [cljs.test :refer [is async]]
                   [cljs.core.async.macros :refer [go]]))
  #?(:cljs(:require  [cljs.core.async :refer [put! <! take! timeout alts!]]))
  )

;test-async and test-within is taken from http://stackoverflow.com/questions/30766215/how-do-i-unit-test-clojure-core-async-go-macros

(defn test-async
  "Asynchronous test awaiting ch to produce a value or close."
  [channel]
  #?(:clj
     (<!! channel)
     :cljs
     (async done
            (take! channel (fn [_] (done))))))

(defn test-within
  "Asserts that ch does not close or produce a value within ms. Returns a
  channel from which the value can be taken."
  [ms channel]
  (go (let [t (timeout ms)
            [v channel] (alts! [channel t])]
        (is (not= channel t)
            (str "Test should have finished within " ms "ms."))
        v)))
