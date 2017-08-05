(ns pancakes.components.http-kit
  (:require [taoensso.timbre :refer [debug fatal]]
            [com.stuartsierra.component :as component]
            [org.httpkit.server :as http]))

(defrecord HttpKit [opts handler-fn]
  component/Lifecycle

  (start [this]
    (debug "Starting HttpKit component")
    (if handler-fn
      (assoc this :stop-fn (http/run-server handler-fn opts))
      (ex-info "No handler-fn defined for HttpKit component" this)))

  (stop [{:keys [stop-fn] :as this}]
    (debug "Stopping the HttpKit component")
    (when stop-fn
      (stop-fn))
    this))

(defn make-http-server
  [opts handler-fn]
  (->HttpKit opts handler-fn))
