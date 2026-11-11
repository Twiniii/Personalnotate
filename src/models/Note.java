package models;

import exceptions.InvalidPriorityException;
import exceptions.InvalidTitleException;
import exceptions.NoteException;
import java.util.Locale;
import java.util.Objects;

public abstract sealed class Note extends BaseEntity
    permits TextNote, ChecklistNote
{
    private final String text;
    private final String category;
    private final int priority;
    private final double plannedMinutes;

    protected Note(
        long id,
        String title,
        String text,
        String category,
        int priority,
        double plannedMinutes
    ) throws NoteException
    {
        super(id, requireTitle(title));

        if (priority < 1 || priority > 10)
        {
            throw new InvalidPriorityException(priority);
        }

        if (!Double.isFinite(plannedMinutes) || plannedMinutes < 0)
        {
            throw new IllegalArgumentException(
                "Час повинен бути скінченним невід’ємним числом."
            );
        }

        this.text = text;
        this.category = category;
        this.priority = priority;
        this.plannedMinutes = plannedMinutes == 0 ? 0.0 : plannedMinutes;

        traceConstructor("Note: спільні поля");
    }

    private static String requireTitle(String title)
        throws InvalidTitleException
    {
        if (title == null || title.isBlank())
        {
            throw new InvalidTitleException(title);
        }

        return title;
    }

    protected static void traceConstructor(String message)
    {
        if (Boolean.getBoolean("notes.trace"))
        {
            System.out.println("[Конструктор] " + message);
        }
    }

    public abstract String getTypeName();

    public abstract String getSummary();

    public String getDisplayText()
    {
        return text;
    }

    public String getTitle()
    {
        return getName();
    }

    public String getText()
    {
        return text;
    }

    public String getCategory()
    {
        return category;
    }

    public int getPriority()
    {
        return priority;
    }

    public double getPlannedMinutes()
    {
        return plannedMinutes;
    }

    public double getPlannedHours()
    {
        return plannedMinutes / 60.0;
    }

    public String getTimeDescription()
    {
        if (plannedMinutes == 0)
        {
            return "не заплановано";
        }

        return String.format(
            Locale.US,
            "%.2f хв (%.2f год)",
            plannedMinutes,
            getPlannedHours()
        );
    }

    @Override
    public String toString()
    {
        return String.format(
            "ID: %d%nТип: %s%nНазва: %s%nТекст: %s%nКатегорія: %s%n"
                + "Пріоритет: %d%nЧас виконання: %s",
            getId(),
            getTypeName(),
            getTitle(),
            text,
            category,
            priority,
            getTimeDescription()
        );
    }

    public boolean hasSameContent(Note other)
    {
        return other != null
            && getClass() == other.getClass()
            && Objects.equals(getTitle(), other.getTitle())
            && Objects.equals(text, other.text)
            && Objects.equals(category, other.category)
            && priority == other.priority
            && Double.compare(plannedMinutes, other.plannedMinutes) == 0;
    }
}