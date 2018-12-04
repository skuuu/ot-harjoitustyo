# Käyttöohje


Lataa tiedosto [saastopossu.jar](https://github.com/skuuu/ot-harjoitustyo/releases/tag/saastopossuAppv.1.0) 

## Konfigurointi
Ohjelman onnistunut käynnistys edellyttää, että .jar -tiedosto ja users.db -tiedosto on asennettu samaan kansioon ko nimillä. (Molemmat erillisinä tiedostoina releasessa). 

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
Uusi käyttäjä luodaan syöttämällä käyttäjätunnus syötekenttään ja klikkaamalla painiketta _sign in_
Jos käyttäjän luominen onnistuu, palataan kirjautumisnäkymään.

## Kulujen lisääminen

Onnistuneen kirjautumisen myötä siirrytään käyttäjän kulut näyttävään päänäkymään. 

Näkymä mahdollistaa kulujen näyttämisen graafisesti. Oletuksena sovellus näyttää kulut kuukauden ajalta. Aikavälin voi määritellä valitsemalla päivät DatePicker-valikosta. Kulun voi lisätä kirjoittamalla summan (eurot, sentit) niille merkattuihin tekstikenttiin ja valitsemalla kululle päivämäärän sekä kategorian _chooose category_ -valikosta. Uuden kategorian voi määritellä _create new_ -valinnalla, jolloin kategorian nimentään avautuu tekstikenttä. Kulu tallettuu klikkaamalla painiketta _Add_. 

Klikkaamalla näkymän oikean reunan painiketta _Settings_ käyttäjä siirtyy Settings-näkymään, missä oman budjettinsa voi määritellä ja muuttaa klikkaamalla _set_, jolloin sovellus palauttaa päänäkymään. Päänäkymään voi palata muuuttamatta budjettia myös _back_ -painikkeella. 

Klikkaamalla aloitusnäkymässä _logout_ käyttäjä siirtyy takaisin kirjautumisnäkymään.
