(ns pancakes.systems
  (:require [com.stuartsierra.component :as component]
            [pancakes.components.http-kit :refer [make-http-server]]
            [org.httpkit.server :refer [with-channel on-receive websocket? send! close]]
            [compojure.core :refer [routes GET ANY]]
            [clojure.edn :as edn]))

(defn make-router
  [function-map]
  (fn [[fkey & args]]
    (let [f (get function-map fkey)]
      (apply f args))))

(def example-function-map
  {:init (fn [] (println "Initialized!"))
   :hello (fn [name] (println "Hello," name))})

(defn make-pancakes-handler
  [router]
  (fn [req]
    (with-channel req channel
      (println channel)
      (if (and channel (websocket? channel))
        (on-receive channel (fn [data]
                              (router (edn/read-string data))))))))

(defn make-ring-handler
  [pancakes-handler]
  (routes (GET "/" [req] "<h1>Connect a websocket.</h1>")
          (GET "/ws" [req] pancakes-handler)))

(def handler (-> (make-router example-function-map)
                 (make-pancakes-handler)
                 (make-ring-handler)))

(defn dev-system
  []
  (component/system-map
   :http (make-http-server {:port 8080} handler)))
