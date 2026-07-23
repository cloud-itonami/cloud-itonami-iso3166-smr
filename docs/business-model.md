# Business model — SMR

Independent public-sector market-entry & procurement compliance service
for San Marino.

- Legge 17 settembre 1999 n.96 (public procurement) -- no single named
  procurement regulator or e-procurement portal; each contracting body
  ("the State or another public Entity, having autonomous
  representation", Art. 1) acts directly. Public notices are aggregated
  on gov.sm under "Bandi, Appalti e Avvisi Pubblici" (Dipartimento
  Funzione Pubblica).
- Registro delle Società / Register of Companies (Legge 23 febbraio
  2006 n.47) + business-operating licence (Legge 23 luglio 2010 n.129)
  -- both administered by the Camera di Commercio (Agenzia per lo
  Sviluppo Economico -- Camera di Commercio S.p.A.), via the
  registroimprese.camcom.sm portal and the "Autorizzazioni ad operare"
  service respectively.
- Codice Operatore Economico tax-registration number (Legge 13 ottobre
  1984 n.91 Art. 30) -- Ufficio Tributario / Segreteria di Stato per le
  Finanze e il Bilancio.

## Trust Controls

Any actual portal registration or filing submission requires
Market-Entry Compliance Governor clearance and ALWAYS escalates to
human sign-off (`:filing/draft`/`:filing/submit` never auto-commit at
any rollout phase, see `src/marketentry/phase.cljc`). A false or
fabricated regulatory-requirement claim is a HARD hold, not overridable
by human approval. The flagship check
(`non-resident-tax-address-missing`, see `src/marketentry/governor.cljc`)
independently re-verifies -- rather than trusts the advisor's own claim
-- that a non-resident awarded bidder has established the San Marino
tax address for service and industrial-licence reference Legge 96/1999
Art. 11 requires before a `:filing/submit` may proceed.
