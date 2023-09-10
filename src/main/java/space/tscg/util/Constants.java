package space.tscg.util;

import java.net.URI;

import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.id.ClientID;

import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import space.tscg.common.dotenv.Dotenv;

@UtilityClass
public final class Constants
{
    public final static String CLIENT_ID_VARIABLE = Dotenv.get("client_id");
    
    public final static ClientID CLIENT_ID = new ClientID(Constants.CLIENT_ID_VARIABLE);
    
    public final static String CALLBACK_URL = "https://api.tscg.network/oauth/callback";

    public final static String LIVE_SERVER   = "https://companion.orerve.net";
    public final static String LEGACY_SERVER = "https://legacy-companion.orerve.net/";
    public final static String AUTH_SERVER   = "https://auth.frontierstore.net";

    public final static Scope  SCOPE     = new Scope("capi", "auth");
    public final static String AUTH_URL  = "/auth";
    public final static String TOKEN_URL = "/token";

    public final static String PROFILE_URL      = "/profile";
    public final static String JOURNAL_URL      = "/journal";
    public final static String FLEETCARRIER_URL = "/fleetcarrier";
    
    public final static HttpUrl CAPI_PROFILE = HttpUrl.parse(LIVE_SERVER + PROFILE_URL);
    public final static HttpUrl CAPI_FLEETCARRIER = HttpUrl.parse(LIVE_SERVER + FLEETCARRIER_URL);
    public final static HttpUrl CAPI_JOURNAL = HttpUrl.parse(LIVE_SERVER + JOURNAL_URL);
    
    public final static URI AUTH_URI = URI.create(AUTH_SERVER + AUTH_URL);
    public final static URI TOKEN_URI = URI.create(AUTH_SERVER + TOKEN_URL);
}
