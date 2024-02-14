package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.BankController;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Models.Transaksjon;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestBankController {

    @InjectMocks
    // denne skal testes
    private BankController bankController;

    @Mock
    // denne skal Mock'es
    private BankRepository repository;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;

    @Test
    public void hentTransaksjoner_loggetInn() {
        // arrange
        List<Transaksjon> transaksjoner = new ArrayList<>();
        transaksjoner.add(new Transaksjon(0, "fra_til_1", 50, "", "melding 1", "0", "konto_nr_1"));
        transaksjoner.add(new Transaksjon(1, "fra_til_2", 100, "", "melding 2", "0", "konto_nr_1"));

        Konto expectedKonto = new Konto("01010110523", "konto_nr_1", 10.0, "type", "NOK", transaksjoner);
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.hentTransaksjoner(anyString(), anyString(), anyString())).thenReturn(expectedKonto);

        // act
        Konto resultat = bankController.hentTransaksjoner("01010110523", "", "");

        // assert
        assertEquals(expectedKonto, resultat);
    }

    @Test
    public void hentTransaksjoner_ikkeLoggetInn() {
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        Konto resultat = bankController.hentTransaksjoner("null", "", "");

        // assert
        assertNull(resultat);
    }

    @Test
    public void hentSaldi_loggetInn() {
        // arrange
        List<Konto> expectedSaldi = Arrays.asList(new Konto(), new Konto());
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.hentSaldi("01010110523")).thenReturn(expectedSaldi);

        // act
        List<Konto> result = bankController.hentSaldi();

        // assert
        assertEquals(expectedSaldi, result);
    }

    @Test
    public void hentSaldi_ikkeLoggetInn() {
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> result = bankController.hentSaldi();

        // assert
        assertNull(result);
    }

    @Test
    public void registrerBetaling_loggetInn() {
        // arrange
        Transaksjon betaling = new Transaksjon(0, "fra_til", 50, "", "melding 1", "0", "konto_nr");
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.registrerBetaling(betaling)).thenReturn("OK");

        // act
        String result = bankController.registrerBetaling(betaling);

        // assert
        assertEquals("OK", result);
    }

    @Test
    public void registrerBetaling_ikkeLoggetInn() {
        // arrange
        Transaksjon betaling = new Transaksjon(0, "fra_til", 50, "", "melding 1", "0", "konto_nr");
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        String result = bankController.registrerBetaling(betaling);

        // assert
        assertNull(result);
    }

    @Test
    public void hentBetalinger_loggetInn() {
        // arrange
        List<Transaksjon> expectedBetalinger = Arrays.asList(new Transaksjon(), new Transaksjon());
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.hentBetalinger("01010110523")).thenReturn(expectedBetalinger);

        // act
        List<Transaksjon> result = bankController.hentBetalinger();

        // assert
        assertEquals(expectedBetalinger, result);
    }

    @Test
    public void hentBetalinger_ikkeLoggetInn() {
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Transaksjon> result = bankController.hentBetalinger();

        // assert
        assertNull(result);
    }

    @Test
    public void utforBetaling_loggetInn_Ok() {
        // arrange
        int txID = 1;
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.utforBetaling(txID)).thenReturn("OK");
        when(repository.hentBetalinger("01010110523")).thenReturn(Arrays.asList(new Transaksjon()));

        // act
        List<Transaksjon> result = bankController.utforBetaling(txID);

        // assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void utforBetaling_loggetInn_IkkeOk() {
        // arrange
        int txID = 2;
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.utforBetaling(txID)).thenReturn("Feil");

        // act
        List<Transaksjon> result = bankController.utforBetaling(txID);

        // assert
        assertNull(result);
    }

    @Test
    public void utforBetaling_ikkeLoggetInn() {
        // arrange
        int txID = 1;
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Transaksjon> result = bankController.utforBetaling(txID);

        // assert
        assertNull(result);
    }

    @Test
    public void hentKundeInfo_loggetInn() {
        // arrange
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKundeInfo(anyString())).thenReturn(enKunde);

        // act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertEquals(enKunde, resultat);
    }

    @Test
    public void hentKundeInfo_IkkeloggetInn() {

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertNull(resultat);
    }

    @Test
    public void hentKonti_LoggetInn()  {
        // arrange
        List<Konto> konti = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        konti.add(konto1);
        konti.add(konto2);

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKonti(anyString())).thenReturn(konti);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertEquals(konti, resultat);
    }

    @Test
    public void hentKonti_IkkeLoggetInn()  {
        // arrange

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertNull(resultat);
    }

    @Test
    public void endreKundeInfo_loggetInn() {
        // arrange
        Kunde innKunde = new Kunde();
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.endreKundeInfo(innKunde)).thenReturn("OK");

        // act
        String result = bankController.endre(innKunde);

        // assert
        assertEquals("OK", result);
    }

    @Test
    public void endreKundeInfo_ikkeLoggetInn() {
        // arrange
        Kunde innKunde = new Kunde();
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        String result = bankController.endre(innKunde);

        // assert
        assertNull(result);
    }
}