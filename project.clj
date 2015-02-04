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
                        ;; test.html and phantom can work from this build
                        {:id "test"
                         :source-paths   ["src" "test"]
                         :compiler       {:output-to     "compiled/test.js"
                                          :source-map    "compiled/test.js.map"
                                          :output-dir    "compiled/test"
                                          :optimizations :none
                                          :pretty-print  true}
                         ;; if you want auto testing uncomment below
                         ;; :notify-command ["phantomjs" "test/bin/runner-none.js" "compiled/test" "compiled/test.js"]
                         }

                        ;; node requires a separate build
                        {:id "test-node"
                         :source-paths   ["src" "test-node"]
                         :compiler       {:output-to     "compiled/test-node.js"
                                          ;;; source maps can't be used
                                          :target :nodejs            ;;; this target required for node, plus a *main* defined in the tests.
                                          :hashbang false            ;;; https://github.com/cemerick/clojurescript.test/issues/68#issuecomment-52981151
                                          :output-dir    "compiled/test-node"
                                          :optimizations :none
                                          :pretty-print  true}}

                        #_{:id "dev"
                         :source-paths   ["src"]
                         :compiler       {:output-to     "compiled/dev.js"
                                          :output-dir    "compiled/dev"
                                          :optimizations :none}}

                        #_{:id "prod"
                         :source-paths   ["src"]
                         :compiler       {:output-to     "compiled/prod.js"
                                          :output-dir    "compiled/prod"
                                          :optimizations :advanced}}]

                :test-commands {"tests"      ["phantomjs" "test/bin/runner-none.js"       "compiled/test"       "compiled/test.js"]
                               "node-tests"  ["node"      "test-node/bin/runner-none.js"  "compiled/test-node"  "compiled/test-node.js"]}}
  
  :aliases {"auto-test" ["do" "clean," "cljsbuild" "auto" "test"]})

