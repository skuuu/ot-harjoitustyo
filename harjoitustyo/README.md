# :money_with_wings: SaastopossuApp  :money_with_wings:



Sovelluksen avulla käyttäjä voi tarkastella budjettiaan graafisena esityksenä haluamallaan aikavälillä, lisätä menoja valitsemilleen päiville, määritellä menoille kategorioita ja nähdä budjettiin liittyviä laskelmia. Sovellus vaatii kirjautumisen ja tallentaa tiedot käyttäjäkohtaisesti tietokantaan.




## Dokumentaatio

[Käyttöohje](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/KayttoOhje.md)

[Vaatimusmäärittely](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/arkkitehtuurikuvaus.md)

[Testausdokumentti](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/testaus.md)

[Työaikakirjanpito](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/tuntikirjanpito.md)

## Releaset

[Viikko 6](https://github.com/skuuu/ot-harjoitustyo/releases/tag/Saastopossuv1.2)  

## Komentorivitoiminnot

### Testaus

Testien suorittaminen:  

```
mvn test
```

Testikattavuusraportin luominen:  

```
mvn jacoco:report
```

Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto _target/site/jacoco/index.html_

### Suoritettavan jarin generointi

.jar -tiedoston generointi:  

```
mvn package
```

komento generoi hakemistoon _target_ suoritettavan jar-tiedoston _Saastopossu-1.0-SNAPSHOT.jar_

### JavaDoc

JavaDoc generoidaan komennolla

```
mvn javadoc:javadoc
```

JavaDocia voi tarkastella avaamalla selaimella tiedosto _target/site/apidocs/index.html_

### Checkstyle  

Koodin Checkstyle-laadun voi tarkistaa komennolla:

```
 mvn jxr:jxr checkstyle:checkstyle
```

Mahdolliset virheilmoitukset selviävät avaamalla selaimella tiedosto _target/site/checkstyle.html_
