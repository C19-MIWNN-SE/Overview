# Requirements, business logic & architecture decisions

Verzamelplaats voor [vereisten](#vereisten-application), [business logic](#business-logic-en-informatie), en [architectuurkeuzes](#architectuurkeuzes). Met ook ruimte voor [wensen](#wensen), [vragen](#vragen), [ideeën](#ideeën).

Doel van de applicatie:
> Een zakelijk smoelenboek maken waar iedereen elkaar kan leren kennen. Afgeschermd en privé, waar iedere gebruiker zelf over zijn informatie gaat. Hierbij zijn er verschillende rollen.

---
## Vereisten application

Algemeen:
- Zakelijk “social” platform
- Afgeschermd
- Niet iedereen kan zich inschrijven
- Verschillende rollen
- Deelnemerpagina voor werkgever
- Deelnemers kunnen zelf hun profiel inrichten

Latere uitwerking:
- De gehele klas kan de detailpagina van andere deelnemers in de klass zien
- Dit moet op de detailpagina komen te staan:
  - Naam
  - (School)mailadres
  - Waar iedereen naartoe gaat (toekomstige werkgever)
  - Optioneel: Wat heb ik hiervoor gedaan
  - Optioneel: _Welke kennis heb ik_
  - Foto
- Over de detailpagina:
  - Een deel van de informatie over een deelnemer wordt meteen ingevuld bij het aanmaken van het account; een deel moet de deelnemer zelf invullen (zoals bijvoorbeeld wat hij hiervoor heeft gedaan)
- Een leerling kan de detailpagina van de docent zien (contact voorkeur, mailadres, (werk)telefoonnummer)

---
## Business logic en informatie

Informatie:
- De applicatie is niet specifiek voor een school, de trigger was Make IT Work; meer algemeen is het een platform waar meerdere organisatie (administratief) gebruik van kunnen maken.
  - Er zit dus een organisatie boven het geheel, bijvoorbeeld een organisatie zoals Make IT Work.


    

### Rollen

Er zijn twee verschillende rollen, docenten ('Instructor') en leerlingen ('Participant'). We maken geen aparte administrator aan, de docent kan nieuwe cohorten (en leerlingen) aanmaken. Een docent kan niet de profielpagina van de leerling aanpassen (doen geen 'content moderation').

Omdat de basis een smoelenboek is, is zoiets als een adres **niet** nodig om op te slaan. Een woonplaatas kan wel handig zijn.

Docent en leerlingen hebben allebei de volgende entiteiten:
- ...

Docent heeft de volgende attributen:
- ...

Leerling heeft de volgende attributen:
- ...

### User-entiteit

De accountnaam is de ...

Rollen in applicatie
- Personeel
- Leerling
- (Later) Werkgever

---
## Architectuurkeuzes

_Voor (architectuur)keuzes die niet uit de code zelf al duidelijk zijn, komt hier de uitleg._

### Rollen

We hebben gekozen om twee verschillende entiteiten aan te maken, 'Instructor' en 'Participant', omdat de attributen verschillend genoeg zijn. Omdat alle docenten en leerlingen ook gekoppeld moeten zijn aan een account, hebben we een apart User-entiteit opgezet die via een One-to-One-relatie is gekoppeld.

---
## Wensen

Wensen voor later:
- Verschillende pagina’s; leerling kan pagina aan klas laten zien en een andere pagina aan het bedrijf.
- Werkgever kan ook inloggen om toekomstige werknemer te bekijken

---
## Vragen
- Moet het cohort zelf ook een foto hebben?
---
## Ideeën

_Hier kan je eventueel alvast wat ideeën neerzetten_

- Navbar aanpassen op basis van waar je bent op de website
- Een search bar toevoegen
- Voor het aanmaken van user accounts, zoals voor de participants, kan je ervoor kiezen om een e-mail te versturen met een link om het account te activeren en meteen een wachtwoord aan te maken / setten