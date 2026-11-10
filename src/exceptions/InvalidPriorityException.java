package exceptions;

public class InvalidPriorityException extends NoteException
{
    private static final long serialVersionUID = 1L;

    public InvalidPriorityException(int priority)
    {
        super(
            "Пріоритет повинен бути від 1 до 10.",
            String.valueOf(priority)
        );
    }
}