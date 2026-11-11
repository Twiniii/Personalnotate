package utils;

public class IdGenerator
{
    private long nextId = 1;

    public long next()
    {
        if (nextId == Long.MAX_VALUE)
        {
            throw new IllegalStateException(
                "Діапазон ID вичерпано."
            );
        }

        return nextId++;
    }
}