/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.javalin.http.Context;
import lombok.Getter;
import panda.std.Result;
import space.tscg.collections.Data;
import space.tscg.web.HttpError;

public final class QueryValidator
{
    private final List<String> missing = new ArrayList<>();
    private final List<String> required = new ArrayList<>();
    @Getter
    private final Context ctx;
    
    public static QueryValidator create(Context ctx, String... queries)
    {
        return new QueryValidator(ctx, queries);
    }
    
    private QueryValidator(Context ctx, String... queries)
    {
        this.ctx = ctx;
        required.addAll(Arrays.asList(queries));
    }
    
    public Result<Context, HttpError> Validate()
    {
        required.forEach(r -> {
            if(!ctx.queryParamMap().containsKey(r)) {
                missing.add(r);
            }
        });
        return missing.isEmpty() ? Result.ok(ctx) : error();
    }
    
    public boolean isErr()
    {
        return this.Validate().isErr();
    }
    
    public <T> Result<T, HttpError> error()
    {
        return HttpError.badRequest(Data.asLinkedHashMap().add("missingQueries", String.join(", ", missing)));
    }
}
