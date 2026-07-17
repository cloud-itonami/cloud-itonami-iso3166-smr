(ns culture.facts
  "Country-level regional-culture catalog for San Marino (SMR) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  San Marino is thinly documented on English Wikipedia beyond the main
  country article: a dedicated Palio delle Balestre (crossbow festival)
  Wikipedia article could not be found -- only the San Marino tourism
  board's own site (sanmarinosite.com) covers it in detail, which is not
  cited here -- so no dedicated :festival entry for it exists; the Feast
  of Saint Marinus is used instead, sourced from the San_Marino article's
  own public-holidays table. This catalog stops at 5 entries rather than
  pad to the usual 6-10 (see README / commit message for the drop).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"SMR"
   [{:culture/id "smr.dish.torta-tre-monti"
     :culture/name "Torta Tre Monti"
     :culture/country "SMR"
     :culture/kind :dish
     :culture/summary "Traditional Sammarinese wafer-layer cake bound with chocolate or hazelnut creme and covered in chocolate fondant, its name (\"Cake of the Three Mountains/Towers\") representing the Three Towers of San Marino; the San Marino Wikipedia article calls it the country's best-known dish."
     :culture/url "https://en.wikipedia.org/wiki/Torta_Tre_Monti"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "smr.dish.piadina"
     :culture/name "Piadina"
     :culture/country "SMR"
     :culture/kind :dish
     :culture/summary "Flatbread described by the San Marino Wikipedia article as \"a dish characteristic of the Italian region of Romagna and of San Marino\" -- an honestly shared regional dish, not exclusive to San Marino."
     :culture/url "https://en.wikipedia.org/wiki/San_Marino"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "smr.beverage.sammarinese-wine"
     :culture/name "Sammarinese wine"
     :culture/country "SMR"
     :culture/kind :beverage
     :culture/summary "San Marino's small but profitable wine industry, producing red wines Brugneto and Tessano and white wines Biancale and Roncale from grapevines cultivated on its slopes for nearly 2000 years; about 6,500 hectolitres are produced yearly, roughly 85% sold domestically."
     :culture/url "https://en.wikipedia.org/wiki/Sammarinese_wine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "smr.festival.feast-of-saint-marinus"
     :culture/name "Feast of Saint Marinus and the Republic"
     :culture/country "SMR"
     :culture/kind :festival
     :culture/summary "National feast held 3 September, celebrating the traditional origin of the Republic of San Marino in AD 301, per the public-holidays table in the San Marino Wikipedia article."
     :culture/url "https://en.wikipedia.org/wiki/San_Marino"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "smr.heritage.historic-centre-mount-titano"
     :culture/name "San Marino Historic Centre and Mount Titano"
     :culture/country "SMR"
     :culture/kind :heritage
     :culture/summary "55-hectare property including Mount Titano and San Marino's historic centre -- fortification towers (Guaita, Cesta, Montale), walls, gates, a 19th-century basilica and Palazzo Pubblico; inscribed as a UNESCO World Heritage Site in 2008 as testimony to the continuity of a free republic since the Middle Ages."
     :culture/url "https://en.wikipedia.org/wiki/Monte_Titano"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-smr culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "SMR"))
                 " SMR entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
