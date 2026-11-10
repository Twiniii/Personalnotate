import java.util.Locale;
import java.util.Objects;

public class Note
{
    String title;
    String text;
    String category;
    int priority;
    double plannedMinutes;

    public Note(
        String title,
        String text,
        String category,
        int priority,
        double plannedMinutes
    )
    {
        this.title = title;
        this.text = text;
        this.category = category;
        this.priority = priority;
        this.plannedMinutes = plannedMinutes;
    }

    public double getPlannedHours()
    {
        return plannedMinutes / 60.0;
    }

    @Override
    public String toString()
    {
        String time;

        if (plannedMinutes == 0)
        {
            time = "не заплановано";
        }
        else
        {
            time = String.format(
                Locale.US,
                "%.2f хв (%.2f год)",
                plannedMinutes,
                getPlannedHours()
            );
        }

        return String.format(
            "Назва: %s%nТекст: %s%nКатегорія: %s%n"
                + "Пріоритет: %d%nЧас виконання: %s",
            title,
            text,
            category,
            priority,
            time
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