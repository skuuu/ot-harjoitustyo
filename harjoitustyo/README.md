# :money_with_wings: SaastopossuApp  :money_with_wings:



Sovelluksen avulla käyttäjä voi tarkastella budjettiaan graafisena esityksenä haluamallaan aikavälillä, lisätä menoja valitsemilleen päiville, määritellä menoille kategorioita ja nähdä budjettiin liittyviä laskelmia. Sovellus vaatii kirjautumisen ja tallentaa tiedot käyttäjäkohtaisesti tietokantaan.

Tällä hetkellä kaipaisin apua seuraavissa asioissa: 
- miten graafisesta käyttöliittymästä vastaavan UserInterface -luokan voisi toteuttaa siistimmin? 



## Dokumentaatio

[Käyttöohje](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/KayttoOhje.md)

[Vaatimusmäärittely](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/arkkitehtuurikuvaus.md)

[Testausdokumentti](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/testaus.md)

[Työaikakirjanpito](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/tuntikirjanpito.md)

## Releaset

[Viikko 4](https://github.com/skuuu/ot-harjoitustyo/releases/tag/saastopossuAppv.1.0)  

## Komentorivitoiminnot

### Testaus

Testit suoritetaan komennolla

```
mvn test
```

Testikattavuusraportti luodaan komennolla

```
mvn jacoco:report
```

Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto _target/site/jacoco/index.html_

### Suoritettavan jarin generointi

Komento

```
mvn package
```

generoi hakemistoon _target_ suoritettavan jar-tiedoston _Saastopossu-1.0-SNAPSHOT.jar_

### JavaDoc

JavaDoc generoidaan komennolla

```
mvn javadoc:javadoc
```

JavaDocia voi tarkastella avaamalla selaimella tiedosto _target/site/apidocs/index.html_

### Checkstyle

Tiedostoon [checkstyle.xml](https://github.com/mluukkai/OtmTodoApp/blob/master/checkstyle.xml) määrittelemät tarkistukset suoritetaan komennolla

```
 mvn jxr:jxr checkstyle:checkstyle
```

Mahdolliset virheilmoitukset selviävät avaamalla selaimella tiedosto _target/site/checkstyle.html_
