public class InvalidPriorityException extends NoteException
{
    public InvalidPriorityException(int priority)
    {
        super(
            "Пріоритет повинен бути від 1 до 10.",
            String.valueOf(priority)
        );
    }
}