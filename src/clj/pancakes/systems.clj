(ns pancakes.systems
  (:require [com.stuartsierra.component :as component]
            [pancakes.components.http-kit :refer [make-http-server]]))

(defn temp-handler
  [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<h1>Hello, world!</h1>"})

(defn dev-system
  []
  (component/system-map
   :http (make-http-server {:port 8080} temp-handler)))
