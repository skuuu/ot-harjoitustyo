# Testausdokumentti

Sovellusta on testattu Junitin avulla. Luodut testit sisältävät sekä yksikkö- että integraatiotestjä ja testaavat laajasti sovelluksen toimintaa. Lisäksi sovellusta on on testattu manuaalisesti ja keräämällä käyttäjäkokemuksista palautetta.

## Yksikkö- ja integraatiotestaus

Sovelluksen testaus suoritetaan erillisessä testitieokannassa. Jokaista luokkaa testataan erillisellä testiluokalla. Testiluokat:   
[ActivityTest](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/test/java/domainTests/ActivityTest.java)  
[UserAccountTest](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/test/java/domainTests/UserAccountTest.java)  
[ActivityDaoTest](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/test/java/daoTests/ActivityDaoTest.java)  
[UserAccountDaoTest](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/test/java/daoTests/UserAccountDaoTest.java)  
[AnalysisTest](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/test/java/logicTests/AnalysisTest.java)  
[ConverterTest](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/test/java/logicTests/ConverterTest.java)  
[LogicTest](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/test/java/logicTests/LogicTest.java)  


### Testauskattavuus

Sovelluksen tämänhetkinen testauksen rivikattavuus on 95% ja haarautumakattavuus 90%. 

<img src="https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/Images/jacocotest.png" width="900">

Testit eivät kata tilanteita, joissa tarvittavia tiedostoja ei löydy.

## Järjestelmätestaus

Sovelluksen järjestelmätestaus on suoritettu manuaalisesti.

### Asennus ja konfigurointi  

Sovellusta on testattu sekä tilanteissa, joissa tarvittavat .db-tiedostot ovat olleet ohjeiden mukaisesti olemassa samassa kansiossa .jar-tiedoston kanssa sekä tilanteissa, joissa tarvittavia .db-tiedostoja ei ole löytynyt, jolloin sovellus on luonut ne Database-luokan ohjeiden mukaisesti.

### Toiminnallisuudet  

Sovellus on testattu laajasti myös virheellisten syötteiden osalta ja sovellus antaa käyttäjälle palautetta virheen laadusta syötekohtaisen virheviestin muodossa. 


## Sovellukseen jääneet laatuongelmat

Sovellus ei anna virheviestiä, mikäli tarvittavia -db -tiedostoja ei löydy. 
Sovellus ei testaa yksittäisiä SQLExceptioneita.
