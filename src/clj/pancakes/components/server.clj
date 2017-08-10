(ns pancakes.components.server
  (:require [taoensso.timbre :refer [debug]]
            [com.stuartsierra.component :as component]
            [org.httpkit.server :refer [with-channel on-receive send!]]
            [compojure.core :refer [GET routes]]
            [clojure.edn :as edn]))

(def client-channels (atom []))

(defrecord PancakesServer [opts http-server]
  component/Lifecycle

  (start [this]
    (debug "Starting pancakes server component")
    (assoc this :http-server (component/start http-server)))

  (stop [this]
    (debug "Stopping pancakes server component.")
    (reset! client-channels [])
    (assoc this :http-server (component/stop http-server))))

(defn- make-pancakes-handler
  [router]
  (fn [req]
    (with-channel req channel
      (swap! client-channels conj channel)
      (on-receive channel (fn [data]
                            (router (edn/read-string data)))))))

(defn- make-ring-handler
  [pancakes-handler]
  (routes (GET "/" [req] "<h1>Connect a websocket.</h1>")
          (GET "/ws" [req] pancakes-handler)))

(defn- make-handler
  [router]
  (-> router
      (make-pancakes-handler)
      (make-ring-handler)))

(defn broadcast
  "Broadcasts given event vector to all connected clients."
  [data]
  (doseq [c @client-channels]
    (send! c (str data))))

(defn make-pancakes-server
  [opts router make-server-fn]
  (let [handler (make-handler router)]
    (->PancakesServer opts
                      (make-server-fn opts handler))))
