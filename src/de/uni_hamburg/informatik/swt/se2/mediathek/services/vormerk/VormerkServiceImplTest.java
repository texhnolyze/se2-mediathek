package de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk;

import de.uni_hamburg.informatik.swt.se2.mediathek.fachwerte.Kundennummer;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Vormerkungskarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.CD;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.DVD;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.ServiceObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class VormerkServiceImplTest
{
    private VormerkServiceImpl _service;
    private final Medium _medium = new DVD("MockTitle", "MockComment", "Matt Damon", 69);
    private final Medium _mediumSchonVorgemerkt = new CD("MockTitle", "MockComment", "MockInterpreter", 420);
    private final Kunde _kunde1 = new Kunde(new Kundennummer(420420), "Hugh", "Mungus");
    private final Kunde _kunde2 = new Kunde(new Kundennummer(123456), "Max", "Mustermann");
    private final Kunde _kunde3 = new Kunde(new Kundennummer(987654), "Alex", "Mustermann");

    private final Kunde _kundeSchonVorgemerkt = new Kunde(new Kundennummer(123123), "Mark", "Vorgemerkt");

    private Map<Medium, Vormerkungskarte> _vormerkungen;

    @Before
    public void setUp()
    {
        Vormerkungskarte _vormerkungskarte = new Vormerkungskarte(_mediumSchonVorgemerkt);
        _vormerkungskarte.merkeVor(_kundeSchonVorgemerkt);

        _vormerkungen = new HashMap<Medium, Vormerkungskarte>();
        _vormerkungen.put(_mediumSchonVorgemerkt, _vormerkungskarte);

        _service = new VormerkServiceImpl(_vormerkungen);
    }

    @Test
    public void merkeVor_erstelltNeueVormerkungsKarte_fallsNichtVorhanden()
            throws VormerkerException
    {
        _service.merkeVor(_kunde1, _medium);

        assertEquals(_vormerkungen.get(_medium).getNaechstenAusleiher(), _kunde1);
    }

    @Test
    public void merkeVor_merktVormerkerAufVormerkungskarte() throws VormerkerException
    {
        _service.merkeVor(_kunde1, _mediumSchonVorgemerkt);

        assertEquals(
            List.of(_kundeSchonVorgemerkt, _kunde1),
            _vormerkungen.get(_mediumSchonVorgemerkt).getVormerker()
        );
    }

    @Test
    public void merkeVor_merktVormerkerAufVormerkungskarte_fuerMedienListe()
            throws VormerkerException
    {
        _service.merkeVor(_kunde1, List.of(_mediumSchonVorgemerkt, _medium));

        assertEquals(
            List.of(_kundeSchonVorgemerkt, _kunde1),
            _vormerkungen.get(_mediumSchonVorgemerkt).getVormerker()
        );

        assertEquals(
            List.of(_kunde1),
            _vormerkungen.get(_medium).getVormerker()
        );
    }

    @Test
    public void merkeVor_informiertUeberAenderungen() throws VormerkerException
    {
        final boolean[] beobachterAufgerufen = { false };
        ServiceObserver beobachter = new ServiceObserver()
        {
            @Override
            public void reagiereAufAenderung()
            {
                beobachterAufgerufen[0] = true;
            }
        };
        _service.registriereBeobachter(beobachter);

        _service.merkeVor(_kunde1, _medium);

        assertTrue(beobachterAufgerufen[0]);
    }

    @Test(expected = VormerkerException.class)
    public void merkeVor_wirftException_wennKundeNichtVormerkenKann() throws VormerkerException
    {
        _service.merkeVor(_kundeSchonVorgemerkt, _mediumSchonVorgemerkt);
    }

    @Test
    public void istVormerkenMoeglich_istWahr_fallsKeineVormerkungskartenVorhanden()
    {
       assertTrue(_service.istVormerkenMoeglich(_medium, _kunde1));
    }

    @Test
    public void istVormerkenMoeglich_istWahr_fallsNochKeineDreiVormerkerExistieren()
    {
        assertTrue(_service.istVormerkenMoeglich(_mediumSchonVorgemerkt,
                _kunde1));
    }

    @Test
    public void istVormerkenMoeglich_istFalsch_fallsKundeSchonVormerkerIst()
    {
        assertFalse(_service.istVormerkenMoeglich(_mediumSchonVorgemerkt, _kundeSchonVorgemerkt));
    }

    @Test
    public void istVormerkenMoeglich_istFalsch_fallsSchonDreiVormerkerExistieren()
            throws VormerkerException
    {
        _service.merkeVor(_kunde1, _mediumSchonVorgemerkt);
        _service.merkeVor(_kunde2, _mediumSchonVorgemerkt);

        assertFalse(_service.istVormerkenMoeglich(_mediumSchonVorgemerkt, _kunde3));
    }

    @Test
    public void istVormerkenMoeglich_istFalsch_fallsEinMediumNichtVorgemerktWerdenKann()
    {
        assertTrue(_service.istVormerkenMoeglich(_medium, _kundeSchonVorgemerkt));
        assertFalse(_service.istVormerkenMoeglich(List.of(_medium, _mediumSchonVorgemerkt), _kundeSchonVorgemerkt));
    }

    @Test
    public void getVormerkerFuer_gibtListeVonVormerkernAusVormerkungskarte()
    {
        assertEquals(_service.getVormerkerFuer(_mediumSchonVorgemerkt), List.of(_kundeSchonVorgemerkt));
    }

    @Test
    public void getVormerkerFuer_gibtLeereListe_fallsKeineVormerkungskartenFuerMediumExistieren()
    {
        assertEquals(_service.getVormerkerFuer(_medium), Collections.emptyList());
    }

    @Test
    public void getNaechstenAusleiherFuer_gibtNaechstenKundenAusVormerkungskarte()
            throws VormerkerException
    {
        _service.merkeVor(_kunde1, _mediumSchonVorgemerkt);

        assertEquals(_service.getNaechstenAusleiherFuer(_mediumSchonVorgemerkt), _kundeSchonVorgemerkt);
    }

    @Test
    public void getVormerkerFuer_gibtNull_fallsKeineVormerkungskartenFuerMediumExistieren()
    {
        assertNull(_service.getNaechstenAusleiherFuer(_medium));
    }

    @Test
    public void getAndRemoveNaechstenAusleiherFuer_gibtNaechstenKundenAusVormerkungskarteUndLoeschtIhn()
            throws VormerkerException
    {
        _service.merkeVor(_kunde1, _mediumSchonVorgemerkt);

        assertEquals(_service.getAndRemoveNaechstenAusleiherFuer(_mediumSchonVorgemerkt), _kundeSchonVorgemerkt);
        assertEquals(_service.getAndRemoveNaechstenAusleiherFuer(_mediumSchonVorgemerkt), _kunde1);
    }

    @Test
    public void getAndRemoveNachestenVormerkerFuer_gibtNull_fallsKeineVormerkungskartenFuerMediumExistieren()
    {
        assertNull(_service.getAndRemoveNaechstenAusleiherFuer(_medium));
    }

    @Test
    public void getAndRemoveNachestenVormerkerFuer_informiertUeberAenderungen()
    {
        final boolean[] beobachterAufgerufen = { false };
        ServiceObserver beobachter = new ServiceObserver()
        {
            @Override
            public void reagiereAufAenderung()
            {
                beobachterAufgerufen[0] = true;
            }
        };
        _service.registriereBeobachter(beobachter);

        _service.getAndRemoveNaechstenAusleiherFuer(_mediumSchonVorgemerkt);

        assertTrue(beobachterAufgerufen[0]);
    }
}