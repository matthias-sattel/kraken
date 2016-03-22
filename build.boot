(set-env!
 :source-paths    #{"src/cljs" "src/clj" "src/cljc"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs          "1.7.48-6"   :scope "test"]
                 [adzerk/boot-cljs-repl     "0.2.0"      :scope "test"]
                 [adzerk/boot-reload        "0.4.1"      :scope "test"]
                 [pandeiro/boot-http        "0.6.3"      :scope "test"]
                 [org.clojure/clojurescript "1.7.122"]
                 [crisptrutski/boot-cljs-test "0.2.0-SNAPSHOT" :scope "test"]
                 [org.omcljs/om "0.8.6"]
                 [org.martinklepsch/boot-garden "1.2.5-3" :scope "test"]
                 [sablono "0.6.0"]
                 [cljsjs/react "0.14.3-0"]
                 [cljsjs/react-dom "0.14.3-1"]
                 [prismatic/dommy "1.1.0" :scope "test"]
                 [org.clojure/core.async "0.2.374"]
                 [prismatic/schema "1.0.5"]
                 [it.frbracch/boot-marginalia "0.1.3-1" :scope "test"]]
 )

(swap! boot.repl/*default-dependencies*
       concat '[[cider/cider-nrepl "0.10.1"]])

(swap! boot.repl/*default-middleware*
       conj 'cider.nrepl/cider-middleware)


(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]]
 '[org.martinklepsch.boot-garden :refer [garden]]
 '[it.frbracch.boot-marginalia :refer [marginalia]])

;(set-env! :source-paths #(conj % "src/cljc"))

(deftask build []
  (comp (speak)
        
        (cljs)
        
        (garden :styles-var 'kraken.styles/screen
:output-to "css/garden.css")))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced}
                      garden {:pretty-print false})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none :source-map true}
                 reload {:on-jsload 'kraken.app/init})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))


(deftask testing []
  (set-env! :source-paths #(conj % "test/cljs" "test/cljc"))
  identity)

;;; This prevents a name collision WARNING between the test task and
;;; clojure.core/test, a function that nobody really uses or cares
;;; about.
(ns-unmap 'boot.user 'test)

(deftask test []
  (comp (testing)
        (test-cljs :js-env :phantom
                   :exit?  true)))

(deftask auto-test []
  (comp (testing)
        (watch)
        (test-cljs :js-env :phantom)))

(deftask doc []
  (set-env! :source-paths #(conj % "src/cljc"))
  (marginalia)
  )
