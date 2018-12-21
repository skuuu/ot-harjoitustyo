# Käyttöohje


Lataa tiedosto [saastopossu.jar](https://github.com/skuuu/ot-harjoitustyo/releases/tag/Saastopossuv1.2) 

## Konfigurointi
Sovelluksen onnistunut käynnistys ja testaus edellyttää, että sovelluksen generoimat .jar -tiedosto, piggyBankDatabase.db ja piggyBankTestDatabase.db löytyvät samasta kansiosta. 

## Ohjelman käynnistäminen

Ohjelma käynnistetään komennolla 

```
java -jar Saastopossu-1.0-SNAPSHOT.jar
```

## Kirjautuminen

Sovellus käynnistyy kirjautumisnäkymään. 
Kirjautuminen onnistuu kirjoittamalla olemassaoleva käyttäjätunnus syötekenttään ja painamalla _login_.

## Uuden käyttäjän luominen

Kirjautumisnäkymästä on mahdollista siirtyä uuden käyttäjän luomisnäkymään panikkeella _create new user account_.
Uusi käyttäjä luodaan syöttämällä käyttäjätunnus syötekenttään ja klikkaamalla painiketta _Confirm_. Käyttäjätunnus voi olla 3-20 merkkiä pitkä, ja sisältää kirjaimia A_Z, a-z ja numeroita 0-9. 

Jos käyttäjän luominen onnistuu, palataan kirjautumisnäkymään.

## Kulujen tarkasteleminen ja poistaminen

Onnistuneen kirjautumisen myötä siirrytään käyttäjän kulut näyttävään päänäkymään. 
Näkymä mahdollistaa kulujen näyttämisen graafisesti. Oletuksena sovellus näyttää kulut kuukauden ajalta. Aikavälin voi määritellä valitsemalla päivät DatePicker-valikosta. 

Pylväsidagrammissa näkyvät lisätyt kulut päiväkohtaisesti ja kategorioittain. Yksittäisen pylvään tietoja pystyy tarkastelemaan viemällä hiiren pylvään päälle, jolloin käyttäjälle näytetään ko pylvään summa euroina.

Klikkaamalla pylvästä avautuu ikkuna, jossa käyttäjä näkee pylvään kulut (yksittäisen kulun summa euroina sekä lisätiedot). 
Käyttäjä voi halutessaan poistaa kulun näppäimellä _delete_:
  
<img src="https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/Images/ScreenshotExpenseScene.jpg" width="700">


  
## Kulujen lisääminen  
Kulun voi lisätä kirjoittamalla summan (eurot, sentit) niille merkattuihin tekstikenttiin. Eurokenttä ei saa olla tyhjä, ja senttikentän syötteen tulee olla max. kahden numeron mittainen. Mikäli senttikenttä on tyhjä, täydentää ohjelma sen automaattisesti nollaksi. Kulu lisätään valitsemalla kululle päivämäärä DatePicker-valikosta sekä kategoria _chooose category_ -valikosta. 

Lisättävälle kululle voi määritellä lisätietoja (esimerkiksi kaupan nimi, ostoksen tietoja) _Expense Description_-kohdassa kirjoittamalla syötekenttään. Syöte voi olla 1-30 merkkiä pitkä ja sisältää kirjaimia a-z, A_Z, välilyöntejä syötteen keskellä ja numeroita 0-9. Jos kenttä jätetään tyhjäksi, ohjelma täydentää sen tekstillä _no description_.

Uuden kategorian voi määritellä _create new_ -valinnalla, jolloin kategorian nimentään avautuu tekstikenttä. Kategoria voi sisältää kirjaimia a-z, A-Z sekä numeroita 0-9. Kategorioita voi olla max. 7 ja luotavan kategorian nimen on oltava uniikki.
Kulu tallettuu klikkaamalla painiketta _Add Expense_. 
Mikäli lisättävä kulu on virheellinen, käyttäjälle näytetään syötekohtainen virheviesti. 


## Budjetin määritteleminen  
Klikkaamalla näkymän oikean reunan painiketta _Settings_ käyttäjä siirtyy Settings-näkymään, missä oman budjettinsa voi määritellä ja muuttaa klikkaamalla _Set_, jolloin sovellus palauttaa päänäkymään. Päänäkymään voi palata muuuttamatta budjettia myös _back_ -painikkeella. 

## Uloskirjautuminen 
Klikkaamalla aloitusnäkymässä _logout_ käyttäjä siirtyy takaisin kirjautumisnäkymään. Sovelluksen voi sulkea oikeasta yläkulmasta.
