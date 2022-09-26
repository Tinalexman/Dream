package dream.util.contain;

public interface Containable<T>
{
    String getName();
    T getValue();
    boolean isContainer();
}
