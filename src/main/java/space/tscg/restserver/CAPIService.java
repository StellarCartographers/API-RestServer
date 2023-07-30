package space.tscg.restserver;

import panda.std.Blank;
import space.tscg.ServerLogger;
import space.tscg.restserver.http.HttpResponse;

public class CAPIService
{
    public static HttpResponse<Blank> callback(String code, String state)
    {
        if ((code == null) || (state == null))
            return HttpResponse.internalServerError("code or state is null");
        ServerLogger.AuthService.log(code);
        ServerLogger.AuthService.log(state);
        return HttpResponse.ok();
    }
}
