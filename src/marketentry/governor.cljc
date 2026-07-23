(ns marketentry.governor
  "Market-Entry Compliance Governor -- the independent compliance layer
  that earns the MarketEntry-LLM the right to commit. The LLM has no
  notion of San Marino procurement law, whether a claimed engagement fee
  actually equals base + months x rate, whether a non-resident awarded
  bidder has actually established the San Marino tax address for
  service Legge 96/1999 Art. 11 requires, whether a business-operating
  licence (Legge 129/2010) has been verified for a filing that requires
  one, or when a draft stops being a draft and becomes a real-world
  portal submission, so this MUST be a separate system able to *reject*
  a proposal and fall back to HOLD.

  `:itonami.blueprint/governor` is `:market-entry-compliance-governor`
  (shared family keyword on blueprints).

  This blueprint's own text (docs/business-model.md Trust Controls:
  'any actual portal registration or filing submission requires
  Market-Entry Compliance Governor clearance and always escalates to
  human sign-off'; 'a false or fabricated regulatory-requirement claim
  is a HARD hold') names exactly the checks below.

  Seven checks, in priority order, ALL HARD violations: a human
  approver CANNOT override them. The confidence/actuation gate is
  SOFT: it asks a human to look (low confidence / actuation), and the
  human may approve -- but see `marketentry.phase`: for `:stake
  :actuation/draft-filing`/`:actuation/submit-filing` NO phase ever
  allows auto-commit either. Two independent layers agree that
  actuation is always a human call.

    1. Spec-basis                  -- did the jurisdiction proposal cite
                                       an OFFICIAL source
                                       (`marketentry.facts`), or invent
                                       one?
    2. Evidence incomplete         -- for `:filing/draft`/
                                       `:filing/submit`, has the
                                       jurisdiction actually been
                                       assessed with a full evidence
                                       checklist on file?
    3. Non-resident tax address
       missing                      -- for `:filing/submit`,
                                       INDEPENDENTLY check that a
                                       non-resident awarded bidder has
                                       established BOTH the required San
                                       Marino tax address for service
                                       AND the Art. 52 industrial-licence
                                       reference (Legge 96/1999 Art. 11,
                                       as amended by Legge 179/2005 Art.
                                       40). FLAGSHIP genuinely new check
                                       for the iso3166 family
                                       (grep-verified absent as a
                                       governor check function name
                                       fleet-wide at build time) -- a
                                       RESIDENCY-CONDITIONED, TWO-PART
                                       compliance-artifact-presence
                                       gate, a check SHAPE distinct from
                                       Andorra's two-axis EUR-threshold
                                       recompute, Liechtenstein's
                                       VAT-turnover-threshold fiscal-rep
                                       gate and Monaco's registry-
                                       membership boolean read (see
                                       `marketentry.registry` docstring
                                       for the full comparison).
    4. Engagement fee mismatch     -- for `:filing/submit`,
                                       INDEPENDENTLY recompute whether
                                       the engagement's own `:claimed-
                                       fee` equals `base-fee +
                                       monthly-rate x monitoring-
                                       months` -- honest reapplication
                                       of the ground-truth-recompute
                                       discipline sibling actors use.
    5. Operating licence
       unverified                   -- for `:filing/submit`, when the
                                       engagement declares
                                       `:requires-operating-licence?
                                       true`, INDEPENDENTLY check
                                       `:operating-licence-verified?`.
                                       CONDITIONAL on the engagement's
                                       own ground truth. Grounded in
                                       Legge 23 luglio 2010 n.129 Art. 2
                                       (any industrial/service/
                                       handicraft/commercial activity
                                       requires a specific licence) --
                                       a SEPARATE legal act from Company
                                       Law 47/2006 registration, though
                                       Art. 8 of the SAME licences law
                                       conditions the legal-person
                                       licence on prior Company Law
                                       registration (a confirmed
                                       sequential two-act model).
    6. Confidence floor / actuation
       gate                          -- LLM confidence below threshold,
                                       OR the op is `:filing/draft`/
                                       `:filing/submit` (REAL acts)
                                       -> escalate.

  Two more guards, double-draft/double-submit prevention, are enforced
  off dedicated `:drafted?`/`:submitted?` facts (never a `:status`
  value)."
  (:require [marketentry.facts :as facts]
            [marketentry.registry :as registry]
            [marketentry.store :as store]))

(def confidence-floor 0.6)

(def high-stakes
  "Stakes grave enough to always require a human, even when clean.
  Drafting a real portal package and submitting a real portal
  registration are the two real-world actuation events this actor
  performs."
  #{:actuation/draft-filing :actuation/submit-filing})

;; ----------------------------- checks -----------------------------

(defn- spec-basis-violations
  "A `:jurisdiction/assess` (or `:filing/draft`/`:filing/submit`)
  proposal with no spec-basis citation is a HARD violation -- never
  invent a jurisdiction's market-entry requirements."
  [{:keys [op]} proposal]
  (when (contains? #{:jurisdiction/assess :filing/draft :filing/submit} op)
    (let [value (:value proposal)]
      (when (or (empty? (:cites proposal))
                (and (contains? value :spec-basis) (nil? (:spec-basis value))))
        [{:rule :no-spec-basis
          :detail "公式spec-basisの引用が無い提案は法域要件として扱えない"}]))))

(defn- evidence-incomplete-violations
  "For `:filing/draft`/`:filing/submit`, the jurisdiction's required
  registration evidence must actually be satisfied."
  [{:keys [op subject]} st]
  (when (contains? #{:filing/draft :filing/submit} op)
    (let [e (store/engagement st subject)
          assessment (store/assessment-of st subject)]
      (when-not (and assessment
                     (facts/required-evidence-satisfied?
                      (:jurisdiction e) (:checklist assessment)))
        [{:rule :evidence-incomplete
          :detail "法域の必要書類(Registro delle Società/Codice Operatore Economico/事業活動免許/代理人確認等)が充足していない状態での提案"}]))))

(defn- non-resident-tax-address-missing-violations
  "For `:filing/submit`, INDEPENDENTLY check that a non-resident awarded
  bidder has established BOTH the San Marino tax address for service
  AND the Art. 52 industrial-licence reference -- the flagship
  genuinely new check this vertical adds. Evaluated for EVERY
  `:filing/submit`; the check itself is conditional on the engagement's
  own declared `:non-resident?` flag (a resident bidder never
  triggers), the same 'evaluated unconditionally, gated internally on
  ground truth' shape Andorra's NRT check and this vertical's own
  operating-licence check use."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (registry/non-resident-tax-address-missing? e)
        [{:rule :non-resident-tax-address-missing
          :detail (str subject " は非居住入札者(non-resident?=" (:non-resident? e)
                      ")だがサンマリノ域内の税務送達先住所(sm-tax-address-established?="
                      (:sm-tax-address-established? e)
                      ")又はLegge7/1961 Art.52産業免許参照(industrial-licence-referenced?="
                      (:industrial-licence-referenced? e)
                      ")の少なくとも一方が未確立 -- Legge96/1999 Art.11(Legge179/2005 Art.40改正)違反")}]))))

(defn- engagement-fee-mismatch-violations
  "For `:filing/submit`, INDEPENDENTLY recompute whether the
  engagement's own claimed fee equals base + months x rate."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when-not (registry/engagement-fee-matches-claim? e)
        [{:rule :engagement-fee-mismatch
          :detail (str subject " の申告手数料(" (:claimed-fee e)
                      ")が独立再計算値(" (registry/compute-engagement-fee e) ")と一致しない")}]))))

(defn- operating-licence-unverified-violations
  "For `:filing/submit`, when the engagement declares
  `:requires-operating-licence? true`, INDEPENDENTLY check
  `:operating-licence-verified?` -- CONDITIONAL on the engagement's own
  ground truth. Grounded in Legge 23 luglio 2010 n.129 Art. 2 (any
  industrial/service/handicraft/commercial activity requires a specific
  licence)."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-operating-licence? e))
                 (not (true? (:operating-licence-verified? e))))
        [{:rule :operating-licence-unverified
          :detail (str subject " は事業活動免許(Legge129/2010 Art.2)確認を要するが未確認 -- 提出提案は進められない")}]))))

(defn- already-drafted-violations
  "For `:filing/draft`, refuses to draft the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/draft)
    (when (store/engagement-already-drafted? st subject)
      [{:rule :already-drafted
        :detail (str subject " は既にドラフト済み")}])))

(defn- already-submitted-violations
  "For `:filing/submit`, refuses to submit the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (when (store/engagement-already-submitted? st subject)
      [{:rule :already-submitted
        :detail (str subject " は既に提出済み")}])))

(defn check
  "Censors a MarketEntry-LLM proposal against the governor rules.
  Returns {:ok? bool :violations [..] :confidence c :escalate? bool
  :high-stakes? bool :hard? bool}."
  [request _context proposal st]
  (let [hard (into []
                   (concat (spec-basis-violations request proposal)
                           (evidence-incomplete-violations request st)
                           (non-resident-tax-address-missing-violations request st)
                           (engagement-fee-mismatch-violations request st)
                           (operating-licence-unverified-violations request st)
                           (already-drafted-violations request st)
                           (already-submitted-violations request st)))
        conf (:confidence proposal 0.0)
        low? (< conf confidence-floor)
        stakes? (boolean (high-stakes (:stake proposal)))
        hard? (boolean (seq hard))]
    {:ok?          (and (not hard?) (not low?) (not stakes?))
     :violations   hard
     :confidence   conf
     :hard?        hard?
     :escalate?    (and (not hard?) (or low? stakes?))
     :high-stakes? stakes?}))

(defn hold-fact
  "The audit fact written when a proposal is rejected (HOLD)."
  [request context verdict]
  {:t          :governor-hold
   :op         (:op request)
   :actor      (:actor-id context)
   :subject    (:subject request)
   :disposition :hold
   :basis      (mapv :rule (:violations verdict))
   :violations (:violations verdict)
   :confidence (:confidence verdict)})
