import exceptions.NoteException;
import factories.NoteFormatterFactory;
import interfaces.Describable;
import interfaces.NoteFormatter;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import models.Note;
import observers.ConsoleNoteObserver;
import observers.NoteStatisticsObserver;
import services.NoteDisplay;
import services.Notebook;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        try
        {
            Notebook notebook = new Notebook();
            NoteStatisticsObserver statistics = new NoteStatisticsObserver();

            notebook.addObserver(new ConsoleNoteObserver());
            notebook.addObserver(statistics);

            NoteFormatter[] formatters = NoteFormatterFactory.createAvailable();

            NoteDisplay display = new NoteDisplay(formatters[0]);

            System.out.println("Personalnotate");

            while (true)
            {
                System.out.println();
                System.out.println("Формат: " + display.getFormatName());
                System.out.println("1. Додати нотатки");
                System.out.println("2. Переглянути список та відкрити нотатку");
                System.out.println("3. Показати та порахувати за пріоритетом");
                System.out.println("4. Відсортувати за пріоритетом");
                System.out.println("5. Знайти нотатку за повним збігом");
                System.out.println("6. Змінити формат відображення");
                System.out.println("7. Переглянути статистику");
                System.out.println("0. Вийти");
                System.out.print("Оберіть дію: ");

                String choice = scanner.nextLine();

                if (choice.equals("0"))
                {
                    break;
                }
                else if (choice.equals("1"))
                {
                    if (notebook.getFreePlaces() == 0)
                    {
                        System.out.println("Масив нотаток заповнений.");
                        continue;
                    }

                    int freePlaces = notebook.getFreePlaces();

                    int amount = readInt(
                        scanner,
                        "Скільки нотаток додати (1–" + freePlaces + "): ",
                        1,
                        freePlaces
                    );

                    for (int i = 0; i < amount; i++)
                    {
                        System.out.println();

                        System.out.println(
                            "Нотатка №" + (notebook.getCount() + 1)
                        );

                        Note note = readValidNote(scanner);
                        notebook.addNote(note);
                    }

                    System.out.println("Додано нотаток: " + amount);
                }
                else if (choice.equals("2"))
                {
                    openNote(
                        scanner,
                        notebook.getNotes(),
                        notebook.getCount(),
                        display
                    );
                }
                else if (choice.equals("3"))
                {
                    if (notebook.getCount() == 0)
                    {
                        System.out.println("Поки немає створених нотаток.");
                        continue;
                    }

                    int searchPriority = readInt(
                        scanner,
                        "Введіть пріоритет для пошуку (1–10): ",
                        1,
                        10
                    );

                    int foundCount = 0;

                    for (int i = 0; i < notebook.getCount(); i++)
                    {
                        if (notebook.getNote(i).getPriority() == searchPriority)
                        {
                            System.out.println();
                            display.show(notebook.getNote(i));
                            foundCount++;
                        }
                    }

                    System.out.println("Кількість знайдених: " + foundCount);
                }
                else if (choice.equals("4"))
                {
                    if (notebook.getCount() == 0)
                    {
                        System.out.println("Поки немає створених нотаток.");
                        continue;
                    }

                    System.out.println("До сортування:");
                    printNotes(notebook.getNotes(), display);

                    notebook.sortByPriority();

                    System.out.println();
                    System.out.println("Після сортування:");
                    printNotes(notebook.getNotes(), display);
                }
                else if (choice.equals("5"))
                {
                    if (notebook.getCount() == 0)
                    {
                        System.out.println("Поки немає створених нотаток.");
                        continue;
                    }

                    System.out.println(
                        "Введіть усі поля нотатки, яку потрібно знайти."
                    );

                    Note sample = readValidNote(scanner);
                    int index = notebook.findNote(sample);

                    if (index == -1)
                    {
                        System.out.println(
                            "Нотатку з повним збігом не знайдено."
                        );
                    }
                    else
                    {
                        System.out.println(
                            "Знайдено нотатку №" + (index + 1)
                        );

                        display.show(notebook.getNote(index));
                    }
                }
                else if (choice.equals("6"))
                {
                    chooseFormatter(
                        scanner,
                        formatters,
                        display,
                        notebook.getNotes(),
                        notebook.getCount()
                    );
                }
                else if (choice.equals("7"))
                {
                    statistics.printStatistics();
                }
                else
                {
                    System.out.println("Такого варіанту немає.");
                }
            }
        }
        finally
        {
            scanner.close();
            System.out.println("До побачення!");
        }
    }

    static Note readValidNote(Scanner scanner)
    {
        while (true)
        {
            try
            {
                return readNote(scanner);
            }
            catch (NoteException error)
            {
                System.out.println(error.getMessage());

                System.out.println(
                    "Некоректне значення: [" + error.getInvalidValue() + "]"
                );

                System.out.println("Введіть нотатку повторно.");
            }
        }
    }

    static Note readNote(Scanner scanner) throws NoteException
    {
        System.out.print("Введіть назву нотатки: ");
        String title = scanner.nextLine();

        System.out.print("Введіть текст нотатки: ");
        String text = scanner.nextLine();

        System.out.print("Введіть категорію: ");
        String category = scanner.nextLine();

        int priority = readInt(
            scanner,
            "Пріоритет (1 — найвищий, 10 — найнижчий): ",
            Integer.MIN_VALUE,
            Integer.MAX_VALUE
        );

        double plannedMinutes = readMinutes(scanner);

        try
        {
            return new Note(
                title,
                text,
                category,
                priority,
                plannedMinutes
            );
        }
        catch (NoteException error)
        {
            System.err.println(
                "[Діагностика] Не вдалося створити нотатку: "
                    + error.getClass().getSimpleName()
            );

            throw error;
        }
    }

    static int readInt(
        Scanner scanner,
        String message,
        int min,
        int max
    )
    {
        while (true)
        {
            System.out.print(message);

            try
            {
                int value = scanner.nextInt();
                scanner.nextLine();

                if (value >= min && value <= max)
                {
                    return value;
                }

                System.out.printf(
                    "Введіть число від %d до %d.%n",
                    min,
                    max
                );
            }
            catch (InputMismatchException error)
            {
                scanner.nextLine();

                System.out.println(
                    "Помилка: потрібно ввести ціле число в межах типу int."
                );
            }
        }
    }

    static double readMinutes(Scanner scanner)
    {
        while (true)
        {
            System.out.print(
                "Час виконання у хвилинах (0 — не заплановано): "
            );

            try
            {
                double value = scanner.nextDouble();
                scanner.nextLine();

                if (Double.isFinite(value) && value >= 0)
                {
                    return value == 0 ? 0.0 : value;
                }

                System.out.println(
                    "Введіть скінченне невід’ємне число."
                );
            }
            catch (InputMismatchException error)
            {
                scanner.nextLine();

                System.out.println(
                    "Помилка: потрібно ввести число. "
                        + "Дробову частину відокремлюйте крапкою."
                );
            }
        }
    }

    static void printTitles(Note[] notes)
    {
        int number = 1;

        for (Note note : notes)
        {
            if (note == null)
            {
                continue;
            }

            System.out.printf(
                "%d. %s%n",
                number,
                note.getTitle()
            );

            number++;
        }
    }

    static int readNoteNumber(Scanner scanner, int noteCount)
    {
        while (true)
        {
            System.out.print(
                "Номер нотатки (0 — повернутися в меню): "
            );

            try
            {
                int selected = Integer.parseInt(
                    scanner.nextLine().trim()
                );

                if (selected == 0)
                {
                    return 0;
                }

                if (selected < 1 || selected > noteCount)
                {
                    throw new IndexOutOfBoundsException(
                        "Номер нотатки повинен бути від 1 до "
                            + noteCount + "."
                    );
                }

                return selected;
            }
            catch (NumberFormatException error)
            {
                System.out.println(
                    "Номер нотатки потрібно ввести цілим числом."
                );
            }
            catch (IndexOutOfBoundsException error)
            {
                System.out.println(error.getMessage());
            }
        }
    }

    static void openNote(
        Scanner scanner,
        Note[] notes,
        int noteCount,
        NoteDisplay display
    )
    {
        if (noteCount == 0)
        {
            System.out.println("Поки немає створених нотаток.");
            return;
        }

        printTitles(notes);

        int selected = readNoteNumber(scanner, noteCount);

        if (selected != 0)
        {
            System.out.println();
            display.show(notes[selected - 1]);
        }
    }

    static void chooseFormatter(
        Scanner scanner,
        NoteFormatter[] formatters,
        NoteDisplay display,
        Note[] notes,
        int noteCount
    )
    {
        Note preview = null;

        if (noteCount > 0)
        {
            System.out.println(
                "Оберіть нотатку для попереднього перегляду:"
            );

            printTitles(notes);

            int selected = readNoteNumber(scanner, noteCount);

            if (selected == 0)
            {
                return;
            }

            preview = notes[selected - 1];
        }

        int number = 1;

        for (NoteFormatter formatter : formatters)
        {
            System.out.println();

            System.out.printf(
                "%d. %s%n",
                number,
                formatter.getName()
            );

            if (formatter instanceof Describable describable)
            {
                System.out.println(
                    describable.getDescription()
                );
            }

            if (preview != null)
            {
                formatter.print(preview);
            }

            number++;
        }

        int selected = readInt(
            scanner,
            "Оберіть формат (0 — залишити поточний): ",
            0,
            formatters.length
        );

        if (selected != 0)
        {
            display.setFormatter(formatters[selected - 1]);

            System.out.println(
                "Обрано формат: " + display.getFormatName()
            );
        }
    }

    static void printNotes(Note[] notes, NoteDisplay display)
    {
        int number = 1;

        for (Note note : notes)
        {
            if (note == null)
            {
                continue;
            }

            System.out.println();
            System.out.println("Нотатка №" + number);

            display.show(note);
            number++;
        }
    }
}