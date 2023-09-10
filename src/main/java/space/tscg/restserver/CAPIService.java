package space.tscg.restserver;

import elite.dangerous.EliteAPI;
import elite.dangerous.capi.FleetCarrierData;
import panda.std.Result;
import space.tscg.common.db.prefab.Member;
import space.tscg.common.db.prefab.TSCGDatabase;
import space.tscg.common.http.Http;
import space.tscg.common.http.HttpError;
import space.tscg.common.http.HttpState;
import space.tscg.database.FrontierAuth;
import space.tscg.restserver.error.MemberNotFound;
import space.tscg.restserver.error.NotAFleetCarrierOwner;
import space.tscg.restserver.http.QueryValidator;
import space.tscg.util.Constants;

public class CAPIService
{
    public static Result<FleetCarrierData, HttpError> callFleetCarrierEndpoint(QueryValidator validator)
    {
        if(validator.isErr())
            return validator.error();
            
        String discordId = validator.getCtx().queryParam("discordid");
        var m = getMember(discordId);
        if(m.isErr())
            return Result.error(m.getError());
        
        var auth = TSCGDatabase.instance().get(m.get().getAuthenticationId(), FrontierAuth.TABLE, FrontierAuth.class);
        String decodedToken = auth.getAccessToken().decodeAsToken().toAuthorizationHeader();
        var call = Http.GET.header("Authorization", decodedToken).call(Constants.CAPI_FLEETCARRIER);
        if(call.getCode() == 204)
        {
            return new NotAFleetCarrierOwner().error();
        } else if(call.getCode() != 200)
        {
            return Result.error(new HttpError(HttpState.fromCode(call.getCode()), null));
        }
        return Result.ok(EliteAPI.fromJson(call.getBody(), FleetCarrierData.class));
    }
    
    private static Result<Member, HttpError> getMember(String discordId)
    {
        var member = Member.get(discordId);
        return member.isPresent() ? Result.ok(member.get()) : new MemberNotFound().error(discordId);
    }
}
