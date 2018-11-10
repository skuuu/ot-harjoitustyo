package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(1000);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void saldoAlussaOikein() {
        String vastaus = kortti.toString();
        assertEquals("saldo: 10.0", vastaus);
        
    }
    @Test
    public void saldoOikein() {
        assertEquals(1000, kortti.saldo());
        
    }
    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(200);
        String vastaus = kortti.toString();
        assertEquals("saldo: 12.0", vastaus);
        
    }
    @Test
    public void saldoVaheneeOikeinJosRahaaTarpeeksi() {
        kortti.otaRahaa(500);
        String vastaus = kortti.toString();
        assertEquals("saldo: 5.0", vastaus);
        
    }
    @Test
    public void saldoEiMuutuJosEiTarpeeksiRahaa() {
        kortti.otaRahaa(11000);
        String vastaus = kortti.toString();
        assertEquals("saldo: 10.0", vastaus);
    }
    @Test
    public void metodiPalauttaaTrueJosRahatRiittavat() {
        assertEquals(true, kortti.otaRahaa(900));
    }
    @Test
    public void metodiPalauttaaFalseJosRahatEiRiita() {
        assertEquals(false, kortti.otaRahaa(20000));
    }
    
}
