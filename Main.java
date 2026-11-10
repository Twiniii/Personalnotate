import java.util.Locale;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        System.out.println("Personalnotate");
        System.out.println();

        System.out.print("Введіть назву нотатки: ");
        String title = scanner.nextLine();

        System.out.print("Введіть текст нотатки: ");
        String text = scanner.nextLine();

        System.out.print("Введіть категорію: ");
        String category = scanner.nextLine();

        System.out.print("Введіть пріоритет (1 — найвищий, 10 — найнижчий): ");

        if (!scanner.hasNextInt())
        {
            System.out.println("Пріоритет потрібно ввести цілим числом.");
            scanner.close();
            return;
        }

        int priority = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Час виконання у хвилинах (0 — не заплановано): ");

        if (!scanner.hasNextDouble())
        {
            System.out.println("Введіть число. Дробову частину відокремлюйте крапкою.");
            scanner.close();
            return;
        }

        double plannedMinutes = scanner.nextDouble();

        if (title.isBlank())
        {
            System.out.println("Назва нотатки не може бути порожньою.");
        }
        else if (priority < 1 || priority > 10)
        {
            System.out.println("Пріоритет повинен бути від 1 до 10.");
        }
        else if (!Double.isFinite(plannedMinutes) || plannedMinutes < 0)
        {
            System.out.println("Час повинен бути скінченним невід’ємним числом.");
        }
        else
        {
            double plannedHours = plannedMinutes / 60.0;

            System.out.println();
            System.out.println("========== НОТАТКА ==========");
            System.out.printf("Назва: %s%n", title);
            System.out.printf("Текст: %s%n", text);
            System.out.printf("Категорія: %s%n", category);
            System.out.printf("Пріоритет: %d%n", priority);

            if (plannedMinutes == 0)
            {
                System.out.println("Час виконання: не заплановано.");
            }
            else
            {
                System.out.printf(
                    Locale.US,
                    "Час виконання: %.2f хв (%.2f год)%n",
                    plannedMinutes,
                    plannedHours
                );
            }

            System.out.println("=============================");
        }

        scanner.close();
    }
}