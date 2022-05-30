package de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Vormerkungskarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.AbstractObservableService;

public class VormerkServiceImpl extends AbstractObservableService
        implements VormerkService
{
    private Map<Medium, Vormerkungskarte> _vormerkungen;

    public VormerkServiceImpl(Map<Medium, Vormerkungskarte> vormerkungen)
    {
        _vormerkungen = vormerkungen;
    }

    @Override
    public void merkeVor(Kunde kunde, List<Medium> medien) throws VormerkerException
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        assert medien != null && medien.size()
                > 0 : "Vorbedingung verletzt: medien != null && medien.size() > 0";

        for (Medium medium : medien)
        {
            merkeVor(kunde, medium);
        }
    }

    @Override
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
        if (!vormerkungskarte.kannVormerken(kunde))
        {
            throw new VormerkerException(
                    "Dieser Kunde kann nicht Vorgemerkt werden.");
        }
        vormerkungskarte.merkeVor(kunde);
        informiereUeberAenderung();
    }

    @Override
    public boolean istVormerkenMoeglich(List<Medium> medien, Kunde kunde)
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        assert medien != null && medien.size()
                > 0 : "Vorbedingung verletzt: medien != null && medien.size() > 0";

        for (Medium medium : medien)
        {
            if (!istVormerkenMoeglich(medium, kunde))
            {
                return false;
            }
        }

        return true;
    }

    @Override
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

    @Override
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

    @Override
    public Kunde getNaechstenAusleiherFuer(Medium medium)
    {
        assert medium != null : "Vorbedingung verletzt: medium != null";

        Vormerkungskarte vormerkungskarte = _vormerkungen.get(medium);
        if (vormerkungskarte == null)
        {
            return null;
        }
        return vormerkungskarte.getNaechstenAusleiher();
    }

    @Override
    public Kunde getAndRemoveNaechstenAusleiherFür(Medium medium)
    {
        assert medium != null : "Vorbedingung verletzt: medium != null";

        Vormerkungskarte vormerkungskarte = _vormerkungen.get(medium);
        if (vormerkungskarte == null)
        {
            return null;
        }
        return vormerkungskarte.getAndRemoveNaechstenAusleiher();
    }
}
