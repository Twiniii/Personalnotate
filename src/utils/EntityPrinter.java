package utils;

import java.util.Collection;
import models.BaseEntity;

public final class EntityPrinter
{
    private EntityPrinter()
    {
    }

    public static void printAll(
        Collection<? extends BaseEntity> items
    )
    {
        for (BaseEntity item : items)
        {
            System.out.println(item);
        }
    }
}