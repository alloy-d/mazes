(defproject mazes "0.1.0-SNAPSHOT"
  :description "Maze generation algorithms."
  :license {:name "BSD"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]
                 [org.omcljs/om "0.9.0"]
                 [prismatic/dommy "1.1.0"]
                 [compojure "1.1.6"]
                 [sablono "0.3.6"]
                 [hiccup "1.0.4"]
                 [javax.servlet/servlet-api "2.5"]]
  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-figwheel "0.4.0"]
            [lein-ring "0.9.6"]]
  :main ^:skip-aot mazes.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.3"]
                                  [org.clojure/java.classpath "0.2.0"]]}}
  :cljsbuild {
              :builds [{:id "default"
                        :source-paths ["src-cljs"]
                        :figwheel true
                        :compiler {:main "mazes.web.core"
                                   :asset-path "js/out"
                                   :figwheel true
                                   :output-dir "resources/public/js/out"
                                   :output-to "resources/public/js/out/main.js"
                                   :optimizations :none
                                   :pretty-print true
                                   :source-map "resources/public/js/out/main.js.map"
                                   :source-map-path "js/out"}}]}
  :figwheel {:ring-handler mazes.web.handler/handler}
  :ring {:handler mazes.web.handler/handler})
