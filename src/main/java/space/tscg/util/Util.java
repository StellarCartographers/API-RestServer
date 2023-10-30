package space.tscg.util;

import java.time.Instant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Util
{
    public static long getExpiresAtEpoch(long expiresIn)
    {
        return Instant.now().plusSeconds(expiresIn).getEpochSecond();
    }
    
    public static boolean isAfter(long expiresAt)
    {
        return Instant.now().isAfter(Instant.ofEpochSecond(expiresAt));
    }
}
