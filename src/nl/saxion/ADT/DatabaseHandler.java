package nl.saxion.ADT;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;

/**
 * Created by Mike on 4-3-2015.
 */
public class DatabaseHandler {
    private static DatabaseHandler instance;
    private DB db;

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
            

            System.out.println("Connection succes");
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

    }


}
