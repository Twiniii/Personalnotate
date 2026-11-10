package services;

import interfaces.NoteFormatter;
import java.util.Objects;
import models.Note;

public class NoteDisplay
{
    private NoteFormatter formatter;

    public NoteDisplay(NoteFormatter formatter)
    {
        this.formatter = Objects.requireNonNull(
            formatter,
            "Формат відображення не може бути null."
        );
    }

    public void setFormatter(NoteFormatter formatter)
    {
        this.formatter = Objects.requireNonNull(
            formatter,
            "Формат відображення не може бути null."
        );
    }

    public String getFormatName()
    {
        return formatter.getName();
    }

    public void show(Note note)
    {
        formatter.print(note);
    }
}