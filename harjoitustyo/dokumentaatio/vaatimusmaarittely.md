# Vaatimusmäärittely  

## Sovelluksen tarkoitus  

Sovellus on budjetoija, jonka avulla voi luoda ja tarkastella budjettia.  
Käyttäjä lisää menonsa ja budjettinsa sovellukseen, ja sovellus laskee päiväkohtaiset menot sekä osuuden tavoitebudjetista sekä luo raportin kuukausittaisista menoista.
 
## Käyttäjät  
Sovelluksella voi olla monta käyttäjää.

## Käyttöliittymäluonnos  
Käyttöliittymä koostuu neljästä eri näkymästä. 
Kirjautumisnäkymässä (passwordScene) käyttäjä kirjautuu sisään tai siirtyy näkymään (createUserScene) jossa luo uuden käyttäjätunnuksen. 
Päänäkymässä (startScene) käyttäjä näkee kuukauden ajalta menonsa. Käyttäjä voi pyytää raporttia haluamaltaan aikaväliltä tai lisätä menon. 
Asetukset-näkymässä (SettingsScene) käyttäjä voi mm. muokata päiväkohtaista budjettiaan. 


## Perusversion tarjoama toiminnallisuus  
### Ennen kirjautumista  
- käyttäjä voi luoda järjestelmään käyttäjätunnuksen
  - käyttäjätunnuksen täytyy olla uniikki
- käyttäjä voi kirjautua järjestelmään
  - kirjautuminen onnistuu syötettäessä olemassaoleva käyttäjätunnus kirjautumislomakkeelle
  - jos käyttäjää ei löydy, ilmoittaa järjestelmä tästä

### Kirjautumisen jälkeen  
- Käyttäjä näkee päivittäisen budjettinsa toteutumisen pylväsdiagrammina kuukauden ajalta
- Käyttäjä voi valita haluamansa aikavälin tarkasteluun
- Käyttäjä voi lisätä menon (summa, kategoria, pvm)
- Käyttäjä voi lisätä menon nimeämällään uudella kategorialla
- Käyttäjä voi siirtyä Settings-näkymään määrittelemään budjettinsa
- Käyttäjä voi kirjautua ulos

## Jatkokehitysideoita  
Perusversion jälkeen järjestelmää täydennetään ajan salliessa esim. seuraavilla toiminnallisuuksilla:  
:ballot_box_with_check: Käyttäjä voi lisätä menoja kategorioittain ja määritellä uuden kategorian    
:black_square_button: Käyttäjätunnuksen vaihtaminen/poistaminen  
:black_square_button:	Menon poistaminen  
:black_square_button: Sovelluksen ulkoasun tyylittely  
