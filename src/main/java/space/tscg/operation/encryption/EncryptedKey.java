package space.tscg.operation.encryption;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;

import space.tscg.TSCGServer;

public final class EncryptedKey
{
    private final String key;
    private final KeyType type;
    
    @JsonCreator
    @ConstructorProperties({"key", "type"})
    private EncryptedKey(@JsonProperty("key") String key, @JsonProperty("type") KeyType type)
    {
        this.key = TSCGServer.TESTING ? key : EncryptDecrypt.encode(key);
        this.type = type;
    }
    
    public static EncryptedKey of(String key, KeyType type)
    {
        return new EncryptedKey(key, type);
    }

    public String getKey()
    {
        return TSCGServer.TESTING ? key : EncryptDecrypt.decode(key);
    }
    
    public KeyType getType()
    {
        return type;
    }
    
    public BearerAccessToken asBearerAccessToken()
    {
        if(getType().equals(KeyType.ACCESS))
            return new BearerAccessToken(key);
        throw new RuntimeException();
    }
    
    public RefreshToken asRefreshToken()
    {
        if(getType().equals(KeyType.REFRESH))
            return new RefreshToken(key);
        throw new RuntimeException();
    }
}
