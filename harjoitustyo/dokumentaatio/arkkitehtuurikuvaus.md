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
Pakkauksen todoapp.dao luokat UserAccounDao ja ActivityDao huolehtivat tietojen tallettamisesta tiedostoihin.

Luokat noudattavat Data Access Object -suunnittelumallia. Luokat on eristetty rajapintojen UseracoounDaoInterface ja ActivityDaoInterface taakse ja sovelluslogiikka ei käytä luokkia suoraan. Luokat ovat Dao-tyyppinsä takia korvattavissa helposti. 



### Tiedostot  

Sovellus tallentaa tiedot sql-tietokantaan (users.db), jossa on kaksi tietokantataulua: UserAccount ja Activity. 

### Päätoiminnallisuudet  

Päätoiminnallisuudet kuvattu seuraavaksi sekvenssikaavioina. 

#### käyttäjän kirjautuminen  
Käyttäjä voi kirjautua aloitusnäkymässä painikkeesta _login_ jonka aktivaatio kutsuu logic-luokan metodia _checkUsername_ ja palauttaa true, jos käyttäjänimi löytyy tietokannasta ja muuten false. 

#### uuden käyttäjän luominen  
Uuden käyttäjän voi luoda _create new user account_ -painikkeesta, jolloin käyttöliittymä kutsuu logic-luokan metodia _createUser_ joka kutsuu UserAccountDao-luokan metodia _saveOrUpdate_ ja palauttaa true, jos tallennus onnistui ja muuten false. False-tapauksissa tulostuu virheviesti. 

<img src="https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/Images/IMG_20181204_235242.jpg" width="500">

#### Activityn luominen  
Activity luodaan valitsemalla päivä, syöttämällä summa ja aktivoimalla painike _add_, jolloin kutsutaan Logic-luokan metodia addExpense. Tästä metodista joko päivitetään tai tallennetaan ActivityDao-luokan saveOrUpdate-metodia käyttäen. 



#### Muut toiminnallisuudet  
Sovelluksen toimintaperiaate on samankaltainen myös muille toiminnoille. Graafinen käyttöliittymä kutsuu logic-luokkaa, josta tarvittaessa kutsutaan edelleen Analysis-luokkaa. Logic-luokalla on pääsy UserAccountDao- ja ActivityDao -luokkiin, rajapintojen UserAccountDaoInterface ja ActivityDaoInterface kautta. 

## Ohjelman rakenteeseen jääneet heikkoudet   
Koodi ei ole vielä valmis...

### käyttöliittymä  



### DAO-luokat  



