# Arkkitehtuurikuvaus

## Rakenne


## Käyttöliittymä

Käyttöliittymä sisältää neljä erillistä näkymää
- kirjautumisnäkymä (passwordScene)
- uuden käyttäjän luominen (newUserScene)
- päänäkymä (startScene)
- asetusnäkymä (SettingsScene)

jokainen näistä on toteutettu omana Scene-olionaan. Näkymistä yksi kerrallaan on näkyvänä eli sijoitettuna sovelluksen Stageen. Graafinen käyttöliittymä on rakennettu ohjelmallisesti luokassa [gui.UserInterface](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/main/java/saastopossuapp/gui/UserInterface.java).

Käyttöliittymä on pyritty eristämään täysin sovelluslogiikasta, se ainoastaan kutsuu sopivin parametrein sovelluslogiikan toteuttavan luokan _logic_ metodeja. Luokka _logic_ taas pääsee käyttämään luokkaa _Converter_, jossa hoidetaan muunnoksia (raha- ja tyyppimuunnokset) sekä _Analysis_, jossa hoidetaan budhettiin liittyvät laskutoimitukset. 

Kun sovelluksen tilanne muuttuu, kutsutaan sovelluksen metodia _refreshScreen_ joka renderöi päänäkymän uudelleen.

## Sovelluslogiikka  

Sovelluksen loogisen datamallin muodostavat luokat [domain.UserAccount](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/main/java/saastopossuapp/domain/UserAccount.java) ja [domain.Activity](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/main/java/saastopossuapp/domain/Activity.java), jotka kuvaavat käyttäjiä ja käyttäjien kuluja (kts. tietokantakaavio kohdasta tietojen pysyväistallennus).

Toiminnallisista kokonaisuuksista vastaa luokka [logic.Logic](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/main/java/saastopossuapp/logic/Logic.java). Luokka tarjoaa kaikille käyttäliittymän toiminnoille oman metodin. Näitä ovat esim.
CheckUsername,
AddExpense,
changeBudget.

Luokka _Logic_ pääsee käsiksi käyttäjiin ja kuluihin tietojen tallennuksesta vastaavaan pakkauksessa _saastopossuapp.dao_ sijaitsevien rajapinnat _ActivityDaoInterface_ ja _UserAccountDaoInterface_ toteuttavien luokkien _ActivityDao_ ja _UserDao_ kautta. 

Muunnosten (tyyppimuunnokset ja rahamuunnokset) käisttely on eriytetty _logic_ -luokasta luokkaan [logic.Converter](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/main/java/saastopossuapp/logic/Converter.java) ja budjettiin liittyvät analyysit luokkaan [logic.Analysis](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/main/java/saastopossuapp/logic/Analysis.java).

Ohjelman osien suhdetta kuvaava luokka/pakkauskaavio:  
<img src="https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/Images/Packagediagram.jpeg" width="600">


## Tietojen pysyväistallennus  
Pakkauksen todoapp.dao luokat [dao.UserAccounDao](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/main/java/saastopossuapp/dao/UserAccountDao.java) ja [dao.ActivityDao](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/main/java/saastopossuapp/dao/ActivityDao.java) huolehtivat tietojen tallettamisesta tiedostoihin.

Luokat noudattavat Data Access Object -suunnittelumallia. Luokat on eristetty rajapintojen [dao.UseracoounDaoInterface](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/main/java/saastopossuapp/dao/UserAccountDaoInterface.java) ja [dao.ActivityDaoInterface](https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/src/main/java/saastopossuapp/dao/ActivityDaoInterface.java) taakse. Luokat ovat Dao-tyyppinsä takia korvattavissa helposti. 



### Tiedostot  

Sovellus tallentaa tiedot sql-tietokantaan (users.db), jossa on kaksi tietokantataulua:  _UserAccount_ ja _Activity_: 
<img src="https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/Images/Databasediagram.jpeg" width="700">

### Päätoiminnallisuudet  

Päätoiminnallisuudet kuvattu seuraavaksi sekvenssikaavioina. 

#### käyttäjän kirjautuminen  
Käyttäjä voi kirjautua aloitusnäkymässä painikkeesta _login_ jonka aktivaatio kutsuu logic-luokan metodia _checkUsername_ ja palauttaa true, jos käyttäjänimi löytyy tietokannasta ja muuten false. 

<img src="https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/Images/loginDiagram.jpg" width="600">

#### Uuden käyttäjän luominen  
Uuden käyttäjän voi luoda _create new user account_ -painikkeesta, jolloin käyttöliittymä kutsuu logic-luokan metodia _createUser_ joka kutsuu UserAccountDao-luokan metodia _saveOrUpdate_ ja palauttaa true, jos tallennus onnistui ja muuten false. False-tapauksissa käyttäjälle näytetään virheviesti. 

<img src="https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/Images/createNewUserDiagram.jpg" width="600">

#### Activityn luominen  
Activity luodaan valitsemalla päivä ja kategoria, syöttämällä summa ja aktivoimalla painike _add_, jolloin kutsutaan Logic-luokan metodia addExpense. Tästä metodista joko päivitetään tai lisätään tietokantaan ActivityDao-luokan saveOrUpdate-metodia käyttäen. 

<img src="https://github.com/skuuu/ot-harjoitustyo/blob/master/harjoitustyo/Images/newActivityDiagram.jpg" width="600">

#### Muut toiminnallisuudet  
Sovelluksen toimintaperiaate on samankaltainen myös muille toiminnoille. Graafinen käyttöliittymä kutsuu logic-luokkaa, josta tarvittaessa kutsutaan edelleen Analysis-luokkaa tai Converter-luokkaa. Logic-luokalla on pääsy UserAccountDao- ja ActivityDao -luokkiin rajapintojen UserAccountDaoInterface ja ActivityDaoInterface kautta. 

## Ohjelman rakenteeseen jääneet heikkoudet   


### käyttöliittymä  
Graafinen käyttöliittymä sisältää paljon parametrillisia metodeja enkä oikein tiennyt, miten ne voisi korvata fiksusti. Konstruktori ei olisi hirveästi tätä vaivaa vähentänyt.





