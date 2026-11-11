package adapters;

import interfaces.Describable;
import interfaces.NoteFormatter;
import java.util.Objects;
import legacy.LegacyNoteRenderer;
import models.Note;

public class LegacyNoteFormatterAdapter
    implements NoteFormatter, Describable
{
    private final LegacyNoteRenderer renderer;

    public LegacyNoteFormatterAdapter(LegacyNoteRenderer renderer)
    {
        this.renderer = Objects.requireNonNull(
            renderer,
            "Компонент відображення не може бути null."
        );
    }

    @Override
    public String getName()
    {
        return "Картка";
    }

    @Override
    public String getDescription()
    {
        return "Усі поля нотатки у вигляді текстової картки.";
    }

    @Override
    public String format(Note note)
    {
        return renderer.renderRecord(
            "ID: " + note.getId()
                + " | " + note.getTypeName()
                + ": " + note.getTitle(),
            note.getDisplayText(),
            note.getCategory(),
            note.getPriority(),
            note.getPlannedMinutes()
        );
    }
}