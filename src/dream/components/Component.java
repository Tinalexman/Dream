package dream.components;

public interface Component
{
    void onStart();
    void onChanged();
    void onStop();

    boolean hasChanged();
    void change(boolean change);
}
