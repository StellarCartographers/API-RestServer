package space.tscg.util;

import lombok.experimental.UtilityClass;
import okhttp3.MediaType;

@UtilityClass
public class HttpUtil
{
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
}
