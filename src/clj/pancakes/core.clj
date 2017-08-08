(ns pancakes.core
  (:require [taoensso.timbre :as timbre :refer [info]]
            [taoensso.timbre.appenders.core :as appenders]
            [pancakes.systems :refer [server-system client-system]]
            [com.stuartsierra.component :as component]))

(timbre/merge-config!
 {:appenders {:spit (appenders/spit-appender {:fname "output.log"})}})

(defn make-server
  [opts function-map]
  (server-system opts function-map))

(defn make-client
  [opts function-map]
  (client-system opts function-map))

(defn run
  [client-or-server]
  (component/start client-or-server))
