package observers;

import interfaces.NoteObserver;
import models.Note;

public class NoteStatisticsObserver implements NoteObserver
{
    private int addedCount;
    private final int[] countsByPriority = new int[11];

    @Override
    public void onNoteAdded(Note note, int totalCount)
    {
        addedCount++;
        countsByPriority[note.getPriority()]++;
    }

    public void printStatistics()
    {
        System.out.println(
            "Додано нотаток за поточний запуск: " + addedCount
        );

        for (int priority = 1; priority < countsByPriority.length; priority++)
        {
            if (countsByPriority[priority] > 0)
            {
                System.out.printf(
                    "Пріоритет %d: %d%n",
                    priority,
                    countsByPriority[priority]
                );
            }
        }
    }
}