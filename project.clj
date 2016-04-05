(defproject sisyphus-ui "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [reagent "0.5.1"]
                 [re-frame "0.7.0"]
                 [re-com "0.8.0"]
                 [secretary "1.2.3"]
                 [garden "1.3.2"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.0-6"]
            [lein-garden "0.2.6"]
            [lein-doo "0.1.6"]
            [lein-less "1.7.5"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"
                                    "resources/public/css/compiled"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :garden {:builds [{:id "screen"
                     :source-paths ["src/clj"]
                     :stylesheet sisyphus-ui.css/screen
                     :compiler {:output-to "resources/public/css/compiled/screen.css"
                                :pretty-print? true}}
                    {:id "editor"
                     :source-paths ["src/clj"]
                     :stylesheet sisyphus-ui.css/editor
                     :compiler {:output-to "resources/public/css/compiled/editor.css"
                                :pretty-print? true}}]}

  
  :less {:source-paths ["less"]
         :target-path  "resources/public/css/compiled"}

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :figwheel {:on-jsload "sisyphus-ui.core/mount-root"}
                        :compiler {:main sisyphus-ui.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true}}

                       {:id "test"
                        :source-paths ["src/cljs" "test/cljs"]
                        :compiler {:output-to "resources/public/js/compiled/test.js"
                                   :main sisyphus-ui.runner
                                   :optimizations :none}}

                       {:id "min"
                        :source-paths ["src/cljs"]
                        :compiler {:main sisyphus-ui.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :closure-defines {goog.DEBUG false}
                                   :pretty-print false}}]})
