# SaastopossuApp

Sovelluksen avulla käyttäjä voi tarkastella budjettiaan graafisena, lisätä menoja valitsemilleen päiville ja nähdä budjettiin liittyviä laskelmia. Sovellusta on mahdollista käyttää useamman rekisteröityneen käyttäjän, joilla kaikilla on oma budjetti.


## Dokumentaatio

[Käyttöohje](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/KayttoOhje.md)

[Vaatimusmäärittely](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus]()

[Testausdokumentti]()

[Työaikakirjanpito](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/dokumentaatio/tuntikirjanpito.md)

## Releaset

[Viikko 3](https://github.com/mluukkai/OtmTodoApp/releases/tag/viikko5)

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

generoi hakemistoon _target_ suoritettavan jar-tiedoston _OtmTodoApp-1.0-SNAPSHOT.jar_

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