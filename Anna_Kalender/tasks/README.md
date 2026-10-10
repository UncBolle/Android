# Aufgaben-Übersicht & Iterativer Entwicklungsplan

Die Spezifikation ([`spec.md`](file:///C:/Users/Mathias/StudioProjects/Android/Anna_Kalender/spec.md)) und der Testplan ([`test_cases.md`](file:///C:/Users/Mathias/StudioProjects/Android/Anna_Kalender/test_cases.md)) wurden in 4 aufeinander aufbauende Phasen unterteilt. Nach jeder Phase entsteht ein voll funktionsfähiges Teilprodukt (*Minimum Viable Product* / MVP).

---

## Phasen-Übersicht

1. **[Phase 1: MVP 1 - Kern-Datenbank & Einfache Terminverwaltung (CRUD)](file:///C:/Users/Mathias/StudioProjects/Android/Anna_Kalender/tasks/01_mvp_core_database_crud.md)**
   - Room-Datenbank, `CalendarEvent`-Modell, ViewModel, einfache Listenansicht & Formular.
   - *Viable Product 1:* Speichern, Bearbeiten, Auflisten und Löschen von Terminen.

2. **[Phase 2: MVP 2 - Kalender-Ansichten (Monat, Woche, Tag)](file:///C:/Users/Mathias/StudioProjects/Android/Anna_Kalender/tasks/02_mvp_calendar_views.md)**
   - Navigation Bar, Monats-Gitter, Wochen-Raster und 24h-Tagesansicht.
   - *Viable Product 2:* Vollständige visuelle Kalendernavigation.

3. **[Phase 3: MVP 3 - Android Startbildschirm App-Widget (Glance)](file:///C:/Users/Mathias/StudioProjects/Android/Anna_Kalender/tasks/03_mvp_homescreen_widget.md)**
   - Jetpack Glance Widget mit heutiger Terminübersicht und Schnellzugriffen.
   - *Viable Product 3:* Widget auf dem Startbildschirm mit Live-Datenupdate.

4. **[Phase 4: MVP 4 - Qualitätssicherung, Automatisierte Tests & Polish](file:///C:/Users/Mathias/StudioProjects/Android/Anna_Kalender/tasks/04_mvp_testing_and_polish.md)**
   - Automatische Room-Tests, ViewModel-Tests, Compose UI-Tests und Widget-Tests.
   - *Viable Product 4:* Vollständige, getestete Release-Version.
