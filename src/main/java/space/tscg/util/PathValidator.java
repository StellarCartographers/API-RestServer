/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.util;

import io.javalin.http.Context;
import lombok.Getter;
import panda.std.Result;
import space.tscg.collections.Data;
import space.tscg.web.HttpError;

public final class PathValidator
{
    @Getter
    private final String path;
    @Getter
    private final Context ctx;
    
    public static PathValidator create(Context ctx, String path)
    {
        return new PathValidator(ctx, path);
    }
    
    private PathValidator(Context ctx, String path)
    {
        this.ctx = ctx;
        this.path = path;
    }
    
    public Result<Context, HttpError> run()
    {
        var hasPath = (this.ctx.pathParam(this.path) != null) || this.ctx.pathParam(this.path).isBlank();
        return hasPath ? Result.ok(ctx) : error();
    }
    
    public String pathSegement()
    {
        if(this.isErr()) { throw new UnsupportedOperationException("PathValidtor '"  + this.path + "' isErr"); }
        return this.ctx.pathParam(this.path);
    }
    
    public boolean isErr()
    {
        return this.run().isErr();
    }
    
    public <T> Result<T, HttpError> error()
    {
        return HttpError.badRequest(Data.asLinkedHashMap().add("error", "incorrect path"));
    }
}
