(ns run-nerd-miner.core
  (:require [edn-config.core :refer [env]])
  (:require [clojure.java.io :as io])
  (:require [clojure.data.xml :as xml])
  (:require [clojure.zip :as zip])
  (:require [clojure.data.zip.xml :as xz])
  
  (:gen-class))

(defn only-files
  [file-s]
  (filter #(.isFile %) file-s)
  )

(def tcx_data
  (->
   (first (only-files (file-seq (io/file (:source-data-dir env)))))
   io/input-stream
   xml/parse
   ))

(def heartrates (map zip/node
                     (map zip/down
                          (xz/xml-> tcx_data :HeartRateBPM :Value))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]

(println tcx_data)
)
