# Käyttöohje


Lataa tiedosto [saastopossu.jar](https://github.com/mluukkai/OtmTodoApp/releases/tag/0.1) 

## Konfigurointi

## Ohjelman käynnistäminen

Ohjelma käynnistetään komennolla 

```
java -jar saastopossu.jar
```

## Kirjautuminen

Sovellus käynnistyy kirjautumisnäkymään. 
Kirjautuminen onnistuu kirjoittamalla olemassaoleva käyttäjätunnus syötekenttään ja painamalla _login_.

## Uuden käyttäjän luominen

Kirjautumisnäkymästä on mahdollista siirtyä uuden käyttäjän luomisnäkymään panikkeella _create new user account_.
Uusi käyttäjä luodaan syöttämällä tiedot syötekenttiin ja klikkaamalla painiketta _sign in_
Jos käyttäjän luominen onnistuu, palataan kirjautumisnäkymään.

## Kulujen lisääminen

Onnistuneen kirjautumisen myötä siirrytään käyttäjän kulut näyttävään näkymään. 

Näkymä mahdollistaa olemassaolevien menojen näyttämisen graafisesti valitsemalla aikavälin, jolta tapahtumat haluaa näytettävän. Kulun voi lisätä kirjoittamalla summan (eurot, sentit) niille merkattuihin tekstikenttiin ja valitsemalla kululle päivämäärän. Kulu tallettuu klikkaamalla painiketta _Add_. 

Klikkaamalla näkymän oikean reunan painiketta _Settings_ käyttäjä siirtyy Settings-näkymään, missä oman budjettinsa voi määritellä ja muuttaa klikkaamalla _set_, jolloin sovellus palauttaa aloitusnäkymään. Aloitusnäkymään voi palata muuuttamatta budjettia myös _back_ -painikkeella. 

Klikkaamalla aloitusnäkymässä _logout_ käyttäjä siirtyy takaisin kirjautumisnäkymään.
