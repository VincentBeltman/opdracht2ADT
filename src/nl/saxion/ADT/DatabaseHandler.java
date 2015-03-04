package nl.saxion.ADT;

import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by Mike on 4-3-2015.
 */
public class DatabaseHandler {
    private static DatabaseHandler instance;
    private DB db;
    private DBCollection userColl;
    private DBCollection recipeColl;

    private DatabaseHandler()
    {

    }

    public static DatabaseHandler getIntance()
    {
        if(instance == null)
        {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    public void connect()
    {
        try {
            MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
            db = mongoClient.getDB("opdracht2");
            recipeColl = db.getCollection("recipes");
            userColl = db.getCollection("users");
            System.out.println("Connection succes");
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

    }

    public void addUser(BasicDBObject addquery)
    {
        userColl.insert(addquery);
    }

    public void addLikeWithUpdate(BasicDBObject addquery, String username)
    {
        BasicDBObject schQuery = new BasicDBObject();
        schQuery.append("_id", username);
        userColl.update(schQuery, addquery);
    }
}
