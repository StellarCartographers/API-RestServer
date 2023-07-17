package space.tscg.restserver;

public enum FleetCarrierError
{
    NONE(""),
    ALREADY_EXISTS("Fleet Carrier with ID '%s' already exists"),
    NOT_FOUND("Fleet Carrier with ID '%s' was not found"),
    EMPTY_RESULT("Nothing contained in Database table"),
    DATABASE_ERROR("Error occoured with the database");

    private final String message;

    FleetCarrierError(){
        this.message = null;
    };

    FleetCarrierError(String message)
    {
        this.message = message;
    }

    public String getMessage(Object... arg)
    {
        if(message.contains("%s"))
            return String.format(message, arg);
        else
            return message;
    }
}
