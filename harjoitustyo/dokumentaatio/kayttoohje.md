# Käyttöohje


Lataa tiedosto [Saastopossu.jar](https://github.com/skuuu/ot-harjoitustyo/releases/tag/Saastopossuv1.3) 

## Konfigurointi
Sovelluksen onnistunut käynnistys ja testaus edellyttää, että sovelluksen generoimat saastopossu.conf-tiedosto, .jar -tiedosto ja .db -tiedostot löytyvät samasta kansiosta. 

Ohjelma olettaa, että ohjelman sisältävän kansion juuressa on saastopossu.conf -tiedosto. 
Tiedostossa määritellään käytettävän tietokannan nimi. 
Käyttäjä voi muuttaa tietokannan nimeä muuttamalla .db -tietokantatiedoston nimen ja saastopossu.conf -tiedoston parametrin "database". 

saastopossu.conf - tiedosto on muotoa:

```
database=saastopossu.db
```
Konfigurointitiedoston voi ladata releasen yhteydestä tai [täältä](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/saastopossu.conf). 


## Ohjelman käynnistäminen

Ohjelma käynnistetään komennolla 

```
java -jar Saastopossu-1.0-SNAPSHOT.jar
```

## Kirjautuminen

Sovellus käynnistyy kirjautumisnäkymään. 
Kirjautuminen onnistuu kirjoittamalla olemassaoleva käyttäjätunnus syötekenttään ja painamalla _Login_.

## Uuden käyttäjän luominen

Kirjautumisnäkymästä on mahdollista siirtyä uuden käyttäjän luomisnäkymään panikkeella _Create new user account_.
Uusi käyttäjä luodaan syöttämällä käyttäjätunnus syötekenttään ja klikkaamalla painiketta _Confirm_. Käyttäjätunnus voi olla 3-20 merkkiä pitkä, ja sisältää kirjaimia A-Z, a-z ja numeroita 0-9. 

Jos käyttäjän luominen onnistuu, palataan kirjautumisnäkymään.

## Kulujen tarkasteleminen ja poistaminen

Onnistuneen kirjautumisen myötä siirrytään käyttäjän kulut näyttävään päänäkymään. 
Näkymä mahdollistaa kulujen näyttämisen graafisesti pylväskaaviona. Oletuksena sovellus näyttää kulut kuukauden ajalta. 
Aikavälin voi määritellä valitsemalla päivät DatePicker-valikoista _From_ ja _To_. 

Pylväsidagrammissa näkyvät lisätyt kulut päiväkohtaisesti ja kategorioittain. Yksittäisen pylvään tietoja pystyy tarkastelemaan viemällä hiiren pylvään päälle, jolloin käyttäjälle näytetään kyseisen pylvään summa euroina.

Klikkaamalla pylvästä avautuu ikkuna, jossa käyttäjä näkee pylvään kulut (yksittäisen kulun summa euroina sekä lisätiedot). 
Käyttäjä voi halutessaan poistaa kulun painikkeellla _Delete_:
  
<img src="https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/Images/screenshotGUI.png" width="700">


  
## Kulujen lisääminen  
Kulun voi lisätä kirjoittamalla summan (eurot, sentit) niille merkattuihin tekstikenttiin kohdassa _Expense Amount_. Eurokenttä ei saa olla tyhjä, ja senttikentän syötteen tulee olla max. kahden numeron mittainen. Mikäli senttikenttä on tyhjä, täydentää ohjelma sen automaattisesti nollaksi. 
Kululle lisätään päivämäärä DatePicker-valikosta _Expense Date_ sekä kategoria _Expense Category_ -valikosta. 

Lisättävälle kululle voi määritellä lisätietoja (esimerkiksi kaupan nimi, ostoksen tietoja) _Expense Description_-syötekentässä. Syöte voi olla 1-30 merkkiä pitkä ja sisältää kirjaimia a-z, A-Z, välilyöntejä syötteen keskellä ja numeroita 0-9. 
Jos kenttä jätetään tyhjäksi, ohjelma täydentää sen tekstillä _no description_.

Uuden kategorian voi määritellä _Expense Category_ -valikon _create new_ -valinnalla, jolloin kategorian nimentään avautuu tekstikenttä. Uusi kategoria voi sisältää kirjaimia a-z, A-Z sekä numeroita 0-9. Kategorioita voi olla max. 7 ja kategorian nimen on oltava uniikki.

Kulu tallennetaan tietokantaan klikkaamalla painiketta _Add Expense_. 
Mikäli lisättävä kulu on virheellinen, käyttäjälle näytetään syötekohtainen virheviesti. 


## Budjetin määritteleminen  
Klikkaamalla näkymän oikean reunan painiketta _Settings_ käyttäjä siirtyy Settings-näkymään, missä oman budjettinsa voi määritellä ja muuttaa klikkaamalla _Set_, jolloin sovellus palauttaa päänäkymään. 
Päänäkymään voi palata muuuttamatta budjettia myös _Back_ -painikkeella. 

## Uloskirjautuminen 
Klikkaamalla aloitusnäkymässä _Logout_ käyttäjä siirtyy takaisin kirjautumisnäkymään. 
Sovelluksen voi sulkea oikeasta yläkulmasta.
