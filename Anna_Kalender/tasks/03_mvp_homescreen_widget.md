# Phase 3: MVP 3 - Android Startbildschirm App-Widget (Glance)

## Ziel
Entwicklung eines Android Homescreen Widgets auf Basis von Jetpack Glance, das die heutigen Termine direkt auf dem Startbildschirm anzeigt.

---

## Task List / Checkliste

- [x] **Task 3.1: Jetpack Glance Abhängigkeiten & Receiver konfigurieren**
  - **Beschreibung:** Glance-Bibliothek in `build.gradle` aufnehmen und `CalendarWidgetReceiver` in `AndroidManifest.xml` deklarieren.
  - **Ergebnis:** Android erkennt das neue Widget in der Widget-Auswahl.

- [x] **Task 3.2: Widget UI & Datensynchronisation (`CalendarGlanceWidget`)**
  - **Beschreibung:** Glance-Composable bauen.
  - **Features:** Anzeige des heutigen Datums, Liste der Termine für heute, Anzeige von *"Keine Termine heute"* bei leerem Tag.
  - **Ergebnis:** Widget rendert Termine korrekt auf dem Homescreen.

- [x] **Task 3.3: Widget Klick-Aktionen & Auto-Update**
  - **Beschreibung:** Intents einrichten: Klick auf Widget öffnet App in der Tagesansicht, Klick auf `+`-Icon öffnet direkt das Erstellformular.
  - **Features:** Auto-Update auslösen, wenn ein Termin in Room hinzugefügt/gelöscht wird.
  - **Ergebnis:** Interaktives Widget mit Direktzugriffen.

---

## Viable Product Kriterium für Phase 3
Das Widget lässt sich auf dem Startbildschirm platzieren, zeigt die heutigen Termine live an und öffnet bei Klick die App bzw. das Erstellformular.
