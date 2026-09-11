(ns statute.facts
  "General-law compliance catalog for San Marino (SMR) -- extends this
  repo's existing `marketentry.facts` (public-procurement market-entry
  only, narrow scope) with a second, orthogonal catalog of statutes a
  company operating in this jurisdiction must generally track for
  compliance. Mirrors cloud-itonami-iso3166-jpn/-deu/-bgr/-aze/-alb/-arm/
  -atg/-ben/-and's `statute.facts` (ADR-2607141700,
  cloud-itonami-compliance-fact-federation).

  Every entry cites an OFFICIAL government-hosted URL -- never
  fabricated. San Marino's official legislative portal is
  consigliograndeegenerale.sm (the Consiglio Grande e Generale --
  Parliament -- itself), curl/pdftotext-verified 2026-07-23: every law
  below was served as a directly-hosted PDF from this domain and
  extracted with `pdftotext -layout`, no JS-SPA or TLS problems, so
  every citation is HIGH confidence, read directly from the law's own
  text, with two noted caveats:

  - The 'Company Law' and 'Law regulating licences...' PDFs are each
    headed '(UNOFFICIAL TRANSLATION)' / 'This text has been prepared
    only for the purpose of an easier consultation' -- i.e. the English
    consolidated reading copies hosted there are explicitly NOT
    themselves the legally authoritative text (the Bollettino Ufficiale
    remains sole authority); this is the same caveat a reader would
    face consulting any jurisdiction's consolidated-text service.
    Confidence is still HIGH because the portal is the Consiglio Grande
    e Generale's own official hosting, not a private third-party
    summarizer.
  - This iteration found a genuine internal inconsistency in San
    Marino's OWN official 'Raccolta Coordinata delle norme in materia
    di lavoro' (Coordinated Collection of Labour Regulations, updated
    to 24 December 2018, curl-fetched as a 302-page PDF and read): its
    own front-matter citation list (page 1) names the base labour
    statute 'Legge 7 febbraio 1961 n.7' (7 February), but the law's own
    body header, immediately before the law's text begins, reads
    'LEGGE 17 febbraio 1961 n. 7' (17 February) -- and an independent
    cross-reference in Legge 96/1999 Art. 11 (San Marino's own
    procurement law, see `marketentry.facts`) corroborates '17 February
    1961'. This catalog cites 17 February 1961 as authoritative (the
    law's own body header plus one independent corroborating citation
    outweigh a single front-matter list entry) and discloses the
    discrepancy honestly rather than silently picking one, the same
    discipline cloud-itonami-iso3166-and's catalog applied to a
    conflicting APDA founding-year claim.
  - Companies/commercial-entity law: Legge 23 febbraio 2006 n.47
    'Company Law' -- CONSOLIDATED TEXT, curl-fetched directly from
    consigliograndeegenerale.sm and read (Art. 1(1): 'the term \"Law\"
    means this Law...'; Art. 1(2): 'the term \"Register\" means the
    Register of Companies envisaged in the following Article 6'). This
    is San Marino's OWN domestic statute (San Marino is not an EU
    member and is not part of any supranational company-law
    instrument), governing both anonymous companies (S.p.A.-equivalent)
    and other company forms after Legge 98/2010 and Decree Law
    179/2010 repealed the older separate Anonymous Companies
    provisions. HIGH confidence.
  - Labour law: Legge 17 febbraio 1961 n. 7, 'Legge per la tutela del
    lavoro e dei lavoratori' (Law for the protection of labour and
    workers) -- fetched as part of the Consiglio Grande e Generale's
    own 'Raccolta Coordinata delle norme in materia di lavoro'
    (aggiornato al 24 dicembre 2018) PDF and read (body header quoted
    above; Titolo I originally governed trade-union registration but
    has since been marked '[ABROGATO]' / repealed, its substance moved
    to Legge 9 maggio 2016 n.59 'Freedom of association and union
    activity in workplaces, collective bargaining and right to strike',
    which this session separately confirmed via the Consiglio Grande e
    Generale's English legislative-texts index). This catalog cites
    Legge 7/1961 as the still-current FRAMEWORK labour statute (per its
    continued presence, current title and periodic amendment in the
    official 2018-updated Raccolta Coordinata) while honestly noting
    specific articles/titles have been repealed and superseded over six
    decades of amendment -- the same non-static-snapshot discipline
    cloud-itonami-iso3166-and applied when it found Llei 35/2008 had
    been replaced by Llei 31/2018. HIGH confidence on the citation,
    MEDIUM on 'still the governing framework in every particular' (this
    session read the front matter and Titolo I-III of a 302-page
    consolidated PDF, not all six decades of amendment in full).
  - Tax law: Legge 13 ottobre 1984 n. 91, 'Introduzione dell'Imposta
    Generale sui Redditi' (Introduction of the General Income Tax) --
    CONSOLIDATED TEXT, curl-fetched directly and read (title page +
    amendment history M1-M9 + Art. 29-30 on tax-return contents,
    including the 'codice operatore economico' business tax-registration
    number cited in `marketentry.facts`). HIGH confidence.
  - Financial-services law (the BCSM angle this vertical was asked to
    investigate): Legge 17 novembre 2005 n. 165, 'Law on companies and
    banking, financial and insurance services' (LISF) -- CONSOLIDATED
    TEXT, curl-fetched directly and read (Art. 1, definitions: lit. j)
    '\"Supervisory authority\": the Central Bank of the Republic of San
    Marino'; lit. l) '\"Central Bank\": the Central Bank of the Republic
    of San Marino'; lit. oo) '\"Statute of the Central Bank\": Law no.
    96 of 29 June 2005'). This document's own header states 'This
    document, drawn up by the Central Bank of the Republic of San
    Marino, is intended solely to facilitate the consultation of the
    consolidated text' -- i.e. this consolidated text is published BY
    the Banca Centrale della Repubblica di San Marino (BCSM) itself,
    the entity this vertical's task specifically named. This iteration
    separately curl-confirmed bcsm.sm resolves (HTTP 200, 2026-07-23)
    as the Central Bank's own live domain, but did NOT deep-read
    bcsm.sm's own site content beyond confirming reachability -- the
    substantive LISF citation above comes from the law text itself, not
    from bcsm.sm's own marketing/informational pages. Legge 96 of 29
    June 2005 (the Central Bank's own Statute) is named by LISF Art.
    1(oo) but was NOT itself independently fetched/read this session --
    disclosed honestly as an unverified secondary citation, not
    included in `catalog` below.

  A law not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of statute entries. `:statute/url` + `:statute/law-number`
  are the citation the governor requires before any compliance-fact
  proposal referencing this law can commit."
  {"SMR"
   [{:statute/id "smr.legge-company-law"
     :statute/title "Legge 23 febbraio 2006 n. 47 \"Company Law\" e successive modifiche"
     :statute/jurisdiction "SMR"
     :statute/kind :law
     :statute/law-number "Legge 23 febbraio 2006, n. 47"
     :statute/url "https://www.consigliograndeegenerale.sm/on-line/documento17134245.html"
     :statute/url-provenance :official-consigliograndeegenerale-sm
     :statute/enacted-date "2006-02-23"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:corporate-governance :incorporation}}
    {:statute/id "smr.legge-tutela-lavoro-lavoratori"
     :statute/title "Legge 17 febbraio 1961 n. 7, \"Legge per la tutela del lavoro e dei lavoratori\""
     :statute/jurisdiction "SMR"
     :statute/kind :law
     :statute/law-number "Legge 17 febbraio 1961, n. 7"
     :statute/url "https://www.consigliograndeegenerale.sm/on-line/documento17102853.html"
     :statute/url-provenance :official-consigliograndeegenerale-sm
     :statute/enacted-date "1961-02-17"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:labor :employment}}
    {:statute/id "smr.legge-imposta-generale-redditi"
     :statute/title "Legge 13 ottobre 1984 n. 91, \"Introduzione dell'Imposta Generale sui Redditi\""
     :statute/jurisdiction "SMR"
     :statute/kind :law
     :statute/law-number "Legge 13 ottobre 1984, n. 91"
     :statute/url "https://www.consigliograndeegenerale.sm/on-line/documento17134300.html"
     :statute/url-provenance :official-consigliograndeegenerale-sm
     :statute/enacted-date "1984-10-13"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:tax}}
    {:statute/id "smr.legge-imprese-servizi-bancari-finanziari-assicurativi"
     :statute/title "Legge 17 novembre 2005 n. 165, \"Legge sulle Imprese e sui Servizi Bancari, Finanziari e Assicurativi\" (LISF)"
     :statute/jurisdiction "SMR"
     :statute/kind :law
     :statute/law-number "Legge 17 novembre 2005, n. 165"
     :statute/url "https://www.consigliograndeegenerale.sm/on-line/documento17134299.html"
     :statute/url-provenance :official-consigliograndeegenerale-sm
     :statute/enacted-date "2005-11-17"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:financial-services :banking :investment}}]})

(defn spec-basis
  "The jurisdiction's statute vector, or nil -- nil means NO spec-basis
  for that jurisdiction yet."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report, same shape/discipline as `marketentry.facts/coverage`:
  never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-smr statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "SMR")) " SMR statutes seeded with an "
                 "official citation. Extend "
                 "`statute.facts/catalog`, never fabricate a law-id or URL.")})))

(defn by-topic
  "Statutes for `iso3` tagged with `topic` (e.g. :labor, :tax,
  :financial-services)."
  [iso3 topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis iso3)))
