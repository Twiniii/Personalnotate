public class NoteException extends Exception
{
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