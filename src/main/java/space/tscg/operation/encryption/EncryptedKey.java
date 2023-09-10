package space.tscg.operation.encryption;

import java.beans.ConstructorProperties;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.nimbusds.oauth2.sdk.token.Token;

import lombok.Getter;

@Getter
public final class EncryptedKey<T extends Token>
{
    private final String encryptedKey;
    private final KeyType keyType;
    
    @JsonCreator
    @ConstructorProperties({"key", "type"})
    private EncryptedKey(@JsonProperty("key") String key, @JsonProperty("type") KeyType type)
    {
        this.encryptedKey = EncryptDecrypt.encode(key);
        this.keyType = type;
    }
    
    public static <T extends Token> EncryptedKey<T> of(T token)
    {
        if(token instanceof BearerAccessToken)
        {
            return new EncryptedKey<>(token.getValue(), KeyType.ACCESS);
        } else if (token instanceof RefreshToken) {
            return new EncryptedKey<>(token.getValue(), KeyType.REFRESH);
        } else {
            throw new IllegalArgumentException("(T token) must be instanceof 'AccessToken' or 'RefreshToken'");
        }
    }
    
    @JsonIgnore
    public T decodeAsToken()
    {
        return keyType.asToken(EncryptDecrypt.decode(encryptedKey));
    }
    
    @JsonIgnore
    public String getDecodedString()
    {
        return EncryptDecrypt.decode(encryptedKey);
    }
}
