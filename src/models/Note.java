package models;

import exceptions.InvalidPriorityException;
import exceptions.InvalidTitleException;
import exceptions.NoteException;
import java.util.Locale;
import java.util.Objects;

public class Note
{
    private final String title;
    private final String text;
    private final String category;
    private final int priority;
    private final double plannedMinutes;

    public Note(
        String title,
        String text,
        String category,
        int priority,
        double plannedMinutes
    ) throws NoteException
    {
        if (title == null || title.isBlank())
        {
            throw new InvalidTitleException(title);
        }

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

        this.title = title;
        this.text = text;
        this.category = category;
        this.priority = priority;
        this.plannedMinutes = plannedMinutes == 0 ? 0.0 : plannedMinutes;
    }

    public String getTitle()
    {
        return title;
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
            "Назва: %s%nТекст: %s%nКатегорія: %s%n"
                + "Пріоритет: %d%nЧас виконання: %s",
            title,
            text,
            category,
            priority,
            getTimeDescription()
        );
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (object == null || getClass() != object.getClass())
        {
            return false;
        }

        Note other = (Note) object;

        return Objects.equals(title, other.title)
            && Objects.equals(text, other.text)
            && Objects.equals(category, other.category)
            && priority == other.priority
            && Double.compare(plannedMinutes, other.plannedMinutes) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(
            title,
            text,
            category,
            priority,
            plannedMinutes
        );
    }
}