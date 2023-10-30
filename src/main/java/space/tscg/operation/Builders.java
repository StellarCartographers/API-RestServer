/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.operation;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import space.tscg.database.FrontierAuth;
import space.tscg.operation.encryption.EncryptedKey;
import space.tscg.operation.encryption.KeyType;

public class Builders {

    public static FrontierAuthentication FrontierAuthBuilder()
    {
        return new FrontierAuthentication();
    }

    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    @JsonPOJOBuilder(withPrefix = "")
    public static class FrontierAuthentication {
        private String accessToken;
        private String refreshToken;
        private long expiresAt;

        public FrontierAuthentication accessToken(final String accessToken) {
            this.accessToken = EncryptedKey.of(accessToken, KeyType.ACCESS).getKey();
            return this;
        }
        
        public FrontierAuthentication refreshToken(final String refreshToken) {
            this.refreshToken = EncryptedKey.of(refreshToken, KeyType.REFRESH).getKey();
            return this;
        }

        public FrontierAuthentication expiresAt(final long expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public FrontierAuth build() {
            return new FrontierAuth(this);
        }
    }
}
