package space.tscg.restserver.http;

import space.tscg.common.http.Data;
import space.tscg.common.http.HttpState;

public enum FleetCarrierError
{
    ALREADY_EXISTS("Fleet Carrier with ID '%s' already exists", HttpState.CONFLICT),
    NOT_FOUND("Fleet Carrier with ID '%s' was not found", HttpState.NOT_FOUND),
    EMPTY_RESULT("Nothing contained in Database table", HttpState.NO_CONTENT),
    DATABASE_ERROR("Error occoured with the database", HttpState.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpState state;

    FleetCarrierError(String message, HttpState state)
    {
        this.message = message;
        this.state = state;
    }

    public HttpState getState(Object... args)
    {
        state.withData(Data.asHashMap().add("error", message.formatted(args)));
        return state;
    }
    
    public HttpState getState()
    {
        state.withData(Data.asHashMap().add("error", message));
        return state;
    }
}
