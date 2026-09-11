(ns marketentry.facts-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.facts :as facts]))

(deftest smr-has-spec-basis
  (let [sb (facts/spec-basis "SMR")]
    (is (some? sb))
    (is (string? (:provenance sb)))
    (is (seq (:required-evidence sb)))
    (is (some? (facts/corporate-number-spec-basis "SMR")))
    (is (some? (facts/non-resident-tax-address-spec-basis "SMR")))
    (is (some? (facts/operating-licence-spec-basis "SMR")))))

(deftest smr-rep-spec-basis-is-honestly-present
  (testing "Company Law 47/2006 Art.1(9) 'Unfit Person' definition, cross-referenced by Licences Law 129/2010 Art.7(1)(f) -- a real, on-point finding, not force-fit"
    (is (some? (facts/rep-spec-basis "SMR")))))

(deftest smr-direct-contracting-threshold-is-honestly-nil
  (testing "Legge 96/1999 Art.11 delegates negotiated-procedure EUR thresholds to an unlocated implementing regulation -- this catalog does not fabricate a number"
    (is (nil? (facts/direct-contracting-threshold-spec-basis "SMR")))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest required-evidence-satisfied
  (let [sb (facts/spec-basis "SMR")
        all (:required-evidence sb)]
    (is (true? (facts/required-evidence-satisfied? "SMR" all)))
    (is (not (facts/required-evidence-satisfied? "SMR" (take 1 all))))
    (is (nil? (facts/required-evidence-satisfied? "ATL" all)))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["SMR" "USA" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 2 (:covered c)))
    (is (= ["ATL"] (:missing-jurisdictions c)))))
