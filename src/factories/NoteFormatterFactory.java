package factories;

import adapters.LegacyNoteFormatterAdapter;
import formatters.CompactNoteFormatter;
import formatters.FullNoteFormatter;
import interfaces.NoteFormatter;
import legacy.LegacyNoteRenderer;

public final class NoteFormatterFactory
{
    private NoteFormatterFactory()
    {
    }

    public static NoteFormatter create(String type)
    {
        if (type == null)
        {
            throw new IllegalArgumentException(
                "Тип формату не вказано."
            );
        }

        return switch (type)
        {
            case "full" -> new FullNoteFormatter();

            case "compact" -> new CompactNoteFormatter();

            case "card" -> new LegacyNoteFormatterAdapter(
                new LegacyNoteRenderer()
            );

            default -> throw new IllegalArgumentException(
                "Невідомий тип формату: " + type
            );
        };
    }

    public static NoteFormatter[] createAvailable()
    {
        return new NoteFormatter[] {
            create("full"),
            create("compact"),
            create("card")
        };
    }
}