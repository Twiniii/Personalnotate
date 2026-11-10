package exceptions;

public class InvalidTitleException extends NoteException
{
    private static final long serialVersionUID = 1L;

    public InvalidTitleException(String title)
    {
        super(
            "Назва нотатки не може бути порожньою або містити лише пробіли.",
            String.valueOf(title)
        );
    }
}