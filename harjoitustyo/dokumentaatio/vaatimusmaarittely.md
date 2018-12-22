# Vaatimusmäärittely  

## Sovelluksen tarkoitus  

Sovellus on budjetoija, jonka avulla voi luoda ja tarkastella budjettia.  
Käyttäjä lisää kulunsa (expense) ja budjettinsa sovellukseen ja sovellus luo graafisen esityksen (pylväsdiagrammi) kuukausittaisista kuluista päivä- ja kategoriakohtaisesti sekä näyttää seuraavat käyttäjän määrittelemän budjetin ja kulujen perusteella tehdyt laskelmat: 
 - Budjetti käyttäjän määrittelemälle aikavälille
 - Budjetista käytetty prosenttiosuus käyttäjän määrittelemällä aikavälillä
 - Kokonaiskulut käyttäjän määrittelemällä aikavälillä
 - Keskimääräinen päiväkohtainen kulutus euroina
 - Budjetin alijäämäisyys käyttäjän määrittelemällä aikavälillä
 
 Käyttäjä voi tarkastella kuluja paitsi päivä- ja kategoriakohtaisesti, myös yksittäisinä kuluina.
 
## Käyttäjät  
Sovelluksella voi olla monta käyttäjää, joilta jokaiselta vaaditaan käyttäjätunnus.

## Käyttöliittymäluonnos  
Käyttöliittymä koostuu viidestä eri näkymästä. 
Kirjautumisnäkymässä (passwordScene) käyttäjä kirjautuu sisään tai siirtyy näkymään (createUserScene) jossa luo uuden käyttäjätunnuksen. 
Päänäkymässä (startScene) käyttäjä näkee kuukauden ajalta menonsa. Käyttäjä voi pyytää raporttia haluamaltaan aikaväliltä tai lisätä menon. 
Asetukset-näkymässä (SettingsScene) käyttäjä voi mm. muokata päiväkohtaista budjettiaan. 
Klikkaamalla pylväskaavion pylväitä avautuu uusi ikkuna ja näkymä (expenseScene), jossa yksittäisen päivän kategorian menoja voi tarkastella ja poistaa.

<img src= "https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/Images/vaatimusmaarittely21.12..jpg" width="800">

## Perusversion tarjoama toiminnallisuus  
### Ennen kirjautumista  
- käyttäjä voi luoda järjestelmään käyttäjätunnuksen
  - käyttäjätunnuksen täytyy olla uniikki, sisältää 2-30 kirjainta (a-z, A-Z) ja/tai numeroa (0-9).
- käyttäjä voi kirjautua järjestelmään
  - kirjautuminen onnistuu syötettäessä olemassaoleva käyttäjätunnus tekstikenttään
  - jos käyttäjää ei löydy, ilmoittaa järjestelmä tästä virheviestillä

### Kirjautumisen jälkeen  
- Käyttäjä näkee päivittäisen budjettinsa toteutumisen pylväsdiagrammina kuukauden ajalta
- Käyttäjä näkee budjettinsa ja kulujen pohjalta automaattisesti tehdyt laskelmat (kokonaiskulut aikavälillä, päiväkohtaiset kulut, valitulle aikavälille laskettu budjetti, valitulla aikavälillä käytetty prosenttiosuus budjetista, säästöt)
- Käyttäjä voi valita haluamansa aikavälin tarkasteluun
- Käyttäjä voi lisätä kulun (summa, pvm, kategoria, lisätiedot)
- Käyttäjä voi lisätä kulun nimeämällään uudella kategorialla
- Käyttäjä voi tarkastella yksittäisen päivän ja kategorian (pylvään) kuluja ja poistaa niitä
- Käyttäjä voi siirtyä Settings-näkymään määrittelemään budjettinsa
- Käyttäjä voi kirjautua ulos

## Jatkokehitysideoita  
Perusversion jälkeen järjestelmää täydennetään ajan salliessa esim. seuraavilla toiminnallisuuksilla:  
:ballot_box_with_check: Käyttäjä voi lisätä kuluja kategorioittain ja määritellä uuden kategorian  
:ballot_box_with_check:	Kulun poistaminen  
:black_square_button: Käyttäjätunnuksen vaihtaminen/poistaminen  
:black_square_button: Sovelluksen ulkoasun tyylittely  
:black_square_button: Tulojen huomiointi

Uudet ominaisuudet, jotka eivät kuuluneet alkuperäiseen vaatimusmäärittelyyn:  
:ballot_box_with_check: Käyttäjä voi lisätä kululle kategorian lisäksi myös lisätietoja (esimerkiksi kauppa, ostos)  
:ballot_box_with_check: Budjettilaskelmiin lisätty laskelma budjetin alijäämäisyydestä  
:ballot_box_with_check: Käytettävyyttä parannettu lisäämällä pylväille tooltipit, jotka näyttävät pylvään kulujen summan  
