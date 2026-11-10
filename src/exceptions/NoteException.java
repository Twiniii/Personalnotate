package exceptions;

public class NoteException extends Exception
{
    private static final long serialVersionUID = 1L;

    private final String invalidValue;

    public NoteException(String message, String invalidValue)
    {
        super(message);
        this.invalidValue = invalidValue;
    }

    public String getInvalidValue()
    {
        return invalidValue;
    }
}