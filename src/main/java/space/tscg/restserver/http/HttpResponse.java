package space.tscg.restserver.http;

import static io.javalin.http.HttpStatus.ACCEPTED;
import static io.javalin.http.HttpStatus.BAD_REQUEST;
import static io.javalin.http.HttpStatus.CONFLICT;
import static io.javalin.http.HttpStatus.CREATED;
import static io.javalin.http.HttpStatus.FORBIDDEN;
import static io.javalin.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static io.javalin.http.HttpStatus.NOT_FOUND;
import static io.javalin.http.HttpStatus.OK;
import static io.javalin.http.HttpStatus.UNAUTHORIZED;
import static panda.std.Blank.BLANK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.javalin.http.HttpStatus;
import panda.std.Blank;

public record HttpResponse<T>(HttpStatus status, String customMessage, T type, Object... extras)
{
    public static <T> HttpResponse<T> ok(T type)
    {
        return new HttpResponse<>(OK, "", type);
    }

    public static HttpResponse<Blank> ok()
    {
        return new HttpResponse<>(OK, "", BLANK);
    }

    public static HttpResponse<Blank> created(String message, Object... extras)
    {
        return new HttpResponse<>(CREATED, message, BLANK, extras);
    }

    public static <T> HttpResponse<Blank> accepted(Object... extras)
    {
        return new HttpResponse<>(ACCEPTED, "", BLANK, extras);
    }

    public static <T> HttpResponse<T> accepted(String message, T type, Object... extras)
    {
        return new HttpResponse<>(ACCEPTED, message, type, extras);
    }

    public static HttpResponse<Blank> unauthorized(String message)
    {
        return new HttpResponse<>(UNAUTHORIZED, message, BLANK);
    }

    public static HttpResponse<Blank> forbidden(String message)
    {
        return new HttpResponse<>(FORBIDDEN, message, BLANK);
    }

    public static <T> HttpResponse<T> badRequest(String message, T type)
    {
        return new HttpResponse<>(BAD_REQUEST, message, type);
    }

    public static <T> HttpResponse<T> notFound(FleetCarrierError message, Object... args)
    {
        return new HttpResponse<>(NOT_FOUND, message.getMessage(args), null);
    }

    public static HttpResponse<Blank> notFound(String message)
    {
        return new HttpResponse<>(NOT_FOUND, message, BLANK);
    }

    public static <T> HttpResponse<T> conflict(FleetCarrierError message, Object... args)
    {
        return new HttpResponse<>(CONFLICT, message.getMessage(args), null);
    }

    public static HttpResponse<Blank> conflict(String message)
    {
        return new HttpResponse<>(CONFLICT, message, BLANK);
    }

    public static <T> HttpResponse<T> internalServerError(String message)
    {
        return new HttpResponse<>(INTERNAL_SERVER_ERROR, message, null);
    }

    public static <T> HttpResponse<T> internalServerError(FleetCarrierError message)
    {
        return new HttpResponse<>(INTERNAL_SERVER_ERROR, message.getMessage(), null);
    }

    private static List<HttpStatus> ERRS = new ArrayList<>();

    static
    {
        ERRS.addAll(Arrays.asList(INTERNAL_SERVER_ERROR, CONFLICT, NOT_FOUND, BAD_REQUEST, FORBIDDEN, UNAUTHORIZED));
    }

    public boolean isError()
    {
        return ERRS.contains(status());
    }
}
