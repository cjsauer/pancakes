(ns pancakes.components.client
  (:require [taoensso.timbre :refer [debug]]
            [com.stuartsierra.component :as component]
            [clojure.edn :as edn]
            [clojure.core.async :refer [go go-loop <! >! >!! chan close!]]))

(defrecord PancakesClient [opts handler-fn in-chan]
  component/Lifecycle

  (start [this]
    (debug "Starting pancakes client component")
    (let [c (chan)]
      (go-loop []
        (when-let [data (<! c)]
          (handler-fn data)
          (recur)))
      (assoc this :in-chan c)))

  (stop [{:keys [in-chan] :as this}]
    (debug "Stopping pancakes client component.")
    (close! in-chan)
    (dissoc this :in-chan)))

(defn receive
  [{:keys [in-chan] :as this} data]
  (>!! in-chan data))

(defn- make-handler
  [router]
  router)

(defn make-pancakes-client
  [opts router]
  (let [handler (make-handler router)]
    (map->PancakesClient {:opts opts
                          :handler-fn handler})))
