(ns cljs-test-none.core-test
  (:require-macros [cemerick.cljs.test :refer (is are deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test]
            [cljs-test-none.core :as core]))


(deftest test-equals
  (is (= (core/return5) 5)))

;;; needed to use the :target :nodejs
(set! *main-cli-fn* #())              ;;  <---------------
