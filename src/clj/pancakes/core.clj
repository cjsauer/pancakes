(ns pancakes.core
  (:require [taoensso.timbre :refer [info]])
  (:gen-class))

(defn -main
  "Entry point to application."
  [& args]
  (info "Warming the syrup..."))
