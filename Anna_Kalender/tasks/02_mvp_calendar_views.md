# Phase 2: MVP 2 - Kalender-Ansichten (Monat, Woche, Tag)

## Ziel
Erweiterung der App um die drei visuellen Kalender-Hauptansichten (Monat, Woche, Tag) mit intuitiver Navigation.

---

## Task List / Checkliste

- [x] **Task 2.1: Haupt-Navigation & Top/Bottom Bar**
  - **Beschreibung:** Navigationsleiste mit den 3 Tabs *"Monat"*, *"Woche"*, *"Tag"* und Floating Action Button (`+`) integrieren.
  - **Ergebnis:** Nahtloser Wechsel zwischen den Ansichten möglich.

- [x] **Task 2.2: Monatsansicht (`MonthViewScreen`)**
  - **Beschreibung:** Monats-Gitter (7 Spalten für Wochentage Mo–So) umsetzen.
  - **Features:** Hervorhebung des heutigen Tages, Punkte/Indikatoren an Tagen mit Terminen, Vor-/Zurück-Blättern zwischen Monaten.
  - **Ergebnis:** Visuelle Übersicht des aktuellen Monats.

- [x] **Task 2.3: Wochenansicht (`WeekViewScreen`)**
  - **Beschreibung:** 7-Tage-Spaltenansicht mit Uhrzeit-Raster.
  - **Features:** Positionierung von Terminen anhand ihrer Start- und Endzeitpunkte.
  - **Ergebnis:** Übersichtliche Wochenplanung.

- [x] **Task 2.4: Tagesansicht (`DayViewScreen`)**
  - **Beschreibung:** 24-Stunden-Zeitleiste für den ausgewählten Tag.
  - **Features:** Chronologische Termin-Karten, Klick auf Stunde öffnet vorgefüllten Erstell-Dialog für diese Uhrzeit.
  - **Ergebnis:** Detaillierte Tagesübersicht.

---

## Viable Product Kriterium für Phase 2
Der Benutzer kann zwischen Monats-, Wochen- und Tagesansicht wechseln, Termine grafisch im Zeitraster sehen und durch Antippen eines Tages/einer Uhrzeit neue Termine anlegen.
