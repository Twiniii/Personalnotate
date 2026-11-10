package models;

import exceptions.NoteException;
import java.util.Arrays;
import java.util.Objects;

public sealed class ChecklistNote extends Note permits ShoppingListNote
{
    private final String[] items;
    private final boolean[] completed;

    public ChecklistNote(
        String title,
        String text,
        String category,
        int priority,
        double plannedMinutes,
        String[] items
    ) throws NoteException
    {
        this(
            title,
            text,
            category,
            priority,
            plannedMinutes,
            items,
            new boolean[items == null ? 0 : items.length]
        );

        traceConstructor(
            "ChecklistNote: короткий конструктор після this(...)"
        );
    }

    public ChecklistNote(
        String title,
        String text,
        String category,
        int priority,
        double plannedMinutes,
        String[] items,
        boolean[] completed
    ) throws NoteException
    {
        super(title, text, category, priority, plannedMinutes);

        if (items == null || items.length == 0
            || completed == null || items.length != completed.length)
        {
            throw new IllegalArgumentException(
                "Список має містити пункти та стан для кожного пункту."
            );
        }

        for (String item : items)
        {
            if (item == null || item.isBlank())
            {
                throw new IllegalArgumentException(
                    "Пункт списку не може бути порожнім."
                );
            }
        }

        this.items = items.clone();
        this.completed = completed.clone();

        traceConstructor("ChecklistNote: пункти та їхні стани");
    }

    public String[] getItems()
    {
        return items.clone();
    }

    public int getItemCount()
    {
        return items.length;
    }

    public boolean isCompleted(int index)
    {
        checkIndex(index);
        return completed[index];
    }

    public void setCompleted(int index, boolean value)
    {
        checkIndex(index);
        completed[index] = value;
    }

    private void checkIndex(int index)
    {
        if (index < 0 || index >= items.length)
        {
            throw new IndexOutOfBoundsException(
                "Пункту з таким індексом немає."
            );
        }
    }

    public int getCompletedCount()
    {
        int count = 0;

        for (boolean value : completed)
        {
            if (value)
            {
                count++;
            }
        }

        return count;
    }

    public int getRemainingCount()
    {
        return items.length - getCompletedCount();
    }

    public String formatItems()
    {
        StringBuilder result = new StringBuilder("Пункти:");

        for (int i = 0; i < items.length; i++)
        {
            result.append(System.lineSeparator())
                .append(i + 1)
                .append(completed[i] ? ". [x] " : ". [ ] ")
                .append(items[i]);
        }

        return result.toString();
    }

    @Override
    public String getTypeName()
    {
        return "Список справ";
    }

    @Override
    public String getSummary()
    {
        return "Виконано: " + getCompletedCount()
            + "/" + items.length
            + "; залишилося: " + getRemainingCount();
    }

    @Override
    public String getDisplayText()
    {
        return super.getDisplayText()
            + System.lineSeparator()
            + formatItems();
    }

    @Override
    public String toString()
    {
        return super.toString()
            + System.lineSeparator()
            + formatItems();
    }

    @Override
    public boolean equals(Object object)
    {
        if (!super.equals(object))
        {
            return false;
        }

        ChecklistNote other = (ChecklistNote) object;

        return Arrays.equals(items, other.items)
            && Arrays.equals(completed, other.completed);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(
            super.hashCode(),
            Arrays.hashCode(items),
            Arrays.hashCode(completed)
        );
    }
}