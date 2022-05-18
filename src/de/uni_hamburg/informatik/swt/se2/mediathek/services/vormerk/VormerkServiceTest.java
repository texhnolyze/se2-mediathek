package de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk;

import de.uni_hamburg.informatik.swt.se2.mediathek.fachwerte.Kundennummer;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Vormerkungskarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.CD;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.DVD;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class VormerkServiceTest
{
    private VormerkService _service;
    private Medium _medium = new DVD("MockTitle", "MockComment", "Matt Damon", 69);

    private Medium _mediumSchonVorgemerkt = new CD("MockTitle", "MockComment", "MockInterpreter", 420);
    private Kunde _kunde = new Kunde(new Kundennummer(420420), "Hugh", "Mungus");

    private Kunde _kundeSchonVorgemerkt = new Kunde(new Kundennummer(123123), "Mark", "Vorgemerkt");

    private Vormerkungskarte _vormerkungskarte;

    private Map<Medium, Vormerkungskarte> _vormerkungen;

    @Before public void setUp() throws Exception
    {
        _vormerkungskarte = new Vormerkungskarte(_mediumSchonVorgemerkt);
        _vormerkungskarte.merkeVor(_kundeSchonVorgemerkt);
        //_vormerkungen = Collections.singletonMap(_medium, _vormerkungskarte);
        _vormerkungen = new HashMap<Medium, Vormerkungskarte>();
        _vormerkungen.put(_mediumSchonVorgemerkt, _vormerkungskarte);
        _service = new VormerkService(_vormerkungen);
    }

    @Test public void merkeVor_erstelltNeueVormerkungsKarte_fallsNichtVorhanden()
            throws VormerkerException
    {
        assertNotEquals(_service.getNaechstenAusleiherFür(_medium), _kunde);

        _service.merkeVor(_kunde, _medium);

        assertEquals(_service.getNaechstenAusleiherFür(_medium), _kunde);

    }

    @Test public void merkeVor_merktVormerkerAufVormerkunskarte()
            throws VormerkerException
    {
        _service.merkeVor(_kunde, _mediumSchonVorgemerkt);

        //assertEquals(_service.getVormerkerFür(_medium),
        //        List.of(_kundeSchonVorgemerkt, _kunde));
        assertEquals(List.of(_kundeSchonVorgemerkt, _kunde),
                _service.getVormerkerFür(_mediumSchonVorgemerkt));
    }

    @Test public void merkeVor_wirftException_wennKundeNichtVormerkenKann()
            throws VormerkerException
    {
        try
        {
            _service.merkeVor(_kundeSchonVorgemerkt, _mediumSchonVorgemerkt);
            fail("Expected VormerkException");
        }
        catch (VormerkerException vormerkerException)
        {
            // Pass
        }
    }

    @Test public void istVormerkenMoeglich()
    {
    }

    @Test public void getVormerkerFür()
    {
    }

    @Test public void getNaechstenAusleiherFür()
    {
    }
}