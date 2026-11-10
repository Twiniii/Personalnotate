package formatters;

import models.Note;

public class CompactNoteFormatter extends AbstractNoteFormatter
{
    public CompactNoteFormatter()
    {
        super(
            "Стислий",
            "Назва, категорія та пріоритет одним рядком."
        );
    }

    @Override
    public String format(Note note)
    {
        return titleLine(note) + String.format(
            " | Категорія: %s | Пріоритет: %d",
            note.getCategory(),
            note.getPriority()
        );
    }
}