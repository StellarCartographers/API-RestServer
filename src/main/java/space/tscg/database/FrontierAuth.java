/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.database;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import space.tscg.api.database.DbEntity;
import space.tscg.database.defined.TSCGDatabase;
import space.tscg.database.entity.TSCGMember;
import space.tscg.operation.Builders;
import space.tscg.operation.encryption.EncryptedKey;
import space.tscg.operation.encryption.KeyType;

@Data
@JsonDeserialize(builder = Builders.FrontierAuthentication.class)
public class FrontierAuth implements DbEntity {
    
    private final UUID id;
    private String accessToken;
    private String refreshToken;
    private long expiresAt;
    
    public FrontierAuth(Builders.FrontierAuthentication builder) {
        this.id = UUID.randomUUID();
        this.accessToken = builder.getAccessToken();
        this.refreshToken = builder.getRefreshToken();
        this.expiresAt = builder.getExpiresAt();
    }
    
    @JsonIgnore
    public boolean updateKeys(String accessToken, String refreshToken, long expiresAt)
    {
        this.accessToken = EncryptedKey.of(accessToken, KeyType.ACCESS).getKey();
        this.refreshToken = EncryptedKey.of(refreshToken, KeyType.REFRESH).getKey();
        this.expiresAt = expiresAt;
        return TSCGDatabase.instance().update(this).operationSucceded();
    }

    @JsonIgnore
    public static FrontierAuth getForMember(TSCGMember member)
    {
        return TSCGDatabase.instance().get(DefinedTable.AUTH.toString(), member.getAuthenticationId(), FrontierAuth.class);
    }
    
    public String getRefreshToken()
    {
        return EncryptedKey.of(refreshToken, KeyType.REFRESH).getKey();
    }
    
    public String getAccessToken()
    {
        return EncryptedKey.of(accessToken, KeyType.ACCESS).getKey();
    }
    
    @Override
    public String getId()
    {
        return id.toString();
    }

    @Override
    public DefinedTable getTable()
    {
        return DefinedTable.AUTH;
    }
}