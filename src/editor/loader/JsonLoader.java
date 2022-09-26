package editor.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.HashMap;
import java.util.Map;

public class JsonLoader
{
    private static final Map<String, Runnable> tasks = new HashMap<>();
    private static final Gson loader = new GsonBuilder().create();

    public static void initialize()
    {

    }

    private static void createMenuBar()
    {
        String contents = "...";

    }

}
