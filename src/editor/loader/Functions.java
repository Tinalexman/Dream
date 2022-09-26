package editor.loader;

import dream.managers.WindowManager;
import editor.util.Constants;
import editor.windows.EditorWindow;

import java.util.HashMap;
import java.util.Map;

public class Functions
{
    private static final Map<String, Runnable> functions = new HashMap<>();

    public static void initialize(Map<String, EditorWindow> editorWindows)
    {
        Functions.functions.put("quitEditor", WindowManager::closeMain);

        EditorWindow settings = editorWindows.get(Constants.engineSettings);
        Functions.functions.put("activateSettings", settings::activate);
    }

    public static Runnable get(String functionName)
    {
        return Functions.functions.getOrDefault(functionName, () -> {});
    }
}
