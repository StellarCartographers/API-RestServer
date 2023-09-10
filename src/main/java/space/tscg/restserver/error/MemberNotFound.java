package space.tscg.restserver.error;

import panda.std.Result;
import space.tscg.common.http.Data;
import space.tscg.common.http.HttpError;

public class MemberNotFound
{
    public <T> Result<T, HttpError> error(String memberId)
    {
        return HttpError.notFound(Data.map()
            .with("exception", "NotAFleetCarrierOwner")
            .with("error", "No Member by DiscordID: " + memberId));
    }
}