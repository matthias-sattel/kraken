(ns kraken.embedded
  (:require [ring.adapter.jetty :as jetty])
  (:gen-class)
  )

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})

(defn -main
  "Startup embedded http server and other components"
  [& args]
  (jetty/run-jetty handler {:port 3001}))
