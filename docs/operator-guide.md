# Operator guide — SMR

Human-gated filing only. `:filing/draft` and `:filing/submit` always
interrupt for human approval, at every rollout phase (see
`src/marketentry/phase.cljc`), regardless of the Market-Entry
Compliance Governor's verdict.

Portal: public procurement notices are aggregated at gov.sm under
"Bandi, Appalti e Avvisi Pubblici" (no dedicated e-procurement platform
domain was found this session). Company registration and the
business-operating licence are both handled by the Camera di Commercio
at registroimprese.camcom.sm.

Before drafting or submitting a filing, confirm:

- the jurisdiction has an official spec-basis (`marketentry.facts`)
- the evidence checklist (Registro delle Società / operating licence /
  Codice Operatore Economico / authorized-representative) is complete
- for non-resident bidders: the San Marino tax address for service and
  the Art. 52 industrial-licence reference are both established
  (Legge 96/1999 Art. 11)
- the claimed engagement fee equals base-fee + monthly-rate x
  monitoring-months
- if the engagement requires an operating licence, it is verified
  (Legge 129/2010)
