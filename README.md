# cloud-itonami-iso3166-smr

**SMR**: San Marino.

- public procurement (Consiglio Grande e Generale, Legge 96/1999)
- Registro delle Società / business licensing (Camera di Commercio, Legge 47/2006 + Legge 129/2010)

AGPL-3.0-or-later.

## Market-entry / statute catalogs

Governed public-sector market-entry compliance actor, same architecture
as `cloud-itonami-iso3166-and`/`-lie`/`-mco`/`-jpn`/`-deu`/`-ben`/`-atg`:

- `src/marketentry/{facts,governor,phase,sim,operation,registry,store,
  marketentryllm}.cljc` -- the actor. `facts.cljc` cites Legge 17
  settembre 1999 n.96 (public procurement, no single named regulator --
  each contracting body acts directly, per Art.1), the Registro delle
  Società / Register of Companies (Legge 23 febbraio 2006 n.47), the
  business-operating licence (Legge 23 luglio 2010 n.129, a confirmed
  SEQUENTIAL two-act model for legal persons per its own Art.8) and the
  Codice Operatore Economico tax-registration number (Legge 13 ottobre
  1984 n.91 Art.30). `governor.cljc`'s flagship check
  (`non-resident-tax-address-missing`) independently verifies that a
  non-resident awarded bidder has established BOTH the San Marino tax
  address for service AND the Art.52 industrial-licence reference Legge
  96/1999 Art.11 (as amended by Legge 179/2005 Art.40) requires -- a
  residency-conditioned, two-part compliance-artifact gate, a check
  shape distinct from Andorra's two-axis EUR-threshold recompute,
  Liechtenstein's VAT-turnover fiscal-rep gate and Monaco's
  registry-membership boolean.
- `src/statute/facts.cljc` -- general-law catalog: Legge 47/2006
  (Company Law), Legge 17 febbraio 1961 n.7 (Legge per la tutela del
  lavoro e dei lavoratori, labour law), Legge 13 ottobre 1984 n.91
  (General Income Tax) and Legge 17 novembre 2005 n.165 (Legge sulle
  Imprese e sui Servizi Bancari, Finanziari e Assicurativi -- LISF,
  whose own consolidated text is published by the Banca Centrale della
  Repubblica di San Marino, BCSM, as the designated "Supervisory
  authority" per its own Art.1).

Every citation is curl/pdftotext-verified against an official source
(consigliograndeegenerale.sm, gov.sm, camcom.sm); see each namespace's
docstring for the full research trail and any honestly-narrowed scope
or disclosed gap (e.g. the negotiated-procedure EUR thresholds Legge
96/1999 Art.11 delegates to an unlocated implementing regulation are
deliberately NOT modeled/fabricated).

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for San Marino:

- `src/culture/facts.cljc` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one. San
Marino is thinly documented on English Wikipedia beyond the main country
article; this catalog carries 5 verified entries rather than pad with
unverifiable claims (see the `culture.facts` ns docstring for the dropped
Palio delle Balestre candidate).
