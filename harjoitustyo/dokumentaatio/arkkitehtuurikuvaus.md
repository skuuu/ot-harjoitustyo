# Arkkitehtuurikuvaus

## Rakenne


## Käyttöliittymä

Käyttöliittymä sisältää neljä erillistä näkymää
- kirjautumisnäkymä
- uuden käyttäjän luominen
- päänäkymä (graafinen esitys menoista)
- Settings-näkymä

jokainen näistä on toteutettu omana [Scene](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html)-oliona. Näkymistä yksi kerrallaan on näkyvänä eli sijoitettuna sovelluksen [stageen](https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html). Graafinen käyttöliittymä on rakennettu ohjelmallisesti luokassa [saastopossu.gui.UserInterface]().

Käyttöliittymä on pyritty eristämään täysin sovelluslogiikasta, se ainoastaan kutsuu sopivin parametrein sovelluslogiikan toteuttavan olion _logic_ metodeja.

Kun sovelluksen tilanne muuttuu, kutsutaan sovelluksen metodia [refreshScreem]() joka renderöi päänäkymän uudelleen.

## Sovelluslogiikka

Sovelluksen loogisen datamallin muodostavat luokat [UserAccount]() ja [Activity](), jotka kuvaavat käyttäjiä ja käyttäjien kuluja:


Toiminnallisista kokonaisuuksista vastaa luokka [Logic](). Luokka tarjoaa kaikille käyttäliittymän toiminnoille oman metodin. Näitä ovat esim.
(täydennetään myöhemmin)

_Logic_ pääsee käsiksi käyttäjiin ja kuluihin tietojen tallennuksesta vastaavaan pakkauksessa _saastopossuapp.dao_ sijaitsevien rajapinnan _Dao_ toteuttavien luokkien _ActivityDao_ ja _UserDao_ kautta. 

Ohjelman osien suhdetta kuvaava luokka/pakkauskaavio:  
<img src="https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/Images/luokkaPakkauskaavio.jpeg" width="500">


## Tietojen pysyväistallennus  

### Tiedostot

### Päätoiminnallisuudet

#### käyttäjän kirjautuminen

#### uuden käyttäjän luominen

#### Activityn luominen

#### Muut toiminnallisuudet

## Ohjelman rakenteeseen jääneet heikkoudet

### käyttöliittymä

### DAO-luokat

