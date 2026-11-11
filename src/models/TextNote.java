package models;

import exceptions.NoteException;
import java.util.Objects;

public final class TextNote extends Note
{
    private final String source;

    public TextNote(
        long id,
        String title,
        String text,
        String category,
        int priority,
        double plannedMinutes
    ) throws NoteException
    {
        this(
            id,
            title,
            text,
            category,
            priority,
            plannedMinutes,
            "не вказано"
        );

        traceConstructor(
            "TextNote: короткий конструктор після this(...)"
        );
    }

    public TextNote(
        long id,
        String title,
        String text,
        String category,
        int priority,
        double plannedMinutes,
        String source
    ) throws NoteException
    {
        super(id, title, text, category, priority, plannedMinutes);

        this.source = source == null || source.isBlank()
            ? "не вказано"
            : source;

        traceConstructor("TextNote: джерело");
    }

    public String getSource()
    {
        return source;
    }

    @Override
    public String getTypeName()
    {
        return "Текстова нотатка";
    }

    @Override
    public String getSummary()
    {
        return "Джерело: " + source;
    }

    @Override
    public String getDisplayText()
    {
        return super.getDisplayText()
            + System.lineSeparator()
            + getSummary();
    }

    @Override
    public String toString()
    {
        return super.toString()
            + System.lineSeparator()
            + getSummary();
    }

    @Override
    public boolean hasSameContent(Note other)
    {
        if (!super.hasSameContent(other))
        {
            return false;
        }

        TextNote sameType = (TextNote) other;

        return Objects.equals(source, sameType.source);
    }
}