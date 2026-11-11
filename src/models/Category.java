package models;

import java.util.Objects;

public final class Category extends BaseEntity
{
    private final String description;

    public Category(long id, String name, String description)
    {
        super(id, name);
        this.description = Objects.requireNonNull(description);
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString()
    {
        return String.format(
            "ID: %d | Категорія: %s | Опис: %s",
            getId(),
            getName(),
            description
        );
    }
}