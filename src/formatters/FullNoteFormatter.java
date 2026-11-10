package formatters;

import models.Note;

public class FullNoteFormatter extends AbstractNoteFormatter
{
    public FullNoteFormatter()
    {
        super(
            "Повний",
            "Усі поля нотатки окремими рядками."
        );
    }

    @Override
    public String format(Note note)
    {
        return note.toString();
    }
}