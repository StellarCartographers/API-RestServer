/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.database;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import space.tscg.api.database.DbEntity;
import space.tscg.database.defined.TSCGDatabase;

@Getter
public class Metrics implements DbEntity
{
    private int requests;

    @JsonCreator
    public Metrics()
    {
        this.requests = 0;
    }
    
    @JsonCreator
    @JsonPropertyOrder("requests")
    public Metrics(@JsonProperty("requests") int requests)
    {
        this.requests = requests;
    }
    
    public static Optional<Metrics> get() {
        return Optional.ofNullable(TSCGDatabase.instance().get(DefinedTable.METRICS, "metrics", Metrics.class));
    }
    
    public static void createIfNeeded()
    {
        if(Metrics.get().isEmpty())
            TSCGDatabase.instance().create(new Metrics());
    }
    
    @JsonIgnore
    public void increment()
    {
        this.requests++;
    }
    
    @Override
    public String getId()
    {
        return "metrics";
    }

    @Override
    public DefinedTable getTable()
    {
        return DefinedTable.METRICS;
    }
}
