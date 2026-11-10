public class InvalidTitleException extends NoteException
{
    public InvalidTitleException(String title)
    {
        super(
            "Назва нотатки не може бути порожньою або містити лише пробіли.",
            String.valueOf(title)
        );
    }
}