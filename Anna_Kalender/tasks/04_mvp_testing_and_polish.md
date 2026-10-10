# Phase 4: MVP 4 - Qualitätssicherung, Automatisierte Tests & Polish

## Ziel
Vollständige Abdeckung mit automatisierten Tests (Room DAO, ViewModel, Compose UI, Widget) und finale Qualitätsprüfung (Sprache & Design).

---

## Task List / Checkliste

- [x] **Task 4.1: Room DAO Unit Tests umsetzen (`CalendarEventDaoTest`)**
  - **Beschreibung:** Ausführen der Tests DAO-01 bis DAO-05 in `src/test/java/` mit In-Memory DB.
  - **Ergebnis:** Grüner Testlauf für alle Datenbank-CRUD-Operationen.

- [x] **Task 4.2: ViewModel & Validierungs-Tests (`CalendarViewModelTest`)**
  - **Beschreibung:** Tests VM-01 bis VM-03 für Titel-Validierung, Ganztägig-Zeiten und deutsche Datumsformatierung.
  - **Ergebnis:** Grüner Testlauf für die Geschäftslogik.

- [x] **Task 4.3: Compose UI & Widget Tests (`ComposeUITest` & `CalendarWidgetTest`)**
  - **Beschreibung:** Tests UI-01 bis UI-04 und WGT-01 bis WGT-03 mit `ComposeTestRule` auf dem Emulator.
  - **Ergebnis:** Automatisierte Bestätigung von Navigation, Formular-Ablauf, Lösch-Dialog und Widget-Rendering.

- [x] **Task 4.4: UI-Polish & Deutsche Sprachprüfung**
  - **Beschreibung:** Überprüfung aller UI-Texte auf Deutsch, Prüfung von Light/Dark Theme Farbschemas.
  - **Ergebnis:** Production-Ready App-Version.

---

## Viable Product Kriterium für Phase 4
Alle automatisierten Tests laufen in Android Studio fehlerfrei durch (`BUILD SUCCESSFUL`). Die App ist vollständig poliert, auf Deutsch und einsatzbereit.
