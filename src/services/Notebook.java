package services;

import interfaces.NoteObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import models.Note;

public class Notebook
{
    private final Note[] notes = new Note[100];
    private final List<NoteObserver> observers = new ArrayList<>();
    private int noteCount;

    public void addObserver(NoteObserver observer)
    {
        Objects.requireNonNull(
            observer,
            "Спостерігач не може бути null."
        );

        if (!observers.contains(observer))
        {
            observers.add(observer);
        }
    }

    public void removeObserver(NoteObserver observer)
    {
        observers.remove(observer);
    }

    public void addNote(Note note)
    {
        Objects.requireNonNull(
            note,
            "Нотатка не може бути null."
        );

        if (noteCount == notes.length)
        {
            throw new IllegalStateException(
                "Масив нотаток заповнений."
            );
        }

        notes[noteCount] = note;
        noteCount++;

        for (NoteObserver observer : new ArrayList<>(observers))
        {
            try
            {
                observer.onNoteAdded(note, noteCount);
            }
            catch (RuntimeException error)
            {
                System.err.println(
                    "Нотатку додано, але один зі спостерігачів не виконав дію: "
                        + error.getMessage()
                );
            }
        }
    }

    public int getCount()
    {
        return noteCount;
    }

    public int getFreePlaces()
    {
        return notes.length - noteCount;
    }

    public Note getNote(int index)
    {
        if (index < 0 || index >= noteCount)
        {
            throw new IndexOutOfBoundsException(
                "Нотатки з таким індексом немає."
            );
        }

        return notes[index];
    }

    public Note[] getNotes()
    {
        return notes.clone();
    }

    public void sortByPriority()
    {
        for (int i = 0; i < noteCount - 1; i++)
        {
            for (int j = 0; j < noteCount - 1 - i; j++)
            {
                if (notes[j].getPriority() > notes[j + 1].getPriority())
                {
                    Note temporary = notes[j];
                    notes[j] = notes[j + 1];
                    notes[j + 1] = temporary;
                }
            }
        }
    }

    public int findNote(Note sample)
    {
        for (int i = 0; i < noteCount; i++)
        {
            if (notes[i].equals(sample))
            {
                return i;
            }
        }

        return -1;
    }
}