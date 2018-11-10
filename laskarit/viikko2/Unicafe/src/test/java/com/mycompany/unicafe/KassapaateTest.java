
package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class KassapaateTest {
    
    Kassapaate kassapaate; 
    Maksukortti kortti;
    private int kassassaRahaa;
    private int edulliset;
    private int maukkaat;
    
 
    public KassapaateTest() {
         
    }
//    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
    
    @Before
    public void setUp() {
        kassapaate = new Kassapaate();
        kassassaRahaa = 100000;
        edulliset = 0;
        maukkaat = 0;
    }
    
    //KÄTEISOSTOT:
    @Test
    public void edullisetAlussaOikein() {
        assertEquals(kassapaate.edullisiaLounaitaMyyty(), 0);      
    }
    
    @Test
    public void maukkaatAlussaOikein() {
        assertEquals(kassapaate.maukkaitaLounaitaMyyty(), 0);      
    }
    @Test
    public void rahamaaraAlussaOikein() {
        assertEquals(kassapaate.kassassaRahaa(), 100000);      
    }
    
    @Test
    public void kunRahatRiittavatNiinedullisestiSyotaessaVaihtorahaOikein() {
        assertEquals(kassapaate.syoEdullisesti(480), 240);      
    }
    @Test
    public void kunRahatRiittavatNiinmaukkaastiSyotaessaVaihtorahaOikein() {
        assertEquals(kassapaate.syoMaukkaasti(800), 400);      
    }
    @Test
    public void kunRahatRiittavatNiinMaukkaastiSyotaessaMaukkaidenMaaraKasvaa() {
        kassapaate.syoMaukkaasti(800);
        assertEquals(kassapaate.maukkaitaLounaitaMyyty(), 1);
    }
    @Test
    public void kunRahatRiittavatNiinedullisestiSyotaessaEdullistenMaaraKasvaa() {
        kassapaate.syoEdullisesti(800);
        assertEquals(kassapaate.edullisiaLounaitaMyyty(), 1);
    
    //Kun rahaa ei riittävästi: 
    }
    @Test
    public void kunRahatEiRiitaNiinEdullisestiSyotaessaVaihtorahaOikein() {
        assertEquals(kassapaate.syoEdullisesti(210), 210);      
    }
    @Test
    public void kunRahatEiRiitaNiinMaukkaastiSyotaessaVaihtorahaOikein() {
        assertEquals(kassapaate.syoMaukkaasti(210), 210);      
    }
    @Test
    public void kunRahatEiRiitaNiinEdullisestiSyotaessaEdullistenMaaraEiKasva() {
        kassapaate.syoEdullisesti(0);
        assertEquals(kassapaate.edullisiaLounaitaMyyty(), 0);
    }
    @Test
    public void kunRahatEiRiitaNiinMaukkaastiSyotaessaMaukkaidenMaaraEiKasva() {
        kassapaate.syoMaukkaasti(0);
        assertEquals(kassapaate.maukkaitaLounaitaMyyty(), 0);
    }
    @Test
    public void kunRahatEiRiitaNiinKassanRahamaaraanEiMuutosta() {
        kassapaate.syoMaukkaasti(210);
        assertEquals(kassapaate.kassassaRahaa(), 100000);
    }
    
    //KORTTIOSTOT:
    @Test
    public void kunRahatRiittavatNiinTrue() {
        kortti = new Maksukortti(10000);
        assertEquals(true, kassapaate.syoEdullisesti(kortti));
    }
    @Test
    public void kunRahatEiRiitaNiinFalse() {
        kortti = new Maksukortti(1);
        assertEquals(false, kassapaate.syoEdullisesti(kortti));
    
    }
    @Test
    public void kunRahatRiittavatNiinEdullistenMaaraKasvaa() {
        kortti = new Maksukortti(10000);
        kassapaate.syoEdullisesti(kortti);
        assertEquals(1, kassapaate.edullisiaLounaitaMyyty());    //odotettu, tulos
    }
    @Test
    public void kunRahatRiittavatNiinMaukkaidenMaaraKasvaa() {
        kortti = new Maksukortti(10000);
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(1, kassapaate.maukkaitaLounaitaMyyty());    //odotettu, tulos
    }
    @Test
    public void kunRahatRiittavatNiinKassanRahamaaraEiMuutu() {
        kortti = new Maksukortti(10000);
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(100000, kassapaate.kassassaRahaa());    //odotettu, tulos
    }
    @Test
    public void kunRahatRiittavatNiinKorttiaVeloitetaan() {
        kortti = new Maksukortti(500);
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(100, kortti.saldo());    //odotettu, tulos
    }
    @Test
    public void kunRahatEiRiitaNiinLounaidenMaaraEiMuutu() {
        kortti = new Maksukortti(100);
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());   
    }
    @Test
    public void kunRahatEiRiitaNiinPalautetaanFalse() {
        kortti = new Maksukortti(100);
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(false, kassapaate.syoMaukkaasti(kortti));    
    }
    @Test
    public void kunRahatEiRiitaNiinKorttiaEiVeloiteta() {
        kortti = new Maksukortti(100);
        kassapaate.syoMaukkaasti(kortti);
        assertEquals(100, kortti.saldo());    
    }
    
    @Test
    public void kunRahaaLadataanKortinSaldoMuuttuu() {
        kortti = new Maksukortti(100);
        kassapaate.lataaRahaaKortille(kortti, 100);
        assertEquals(200, kortti.saldo());    
    }
    @Test
    public void kunRahaaLadataanKassaanLisaaRahaa() {
        kortti = new Maksukortti(100);
        kassapaate.lataaRahaaKortille(kortti, 100);
        assertEquals(100100, kassapaate.kassassaRahaa());    
    }
    @Test
    public void negatiivinenLatausEiMuutaKortinSaldoa() {
        kortti = new Maksukortti(100);
        kassapaate.lataaRahaaKortille(kortti, -100);
        assertEquals(100000, kassapaate.kassassaRahaa());    
    }

}
