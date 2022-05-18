package de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Vormerkungskarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.AbstractObservableService;

public class VormerkService extends AbstractObservableService
{
    private Map<Medium, Vormerkungskarte> _vormerkungen;

    public VormerkService(Map <Medium, Vormerkungskarte> vormerkungen)
    {
        _vormerkungen = vormerkungen;
    }

    /**
     * Merkt einen neuen Kunden bei einem Medium vor.
     *
     * @param kunde Auf den vorgemerkt werden soll
     * @param medien Liste von medien die vorgemerkt werden soll
     * @throws VormerkerException
     *
     * @require kunde != null
     * @require medien != null && medien.size() > 0
     */
    public void merkeVor(Kunde kunde, List<Medium> medien) throws VormerkerException
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        assert medien != null && medien.size() > 0 : "Vorbedingung verletzt: medien != null && medien.size() > 0";

        for (Medium medium : medien)
        {
            merkeVor(kunde, medium);
        }
    }

    /**
     * Merkt einen neuen Kunden bei einem Medium vor.
     * 
     * @param kunde Auf den vorgemerkt werden soll
     * @param medium Medium das Vorgemerkt werden soll
     * @throws VormerkerException 
     * 
     * @require kunde != null
     * @require medium != null
     */
    public void merkeVor(Kunde kunde, Medium medium) throws VormerkerException
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        assert medium != null : "Vorbedingung verletzt: medium != null";

        Vormerkungskarte vormerkungskarte = _vormerkungen.get(medium);
        if (vormerkungskarte == null)
        {
            vormerkungskarte = new Vormerkungskarte(medium);
            _vormerkungen.put(medium, vormerkungskarte);
        }
        if (vormerkungskarte.kannVormerken(kunde))
        {
            vormerkungskarte.merkeVor(kunde);
            return;
        }

        throw new VormerkerException(
                "Dieser Kunde kann nicht Vorgemerkt werden.");
    }

    /**
     * Gibt zurück ob der Kunde die Medien vormerken könnte.
     *
     * @param medien Die geprüft werden soll.
     * @param kunde Der Kunde für den geprüft werden soll
     * @return boolean true, wenn möglich; false, wenn nicht möglich
     *
     * @require kunde != null
     * @require medien != null && medien.size() > 0
     */
    public boolean istVormerkenMoeglich(List<Medium> medien, Kunde kunde)
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        assert medien != null && medien.size() > 0 : "Vorbedingung verletzt: medien != null && medien.size() > 0";

        for (Medium medium : medien) {
          if (!istVormerkenMoeglich(medium, kunde)) {
              return false;
          }
        }

        return true;
    }

    /**
     * Gibt zurück ob der Kunde das Medium vormerken könnte.
     * 
     * @param medium Das geprüft werden soll.
     * @param kunde Der Kunde für den geprüft werden soll
     * @return boolean true, wenn möglich; false, wenn nicht möglich
     * 
     * @require kunde != null
     * @require medium != null
     */
    public boolean istVormerkenMoeglich(Medium medium, Kunde kunde)
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        assert medium != null : "Vorbedingung verletzt: medium != null";

        Vormerkungskarte vormerkungskarte = _vormerkungen.get(medium);
        if (vormerkungskarte == null)
        {
            return true;
        }
        return vormerkungskarte.kannVormerken(kunde);
    }

    /**
     * Gibt die Vormerker des Mediums
     * 
     * @param medium betrffendes Medium
     * @return List<Kunde> Liste an Vormerkern
     * 
     * @require medium != null
     */
    public List<Kunde> getVormerkerFür(Medium medium)
    {
        assert medium != null : "Vorbedingung verletzt: medium != null";

        Vormerkungskarte vormerkungskarte = _vormerkungen.get(medium);
        if (vormerkungskarte == null)
        {
            return new ArrayList<Kunde>();
        }
        return vormerkungskarte.getVormerker();
    }

    /**
     * Gibt den nächsten Ausleiher für ein Medium
     * 
     * @param medium betrffendes Medium
     * @return Kunde nächster Ausleiher
     * 
     * @require medium != null
     */
    public Kunde getNaechstenAusleiherFür(Medium medium)
    {
        assert medium != null : "Vorbedingung verletzt: medium != null";

        Vormerkungskarte vormerkungskarte = _vormerkungen.get(medium);
        if (vormerkungskarte == null)
        {
            return null;
        }
        return vormerkungskarte.getNaechstenAusleiher();
    }
}
