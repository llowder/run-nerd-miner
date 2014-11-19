(ns run-nerd-miner.core
  (:require [edn-config.core :refer [env]])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "v: " (:config-version env) " g: " (:graph-data-dir env) " s: " (:source-data-dir env))
  (pr-str env)
  )

