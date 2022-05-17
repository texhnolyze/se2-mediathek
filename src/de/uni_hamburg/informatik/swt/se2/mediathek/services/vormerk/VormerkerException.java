package de.uni_hamburg.informatik.swt.se2.mediathek.services.vormerk;

/**
 * Eine VormerkerException signalisiert, dass das Vormerken eines
 * Mediums fehlgeschlagen ist.
 */
public class VormerkerException extends Exception
{

    private static final long serialVersionUID = 1L;

    /**
     * Initilaisert eine neue Exception mit der übergebenen Fehlermeldung.
     * 
     * @param message Eine Fehlerbeschreibung.
     */
    public VormerkerException(String message)
    {
        super(message);
    }
}
