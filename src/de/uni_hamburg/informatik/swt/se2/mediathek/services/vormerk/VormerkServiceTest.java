package de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk;

import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Vormerkungskarte;
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
    private Medium _medium;

    private Medium _mediumSchonVorgemerkt;
    private Kunde _kunde;

    private Kunde _kundeSchonVorgemerkt;

    private Vormerkungskarte _vormerkungskarte;

    private Map<Medium, Vormerkungskarte> _vormerkungen;

    @Before public void setUp() throws Exception
    {
        _vormerkungskarte = new Vormerkungskarte(_mediumSchonVorgemerkt);
        _vormerkungskarte.merkeVor(_kundeSchonVorgemerkt);
        _vormerkungen = Collections.singletonMap(_medium, _vormerkungskarte);
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

        assertEquals(_service.getVormerkerFür(_medium),
                List.of(_kundeSchonVorgemerkt, _kunde));
    }

    @Test public void merkeVor_wirftException_wennKundeNichtVormerkenKann()
            throws VormerkerException
    {
        _service.merkeVor(_kunde, _mediumSchonVorgemerkt);

        assertEquals(_service.getVormerkerFür(_medium),
                List.of(_kundeSchonVorgemerkt, _kunde));
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