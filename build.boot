(def +version+ "0.1.2")
(task-options! pom {:project 'cjsauer/pancakes
                    :version +version+
                    :description "Clojure RPC over WebSockets"
                    :license {"MIT" "https://opensource.org/licenses/MIT"}
                    :url "https://github.com/cjsauer/pancakes"})

(set-env! :source-paths #{"src/clj"}
          :resource-paths #{"resources"}
          :dependencies '[[org.clojure/clojure "1.8.0"]
                          [org.clojure/core.async "0.3.443" :exclusions [org.clojure/clojurescript]]
                          [com.taoensso/timbre "4.10.0"]
                          [com.stuartsierra/component "0.3.2"]
                          [http-kit "2.2.0" :scope "provided"]
                          [org.danielsz/system "0.4.0" :scope "test"]
                          [adzerk/bootlaces "0.1.13" :scope "test"]
                          [compojure "1.6.0"]])

(require '[pancakes.core :as core]
         '[pancakes.systems :refer [dev-system]]
         '[system.boot :refer [system]]
         '[adzerk.bootlaces :refer :all])

(bootlaces! +version+ :dont-modify-paths? true)

(deftask dev
  "Launches the development environment, complete with server, REPL, and auto reloading."
  []
  (set-env! :source-paths #(conj % "src/dev"))
  (comp (repl :server true :init-ns 'user)
        (watch)
        (system :sys #'dev-system :auto true :files ["http_kit.clj" "systems.clj" "server.clj" "client.clj"])))

(deftask build
  "Builds and installs the project jar file"
  []
  (comp (build-jar)
        (target)))

(deftask uberjar
  "Produces a runnable uberjar file inside the target directory."
  []
  (comp (aot :namespace #{'pancakes.main})
        (uber)
        (jar :file "pancakes-standalone.jar" :main 'pancakes.main)
        (sift :include #{#"pancakes-standalone.jar"})
        (target)))

(deftask deploy-snapshot
  "Builds and deploys project jar file as a snapshot to Clojars"
  []
  (comp (build-jar) (push-snapshot)))

(deftask deploy-release
  "Builds and deploys project jar file as an official release to Clojars"
  []
  (comp (build-jar) (push-release)))
