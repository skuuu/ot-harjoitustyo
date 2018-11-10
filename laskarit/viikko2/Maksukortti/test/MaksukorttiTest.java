
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaksukorttiTest {
    
    Maksukortti kortti;
    

 
    
    @Before
    public void setUp() {
       kortti = new Maksukortti(10);

    }
    

    
    @Test
    public void konstruktoriAsettaaSaldonOikein() {
        String vastaus = kortti.toString(); 
        assertEquals("Kortilla on rahaa 10.0 euroa", vastaus);
    }
    @Test
    public void syoMaukkaastiEiVieSaldoaNegatiiviseksi (){
        kortti = new Maksukortti(3);
        String vastaus = kortti.toString();
        assertEquals("Kortilla on rahaa 3.0 euroa", vastaus);
        
    }
    @Test
    public void negatiivisenSummanLataaminenEiMuutaSaldoa (){
        kortti = new Maksukortti(3);
        kortti.lataaRahaa(-1);
        String vastaus = kortti.toString();
        assertEquals("Kortilla on rahaa 3.0 euroa", vastaus);
        
    }
    @Test
    public void tasasummallaVoiOstaaEdullisenLounaan (){
        kortti = new Maksukortti(2.5);
        kortti.syoEdullisesti();
        String vastaus = kortti.toString();
        assertEquals("Kortilla on rahaa 0.0 euroa", vastaus);


    }
    @Test
    public void tasasummallaVoiOstaaMaukkaanLounaan (){
        kortti = new Maksukortti(4.0);
        kortti.syoMaukkaasti();
        String vastaus = kortti.toString();
        assertEquals("Kortilla on rahaa 0.0 euroa", vastaus);
    }

}
