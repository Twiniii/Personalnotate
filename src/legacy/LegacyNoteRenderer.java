package legacy;

import java.util.Locale;

public class LegacyNoteRenderer
{
    public String renderRecord(
        String caption,
        String body,
        String group,
        int importance,
        double minutes
    )
    {
        String time = minutes == 0
            ? "не заплановано"
            : String.format(
                Locale.US,
                "%.2f хв (%.2f год)",
                minutes,
                minutes / 60.0
            );

        return String.format(
            "========== %s ==========%n"
                + "Категорія: %s | Пріоритет: %d%n"
                + "%s%nЧас виконання: %s%n"
                + "==============================",
            caption,
            group,
            importance,
            body,
            time
        );
    }
}