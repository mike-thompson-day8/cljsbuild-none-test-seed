(ns cljs-test-none.another-test
  (:require-macros [cemerick.cljs.test :refer (is are deftest with-test run-tests testing test-var)])
  (:require [cemerick.cljs.test]
            [clojure.string :as string]))


(deftest test-equals
  (is (= 1 1))
  (is (= 5 5)))


(deftest test-join
  (is  (= "ab" (string/join ["a" "b"]))))

