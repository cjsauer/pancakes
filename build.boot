(set-env! :source-paths #{"src/clj"}
          :resource-paths #{"resources"}
          :dependencies '[[org.clojure/clojure "1.8.0"]
                          [org.clojure/clojurescript "1.9.854"]
                          [org.clojure/core.async "0.3.443"]
                          [com.taoensso/timbre "4.10.0"]
                          [com.stuartsierra/component "0.3.2"]
                          [http-kit "2.2.0" :scope "provided"]
                          [org.danielsz/system "0.4.0" :scope "test"]
                          [compojure "1.6.0"]])

(require '[pancakes.core :as core]
         '[pancakes.systems :refer [dev-system]]
         '[system.boot :refer [system]])

(deftask dev
  "Launches the development environment, complete with server, REPL, and auto reloading."
  []
  (set-env! :source-paths #(conj % "src/dev"))
  (comp (repl :server true :init-ns 'user)
        (watch)
        (system :sys #'dev-system :auto true :files ["http_kit.clj" "systems.clj"])))
