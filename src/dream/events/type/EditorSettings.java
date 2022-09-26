package dream.events.type;

import dream.events.Event;
import dream.events.EventType;

public class EditorSettings extends Event
{
    private Runnable task;

    public EditorSettings(Runnable task)
    {
        super(EventType.EditorSettings);
        this.task = task;
    }
}
