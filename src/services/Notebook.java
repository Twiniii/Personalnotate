package services;

import interfaces.NoteObserver;
import interfaces.NoteService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import models.Note;

public class Notebook
{
    private final NoteService service;
    private final List<NoteObserver> observers = new ArrayList<>();

    public Notebook(NoteService service)
    {
        this.service = Objects.requireNonNull(service);
    }

    public void addObserver(NoteObserver observer)
    {
        Objects.requireNonNull(observer);

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
        Objects.requireNonNull(note);

        for (Note existing : service.findAll())
        {
            if (existing.getId() == note.getId())
            {
                throw new IllegalArgumentException(
                    "Цей ID уже використовується."
                );
            }
        }

        service.save(note);
        int total = getCount();

        for (NoteObserver observer : new ArrayList<>(observers))
        {
            try
            {
                observer.onNoteAdded(note, total);
            }
            catch (RuntimeException error)
            {
                System.err.println(
                    "Помилка сповіщення: " + error.getMessage()
                );
            }
        }
    }

    public void updateNote(Note note)
    {
        Objects.requireNonNull(note);
        service.findById(note.getId());
        service.save(note);
    }

    public boolean deleteNote(long id)
    {
        return service.delete(id);
    }

    public Note getNote(long id)
    {
        return service.findById(id);
    }

    public List<Note> getNotes()
    {
        return service.findAll();
    }

    public int getCount()
    {
        return service.findAll().size();
    }

    public List<Note> sortedByPriority()
    {
        return service.findAllSorted(
            Comparator.comparingInt(Note::getPriority)
        );
    }

    public List<Note> sortedByName()
    {
        return service.findAllSortedByName();
    }

    public List<Note> sortedByNameAndPriority()
    {
        return service.findAllSortedByNameAndByPriority();
    }

    public Note findNote(Note sample)
    {
        for (Note note : service.findAll())
        {
            if (note.hasSameContent(sample))
            {
                return note;
            }
        }

        return null;
    }
}