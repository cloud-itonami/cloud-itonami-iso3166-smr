(ns culture.facts-test
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [culture.facts :as facts]))

(deftest smr-has-culture-basis
  (let [sb (facts/spec-basis "SMR")]
    (is (= 5 (count sb)))
    (is (= (count sb) (count (set (map :culture/id sb)))))
    (is (every? #(str/starts-with? (:culture/url %) "https://") sb))
    (is (every? #(= "SMR" (:culture/country %)) sb))
    (is (every? #(nil? (:culture/municipality %)) sb))
    (is (every? #(seq (:culture/summary %)) sb))
    (is (every? #(string? (:culture/retrieved-at %)) sb))))

(deftest unknown-jurisdiction-has-no-basis
  (is (nil? (facts/spec-basis "FRA")))
  (is (nil? (facts/spec-basis "zzz"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["SMR" "FRA"])]
    (is (= 2 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["FRA"] (:missing-jurisdictions c)))))

(deftest by-kind-filters
  (is (= 2 (count (facts/by-kind "SMR" :dish))))
  (is (= ["smr.heritage.historic-centre-mount-titano"]
         (mapv :culture/id (facts/by-kind "SMR" :heritage))))
  (is (empty? (facts/by-kind "SMR" :craft)))
  (is (empty? (facts/by-kind "FRA" :dish))))

(deftest tx-file-matches-catalog
  (let [tx (edn/read-string (slurp "data/culture-tx.edn"))
        flat (mapcat val (sort-by key facts/catalog))]
    (is (= (vec flat) (vec tx)))))
