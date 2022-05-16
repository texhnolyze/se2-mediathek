# Workflow Vormerken

Das UI (AusleiheUI/AusleiheMedienAuflisterUI) zeigt Kunden und Medien an
-> vormerken von Medium nur möglich, wenn noch nicht von 3 Kunden vorgemerkt (Aktualisierung passiert über registrieren von Beobachtern)
-> Kunde und Medium wird zum Vormerken selektiert
-> falls noch nicht 3 mal vorgemerkt und noch nicht vom Kunden vorgemerkt, wird über Buttoninteraktion vorgemerkt (istVormerkenMoeglich)
-> call von VormerkWerkzeug.merkeAusgewaehlteMedienVor()
  -> holt sich ausgewaehlten Kunden/Medium
-> call von VormerkService.merkeVor(medium, kunde)
  -> VormerkService holt sich VormerkKarten für Medium aus seiner Map (_vormerkungen)
    -> Falls VormerkKarte nicht existiert wird eine neue erstellt (mit MediumId und Kunde)
    -> Falls VormerkKarte existiert wird diese weiter genutzt
