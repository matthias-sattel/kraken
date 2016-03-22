(ns kraken.connection
  (:require [kraken.schema :as schema]
            [schema.core :as s])
  )

(defn is-connection? [connection]
  (let [exception-thrown (atom true)]
    (try
      (s/validate schema/Connection connection)
      (catch #?(:clj Exception
                :cljs :default) e (reset! exception-thrown false))
      )
    @exception-thrown
    )
  ;; (if
  ;;     (contains? connection :label)
  ;;   (contains? connection :type)
  ;;   false)
  )
