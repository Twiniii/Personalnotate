package models;

import exceptions.NoteException;
import java.util.Objects;

public final class ShoppingListNote extends ChecklistNote
{
    private final String store;

    public ShoppingListNote(
        long id,
        String title,
        String text,
        String category,
        int priority,
        double plannedMinutes,
        String[] items,
        String store
    ) throws NoteException
    {
        this(
            id,
            title,
            text,
            category,
            priority,
            plannedMinutes,
            items,
            new boolean[items == null ? 0 : items.length],
            store
        );

        traceConstructor(
            "ShoppingListNote: короткий конструктор після this(...)"
        );
    }

    public ShoppingListNote(
        long id,
        String title,
        String text,
        String category,
        int priority,
        double plannedMinutes,
        String[] items,
        boolean[] completed,
        String store
    ) throws NoteException
    {
        super(
            id,
            title,
            text,
            category,
            priority,
            plannedMinutes,
            items,
            completed
        );

        this.store = store == null || store.isBlank()
            ? "не вказано"
            : store;

        traceConstructor("ShoppingListNote: магазин");
    }

    public String getStore()
    {
        return store;
    }

    @Override
    public String getTypeName()
    {
        return "Список покупок";
    }

    @Override
    public String getSummary()
    {
        return "Залишилося купити: " + getRemainingCount()
            + "; магазин: " + store;
    }

    @Override
    public String getDisplayText()
    {
        return super.getDisplayText()
            + System.lineSeparator()
            + "Магазин: " + store;
    }

    @Override
    public String toString()
    {
        return super.toString()
            + System.lineSeparator()
            + "Магазин: " + store;
    }

    @Override
    public boolean hasSameContent(Note other)
    {
        if (!super.hasSameContent(other))
        {
            return false;
        }

        ShoppingListNote sameType = (ShoppingListNote) other;

        return Objects.equals(store, sameType.store);
    }
}