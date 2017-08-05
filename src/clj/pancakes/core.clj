(ns pancakes.core
  (:require [taoensso.timbre :as timbre :refer [info]]
            [taoensso.timbre.appenders.core :as appenders]))

(timbre/merge-config!
 {:appenders {:spit (appenders/spit-appender {:fname "output.log"})}})

(defn run
  "Entry point to application."
  [& args]
  (info "Warming the syrup..."))
