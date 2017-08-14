(ns pancakes.systems
  (:require [pancakes.components.server :refer [make-pancakes-server broadcast]]
            [pancakes.components.client :refer [make-pancakes-client]]
            [pancakes.components.http-kit :refer [make-http-server]]
            [com.stuartsierra.component :as component]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Main system functions
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- make-router
  [function-map]
  (fn [[fkey & args]]
    (let [f (get function-map fkey)]
      (apply f args))))

(defn server-system
  [opts function-map]
  (let [router (make-router function-map)]
    (component/system-map
     :server (make-pancakes-server opts router make-http-server))))

(defn client-system
  [opts function-map]
  (let [router (make-router function-map)]
    (component/system-map
     :client (make-pancakes-client opts router))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Development system functions
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def dev-routes-server
  {:init (fn [] (println "Initialized server!"))
   :hello (fn [name] (println "Server (hello): " name))
   :toggle (fn [] (broadcast [:toggle]))
   :warriors (fn [] (broadcast [:warriors]))
   :cavaliers (fn [] (broadcast [:cavaliers]))})

(def dev-routes-client
  {:init (fn [] (println "Initialized client!"))
   :hello (fn [name] (println "Client (hello): " name))})

(def dev-router-server (make-router dev-routes-server))
(def dev-router-client (make-router dev-routes-client))

(defn dev-system
  []
  (component/system-map
   :server (make-pancakes-server {:port 8080} dev-router-server make-http-server)
   :client (make-pancakes-client {} dev-router-client)))
