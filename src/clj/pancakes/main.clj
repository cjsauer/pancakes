(ns pancakes.main
  (:require [pancakes.systems :as sys]
            [com.stuartsierra.component :as component])
  (:gen-class))

(defn -main
  [& args]
  (let [sys (sys/server-system {:port 8080} sys/dev-routes-server)]
    (component/start sys)))
