package space.tscg;

import java.util.Set;

import space.tscg.common.dotenv.Dotenv;
import space.tscg.common.dotenv.DotenvEntry;

public final class Env
{
    private final Dotenv dotenv;

    private static final Env _instance = new Env();

    private Env()
    {
        this.dotenv = Dotenv.configure().defaultConfiguration();
    }

    public static String get(String key)
    {
        return _instance.dotenv.get(key);
    }

    public static int getAsInt(String key)
    {
        return _instance.dotenv.getAsInt(key);
    }

    public static Set<DotenvEntry> getMap()
    {
        return _instance.dotenv.entries();
    }
}
