package de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk;

import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Kunde;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.Vormerkungskarte;
import de.uni_hamburg.informatik.swt.se2.mediathek.materialien.medien.Medium;
import de.uni_hamburg.informatik.swt.se2.mediathek.services.ObservableService;

import java.util.ArrayList;
import java.util.List;


/**
 * Der VormerkService erlaubt es, Medien vorzumerken.
 *
 * Für jedes neu vorgemerktes Medium wird eine neue Vormerkungskarte angelegt.
 * Die maximale Anzahl von Vormerkern für ein Medium ist 3.
 * Es ist nur dem ersten Vormerker als nächstes möglich ein Medium auszuleihen.
 *
 * VormerkService ist ein ObservableService, als solcher bietet er die
 * Möglichkeit über Vormerkungsvorgänge zu informieren. Beobachter müssen das
 * Interface ServiceObserver implementieren.
 */
public interface VormerkService extends ObservableService
{
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
    void merkeVor(Kunde kunde, List<Medium> medien) throws VormerkerException;

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
    void merkeVor(Kunde kunde, Medium medium) throws VormerkerException;

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
    boolean istVormerkenMoeglich(List<Medium> medien, Kunde kunde);

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
    boolean istVormerkenMoeglich(Medium medium, Kunde kunde);

    /**
     * Gibt die Vormerker des Mediums
     *
     * @param medium betrffendes Medium
     * @return List<Kunde> Liste an Vormerkern
     *
     * @require medium != null
     */
    List<Kunde> getVormerkerFür(Medium medium);

    /**
     * Gibt den nächsten Ausleiher für ein Medium
     *
     * @param medium betrffendes Medium
     * @return Kunde nächster Ausleiher
     *
     * @require medium != null
     */
    Kunde getNaechstenAusleiherFuer(Medium medium);

    /**
     * Gibt den nächsten Ausleiher für ein Medium und entfernt ihn aus den Vormerkungen
     *
     * @param medium betreffendes Medium
     * @return Kunde nächster Ausleiher
     *
     * @require medium != null
     */
    Kunde getAndRemoveNaechstenAusleiherFür(Medium medium);
}
