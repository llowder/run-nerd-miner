
(defproject run-nerd-miner "0.1.0-SNAPSHOT"
  :description "Data analysis tool for runner."
  :url "https://github.com/llowder/run-nerd-miner"
  :license {:name "Apache 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.zip "0.1.1"]
                 [incanter "1.2.3-SNAPSHOT"]
                 [edn-config "0.2"]
                 [puppetlabs/kitchensink "1.0.0"]
                 [prismatic/schema "0.3.3"]
                 
                 ]
  :profiles {:prod {:resource-paths ["config/prod"]}
             :dev  {:resource-paths ["config/dev"]}
             :test {:resource-paths ["config/test"]}
             :uberjar {:aot :all}
             
             }
  :main ^:skip-aot run-nerd-miner.core
  :target-path "target/%s"
)
