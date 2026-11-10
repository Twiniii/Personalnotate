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
        return titleLine(note) + String.format(
            "%nТекст: %s%nКатегорія: %s%nПріоритет: %d%nЧас виконання: %s",
            note.getText(),
            note.getCategory(),
            note.getPriority(),
            note.getTimeDescription()
        );
    }
}