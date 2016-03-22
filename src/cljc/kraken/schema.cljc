(ns kraken.schema
  (:require [schema.core :as s :include-macros true])
  )

(def Connection
  "Schema representing simple connection data"
  {(s/required-key :type) s/Keyword
   (s/required-key :label) s/Str
   (s/optional-key :user) s/Str
   (s/optional-key :password) s/Str
   (s/required-key :host) s/Str
   (s/required-key :port) s/Str
   s/Keyword s/Str})
