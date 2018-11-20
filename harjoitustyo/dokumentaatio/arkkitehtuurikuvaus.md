# Arkkitehtuurikuvaus

## Rakenne


## Käyttöliittymä

Käyttöliittymä sisältää neljä erillistä näkymää
- kirjautumisnäkymä
- uuden käyttäjän luominen
- päänäkymä (graafinen esitys menoista)
- Settings-näkymä


jokainen näistä on toteutettu omana [Scene](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html)-oliona. Näkymistä yksi kerrallaan on näkyvänä eli sijoitettuna sovelluksen [stageen](https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html). Käyttöliittymä on rakennettu ohjelmallisesti luokassa [saastopossu.ui.TodoUi]().

Käyttöliittymä on pyritty eristämään täysin sovelluslogiikasta, se ainoastaan kutsuu sopivin parametrein sovelluslogiikan toteuttavan olion _logic_ metodeja.

Kun sovelluksen todolistan tilanne muuttuu, eli uusi käyttäjä kirjautuu, todoja merkitään tehdyksi tai niitä luodaan, kutsutaan sovelluksen metodia [refreshScreem]() joka renderöi päänäkymän uudelleen.

## Sovelluslogiikka

Sovelluksen loogisen datamallin muodostavat luokat [UserAccount]() ja [Activity](), jotka kuvaavat käyttäjiä ja käyttäjien kuluja:


Toiminnallisista kokonaisuuksista vastaa luokka [Logic](),. Luokka tarjoaa kaikille käyttäliittymän toiminnoille oman metodin. Näitä ovat esim.
(täydennetään myöhemmin)

_Logic_ pääsee käsiksi käyttäjiin ja kuluihin tietojen tallennuksesta vastaavaan pakkauksessa _saastopossuapp.dao_ sijaitsevien rajapinnat _ActivityDao_ ja _UserDao_ toteuttavien luokkien kautta. 

TodoServicen ja ohjelman muiden osien suhdetta kuvaava luokka/pakkauskaavio:


## Tietojen pysyväistallennus

Pakkauksen _todoapp.dao_ luokat _UserDao_ ja _ActivityDao_ huolehtivat tietojen tallettamisesta tietokantaan.

Luokat noudattavat [Data Access Object](https://en.wikipedia.org/wiki/Data_access_object) -suunnittelumallia ja ne on tarvittaessa mahdollista korvata uusilla toteutuksilla, jos sovelluksen datan talletustapaa päätetään vaihtaa. Luokat onkin eristetty rajapintojen _ActivityDao_ ja _UserDao_ taakse ja sovelluslogiikka ei käytä luokkia suoraan.


### Tiedostot

### Päätoiminnallisuudet

Kuvataan seuraavaksi sovelluksen toimintalogiikka muutaman päätoiminnallisuuden osalta sekvenssikaaviona.

#### käyttäjän kirjaantuminen

Kun kirjautumisnäkymässä on syötekenttään kirjoitettu käyttäjätunnus ja klikataan painiketta _login_ etenee sovelluksen kontrolli seuraavasti:



#### uuden käyttäjän luominen

Kun uuden käyttäjän luomisnäkymässä on syötetty käyttäjätunnus joka ei ole jo käytössä sekä nimi ja klikataan painiketta _create new User Account_ etenee sovelluksen kontrolli seuraavasti:



#### Activityn luominen

Uuden Activityn luovan _Add_-painikkeen klikkaamisen jälkeen sovelluksen kontrolli eteneeseuraavasti:



#### Muut toiminnallisuudet


## Ohjelman rakenteeseen jääneet heikkoudet

### käyttöliittymä

### DAO-luokat


DAO-toteutusten automaattiset testit tekisivät refaktoroinnin suhteellisen riskittömäksi.
