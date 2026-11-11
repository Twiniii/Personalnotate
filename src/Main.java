import exceptions.EntityNotFoundException;
import exceptions.NoteException;
import factories.NoteFormatterFactory;
import interfaces.CRUDService;
import interfaces.Describable;
import interfaces.NoteFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import models.Category;
import models.ChecklistNote;
import models.Note;
import models.ShoppingListNote;
import models.TextNote;
import observers.ConsoleNoteObserver;
import observers.NoteStatisticsObserver;
import services.InMemoryCrudService;
import services.InMemoryNoteService;
import services.NoteDisplay;
import services.Notebook;
import utils.EntityPrinter;
import utils.IdGenerator;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        try
        {
            IdGenerator ids = new IdGenerator();

            Notebook notebook = new Notebook(
                new InMemoryNoteService()
            );

            CRUDService<Category> categories =
                new InMemoryCrudService<>();

            NoteStatisticsObserver statistics =
                new NoteStatisticsObserver();

            notebook.addObserver(new ConsoleNoteObserver());
            notebook.addObserver(statistics);

            NoteFormatter[] formatters =
                NoteFormatterFactory.createAvailable();

            NoteDisplay display = new NoteDisplay(formatters[0]);

            while (true)
            {
                printMenu(display);
                String choice = scanner.nextLine();

                if (choice.equals("0"))
                {
                    break;
                }

                try
                {
                    switch (choice)
                    {
                        case "1" ->
                        {
                            int amount = readInt(
                                scanner,
                                "Скільки додати (1–100 за раз): ",
                                1,
                                100
                            );

                            for (int i = 0; i < amount; i++)
                            {
                                Note note = readValidNote(
                                    scanner,
                                    ids.next(),
                                    false,
                                    categories
                                );

                                notebook.addNote(note);
                            }
                        }

                        case "2" ->
                        {
                            Note note = chooseNote(scanner, notebook);

                            if (note != null)
                            {
                                display.show(note);
                            }
                        }

                        case "3" ->
                            showByPriority(scanner, notebook, display);

                        case "4" ->
                            showSorted(
                                notebook.getNotes(),
                                notebook.sortedByPriority(),
                                display
                            );

                        case "5" ->
                            searchByContent(
                                scanner,
                                notebook,
                                categories,
                                display
                            );

                        case "6" ->
                            chooseFormatter(
                                scanner,
                                formatters,
                                display,
                                notebook
                            );

                        case "7" ->
                        {
                            System.out.println(
                                "Зараз у блокноті: " + notebook.getCount()
                            );

                            statistics.printStatistics();
                        }

                        case "8" ->
                            changeItemState(scanner, notebook, display);

                        case "9" ->
                        {
                            Note old = chooseNote(scanner, notebook);

                            if (old != null)
                            {
                                System.out.println(
                                    "Введіть усі нові поля та стани пунктів."
                                );

                                Note updated = readValidNote(
                                    scanner,
                                    old.getId(),
                                    true,
                                    categories
                                );

                                notebook.updateNote(updated);

                                System.out.println(
                                    "Оновлено ID " + old.getId()
                                        + ". Кількість нотаток: "
                                        + notebook.getCount()
                                );
                            }
                        }

                        case "10" ->
                        {
                            printTitles(notebook.getNotes());

                            long id = readId(scanner);

                            if (id != 0)
                            {
                                System.out.println(
                                    notebook.deleteNote(id)
                                        ? "Нотатку видалено."
                                        : "Нотатки з таким ID немає."
                                );
                            }
                        }

                        case "11" ->
                            showSorted(
                                notebook.getNotes(),
                                notebook.sortedByName(),
                                display
                            );

                        case "12" ->
                            showSorted(
                                notebook.getNotes(),
                                notebook.sortedByNameAndPriority(),
                                display
                            );

                        case "13" ->
                            manageCategories(scanner, categories, ids);

                        default ->
                            System.out.println("Такого варіанту немає.");
                    }
                }
                catch (EntityNotFoundException error)
                {
                    System.out.println(error.getMessage());
                }
                catch (IllegalArgumentException error)
                {
                    System.out.println(error.getMessage());
                }
            }
        }
        finally
        {
            scanner.close();
            System.out.println("До побачення!");
        }
    }

    static void printMenu(NoteDisplay display)
    {
        System.out.println(
            "\nPersonalnotate | Формат: " + display.getFormatName()
        );

        System.out.println("1. Додати нотатки");
        System.out.println("2. Переглянути список та відкрити за ID");
        System.out.println("3. Показати та порахувати за пріоритетом");
        System.out.println("4. Показати відсортованими за пріоритетом");
        System.out.println("5. Знайти за повним збігом вмісту");
        System.out.println("6. Змінити формат відображення");
        System.out.println("7. Переглянути статистику");
        System.out.println("8. Змінити стан пункту списку");
        System.out.println("9. Редагувати нотатку");
        System.out.println("10. Видалити нотатку");
        System.out.println("11. Показати відсортованими за назвою");
        System.out.println("12. Показати за назвою та пріоритетом");
        System.out.println("13. Довідник категорій");
        System.out.println("0. Вийти");
        System.out.print("Оберіть дію: ");
    }

    static long readId(Scanner scanner)
    {
        while (true)
        {
            System.out.print("ID (0 — повернутися): ");

            try
            {
                long id = Long.parseLong(scanner.nextLine().trim());

                if (id >= 0)
                {
                    return id;
                }

                System.out.println("ID не може бути від’ємним.");
            }
            catch (NumberFormatException error)
            {
                System.out.println(
                    "Введіть ціле число в межах типу long."
                );
            }
        }
    }

    static void printTitles(List<Note> notes)
    {
        if (notes.isEmpty())
        {
            System.out.println("Поки немає створених нотаток.");
        }

        for (Note note : notes)
        {
            System.out.printf(
                "ID %d. %s%n",
                note.getId(),
                note.getTitle()
            );
        }
    }

    static Note chooseNote(Scanner scanner, Notebook notebook)
    {
        List<Note> notes = notebook.getNotes();

        printTitles(notes);

        if (notes.isEmpty())
        {
            return null;
        }

        long id = readId(scanner);

        return id == 0 ? null : notebook.getNote(id);
    }

    static void showByPriority(
        Scanner scanner,
        Notebook notebook,
        NoteDisplay display
    )
    {
        int priority = readInt(
            scanner,
            "Пріоритет для пошуку (1–10): ",
            1,
            10
        );

        int count = 0;

        for (Note note : notebook.getNotes())
        {
            if (note.getPriority() == priority)
            {
                display.show(note);
                count++;
            }
        }

        System.out.println("Кількість знайдених: " + count);
    }

    static void printNotes(List<Note> notes, NoteDisplay display)
    {
        for (Note note : notes)
        {
            System.out.println();
            System.out.println(note.getSummary());
            display.show(note);
        }
    }

    static void showSorted(
        List<Note> before,
        List<Note> sorted,
        NoteDisplay display
    )
    {
        if (before.isEmpty())
        {
            System.out.println("Поки немає створених нотаток.");
            return;
        }

        System.out.println("До сортування:");
        printNotes(before, display);

        System.out.println("Відсортований список:");
        printNotes(sorted, display);

        System.out.println(
            "Порядок у сховищі залишився незмінним."
        );
    }

    static void searchByContent(
        Scanner scanner,
        Notebook notebook,
        CRUDService<Category> categories,
        NoteDisplay display
    )
    {
        List<Note> notes = notebook.getNotes();

        if (notes.isEmpty())
        {
            System.out.println("Поки немає створених нотаток.");
            return;
        }

        System.out.println(
            "Введіть тип, усі поля та поточні стани пунктів."
        );

        Note sample = readValidNote(
            scanner,
            notes.get(0).getId(),
            true,
            categories
        );

        Note found = notebook.findNote(sample);

        if (found == null)
        {
            System.out.println(
                "Нотатку з повним збігом не знайдено."
            );
        }
        else
        {
            System.out.println(
                "Знайдено нотатку з ID " + found.getId()
            );

            display.show(found);
        }
    }

    static void chooseFormatter(
        Scanner scanner,
        NoteFormatter[] formatters,
        NoteDisplay display,
        Notebook notebook
    )
    {
        Note preview = null;

        if (notebook.getCount() > 0)
        {
            System.out.println(
                "Оберіть нотатку для попереднього перегляду:"
            );

            preview = chooseNote(scanner, notebook);

            if (preview == null)
            {
                return;
            }
        }

        int number = 1;

        for (NoteFormatter formatter : formatters)
        {
            System.out.printf(
                "%n%d. %s%n",
                number++,
                formatter.getName()
            );

            if (formatter instanceof Describable describable)
            {
                System.out.println(describable.getDescription());
            }

            if (preview != null)
            {
                formatter.print(preview);
            }
        }

        int selected = readInt(
            scanner,
            "Формат (0 — залишити поточний): ",
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

    static void changeItemState(
        Scanner scanner,
        Notebook notebook,
        NoteDisplay display
    )
    {
        Note note = chooseNote(scanner, notebook);

        if (note == null)
        {
            return;
        }

        String message = switch (note)
        {
            case ShoppingListNote shopping ->
                "Залишилося купити: " + shopping.getRemainingCount();

            case ChecklistNote checklist ->
                "Залишилося виконати: " + checklist.getRemainingCount();

            case TextNote textNote ->
                "Текстова нотатка не має пунктів для позначення.";
        };

        System.out.println(message);

        if (!(note instanceof ChecklistNote checklist))
        {
            return;
        }

        System.out.println(checklist.formatItems());

        int item = readInt(
            scanner,
            "Номер пункту (0 — повернутися): ",
            0,
            checklist.getItemCount()
        );

        if (item == 0)
        {
            return;
        }

        int state = readInt(
            scanner,
            "Стан (0 — не виконано, 1 — виконано): ",
            0,
            1
        );

        checklist.setCompleted(item - 1, state == 1);
        notebook.updateNote(note);

        System.out.println("Стан пункту змінено.");
        display.show(note);
    }

    static Category readCategory(Scanner scanner, long id)
    {
        System.out.print("Назва категорії: ");
        String name = scanner.nextLine();

        System.out.print("Опис категорії: ");
        String description = scanner.nextLine();

        return new Category(id, name, description);
    }

    static void manageCategories(
        Scanner scanner,
        CRUDService<Category> categories,
        IdGenerator ids
    )
    {
        while (true)
        {
            System.out.println("\n1. Додати категорію");
            System.out.println("2. Показати категорії за назвою");
            System.out.println("3. Редагувати категорію");
            System.out.println("4. Видалити категорію");
            System.out.println("5. Знайти категорію за ID");
            System.out.println("0. Повернутися");

            String choice = scanner.nextLine();

            if (choice.equals("0"))
            {
                return;
            }

            try
            {
                switch (choice)
                {
                    case "1" ->
                    {
                        Category category = readCategory(
                            scanner,
                            ids.next()
                        );

                        categories.save(category);

                        System.out.println(
                            "Додано категорію з ID " + category.getId()
                        );
                    }

                    case "2" ->
                    {
                        List<Category> items =
                            categories.findAllSortedByName();

                        if (items.isEmpty())
                        {
                            System.out.println("Категорій поки немає.");
                        }

                        EntityPrinter.printAll(items);
                    }

                    case "3" ->
                    {
                        long id = readId(scanner);

                        if (id != 0)
                        {
                            categories.findById(id);
                            categories.save(readCategory(scanner, id));

                            System.out.println(
                                "Категорію оновлено. Кількість: "
                                    + categories.findAll().size()
                            );
                        }
                    }

                    case "4" ->
                    {
                        long id = readId(scanner);

                        if (id != 0)
                        {
                            System.out.println(
                                categories.delete(id)
                                    ? "Категорію видалено."
                                    : "Категорії з таким ID немає."
                            );
                        }
                    }

                    case "5" ->
                    {
                        long id = readId(scanner);

                        if (id != 0)
                        {
                            System.out.println(categories.findById(id));
                        }
                    }

                    default ->
                        System.out.println("Такого варіанту немає.");
                }
            }
            catch (EntityNotFoundException error)
            {
                System.out.println(error.getMessage());
            }
            catch (IllegalArgumentException error)
            {
                System.out.println(error.getMessage());
            }
        }
    }

    static Note readValidNote(
        Scanner scanner,
        long id,
        boolean search,
        CRUDService<Category> categories
    )
    {
        while (true)
        {
            try
            {
                return readNote(scanner, id, search, categories);
            }
            catch (NoteException error)
            {
                System.out.println(error.getMessage());

                System.out.println(
                    "Некоректне значення: ["
                        + error.getInvalidValue() + "]"
                );

                System.out.println("Введіть нотатку повторно.");
            }
            catch (IllegalArgumentException error)
            {
                System.out.println(error.getMessage());
                System.out.println("Введіть нотатку повторно.");
            }
        }
    }

    static Note readNote(
        Scanner scanner,
        long id,
        boolean search,
        CRUDService<Category> categories
    ) throws NoteException
    {
        System.out.println("1. Текстова нотатка");
        System.out.println("2. Список справ");
        System.out.println("3. Список покупок");

        int type = readInt(scanner, "Тип нотатки: ", 1, 3);

        System.out.print("Введіть назву нотатки: ");
        String title = scanner.nextLine();

        System.out.print("Введіть текст нотатки: ");
        String text = scanner.nextLine();

        List<Category> available = categories.findAllSortedByName();

        if (!available.isEmpty())
        {
            System.out.println(
                "Доступні категорії (введіть назву або власний текст):"
            );

            EntityPrinter.printAll(available);
        }

        System.out.print("Введіть категорію: ");
        String category = scanner.nextLine();

        int priority = readInt(
            scanner,
            "Пріоритет (1 — найвищий, 10 — найнижчий): ",
            Integer.MIN_VALUE,
            Integer.MAX_VALUE
        );

        double minutes = readMinutes(scanner);

        try
        {
            if (type == 1)
            {
                System.out.print("Джерело (Enter — не вказано): ");
                String source = scanner.nextLine();

                if (source.isBlank())
                {
                    return new TextNote(
                        id, title, text, category, priority, minutes
                    );
                }

                return new TextNote(
                    id, title, text, category, priority, minutes, source
                );
            }

            int amount = readInt(
                scanner,
                "Кількість пунктів (1–100): ",
                1,
                100
            );

            String[] items = new String[amount];
            boolean[] completed = new boolean[amount];

            for (int i = 0; i < amount; i++)
            {
                while (true)
                {
                    System.out.print("Пункт №" + (i + 1) + ": ");
                    items[i] = scanner.nextLine();

                    if (!items[i].isBlank())
                    {
                        break;
                    }

                    System.out.println("Пункт не може бути порожнім.");
                }

                if (search)
                {
                    completed[i] = readInt(
                        scanner,
                        "Стан (0 — не виконано, 1 — виконано): ",
                        0,
                        1
                    ) == 1;
                }
            }

            if (type == 2)
            {
                if (search)
                {
                    return new ChecklistNote(
                        id,
                        title,
                        text,
                        category,
                        priority,
                        minutes,
                        items,
                        completed
                    );
                }

                return new ChecklistNote(
                    id, title, text, category, priority, minutes, items
                );
            }

            System.out.print("Магазин (Enter — не вказано): ");
            String store = scanner.nextLine();

            if (search)
            {
                return new ShoppingListNote(
                    id,
                    title,
                    text,
                    category,
                    priority,
                    minutes,
                    items,
                    completed,
                    store
                );
            }

            return new ShoppingListNote(
                id,
                title,
                text,
                category,
                priority,
                minutes,
                items,
                store
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
                    "Помилка: потрібно ввести ціле число "
                        + "в межах типу int."
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
}