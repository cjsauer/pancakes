(ns pancakes.components.server
  (:require [taoensso.timbre :refer [debug]]
            [com.stuartsierra.component :as component]
            [org.httpkit.server :refer [with-channel on-receive]]
            [compojure.core :refer [GET routes]]
            [clojure.edn :as edn]))

(defrecord PancakesServer [opts http-server]
  component/Lifecycle

  (start [this]
    (debug "Starting pancakes server component")
    (assoc this :http-server (component/start http-server)))

  (stop [this]
    (debug "Stopping pancakes server component.")
    (assoc this :http-server (component/stop http-server))))


(defn- make-pancakes-handler
  [router]
  (fn [req]
    (with-channel req channel
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

(defn make-pancakes-server
  [opts router make-server-fn]
  (let [handler (make-handler router)]
    (->PancakesServer opts
                      (make-server-fn opts handler))))
