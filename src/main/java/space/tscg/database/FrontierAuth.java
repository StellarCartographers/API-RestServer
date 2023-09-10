package space.tscg.database;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;

import lombok.Data;
import space.tscg.common.db.modal.DbEntity;
import space.tscg.common.db.prefab.Member;
import space.tscg.common.db.prefab.TSCGDatabase;
import space.tscg.operation.Builders;
import space.tscg.operation.encryption.EncryptedKey;

@Data
@JsonDeserialize(builder = Builders.FrontierAuthentication.class)
public class FrontierAuth implements DbEntity {
    
    public static String TABLE = "auth";
    
    private final UUID id;
    private EncryptedKey<BearerAccessToken> accessToken;
    private EncryptedKey<RefreshToken> refreshToken;
    private long expireEpochSecond;
    
    public FrontierAuth(Builders.FrontierAuthentication builder) {
        this.id = UUID.randomUUID();
        this.accessToken = builder.getAccessToken();
        this.refreshToken = builder.getRefreshToken();
        this.expireEpochSecond = setExpireEpoch(builder.getExpiresIn());
    }
    
    @JsonIgnore
    public boolean updateKeys(BearerAccessToken accessToken, RefreshToken refreshToken, long expiresIn)
    {
        this.accessToken = EncryptedKey.of(accessToken);
        this.refreshToken = EncryptedKey.of(refreshToken);
        this.expireEpochSecond = setExpireEpoch(expiresIn);
        return TSCGDatabase.instance().update(this).operationSucceded();
    }
    
    @JsonIgnore
    private long setExpireEpoch(long seconds)
    {
        return Instant.now().plusSeconds(seconds).getEpochSecond();
    }

    @JsonIgnore
    public boolean isAccessTokenExpired()
    {
        return Instant.now().isAfter(Instant.ofEpochSecond(expireEpochSecond));
    }
    
    @JsonIgnore
    public FrontierAuth getForMember(Member member)
    {
        return TSCGDatabase.instance().get(getTableName(), member.getAuthenticationId(), FrontierAuth.class);
    }
    
    @Override
    public String getId()
    {
        return id.toString();
    }

    @Override
    public String getTableName()
    {
        return TABLE;
    }
}