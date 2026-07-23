(ns statute.facts-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [statute.facts :as facts]))

(deftest smr-has-spec-basis
  (let [sb (facts/spec-basis "SMR")]
    (is (= 4 (count sb)))
    (is (every? #(str/starts-with? (:statute/url %) "https://") sb))
    (is (every? :statute/law-number sb))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["SMR" "JPN" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["ATL" "JPN"] (:missing-jurisdictions c)))))

(deftest by-topic-filters
  (is (= ["smr.legge-tutela-lavoro-lavoratori"]
         (mapv :statute/id (facts/by-topic "SMR" :labor))))
  (is (= ["smr.legge-imprese-servizi-bancari-finanziari-assicurativi"]
         (mapv :statute/id (facts/by-topic "SMR" :financial-services))))
  (is (empty? (facts/by-topic "SMR" :environment)))
  (is (empty? (facts/by-topic "ATL" :labor))))
