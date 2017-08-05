(ns pancakes.core
  (:require [taoensso.timbre :as timbre :refer [info]]
            [taoensso.timbre.appenders.core :as appenders]))

(timbre/merge-config!
 {:appenders {:spit (appenders/spit-appender {:fname "output.log"})}})

(defn make-router
  [function-map]
  (fn [[fkey & args]]
    (let [f (get function-map fkey)]
      (apply f args))))
