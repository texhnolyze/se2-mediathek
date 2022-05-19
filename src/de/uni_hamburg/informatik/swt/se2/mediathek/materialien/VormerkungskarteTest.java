package de.uni_hamburg.informatik.swt.se2.mediathek.materialien;

import de.uni_hamburg.informatik.swt.se2.mediathek.fachwerte.Kundennummer;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.CD;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;
import org.junit.Test;

import static org.junit.Assert.*;

public class VormerkungskarteTest {
    private Medium _m1 = new CD("CD-Title", "Comment", "Interpreter", 150);
    private final Kunde _k1 = new Kunde(new Kundennummer(111111), "FirstName", "LastName");
    private final Kunde _k2 = new Kunde(new Kundennummer(222222), "FN", "LN");
    private final Kunde _k3 = new Kunde(new Kundennummer(333333), "FN", "LN");
    private final Kunde _k4 = new Kunde(new Kundennummer(444444), "FN", "LN");

    @Test
    public void testKannVormerkenNachKapazitaet()
    {
        // Queue full
        Vormerkungskarte v1 = getNewVormerkungskarte();
        assertTrue(v1.kannVormerken(_k1));

        v1.merkeVor(_k2);
        assertTrue(v1.kannVormerken(_k1));

        v1.merkeVor(_k3);
        assertTrue(v1.kannVormerken(_k1));

        v1.merkeVor(_k4);
        assertFalse(v1.kannVormerken(_k1));
    }

    @Test
    public void testKannVormerkenNachDoppeltemObjekt()
    {
        // Queue contains Kunde object
        Vormerkungskarte v2 = getNewVormerkungskarte();
        v2.merkeVor(_k1);
        assertFalse(v2.kannVormerken(_k1));
    }

    @Test
    public void testMerkeVor()
    {
        Vormerkungskarte v1 = getNewVormerkungskarte();

        assertFalse(v1.istKundeVorgemerkt(_k1));
        assertTrue(v1.kannVormerken(_k1));

        v1.merkeVor(_k1);

        assertTrue(v1.sindVormerkungenVorhanden());
        assertTrue(v1.istKundeVorgemerkt(_k1));
    }

    @Test
    public void testGetVormerker()
    {
        Vormerkungskarte v1 = getNewVormerkungskarte();

        assertEquals(0, v1.getVormerker().size());

        v1.merkeVor(_k1);
        assertEquals(1, v1.getVormerker().size());
        assertEquals(_k1, v1.getVormerker().get(0));

        v1.merkeVor(_k2);
        assertEquals(2, v1.getVormerker().size());
        assertEquals(_k1, v1.getVormerker().get(0));
        assertEquals(_k2, v1.getVormerker().get(1));

        v1.merkeVor(_k3);
        assertEquals(3, v1.getVormerker().size());
        assertEquals(_k1, v1.getVormerker().get(0));
        assertEquals(_k2, v1.getVormerker().get(1));
        assertEquals(_k3, v1.getVormerker().get(2));
    }

    @Test
    public void testGetNaechstenAusleiher()
    {
        Vormerkungskarte v1 = getNewVormerkungskarte();

        assertFalse(v1.sindVormerkungenVorhanden());

        v1.merkeVor(_k1);

        assertEquals(_k1, v1.getNaechstenAusleiher());
        assertEquals(_k1, v1.getNaechstenAusleiher());
        assertTrue(v1.istKundeVorgemerkt(_k1));
    }

    @Test
    public void testGetAndRemoveNaechstenAusleiher()
    {
        Vormerkungskarte v1 = getNewVormerkungskarte();

        assertFalse(v1.sindVormerkungenVorhanden());

        v1.merkeVor(_k1);
        v1.merkeVor(_k2);

        assertEquals(_k1, v1.getAndRemoveNaechstenAusleiher());
        assertEquals(_k2, v1.getAndRemoveNaechstenAusleiher());

        assertFalse(v1.sindVormerkungenVorhanden());
    }

    @Test
    public void testSindVormerkungenVorhanden()
    {
        Vormerkungskarte v1 = getNewVormerkungskarte();

        assertFalse(v1.sindVormerkungenVorhanden());

        v1.merkeVor(_k1);

        assertTrue(v1.sindVormerkungenVorhanden());

        v1.getAndRemoveNaechstenAusleiher();

        assertFalse(v1.sindVormerkungenVorhanden());
    }

    @Test
    public void testIstKundeVorgemerkt()
    {
        Vormerkungskarte v1 = getNewVormerkungskarte();

        assertFalse(v1.istKundeVorgemerkt(_k1));

        v1.merkeVor(_k1);

        assertTrue(v1.istKundeVorgemerkt(_k1));

        v1.getAndRemoveNaechstenAusleiher();

        assertFalse(v1.istKundeVorgemerkt(_k1));
    }

    private Vormerkungskarte getNewVormerkungskarte()
    {
        return new Vormerkungskarte(_m1);
    }
}
