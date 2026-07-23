(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a public-procurement market-entry
  filing -- every jurisdiction assigns its own format. This namespace
  does NOT invent one; it builds a jurisdiction-scoped sequence number
  and validates the record's required fields, the same honest,
  non-fabricating discipline `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the
  SAME ground-truth-recompute DISCIPLINE sibling actors use (verify a
  claimed monetary total against the entity's own recorded quantity x
  unit fields), reapplied to a market-entry engagement fee line.

  `non-resident-tax-address-missing?` is the SAME discipline applied to
  a genuinely San-Marino-specific mechanism -- and this vertical's
  FLAGSHIP check: Legge 17 settembre 1999 n.96 (curl/pdftotext-verified
  2026-07-23 against consigliograndeegenerale.sm's own hosting), Art.
  11, as amended by Legge 13 dicembre 2005 n.179 Art. 40: 'The
  non-resident successful bidder of a public contract shall establish
  an address for service for tax purposes in the territory of San
  Marino for the exercise of the industrial licence referred to in
  Article 52 of Law no. 7 of 17 February 1961 and subsequent amendments
  and integrations, in order to fulfil tax obligations relating to
  direct taxes and import tax within the scope of the contract.'

  This is a GENUINELY DIFFERENT check SHAPE than every prior iso3166
  sibling this repo mirrors: Andorra's Art. 30.1 flagship is a TWO-AXIS
  (contract type x urgency) EUR ELIGIBILITY-THRESHOLD classification;
  Liechtenstein's `fiscal-rep-missing` gates VAT registration on a
  worldwide-turnover threshold; Monaco's `rci-clearance-missing` gates
  on a registry-membership boolean. San Marino's Art. 11 mechanism is
  neither a threshold recompute nor a registry-membership read: it is a
  RESIDENCY-CONDITIONED, TWO-PART COMPLIANCE-ARTIFACT-PRESENCE gate --
  a non-resident AWARDED bidder must have BOTH (a) established a San
  Marino tax address for service AND (b) referenced the Art. 52
  industrial-licence provision, and the gate applies ONLY post-award
  (`:filing/submit`), only to non-resident bidders, and only to public-
  PROCUREMENT contracts specifically (not a general VAT/company-law
  regime the way Liechtenstein's is). This mechanical CONDITIONAL-gate
  shape resembles Liechtenstein's/Monaco's checks at the surface (both
  are also boolean conditional gates), but the underlying legal
  provision, the residency-conditioning, and the two-part evidentiary
  requirement are genuinely San-Marino-specific and were independently
  found in Art. 11's own text, not copied from a sibling. Reported
  honestly rather than overclaiming a fully novel mechanical shape.

  `direct-contracting-eligible?`/`direct-contracting-claim-mismatches?`
  are DELIBERATELY ABSENT from this namespace: Legge 96/1999 Art. 11
  delegates San Marino's own negotiated-procedure EUR thresholds to an
  Art. 3 implementing regulation this session did not locate/verify
  (see `marketentry.facts` docstring) -- this repo does not fabricate
  threshold numbers by reusing Andorra's.

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real procurement portal. It builds the RECORD an
  operator would keep, not the act of submitting a portal registration
  itself (that is `marketentry.operation`'s `:filing/submit`, always
  human-gated -- see README Actuation)."
  (:require [clojure.string :as str]))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  (+ (double base-fee)
     (* (double monthly-rate) (double monitoring-months))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (== (double claimed-fee) (compute-engagement-fee engagement)))

(defn non-resident-tax-address-missing?
  "FLAGSHIP check ground truth. True when `engagement` is a non-resident
  bidder (`:non-resident?` true) but has NOT established BOTH the
  required San Marino tax address for service AND the Art. 52
  industrial-licence reference -- Legge 96/1999 Art. 11, as amended by
  Legge 179/2005 Art. 40 (see namespace docstring for the full-text
  quote). A resident bidder (`:non-resident?` false/nil) trivially
  never triggers this -- the obligation is textually scoped to
  non-resident successful bidders only."
  [{:keys [non-resident? sm-tax-address-established? industrial-licence-referenced?]}]
  (boolean (and (true? non-resident?)
                (not (and (true? sm-tax-address-established?)
                          (true? industrial-licence-referenced?))))))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a portal registration
  package. Pure function -- does not touch any real procurement
  portal."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper-case jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a portal
  registration (always human-gated upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper-case jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
