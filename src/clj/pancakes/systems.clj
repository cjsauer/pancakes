(ns pancakes.systems
  (:require [pancakes.core :as p]
            [pancakes.components.server :refer [make-pancakes-server]]
            [pancakes.components.http-kit :refer [make-http-server]]
            [com.stuartsierra.component :as component]))

(def dev-routes
  {:init (fn [] (println "Initialized!"))
   :hello (fn [name] (println "Hello" name))})

(def dev-router (p/make-router dev-routes))

(defn dev-system
  []
  (component/system-map
   :pancakes (make-pancakes-server {:port 8080} dev-router make-http-server)))

(defn prod-system
  [router]
  (component/system-map
   :pancakes (make-pancakes-server {:port 8080} router make-http-server)))
