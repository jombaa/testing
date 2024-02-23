package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKundeController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestKundeController {

    @InjectMocks
    //Denne skal testes
    private AdminKundeController kundeController;

    @Mock
    //Denne skal mockes
    private AdminRepository repository;

    @Mock
    //Denne skal Mockes
    private Sikkerhet sjekk;

    @Test
    public void test_hentAlleOk() {

        //Arrange
        Kunde kunde1 = new Kunde("12345678901", "Bror",
                "Brorson", "Blindveiveien 1", "1234",
                "Huttiheita", "99887766", "Passord123");

        when(sjekk.loggetInn()).thenReturn("12345678901");

        Kunde kunde2 = new Kunde("23456789012", "Tor",
                "Torson", "Snurundtveien 1", "4321",
                "Langtvekkistan", "11223344", "321Passord");

        when(sjekk.loggetInn()).thenReturn("23456789012");

        List<Kunde> kundeListe = new ArrayList<>();
        kundeListe.add(kunde1);
        kundeListe.add(kunde2);
        
        Mockito.when(repository.hentAlleKunder()).thenReturn(kundeListe);
        
        //Act
        List<Kunde> resultat = kundeController.hentAlle();
        
        //Assert
        assertEquals(kundeListe, resultat);
    }

    @Test
    public void test_hentAlleFeil() {

        //Arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //Act
        List<Kunde> resultat = kundeController.hentAlle();

        //Assert
        assertNull(resultat);
    }

    @Test
    public void lagreKundeOk() {

        //Arrange
        Kunde enKunde = new Kunde("12345678901", "Bror",
                "Brorson", "Blindveiveien 1", "1234",
                "Huttiheita", "99887766", "Passord123");

        when(sjekk.loggetInn()).thenReturn("12345678901");

        when(repository.registrerKunde(any())).thenReturn("OK");

        //Act
        String resultat = kundeController.lagreKunde(enKunde);

        //Assert
        assertEquals("OK", resultat);
    }

    @Test
    public void lagreKundeFeil() {

        //Arrange
        Kunde enKunde = new Kunde("12345678901", "Bror",
                "Brorson", "Blindveiveien 1", "1234",
                "Huttiheita", "99887766", "Passord123");

        when(sjekk.loggetInn()).thenReturn(null);

        //Act
        String resultat = kundeController.lagreKunde(enKunde);

        //Assert
        assertEquals("Ikke logget inn", resultat);
    }

    @Test
    public void endreKundeOk() {

        //Arrange
        Kunde enKunde = new Kunde("12345678901", "Bror",
                "Brorson", "Blindveiveien 1", "1234",
                "Huttiheita", "99887766", "Passord123");

        when(sjekk.loggetInn()).thenReturn("12345678901");

        when(repository.endreKundeInfo(any())).thenReturn("OK");

        //Act
        String resultat = kundeController.endre(enKunde);

        //Assert
        assertEquals("OK", resultat);
    }

    @Test
    public void endreKundeFeil() {

        //Arrange
        Kunde enKunde = new Kunde("12345678901", "Bror",
                "Brorson", "Blindveiveien 1", "1234",
                "Huttiheita", "99887766", "Passord123");

        when(sjekk.loggetInn()).thenReturn(null);

        //Act
        String resultat = kundeController.endre(enKunde);

        //Assert
        assertEquals("Ikke logget inn", resultat);
    }

    @Test
    public void slettKundeOk() {

        //Arrange
        Kunde enKunde = new Kunde("12345678901", "Bror",
                "Brorson", "Blindveiveien 1", "1234",
                "Huttiheita", "99887766", "Passord123");

        when(sjekk.loggetInn()).thenReturn("12345678901");

        when(repository.slettKunde(any())).thenReturn("OK");

        //Act
        String resultat = kundeController.slett(enKunde.getPersonnummer());

        //Assert
        assertEquals("OK", resultat);
    }

    @Test
    public void slettKundeFeil() {

        //Arrange
        Kunde enKunde = new Kunde("12345678901", "Bror",
                "Brorson", "Blindveiveien 1", "1234",
                "Huttiheita", "99887766", "Passord123");

        when(sjekk.loggetInn()).thenReturn(null);

        //Act
        String resultat = kundeController.slett(enKunde.getPersonnummer());

        //Assert
        assertEquals("Ikke logget inn", resultat);
    }
}