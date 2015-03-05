package nl.saxion.ADT;

import com.mongodb.*;
import org.bson.types.ObjectId;

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

    public void addUser(BasicDBObject addQuery)
    {
        userColl.insert(addQuery);
    }

    public ObjectId addRecipe(BasicDBObject addQuery)
    {
        recipeColl.insert(addQuery);
        return (ObjectId)addQuery.get("_id");
    }

    public void addLikeToUserWithUpdate(BasicDBObject addQuery, String username)
    {
        userColl.update(new BasicDBObject("_id", username), addQuery);
    }

    public void addLikeToRecipeWithUpdate(BasicDBObject addQuery, int recipeID)
    {
        recipeColl.update(new BasicDBObject("_id", recipeID), addQuery);
    }

    public void addRecipeToUserWithUpdate(BasicDBObject addQuery, String username)
    {
        userColl.update(new BasicDBObject("_id", username), addQuery);
    }
}
