package observers;

import interfaces.NoteObserver;
import models.Note;

public class ConsoleNoteObserver implements NoteObserver
{
    @Override
    public void onNoteAdded(Note note, int totalCount)
    {
        System.out.printf(
            "Додано нотатку «%s». Усього нотаток: %d.%n",
            note.getTitle(),
            totalCount
        );
    }
}
