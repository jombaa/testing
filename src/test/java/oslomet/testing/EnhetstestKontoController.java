package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.bind.annotation.GetMapping;
import oslomet.testing.API.AdminKontoController;
import oslomet.testing.API.BankController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Transaksjon;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestKontoController {
    @InjectMocks
    private AdminKontoController adminKontoController;

    @Mock
    private AdminRepository adminrepository;

    @Mock
    private Sikkerhet sjekk;

    @Test
    public void testHentAlleKontoerNaarInnloget() {
        List <Transaksjon> transaksjoner = new ArrayList<>();
        List <Konto> kontoer= new ArrayList<>();

        //Eksempel konto
        Konto konto1 = new Konto("12531568","351646", 1000.00, "Lønnskonto", "Nok", transaksjoner);
        kontoer.add(konto1);

        //Eksempel transaksjon
        Transaksjon transaksjon1 = new Transaksjon(10245, "1234", 500, "20-01-2024", "Betaling", "blabla", "4567" );
        transaksjoner.add(transaksjon1);

        Mockito.when(sjekk.loggetInn()).thenReturn(konto1.getPersonnummer());

        Mockito.when(adminrepository.hentAlleKonti()).thenReturn(kontoer);

        //Act
        List<Konto>resultat=adminKontoController.hentAlleKonti();

        //assert
        assertEquals(kontoer, resultat);

    }

    @Test
    public void testHentAlleKontoerIkkeInnloget(){
        Mockito.when(sjekk.loggetInn()).thenReturn(null);

        //Act
        List<Konto> resulat = adminKontoController.hentAlleKonti();

        //assert
        assertNull(resulat);


    }

    Konto testKonto = new Konto("123456789", "123", 1000.0, "Sparekonto", "NOK", new ArrayList<>());
    @Test
    public void testRegistrerKontoLoggetInn(){

        Mockito.when(sjekk.loggetInn()).thenReturn("123456789");
        Mockito.when(adminrepository.registrerKonto(Mockito.any(Konto.class))).thenReturn("Konto registrert");

        // Act
        String resultat = adminKontoController.registrerKonto(testKonto);

        // Assert
        assertEquals("Konto registrert", resultat);
    }

    @Test
    public void testRegistrerKontoIkkeLoggetInn(){
        Mockito.when(sjekk.loggetInn()).thenReturn(null);
        // Act
        String resultat = adminKontoController.registrerKonto(testKonto);

        // Assert
        assertEquals("Ikke innlogget", resultat);

    }

    @Test
    public void testEndreKontoLoggetInn(){
        Mockito.when(sjekk.loggetInn()).thenReturn("123456789");
        Mockito.when(adminrepository.endreKonto(Mockito.any(Konto.class))).thenReturn("Konto endret");

        // Act
        String resultat = adminKontoController.endreKonto(testKonto);

        // Assert
        assertEquals("Konto endret", resultat);
    }

    @Test
    public void testEndreKontoIkkeLoggetInn(){
        Mockito.when(sjekk.loggetInn()).thenReturn(null);
        String resultat = adminKontoController.endreKonto(testKonto);
        assertEquals("Ikke innlogget", resultat);
    }

    @Test
    public void testSlettKontoLoggetInn() {
        Mockito.when(sjekk.loggetInn()).thenReturn(testKonto.getPersonnummer());
        Mockito.when(adminrepository.slettKonto(testKonto.getKontonummer())).thenReturn("Konto slettet");

        String resultat = adminKontoController.slettKonto(testKonto.getKontonummer());

        assertEquals("Konto slettet", resultat);
    }

    @Test
    public void testSlettKontoIkkeLoggetInn() {
        Mockito.when(sjekk.loggetInn()).thenReturn(null);
        String resultat = adminKontoController.slettKonto(testKonto.getKontonummer());
        assertEquals("Ikke innlogget", resultat);
    }


}







