(set-env! :source-paths #{"src/clj" "src/cljs"}
          :resource-paths #{"resources"}
          :dependencies '[[org.clojure/clojure "1.8.0"]
                          [org.clojure/clojurescript "1.9.854"]
                          [com.taoensso/timbre "4.10.0"]])

(require '[pancakes.core :as core])

(deftask run
  "Runs the application."
  []
  (core/-main))
