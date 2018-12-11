# Käyttöohje


Lataa tiedosto [saastopossu.jar](https://github.com/skuuu/ot-harjoitustyo/releases/tag/Saastopossuv1.2) 

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
Uusi käyttäjä luodaan syöttämällä käyttäjätunnus syötekenttään ja klikkaamalla painiketta _sign in_. KÄyttäjätunnus voi olla 3-20 merkkiä pitkä, ja sisältää kirjaimia A_Z, a-z ja numeroita 0-9. 

Jos käyttäjän luominen onnistuu, palataan kirjautumisnäkymään.

## Kulujen tarkasteleminen  

Onnistuneen kirjautumisen myötä siirrytään käyttäjän kulut näyttävään päänäkymään. 
Näkymä mahdollistaa kulujen näyttämisen graafisesti. Oletuksena sovellus näyttää kulut kuukauden ajalta. Aikavälin voi määritellä valitsemalla päivät DatePicker-valikosta. 

## Kulujen lisääminen  
Kulun voi lisätä kirjoittamalla summan (eurot, sentit) niille merkattuihin tekstikenttiin. Eurokenttä ei saa olla tyhjä, ja senttikentän syötteen tulee olla max. kahden numeron mittainen. Mikäli senttikenttä on tyhjä, täydentää ohjelma sen automaattisesti nollaksi. Kulu lisätään valitsemalla kululle päivämäärä DatePicker-valikosta sekä kategoria _chooose category_ -valikosta. Uuden kategorian voi määritellä _create new_ -valinnalla, jolloin kategorian nimentään avautuu tekstikenttä. Kategorian nimentä onnistuu samojen rajoitteiden mukaan kuin käyttäjätunnuksenkin. 
Kulu tallettuu klikkaamalla painiketta _Add_. 


## Budjetin määritteleminen  
Klikkaamalla näkymän oikean reunan painiketta _Settings_ käyttäjä siirtyy Settings-näkymään, missä oman budjettinsa voi määritellä ja muuttaa klikkaamalla _set_, jolloin sovellus palauttaa päänäkymään. Päänäkymään voi palata muuuttamatta budjettia myös _back_ -painikkeella. 

## Uloskirjautuminen 
Klikkaamalla aloitusnäkymässä _logout_ käyttäjä siirtyy takaisin kirjautumisnäkymään. Sovelluksen voi sulkea oikeasta yläkulmasta.
