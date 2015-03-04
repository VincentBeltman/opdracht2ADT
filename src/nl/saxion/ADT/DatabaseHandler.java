package nl.saxion.ADT;

import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by Mike on 4-3-2015.
 */
public class DatabaseHandler {
    private static DatabaseHandler instance;
    private DB db;
    private DBCollection usersCollection;
    private DBCollection recipesCollection;

    private  DatabaseHandler()
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
            getColections();
            System.out.println("Connection succes");
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

    }

    public void getColections()
    {
        if(db != null)
        {

            recipesCollection = db.getCollection("recipes");
            usersCollection = db.getCollection("users");


        }
    }


}
