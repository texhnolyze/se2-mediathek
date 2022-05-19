package de.uni_hamburg.informatik.swt.se2.mediathek.materialien;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;

public class Vormerkungskarte
{
    private final Medium _medium;
    private final ArrayBlockingQueue<Kunde> _queue;

    /**
     * Initialisiert eine neue Vormerkungskarte.
     *
     * @param medium Das Medium, zu dem diese Vormerkungskarte gehört
     *
     * @require medium != null
     */
    public Vormerkungskarte(Medium medium)
    {
        assert medium != null : "Vorbedingung verletzt: medium != null";

        _medium = medium;
        _queue = new ArrayBlockingQueue<Kunde>(3, true);
    }

    /**
     * Gibt zurück, ob ein bestimmter Kunde ein Medium vormerken kann.
     *
     * @param kunde Der Kunde, der sich auf dieser Vormerkungskarte abgebildet sehen will
     *
     * @require kunde != null
     *
     * @return Ob der Kunde das Medium vormerken kann
     */
    public boolean kannVormerken(Kunde kunde)
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";

        return !_queue.contains(kunde) && _queue.remainingCapacity() > 0;
    }

    /**
     * Merkt das hinterlegte Medium für den Kunden vor.
     *
     * @param kunde Der Kunde, der das Medium vormerken möchte
     *
     * @require kunde != null
     * @require kannVormerken(kunde)
     *
     * @ensure sindVormerkungenVorhanden()
     * @ensure istKundeVorgemerkt(kunde)
     */
    public void merkeVor(Kunde kunde)
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";
        assert kannVormerken(kunde) : "Vorbedingung verletzt: kannVormerken(kunde)";

        _queue.offer(kunde);
    }

    /**
     * Gibt eine Liste mit den Kunden zurück, die das hier hinterlegte Medium bereits
     * vormerken.
     *
     * @return Die vormerkenden Kunden; eine leere Liste, wenn es keine Vormerkenden gibt
     */
    public List<Kunde> getVormerker()
    {
        List<Kunde> vormerker = new ArrayList<Kunde>();

        _queue.forEach(kunde -> {
            if (kunde == null)
            {
                return;
            }

            vormerker.add(kunde);
        });

        return vormerker;
    }

    /**
     * Gibt zurück, ob Vormerkungen vorhanden sind.
     *
     * @return Ob Vormerkungen vorhanden sind
     */
    public boolean sindVormerkungenVorhanden()
    {
        return _queue.size() > 0;
    }

    /**
     * Gibt den Kunden, der als nächstes dran wäre, zurück, ohne ihn aus der Schlange zu entfernen
     *
     * @require sindVormerkungenVorhanden()
     *
     * @return Der Kunde, der als nächstes dran wäre
     */
    public Kunde getNaechstenAusleiher()
    {
        assert sindVormerkungenVorhanden() : "Vorbedingung verletzt: sindVormerkungenVorhanden()";

        return _queue.peek();
    }

    /**
     * Entfernt den Kunden, der als nächstes dran ist, das Medium auszuleihen und gibt ihn zurück.
     *
     * @return Den Kunden, der als nächstes dran ist, das Medium auszuleihen.
     *
     * @require sindVormerkungenVorhanden()
     */
    public Kunde getAndRemoveNaechstenAusleiher()
    {
        assert sindVormerkungenVorhanden() : "Vorbedingung verletzt: sindVormerkungenVorhanden()";

        return _queue.poll();
    }

    /**
     * Gibt zurück, ob ein bestimmter Kunde bereits vormerkt.
     *
     * @param kunde Der Kunde, auf den überprüft werden soll
     * @return Ob der Kunde bereits vormerkt
     *
     * @require kunde != null
     */
    public boolean istKundeVorgemerkt(Kunde kunde)
    {
        assert kunde != null : "Vorbedingung verletzt: kunde != null";

        return _queue.contains(kunde);
    }
}
