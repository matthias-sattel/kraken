(ns kraken.embedded
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [bidi.ring :refer (make-handler)])
  (:gen-class)
  )

(defn index-handler [request]
  (response/content-type
   (response/resource-response "index.html" {:root ""})
   "text/html"))

(defn hello-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World "})

(def app-handler
  (let [handler (make-handler ["/" {"" index-handler
                                    "hello" hello-handler
                                    "index" index-handler}])]
    (-> handler
        (wrap-resource "")
        (wrap-content-type)
        (wrap-not-modified))
    ))

(defn -main
  "Startup embedded http server and other components"
  [& args]
  (jetty/run-jetty
   app-handler
   {:port 3001}))
