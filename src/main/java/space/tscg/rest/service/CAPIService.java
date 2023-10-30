/**
 * Copyright (c) 2023 The Stellar Cartographers' Guild.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.rest.service;

import org.tinylog.Logger;

import com.nimbusds.oauth2.sdk.RefreshTokenGrant;
import com.nimbusds.oauth2.sdk.token.RefreshToken;

import elite.dangerous.Elite4J.CAPI;
import elite.dangerous.capi.Profile;
import panda.std.Blank;
import panda.std.Result;
import space.tscg.collections.Data;
import space.tscg.database.DefinedTable;
import space.tscg.database.FrontierAuth;
import space.tscg.database.defined.TSCGDatabase;
import space.tscg.database.entity.TSCGMember;
import space.tscg.internal.CAPIRequest;
import space.tscg.internal.local.LocalTestingData;
import space.tscg.misc.error.MemberNotFound;
import space.tscg.misc.error.NotAFleetCarrierOwner;
import space.tscg.util.Constants;
import space.tscg.util.PathValidator;
import space.tscg.util.Util;
import space.tscg.web.HttpError;
import space.tscg.web.States;
import space.tscg.web.Web;

public class CAPIService
{
    public static Result<String, HttpError> callFleetCarrierTest(PathValidator validator)
    {
        Logger.info(validator.pathSegement());
        if (validator.isErr())
            return validator.error();
        var m = getMember(validator.pathSegement());
        if (m.isErr())
            return Result.error(m.getError());

        return Result.ok(LocalTestingData.fleetCarrierDataAsString());
    }

    public static Result<String, HttpError> callFleetCarrierEndpoint(PathValidator validator)
    {
        if (validator.isErr())
            return validator.error();
        String discordId = validator.getCtx().pathParam(validator.getPath());
        var m = getMember(discordId);
        if (m.isErr())
            return Result.error(m.getError());
        var auth = FrontierAuth.getForMember(m.get());
        if (Util.isAfter(auth.getExpiresAt())) {
            var refresh = handleTokenRefresh(auth);
            if (refresh.isErr())
                return HttpError.internalServerError(Data.of("Error encountered attempting to refresh access token"));

            var newAuth = FrontierAuth.getForMember(m.get());
            if (!auth.equals(newAuth)) {
                auth = newAuth;
            }
        }

        String bearer = "Bearer %s".formatted(auth.getAccessToken());
        var call = Web.GET.header("Authorization", bearer).call(Constants.CAPI_FLEETCARRIER);
        if (call.getCode() == 204) {
            return new NotAFleetCarrierOwner().error();
        } else if (call.getCode() != 200) {
            return Result.error(new HttpError(States.fromCode(call.getCode())));
        }

        return Result.ok(call.getBody());
    }

    public static Result<Profile, HttpError> callProfileEndpoint(PathValidator validator)
    {
        if (validator.isErr())
            return validator.error();
        String discordId = validator.getCtx().pathParam(validator.getPath());
        var m = getMember(discordId);
        if (m.isErr())
            return Result.error(m.getError());
        var auth = TSCGDatabase.instance().get(DefinedTable.AUTH, m.get().getAuthenticationId(), FrontierAuth.class);

        // Check if our access token is already expired
        if (Util.isAfter(auth.getExpiresAt())) {
            var refresh = handleTokenRefresh(auth);
            if (refresh.isErr())
                return HttpError.internalServerError(Data.of("Error encountered attempting to refresh access token"));

            var newAuth = TSCGDatabase.instance().get(DefinedTable.AUTH, m.get().getAuthenticationId(), FrontierAuth.class);
            if (!auth.equals(newAuth)) {
                auth = newAuth;
            }
        }

        String decodedToken = auth.getAccessToken();
        var call = Web.GET.header("Authorization", "Bearer " + decodedToken).call(Constants.CAPI_PROFILE);
        if (call.getCode() != 200) {
            return Result.error(new HttpError(States.fromCode(call.getCode())));
        }

        return Result.ok(CAPI.parse(call.getBody(), Profile.class).get());
    }

    private static Result<TSCGMember, HttpError> getMember(String discordId)
    {
        var member = TSCGMember.get(discordId);
        return member.<Result<TSCGMember, HttpError>> map(Result::ok).orElseGet(() -> new MemberNotFound().error(discordId));
    }

    private static Result<Blank, HttpError> handleTokenRefresh(FrontierAuth auth)
    {
        var refresh = new RefreshTokenGrant(new RefreshToken(auth.getRefreshToken()));
        var request = new CAPIRequest.Refresh(refresh);

        var response = request.processRequest();

        if (response.isErr())
            return Result.error(response.getError());

        var tokens = response.get();
        var toUpdate = TSCGDatabase.instance().get(DefinedTable.AUTH, auth.getId(), FrontierAuth.class);

        toUpdate.updateKeys(tokens.getAccessToken().toString(), tokens.getRefreshToken().toString(), tokens.getAccessToken().getLifetime());
        TSCGDatabase.instance().update(toUpdate);

        return Result.ok();
    }
}
