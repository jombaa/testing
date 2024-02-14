package oslomet.testing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpSession;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertEquals;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestSikkerhetsController {
    @InjectMocks
    private Sikkerhet sikkerhetsController;

    @Mock
    private BankRepository repository;

    @Mock
    private MockHttpSession session;

    @Before
    public void initSession() {
        Map<String, Object> attributes = new HashMap<>();

        doAnswer(new Answer<>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                return attributes.get(key);
            }
        }).when(session).getAttribute(anyString());

        doAnswer(new Answer<>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                Object value = invocation.getArguments()[1];
                attributes.put(key, value);
                return null;
            }
        }).when(session).setAttribute(anyString(), any());
    }

    @Test
    public void testSjekkLoggInn_Godkjent() {

        String personnummer = "12345678901";
        String passord = "RiktigPassord123";

        when(repository.sjekkLoggInn(personnummer, passord)).thenReturn("OK");
        String resultat = sikkerhetsController.sjekkLoggInn(personnummer, passord);
        assertEquals("OK", resultat);
        verify(session).setAttribute("Innlogget", personnummer);
    }
    @Test
    public void testSjekkLoggInn_FeilPersonnummer() {

        String personnummer = "invalid";
        String passord = "RiktigPassord123";

        String resultat = sikkerhetsController.sjekkLoggInn(personnummer, passord);
        assertEquals("Feil i personnummer", resultat);
        verify(repository, never()).sjekkLoggInn(anyString(), anyString());
        verify(session, never()).setAttribute(anyString(), any());
    }
    @Test
    public void testSjekkLoggInn_FeilPassord() {

        String personnummer = "12345678901";
        String passord = "feil";

        String resultat = sikkerhetsController.sjekkLoggInn(personnummer, passord);
        assertEquals("Feil i passord", resultat);
        verify(repository, never()).sjekkLoggInn(anyString(), anyString());
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    public void testLoggUt() {
        sikkerhetsController.loggUt();
        verify(session).setAttribute("Innlogget", null);
    }

    @Test
    public void testLoggInnAdmin_Godkjent() {

        String bruker = "Admin";
        String passord = "Admin";

        String result = sikkerhetsController.loggInnAdmin(bruker, passord);

        assertEquals("Logget inn", result);
        verify(session).setAttribute("Innlogget", "Admin");
    }

    @Test
    public void testLoggInnAdmin_IkkeGodkjent() {

        String bruker = "Invalid";
        String passord = "Invalid";

        String result = sikkerhetsController.loggInnAdmin(bruker, passord);

        assertEquals("Ikke logget inn", result);
        verify(session).setAttribute("Innlogget", null);
    }


    @Test
    public void testLoggetInn_UserLoggedIn() {

        String personnummer = "12345678901";

        when(session.getAttribute("Innlogget")).thenReturn(personnummer);
        String resultat = sikkerhetsController.loggetInn();
        assertEquals(personnummer, resultat);
    }

    @Test
    public void testLoggetInn_UserNotLoggedIn() {
        when(session.getAttribute("Innlogget")).thenReturn(null);
        String result = sikkerhetsController.loggetInn();
        assertEquals(null, result);
    }
}
