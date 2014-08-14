(defproject cljs_test_none "0.1.0"
  :description "Testing with :optimizations :none"
  :url ""
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}


  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2280"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]
            [com.cemerick/clojurescript.test "0.3.1"]]

  :cljsbuild { :builds [
                        {:id "test"
                         :source-paths   ["src" "test"]
                         :compiler       {:output-to     "compiled/test.js"
                                          :source-map    "compiled/test.js.map"
                                          :output-dir    "compiled/test"
                                          :optimizations :none
                                          :pretty-print  true}}

                        {:id "test-node"
                         :source-paths   ["src" "test"]
                         :compiler       {:output-to     "compiled/test-node.js"
                                          ;;; source maps can't be used
                                          :target :nodejs
                                          ;;; this target requires a main to be defined
                                          :output-dir    "compiled/test-node"
                                          :optimizations :none
                                          :pretty-print  true}}

                        {:id "dev"
                         :source-paths   ["src"]
                         :compiler       {:output-to     "compiled/dev.js"
                                          :output-dir    "compiled/dev"
                                          :optimizations :none}}

                        {:id "prod"
                         :source-paths   ["src"]
                         :compiler       {:output-to     "compiled/prod.js"
                                          :output-dir    "compiled/prod"
                                          :optimizations :advanced}}]

               :test-commands {"unit-tests" ["phantomjs" "test/bin/runner-none.js"  "compiled/test" "compiled/test.js"]
                               "node-tests" ["node" "test/bin/runner-node.js"  "compiled/test-node" "compiled/test-node.js"]}}

  :source-paths ["src" "test"]
  :test-paths ["spec"]

  :aliases {"auto-test" ["do" "clean," "cljsbuild" "auto" "test"]}
  )

