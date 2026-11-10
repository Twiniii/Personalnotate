package formatters;

import models.Note;

public class CompactNoteFormatter extends AbstractNoteFormatter
{
    public CompactNoteFormatter()
    {
        super(
            "Стислий",
            "Назва, тип, категорія, пріоритет і короткий підсумок."
        );
    }

    @Override
    public String format(Note note)
    {
        return titleLine(note) + String.format(
            " | Тип: %s | Категорія: %s | Пріоритет: %d | %s",
            note.getTypeName(),
            note.getCategory(),
            note.getPriority(),
            note.getSummary()
        );
    }
}