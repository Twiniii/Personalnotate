import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        try
        {
            Note[] notes = new Note[100];
            int noteCount = 0;

            System.out.println("Personalnotate");

            while (true)
            {
                System.out.println();
                System.out.println("1. Додати нотатки");
                System.out.println("2. Переглянути список та відкрити нотатку");
                System.out.println("3. Показати та порахувати за пріоритетом");
                System.out.println("4. Відсортувати за пріоритетом");
                System.out.println("5. Знайти нотатку за повним збігом");
                System.out.println("0. Вийти");
                System.out.print("Оберіть дію: ");

                String choice = scanner.nextLine();

                if (choice.equals("0"))
                {
                    break;
                }
                else if (choice.equals("1"))
                {
                    if (noteCount == notes.length)
                    {
                        System.out.println("Масив нотаток заповнений.");
                        continue;
                    }

                    int freePlaces = notes.length - noteCount;

                    int amount = readInt(
                        scanner,
                        "Скільки нотаток додати (1–" + freePlaces + "): ",
                        1,
                        freePlaces
                    );

                    for (int i = 0; i < amount; i++)
                    {
                        System.out.println();
                        System.out.println("Нотатка №" + (noteCount + 1));

                        notes[noteCount] = readValidNote(scanner);
                        noteCount++;
                    }

                    System.out.println("Додано нотаток: " + amount);
                }
                else if (choice.equals("2"))
                {
                    openNote(scanner, notes, noteCount);
                }
                else if (choice.equals("3"))
                {
                    if (noteCount == 0)
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

                    for (int i = 0; i < noteCount; i++)
                    {
                        if (notes[i].priority == searchPriority)
                        {
                            System.out.println();
                            System.out.println(notes[i]);
                            foundCount++;
                        }
                    }

                    System.out.println("Кількість знайдених: " + foundCount);
                }
                else if (choice.equals("4"))
                {
                    if (noteCount == 0)
                    {
                        System.out.println("Поки немає створених нотаток.");
                        continue;
                    }

                    System.out.println("До сортування:");
                    printNotes(notes);

                    sortByPriority(notes, noteCount);

                    System.out.println();
                    System.out.println("Після сортування:");
                    printNotes(notes);
                }
                else if (choice.equals("5"))
                {
                    if (noteCount == 0)
                    {
                        System.out.println("Поки немає створених нотаток.");
                        continue;
                    }

                    System.out.println(
                        "Введіть усі поля нотатки, яку потрібно знайти."
                    );

                    Note sample = readValidNote(scanner);
                    int index = findNote(notes, noteCount, sample);

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

                        System.out.println(notes[index]);
                    }
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

    static void openNote(
        Scanner scanner,
        Note[] notes,
        int noteCount
    )
    {
        if (noteCount == 0)
        {
            System.out.println("Поки немає створених нотаток.");
            return;
        }

        int number = 1;

        for (Note note : notes)
        {
            if (note == null)
            {
                continue;
            }

            System.out.printf("%d. %s%n", number, note.title);
            number++;
        }

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
                    return;
                }

                if (selected < 1 || selected > noteCount)
                {
                    throw new IndexOutOfBoundsException(
                        "Номер нотатки повинен бути від 1 до " + noteCount + "."
                    );
                }

                System.out.println();
                System.out.println(notes[selected - 1]);
                return;
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

    static void printNotes(Note[] notes)
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
            System.out.println(note);

            number++;
        }
    }

    static void sortByPriority(Note[] notes, int noteCount)
    {
        for (int i = 0; i < noteCount - 1; i++)
        {
            for (int j = 0; j < noteCount - 1 - i; j++)
            {
                if (notes[j].priority > notes[j + 1].priority)
                {
                    Note temporary = notes[j];
                    notes[j] = notes[j + 1];
                    notes[j + 1] = temporary;
                }
            }
        }
    }

    static int findNote(Note[] notes, int noteCount, Note sample)
    {
        for (int i = 0; i < noteCount; i++)
        {
            if (notes[i].equals(sample))
            {
                return i;
            }
        }

        return -1;
    }
}