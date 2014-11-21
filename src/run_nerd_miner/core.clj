(ns run-nerd-miner.core
  (:require [edn-config.core :refer [env]])
  (:require [clojure.java.io :as io])
  (:require [ clojure.data.xml :as xmld])
  (:gen-class))

(defn only-files
  [file-s]
  (filter #(.isFile %) file-s)
  )


(defn read-xml-file
  [s_file]
    (let [input-xml (try
                      (java.io.FileInputStream. s_file)
                      (catch Exception e ))]
      (if-not (nil? input-xml)
        (xmld/parse input-xml)
        nil))
  )

(defn read-in-files
  (def directory (io/file (:source-data-dir env)))
  (def files ( only-files (file-seq directory)))

  (def xml-data {:ignore "this"})
  
  (doseq [f files]
    (xml-data {(.getName f) (read-xml-file f)})
    )
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (def test-data (read-in-files))
  (println test-data)
  
  
)
