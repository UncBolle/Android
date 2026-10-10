# Phase 1: MVP 1 - Kern-Datenbank & Einfache Terminverwaltung (CRUD)

## Ziel
Eine lauffähige erste Version (MVP 1), in der Termine lokal in einer Room-Datenbank gespeichert, angezeigt, bearbeitet und gelöscht werden können.

---

## Task List / Checkliste

- [x] **Task 1.1: Projektstruktur & Abhängigkeiten einrichten**
  - **Beschreibung:** Android-Projekt mit Kotlin und Jetpack Compose konfigurieren.
  - **Abhängigkeiten:** Room (`androidx.room`), Navigation Compose, Coroutines, Material 3, Glance.
  - **Ergebnis:** Aufbau der Gradle-Konfiguration (`settings.gradle.kts`, `build.gradle.kts`, `libs.versions.toml`) und Ordnerstruktur.

- [x] **Task 1.2: Room-Datenmodell & DAO erstellen**
  - **Beschreibung:** `CalendarEvent`-Entität sowie `CalendarEventDao` und `AppDatabase` implementieren.
  - **Felder:** `id`, `title`, `description`, `startTimestamp`, `endTimestamp`, `isAllDay`, `colorHex`, `location`.
  - **Ergebnis:** Lokale Room-Datenbank vollständig eingerichtet.

- [x] **Task 1.3: Repository & ViewModel aufbauen**
  - **Beschreibung:** `CalendarRepository` und `CalendarViewModel` erstellen.
  - **Logik:** CRUD-Operationen an die DB, Validierung (Pflichtfeld Titel), StateFlow für UI.
  - **Ergebnis:** ViewModel stellt reaktive Daten (StateFlow) bereit.

- [x] **Task 1.4: Einfache Termin-Liste & Formular (Compose UI)**
  - **Beschreibung:** Erstellen von `EventListScreen` und `AddEditEventDialog`.
  - **UI-Komponenten:** TextFields für Titel, Beschreibung, Standort; Ganztägig-Switch; Speicher-, Abbrechen- & Löschen-Buttons.
  - **Ergebnis:** Lauffähige App – Termine können eingegeben, in der Liste angezeigt, bearbeitet und gelöscht werden.

---

## Viable Product Kriterium für Phase 1 (Erfüllt ✅)
Die App lässt sich starten, speichert neue Termine in Room ab und listet sie chronologisch in einer einfachen Listenansicht auf.
