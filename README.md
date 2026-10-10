# 📅 Anna Kalender (Android)

Eine performante, moderne und rein lokale Kalender-App für Android, entwickelt in **Kotlin** mit **Jetpack Compose (Material Design 3)** und **Jetpack Glance App-Widget**.

---

## 🌟 Highlights & Features

- 📅 **Drei Hauptansichten (Monat, Woche, Tag):**
  - **Monatsansicht:** 7-Spalten-Gitter (Mo–So) mit Termin-Indikatoren und Tagesübersicht.
  - **Wochenansicht:** 7-Tage-Spaltenraster mit 24-Stunden-Zeitleiste.
  - **Tagesansicht:** Chronologische 24-Stunden-Detailansicht.
- 📝 **Terminverwaltung (CRUD):**
  - Titel (Pflichtfeld), Beschreibung, Standort, Ganztägig-Option, Start- & Endzeit.
  - Native **DatePicker** und **TimePickerDialog** Integration.
  - Sicherheitsabfrage vor dem Löschen von Terminen.
- 📲 **Homescreen App-Widget (Jetpack Glance):**
  - Live-Anzeige der heutigen Termine direkt auf dem Startbildschirm.
  - Schnellzugriff zum Öffnen der App oder zum schnellen Erstellen eines neuen Termins (`+`).
  - Automatisches Widget-Update bei jeder Datenbankänderung.
- 🔒 **100% Lokaler Datenschutz:**
  - Speicherung aller Daten in lokaler SQLite-Datenbank (**Room**).
  - Keine Cloud-Synchronisation, keine Tracker.
- 🎨 **Material 3 Design & Adaptive Icons:**
  - Modernes Farbschema mit hellem und dunklem Modus.
  - Vektor-basiertes Adaptive Launcher Icon.

---

## 🛠️ Technologie-Stack

- **Programmiersprache:** Kotlin
- **UI Framework:** Jetpack Compose mit Material Design 3
- **App-Widget:** Jetpack Glance AppWidget
- **Architektur:** MVVM / Clean Architecture (ViewModel, StateFlow, Repository)
- **Datenbank:** Room Database (SQLite)
- **Asynchronität:** Kotlin Coroutines & Flow
- **Testing:** JUnit 4, Robolectric 4.17, Room In-Memory, Compose UI Testing

---

## 🚀 Projekt bauen & ausführen

### Voraussetzungen
- Android Studio Ladybug (2024.2) oder neuer
- Android SDK (Min SDK 24, Target SDK 35)
- Java 17 / JDK 17

### Gradle Commands
```bash
# Debug APK erstellen
./gradlew assembleDebug

# Alle Unit- & UI-Tests ausführen
./gradlew test
```

---

## 🧪 Tests
Das Projekt verfügt über automatisierte Unit-, DAO-, ViewModel-, Compose UI- und Widget-Tests:
```bash
./gradlew testDebugUnitTest
```
Ergebnis: **17 passed, 0 skipped, 0 failed**.
