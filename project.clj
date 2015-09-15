(defproject mazes "0.1.0-SNAPSHOT"
  :description "Maze generation algorithms."
  :license {:name "BSD"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]
                 [prismatic/dommy "1.1.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.4"]]
  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-ring "0.9.6"]]
  :main ^:skip-aot mazes.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.3"]
                                  [org.clojure/java.classpath "0.2.0"]]}}
  :cljsbuild {
    :builds [{:source-paths ["src-cljs"]
              :compiler {:output-to "resources/public/js/main.js"
                         :optimizations :whitespace
                         :pretty-print true}}]}
  :ring {:handler mazes.web.handler/handler})
