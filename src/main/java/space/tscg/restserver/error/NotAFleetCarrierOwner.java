package space.tscg.restserver.error;

import panda.std.Result;
import space.tscg.common.http.Data;
import space.tscg.common.http.HttpError;

public class NotAFleetCarrierOwner
{
    public <T> Result<T, HttpError> error()
    {
        return HttpError.notFound(Data.map()
            .with("exception", "NotAFleetCarrierOwner")
            .with("error", "Not a FleetCarrier Owner"));
    }
}