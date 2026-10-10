# Spezifikation: Simple Kalender-App (Android)

## 1. Übersicht
Eine einfache, performante und rein lokale Kalender-Anwendung für Android-Geräte, entwickelt in Kotlin mit Jetpack Compose (Material 3). Die Benutzeroberfläche ist vollständig auf Deutsch gehalten.

---

## 2. Zielplattform & Technologie-Stack
- **Betriebssystem:** Android (Min SDK 24+, Target SDK 35/aktuell)
- **Programmiersprache:** Kotlin
- **UI-Framework:** Jetpack Compose mit Material Design 3
- **App-Widget:** Jetpack Glance (Compose-basiertes App-Widget)
- **Architektur:** MVVM / Clean Architecture (ViewModel, StateFlow, Repository)
- **Datenbank / Lokaler Speicher:** Room Database (SQLite)
- **Sprache / Lokalisierung:** Ausschließlich Deutsch (`de-DE`)

---

## 3. Funktionale Anforderungen

### 3.1 Kalender-Ansichten
1. **Monatsansicht (Month View):**
   - Übersicht des gewählten Monats im Gitter-Layout.
   - Hervorhebung des heutigen Tages.
   - Indikatoren/Punkte für Tage mit eingetragenen Terminen.
   - Wechsel zwischen Monaten (Navigation Vor/Zurück & Wischen).
   - Tippen auf einen Tag öffnet die Tagesansicht oder eine Detailauswahl.

2. **Wochenansicht (Week View):**
   - Darstellung der 7 Tage der aktuellen Woche.
   - Zeitleiste für Tageszeiten.
   - Übersichtliche Anzeige von Terminen innerhalb des Zeitrasters.

3. **Tagesansicht (Day View):**
   - Detaillierte Ansicht der 24 Stunden eines Tages.
   - Chronologische Auflistung aller Termine für diesen Tag.
   - Schnellzugriff zum Erstellen neuer Termine an bestimmten Uhrzeiten.

4. **Navigation zwischen Ansichten:**
   - Navigation Bar oder Tab-Leiste zum einfachen Wechseln zwischen Monats-, Wochen- und Tagesansicht.

---

### 3.2 Terminverwaltung (CRUD)
- **Termin erstellen & bearbeiten:**
  - **Titel:** Pflichtfeld (z. B. "Zahnarzttermin")
  - **Beschreibung / Notiz:** Optionales Freitextfeld
  - **Datum & Uhrzeit:** Start- und Endzeitpunkt (mit DatePicker & TimePicker)
  - **Ganztägig:** Switch/Checkbox (deaktiviert Uhrzeitauswahl)
  - **Farbe / Kategorie:** Auswählbare Farbmarkierung (z. B. Arbeit, Privat, Sport, Wichtig)
  - **Standort:** Optionales Textfeld (z. B. "Musterstraße 1, Berlin")
- **Termin löschen:** Bestätigungsdialog vor dem endgültigen Löschen.
- **Terminansicht / Details:** Tippen auf einen Termin öffnet eine Detailkarte oder einen Dialog.

---

### 3.3 Homescreen Widget (App-Widget)
- **Tagesübersicht auf dem Startbildschirm:**
  - Zeigt das aktuelle Datum (z. B. *"Heute, 24. Mai"*) und die anstehenden Termine des Tages.
  - Zeigt einen leeren Status (*"Keine Termine heute"*), falls keine Einträge vorliegen.
- **Schnellzugriffe (Intents):**
  - Tippen auf das Widget öffnet die App in der Tagesansicht des heutigen Tages.
  - Tippen auf ein `+`-Symbol im Widget öffnet direkt den Dialog zum Erstellen eines neuen Termins.
- **Automatische Aktualisierung:** Das Widget aktualisiert sich automatisch bei Änderungen in der Datenbank sowie um Mitternacht zum Tageswechsel.

---

### 3.4 Datenhaltung & Datenschutz
- **Lokale Speicherung:** Alle Termine werden lokal auf dem Smartphone in einer SQLite-Datenbank (Room) gespeichert.
- **Keine Cloud / Kein Netzwerk:** Keine Synchronisation mit externen Kalendern oder Servern.
- **Keine Erinnerungen / Push-Benachrichtigungen:** Auf Benachrichtigungs-Systeme/Alarme wird verzichtet.

---

## 4. UI / UX Design & Sprache
- **Sprache:** Reine deutsche Benutzeroberfläche (Datumsformate wie `TT.MM.JJJJ`, Wochentage `Mo`, `Di`, `Mi` etc., Zeitformat `24 Stunden`).
- **Design:** Modernes Material 3 Design mit automatischer Unterstützung für **Light Mode** und **Dark Mode**.
- **Bedienung:** Intuitive Navigation und klare Floating Action Buttons (FAB) zum Erstellen von Terminen.

---

## 5. Datenmodell (Entwurf)

```kotlin
@Entity(tableName = "events")
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String? = null,
    val startTimestamp: Long, // Epoch ms
    val endTimestamp: Long,   // Epoch ms
    val isAllDay: Boolean = false,
    val colorHex: String = "#6200EE",
    val location: String? = null
)
```
