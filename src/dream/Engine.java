package dream;


import dream.events.EventManager;
import dream.io.Input;
import dream.managers.ResourcePool;
import dream.managers.WindowManager;
import editor.Editor;

public class Engine
{
    public static final String resourcePath;

    public static final double nanoSeconds = 0.000000001;
    public static final long inverseNanoseconds = 1000000000L;
    public static float frameRate = 1000F;
    public static final float frameTime = 1 / frameRate;
    public static int framesPerSecond;
    public static int frames = 0;

    public static volatile boolean isRunning = true;

    private static Editor editor;

    static
    {
        resourcePath = System.getProperty("user.dir") + "\\res";
    }

    public static void start()
    {
        preLoop();
        loop();
        postLoop();
    }

    private static void preLoop()
    {
        Input.initialize();

        long mainWindowID = WindowManager.initialize();

        ResourcePool.loadIcons();

        Engine.editor = new Editor(mainWindowID);
    }

    private static void loop()
    {
        isRunning = true;

        boolean render = false;
        long lastTime = System.nanoTime();
        long frameCounter = 0;
        double unprocessedTime = 0.0;

        while(Engine.isRunning)
        {
            long startTime = System.nanoTime();
            long delta = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += (delta * nanoSeconds);
            frameCounter += delta;

            handleInput();

            while(unprocessedTime > frameTime)
            {
                render = true;
                unprocessedTime -= frameTime;

                if(frameCounter >= inverseNanoseconds)
                {
                    Engine.framesPerSecond = frames;
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render)
            {
                updateState((float) (delta * nanoSeconds));
                frames++;
            }

            if(!WindowManager.isMainRunning())
                Engine.isRunning = false;
        }
    }

    private static void postLoop()
    {
        Engine.editor.destroy();
        WindowManager.destroy();
    }

    private static void handleInput()
    {
        Engine.editor.input();
    }

    private static void updateState(float delta)
    {
        WindowManager.startMain();
        Engine.editor.refresh();
        Input.update();
        WindowManager.endMain();
        EventManager.alert();
    }


}
