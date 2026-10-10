# Testfälle & Testplan: Simple Kalender-App (Android)

Auf Basis der [`spec.md`](file:///C:/Users/Mathias/StudioProjects/Android/Anna_Kalender/spec.md) definiert dieses Dokument alle **automatisierten Testfälle**, die in Android Studio als **Unit Tests** (lokal auf der JVM) und **Instrumentierte UI Tests** (auf Emulator/Gerät) ausgeführt werden sollen.

---

## 1. Übersicht der Test-Ebenen

| Test-Typ | Werkzeuge / Frameworks | Fokus |
| :--- | :--- | :--- |
| **1. Unit Tests (Datenbank / DAO)** | Room In-Memory Database, JUnit 4/5, Kotlin Coroutines Test | CRUD-Operationen & SQL-Abfragen im Speicher |
| **2. Unit Tests (ViewModel & Logik)** | JUnit 4/5, MockK / Fake Repository, Turbine (StateFlow) | Geschäftslogik, Pflichtfeld-Validierung, Formate |
| **3. Compose UI / Integrationstests** | `androidx.compose.ui.test.junit4`, AndroidX Test Runner | Formular-Eingaben, Navigation, Deutsche Dialoge & Labels |
| **4. App-Widget Tests (Glance / Widget)** | `androidx.glance.appwidget.testing`, AndroidX Test | Widget-Rendering, Datum-Aktualisierung, App-Launch Intents |

---

## 2. Unit Tests: Datenbank & DAO (`CalendarEventDaoTest`)

Diese Tests laufen lokal im Speicher (`Room.inMemoryDatabaseBuilder`) ohne physische Festplattenspeicherung.

### Testfall DAO-01: Termin erfolgreich anlegen und auslesen
- **Ziel:** Verifizieren, dass ein `CalendarEvent` in Room gespeichert und per ID abgerufen werden kann.
- **Testschritte:**
  1. Erstelle ein `CalendarEvent` (z.B. Titel: *"Zahnarzt"*).
  2. Rufe `dao.insertEvent(event)` auf.
  3. Lade das Event mit `dao.getEventById(id)`.
- **Erwartetes Ergebnis:** Das zurückgegebene Objekt stimmt mit allen Feldern (Titel, Start/Endzeit, Ganztägig, Farbe, Standort) überein.

### Testfall DAO-02: Pflichtfeld- und Nulldaten-Prüfung
- **Ziel:** Optional-Felder wie `description` oder `location` dürfen `null` sein, ohne dass Room Fehler wirft.
- **Testschritte:**
  1. Erstelle ein `CalendarEvent` mit `description = null` und `location = null`.
  2. Füge es in die DB ein und lies es aus.
- **Erwartetes Ergebnis:** Einfügen gelingt ohne NullPointerException, Felder sind `null`.

### Testfall DAO-03: Termine nach Datumsbereich filtern
- **Ziel:** Abfrage aller Termine zwischen `startTimestamp` und `endTimestamp` für Tages-/Wochen-/Monatsansicht.
- **Testschritte:**
  1. Füge 3 Termine ein: 1x gestern, 1x heute, 1x nächste Woche.
  2. Frage Termine für das Zeitfenster von "heute 00:00" bis "heute 23:59" ab (`getEventsBetween(start, end)`).
- **Erwartetes Ergebnis:** Es wird genau 1 Termin (der von heute) zurückgegeben.

### Testfall DAO-04: Termin aktualisieren (Update)
- **Ziel:** Geänderte Termindaten überschreiben den bestehenden Eintrag.
- **Testschritte:**
  1. Füge Termin *"Meeting"* ein.
  2. Ändere Titel zu *"Wichtiges Meeting"* und führe `dao.updateEvent(updatedEvent)` aus.
- **Erwartetes Ergebnis:** Der Datenbankeintrag enthält den neuen Titel *"Wichtiges Meeting"*.

### Testfall DAO-05: Termin löschen (Delete)
- **Ziel:** Ein Termin wird nach Aufruf von `dao.deleteEvent(event)` vollständig entfernt.
- **Testschritte:**
  1. Füge Termin ein.
  2. Rufe `dao.deleteEvent(event)` auf.
- **Erwartetes Ergebnis:** `dao.getEventById(id)` liefert `null` oder die Liste aller Termine ist leer.

---

## 3. Unit Tests: ViewModel & Logik (`CalendarViewModelTest`)

### Testfall VM-01: Validierung - Leerer Titel verhindert Speichern
- **Ziel:** Ein Termin ohne Titel darf nicht gespeichert werden.
- **Testschritte:**
  1. ViewModel initialisieren.
  2. `onTitleChanged("")` eingeben.
  3. `saveEvent()` aufrufen.
- **Erwartetes Ergebnis:** Der UI-State zeigt eine Fehlermeldung auf Deutsch an (z. B. *"Bitte gib einen Titel ein"*), und das Repository wird nicht aufgerufen.

### Testfall VM-02: Ganztägig-Umschaltung deaktiviert Uhrzeitbereich
- **Ziel:** Beim Aktivieren von "Ganztägig" werden Uhrzeitfelder ausgeblendet oder ignoriert.
- **Testschritte:**
  1. `onAllDayToggled(true)` aufrufen.
- **Erwartetes Ergebnis:** `isAllDay` im UI-State wird `true`, Start- und Endzeitpunkte werden auf 00:00 Uhr bis 23:59 Uhr des gewählten Tages gesetzt.

### Testfall VM-03: Datums- und Zeitformatierung auf Deutsch (`de-DE`)
- **Ziel:** Prüfen, ob Datums- und Uhrzeitformatierer korrekte deutsche Formate liefern.
- **Testschritte:**
  1. Wandle einen Zeitstempel (z. B. 03.10.2025 14:30) per `DateFormatter` um.
- **Erwartetes Ergebnis:** Datumsstring entspricht `"03.10.2025"`, Zeitstring entspricht `"14:30"`, Wochentage z.B. `"Fr"`.

---

## 4. Instrumentierte UI-Tests (`ComposeUITest`)

Diese Tests werden direkt mit dem `ComposeTestRule` auf dem Android-Emulator abgearbeitet.

### Testfall UI-01: App-Start & Navigation zwischen Ansichten
- **Ziel:** Die Hauptansicht lädt fehlerfrei und ermöglicht den Wechsel zwischen Monat, Woche und Tag.
- **Testschritte:**
  1. Starte `MainActivity`.
  2. Verifiziere Sichtbarkeit der Navigation Bar mit den Elementen `"Monat"`, `"Woche"`, `"Tag"`.
  3. Klicke auf `"Woche"`.
- **Erwartetes Ergebnis:** Wochenansicht wird angezeigt. Klick auf `"Tag"` öffnet die Tagesansicht.

### Testfall UI-02: Termin-Erstellung über Dialog / Formular (End-to-End)
- **Ziel:** Ein Benutzer klickt auf den Floating Action Button (+), füllt das Formular aus und speichert den Termin.
- **Testschritte:**
  1. Klicke auf den FAB mit Icon `+` oder Text `"Neuer Termin"`.
  2. Gib im Textfeld mit Tag/Label `"Titel"` den Text *"Projektbesprechung"* ein.
  3. Gib im Textfeld mit Tag/Label `"Standort"` den Text *"Büro Raum 3"* ein.
  4. Klicke auf den Button `"Speichern"`.
- **Erwartetes Ergebnis:** Das Formular schließt sich. Der Termin *"Projektbesprechung"* ist in der Tages-/Monatsansicht sichtbar.

### Testfall UI-03: Termin löschen mit Bestätigungsdialog
- **Ziel:** Ein Termin lässt sich erst nach Bestätigung eines Sicherheitsdialogs löschen.
- **Testschritte:**
  1. Erstelle einen Termin *"Test-Termin"*.
  2. Klicke auf den Termin, um die Details zu öffnen.
  3. Klicke auf den Button `"Löschen"`.
  4. Verifiziere den Bestätigungsdialog (Text: *"Möchtest du diesen Termin wirklich löschen?"*).
  5. Klicke im Dialog auf `"Löschen"`.
- **Erwartetes Ergebnis:** Der Dialog schließt sich und der Termin *"Test-Termin"* verschwindet aus der Kalenderansicht.

### Testfall UI-04: Deutsche Sprachkonsistenz
- **Ziel:** Sämtliche sichtbare Texte, Button-Beschriftungen und Dialoge sind auf Deutsch.
- **Testschritte:**
  1. Durchsuche den UI-Baum nach harten englischen Begriffen (z. B. *"Save"*, *"Delete"*, *"Title"*, *"Cancel"*).
- **Erwartetes Ergebnis:** Keine englischen UI-Texte auffindbar; stattdessen exklusiv deutsche Begriffe (`"Speichern"`, `"Löschen"`, `"Titel"`, `"Abbrechen"`).

---

## 5. Homescreen App-Widget Tests (`CalendarWidgetTest`)

### Testfall WGT-01: Anzeige heutiger Termine im Widget
- **Ziel:** Das Widget zeigt nach dem Hinzufügen eines neuen Termins für heute den aktuellen Termin korrekt an.
- **Testschritte:**
  1. Rendere das `CalendarGlanceWidget` über Glance AppWidget Testing.
  2. Füge einen heutigen Termin *"Team-Meeting 10:00 Uhr"* in die DB ein.
  3. Triggere ein Widget-Update.
- **Erwartetes Ergebnis:** Der Text *"Team-Meeting"* sowie das heutige Datum sind auf der Widget-Oberfläche zu sehen.

### Testfall WGT-02: Leerer Zustand ("Keine Termine heute")
- **Ziel:** Wenn an einem Tag keine Termine vorliegen, zeigt das Widget eine benutzerfreundliche deutsche Hinweismeldung.
- **Testschritte:**
  1. DB ist für den heutigen Tag leer.
  2. Rendere das Widget.
- **Erwartetes Ergebnis:** Der Text *"Keine Termine heute"* wird angezeigt.

### Testfall WGT-03: Widget Klick-Intents (App & Erstellmaske öffnen)
- **Ziel:** Ein Klick auf das Widget öffnet die Haupt-App an der passenden Stelle.
- **Testschritte:**
  1. Klicke auf den Widget-Hauptbereich -> Prüfe, ob ein Intent auf `MainActivity` gefeuert wird.
  2. Klicke auf den `+`-Button im Widget -> Prüfe, ob der Intent mit Extra `ACTION_CREATE_EVENT` gesendet wird.
- **Erwartetes Ergebnis:** Beide Intents werden korrekt mit den passenden Parametersätzen ausgelöst.

---

## 6. Automatische Testausführung in Android Studio

Nach Implementierung können die Tests wie folgt ausgeführt werden:

1. **Unit Tests ausführen:**
   - Rechtsklick auf den Ordner `src/test/java` -> **"Run 'Tests in 'java''"**
   - Oder über Terminal: `./gradlew test`

2. **Instrumentierte UI & Widget Tests ausführen:**
   - Emulator starten.
   - Rechtsklick auf den Ordner `src/androidTest/java` -> **"Run 'Tests in 'java''"**
   - Oder über Terminal: `./gradlew connectedAndroidTest`
