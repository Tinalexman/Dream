package dream.events;

public enum EventType
{
    // WINDOW FRAMES
    StartWindowFrame,
    EndWindowFrame,
    WindowResize,

    // PHYSICS STUFF
    StartPhysicsSimulation,
    EndPhysicsSimulation,

    // VIEWPORT STUFF
    StartViewportRuntime,
    EndViewportRuntime,

    // EDITOR STUFF
    EditorOptions,

    EditorSettings
}
