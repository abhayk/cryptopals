package objects;

public class MutableInt
{
    int value = 0;

    public void increment()
    {
        ++value;
    }

    public int get()
    {
        return value;
    }

    public int incrementAndGet()
    {
        increment();
        return value;
    }
}
