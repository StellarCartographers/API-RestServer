package space.tscg.operation.encryption;

import java.lang.reflect.InvocationTargetException;

import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.nimbusds.oauth2.sdk.token.Token;

public enum KeyType {
    ACCESS(BearerAccessToken.class),
    REFRESH(RefreshToken.class);

    Class<? extends Token> clzToken;
    
    KeyType(Class<? extends Token> clz)
    {
        this.clzToken = clz;
    }
    
    @SuppressWarnings("unchecked")
    <T extends Token> T asToken(String key)
    {
        try
        {
            return (T) clzToken.getDeclaredConstructor(String.class).newInstance(key);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
        {
            return null;
        }
    }
}