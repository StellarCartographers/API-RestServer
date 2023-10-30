/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.internal.local;

import static com.rethinkdb.RethinkDB.*;

import java.util.ArrayList;

import org.tinylog.Logger;
import org.tinylog.TaggedLogger;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlAuthError;
import com.rethinkdb.gen.exc.ReqlOpFailedError;
import com.rethinkdb.gen.exc.ReqlRuntimeError;
import com.rethinkdb.net.Connection;

import space.tscg.database.DefinedTable;
import space.tscg.properties.dot.Dotenv;

public final class LocalTesting
{
    private static TaggedLogger logger = Logger.tag("LocalTesting");
    
    @SuppressWarnings("unchecked")
    public static void setupDefinedTableIfNeeded()
    {
        Connection conn = null;
        try
        {
            conn = new Connection.Builder().hostname(Dotenv.get("db_host")).user("admin").connect();
        } catch (ReqlAuthError e1)
        {
            try
            {
                conn = new Connection.Builder().hostname(Dotenv.get("db_host")).user(Dotenv.get("db_user")).connect();
            } catch (ReqlAuthError e2)
            {
                try
                {
                    conn = new Connection.Builder().hostname(Dotenv.get("db_host")).user(Dotenv.get("db_user"), Dotenv.get("db_password")).connect();
                } catch (ReqlAuthError e3)
                {
                    throw new RuntimeException("Could not connect to localhost database, tables must be created manually if they do not exist");
                }
            }
        }
        
        var dbList = (ArrayList<String>) RethinkDB.r.dbList().run(conn).first();
        
        if(!dbList.contains(Dotenv.get("database")))
        {
            var dbOp = r.dbCreate(Dotenv.get("database")).run(conn).single();
            if(dbOp instanceof ReqlRuntimeError)
                logger.info("Database '%s' already exists.... Uh this should not have happened captain".formatted(Dotenv.get("database")));
            else
                logger.info("Database '%s' created".formatted(Dotenv.get("database")));
        } else {
            logger.info("CREATE DATABASE SKIPPED | Database '%s' exists ".formatted(Dotenv.get("database")));
        }
        
        var tableList = (ArrayList<String>) RethinkDB.r.db(Dotenv.get("database")).tableList().run(conn).first();
        
        for (DefinedTable table : DefinedTable.values())
        {
            if(!tableList.contains(table.toString()))
            {
                var tableOp = r.db(Dotenv.get("database")).tableCreate(table.toString()).run(conn).single();
                if(tableOp instanceof ReqlOpFailedError)
                    logger.info("Table '%s' already exists.... Uh this should not have happened captain".formatted(table.toString()));
                else
                    logger.info("Table '%s' created".formatted(table.toString()));
            } else {
                logger.info("CREATE TABLE SKIPPED | Table '%s' exists ".formatted(table.toString()));
            }
        }
        
        conn.close();
    }
}
