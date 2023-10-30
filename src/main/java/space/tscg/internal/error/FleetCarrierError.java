/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.internal.error;

import space.tscg.collections.Data;
import space.tscg.web.HttpState;
import space.tscg.web.States;

public enum FleetCarrierError
{
    ALREADY_EXISTS("Fleet Carrier with ID '%s' already exists",States.CONFLICT),
    NOT_FOUND("Fleet Carrier with ID '%s' was not found", States.NOT_FOUND),
    EMPTY_RESULT("Nothing contained in Database table", States.NO_CONTENT),
    DATABASE_ERROR("Error occoured with the database", States.INTERNAL_SERVER_ERROR);

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
