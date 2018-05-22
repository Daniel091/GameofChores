# PemGAMI
Englisch 

## First Feature Goals to be done until 1.6.2018
- Aufgabenplaner (Main-Feature)
- Bewohner des Monats
- Anmeldungsflow

### Aufgabenplaner
- Bonuspunkte Berechnung nach Zeit
- Screen1: Forumlar fürs Festlegen der Dauer, Name der Aufgabe, Zeit, Rotation, ..
- Screen2: Was steht heute an, Übersicht (Home Seite)
- Notifications für Aufgaben
- Nice to have: Regex, für Bonuspunkte (Bad, ..) 

### Bewohner des Monats
- Screen1: Ranglisten, am ersten Tag des neuen Monats, Auswertung und Ranglisten Screen
- Screen2: Derzeitiger Stand

### Anmeldung
- Benutzername, WG
- Screen1: Neue WG erstellen oder in WG einschreiben
- Screen2: WG-Erstellung, WG, Name, Bewohner, generiert einen Code
- Screen3: WG-Code eintippen

## Update 1 
- Pinnwand
- Gambling

### Gambling
- TO be done

### Pinnwand
- Titel, Beschreibung, Datum
- Screen1: Übersicht
- Screen2: Eingabe


## Git Tutorial

### Branching:
New Branch, switch to branch, commit push
```
git branch name_of_branch 
git checkout name_of_branch
git push origin name_of_branch
```

### Merging:
```
git checkout master
git merge name_of_branch_to_merge
```
-> write commit message for this merge then push it; (in vim ESC; :exit)

### Delete branch

locally: `git branch -d name_of_branch`
remote: `git push origin --delete the_remote_branch`


