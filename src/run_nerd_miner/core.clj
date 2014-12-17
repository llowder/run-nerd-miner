(ns run-nerd-miner.core
  (:require [edn-config.core :refer [env]])
  (:require [clojure.java.io :as io])
  (:require [clojure.data.xml :as xml])
  (:require [clojure.zip :as zip])
  (:require [clojure.data.zip.xml :as xz])
  (:require [clojure.pprint :as pp])
  (:gen-class))

(defn only-files
  "Scans a directory and returns a sequence of files"
  [file-s]
  (filter #(.isFile %) file-s)
  )

(def tcx_data (-> (first (only-files (file-seq (io/file (:source-data-dir env))))) io/input-stream xml/parse))

(def tcx_data-zip (zip/xml-zip tcx_data))

(defn laps
  "Extracts the lap start times from a passed in tcx zip"
  ([] (laps tcx_data-zip))
  ([data] (xz/xml-> data :Activities :Activity :Lap (xz/attr :StartTime)))
  )

(defn lap-trackpoint-times
  "Extracts all the trackpoint times from a lap"
  [lap]
  (xz/xml-> tcx_data-zip :Activities :Activity :Lap (xz/attr= :StartTime lap) :Track :Trackpoint :Time xz/text )
  )

(defn all-trackpoints-times
  "Get the track point times from all laps"
  ([all-laps] (all-trackpoints-times all-laps () ))
  ([all-laps lap-times]
     (if (empty? all-laps)
       lap-times
       (all-trackpoints-times (rest all-laps) (concat lap-times (lap-trackpoint-times (first all-laps))))
       )))

(defn heartbeat
  "Get the heart rate data for a given track point"
  ([lap tp-time]
     (xz/xml->
      (xz/xml1-> tcx_data-zip :Activities :Activity :Lap (xz/attr= :StartTime lap) :Track :Trackpoint :Time (xz/text= tp-time) )
      zip/right zip/right zip/right zip/right zip/down xz/text)      
     ))

(defn tp-lat
  [lap tp-time]
  (xz/xml->
   (xz/xml1-> tcx_data-zip :Activities :Activity :Lap (xz/attr= :StartTime lap) :Track :Trackpoint :Time (xz/text= tp-time))
   zip/right zip/down xz/text)
  )

(defn tp-long
  [lap tp-time]
  (xz/xml->
   (xz/xml1-> tcx_data-zip :Activities :Activity :Lap (xz/attr= :StartTime lap) :Track :Trackpoint :Time (xz/text= tp-time))
   zip/right zip/down zip/right xz/text)
  )

(defn position
  "Gets the positional data for a given trackpoint"
  [lap tp-time]
  {"lat" (tp-lat lap tp-time),
   "long" (tp-long lap tp-time)
  }
  )

(defn altmeter
  "Get the heart rate data for a given track point"
  ([lap tp-time]
     (xz/xml->
      (xz/xml1-> tcx_data-zip :Activities :Activity :Lap (xz/attr= :StartTime lap) :Track :Trackpoint :Time (xz/text= tp-time) )
      zip/right zip/right xz/text)      
     ))

(defn distmeter
  "Get the heart rate data for a given track point"
  ([lap tp-time]
     (xz/xml->
      (xz/xml1-> tcx_data-zip :Activities :Activity :Lap (xz/attr= :StartTime lap) :Track :Trackpoint :Time (xz/text= tp-time) )
      zip/right zip/right zip/right zip/down xz/text)      
     ))

(defn speed
  "Get the heart rate data for a given track point"
  ([lap tp-time]
     (xz/xml->
      (xz/xml1-> tcx_data-zip :Activities :Activity :Lap (xz/attr= :StartTime lap) :Track :Trackpoint :Time (xz/text= tp-time) )
      zip/right zip/right zip/right zip/right zip/right zip/down zip/down xz/text)      
     ))

(defn runcadence
  "Get the heart rate data for a given track point"
  ([lap tp-time]
     (xz/xml->
      (xz/xml1-> tcx_data-zip :Activities :Activity :Lap (xz/attr= :StartTime lap) :Track :Trackpoint :Time (xz/text= tp-time) )
      zip/right zip/right zip/right zip/right zip/right zip/down zip/down zip/right xz/text)      
     ))

(defn trackpoint
  [lap tp-time]
  {
   :time tp-time
   :position (position lap tp-time)
   :heartbeat (heartbeat lap tp-time)
   :altitude (altmeter lap tp-time)
   :distance (distmeter lap tp-time)
   :cadence (runcadence lap tp-time)}
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]

  ;  (pp/pprint  (xz/xml-> (xz/xml1-> tcx_data-zip :Activities :Activity :Lap (xz/attr= :StartTime (first (laps tcx_data-zip))) :Track :Trackpoint :Time) zip/right zip/right zip/right zip/right zip/down xz/text) )
  ;  (pp/pprint (xz/xml-> (xz/xml1-> tcx_data-zip :Activities :Activity :Lap (xz/attr= :StartTime (first (laps tcx_data-zip))):Track :Trackpoint :Time (xz/text= (first (all-trackpoints-times (laps tcx_data-zip))))) zip/right zip/right zip/right zip/right zip/down xz/text) )
;  (pp/pprint (first (laps tcx_data-zip)))
;  (pp/pprint (second (all-trackpoints-times (laps tcx_data-zip))))
;  (pp/pprint (position (first (laps tcx_data-zip)) (second (all-trackpoints-times (laps tcx_data-zip)))))
;  (pp/pprint (heartbeat (first (laps tcx_data-zip)) (nth (all-trackpoints-times (laps tcx_data-zip)) 4  )))
;  (pp/pprint (distmeter (first (laps tcx_data-zip)) (nth (all-trackpoints-times (laps tcx_data-zip)) 4  )))
;  (pp/pprint (altmeter (first (laps tcx_data-zip)) (nth (all-trackpoints-times (laps tcx_data-zip)) 4  )))
;  (pp/pprint (tp-lat (first (laps tcx_data-zip)) (nth (all-trackpoints-times (laps tcx_data-zip)) 4  )))
;  (pp/pprint (tp-long (first (laps tcx_data-zip)) (nth (all-trackpoints-times (laps tcx_data-zip)) 4  )))
;  (pp/pprint (position (first (laps tcx_data-zip)) (nth (all-trackpoints-times (laps tcx_data-zip)) 4  )))
;  (pp/pprint (speed (first (laps tcx_data-zip)) (nth (all-trackpoints-times (laps tcx_data-zip)) 4  )))
;  (pp/pprint (runcadence (first (laps tcx_data-zip)) (nth (all-trackpoints-times (laps tcx_data-zip)) 4  )))
  (pp/pprint (trackpoint (first (laps tcx_data-zip)) (nth (all-trackpoints-times (laps tcx_data-zip)) 4  )))
  
  )
