package nl.saxion.ADT;

import com.mongodb.*;
import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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

    public void addReviewToRecipe(BasicDBObject addQuery, ObjectId recipeID)
    {
        recipeColl.update(new BasicDBObject("_id", recipeID), addQuery);
    }

    public void addCommentToRecipe(BasicDBObject addQuery, ObjectId recipeID)
    {
        recipeColl.update(new BasicDBObject("_id", recipeID), addQuery);
    }
    public void addLikeToRecipeWithUpdate(BasicDBObject updQuery, ObjectId recipeID, ObjectId commentID)
    {
        BasicDBObject selQuery = new BasicDBObject()
                .append("_id", recipeID)
                .append("comments._id", commentID);
        recipeColl.update(selQuery, updQuery);
    }

    public void addRecipeToUserWithUpdate(BasicDBObject addQuery, String username)
    {
        userColl.update(new BasicDBObject("_id", username), addQuery);
    }

    public DBObject findCommentPath(BasicDBObject selQuery) {
        List<DBObject> aggregate = new ArrayList<DBObject>();
        BasicDBObject colQuery = new BasicDBObject()
                .append("comments.path", 1)
                .append("comments.depth", 1)
                .append("_id", 0);
        aggregate.add(new BasicDBObject("$unwind", "$comments"));
        aggregate.add(new BasicDBObject("$match", selQuery));
        aggregate.add(new BasicDBObject("$project", colQuery));
        DBObject returnValue = null;
        for (DBObject obj : recipeColl.aggregate(aggregate).results()) {
            returnValue = ((DBObject) obj.get("comments"));
            break;
        }
        return returnValue;
    }

    public void findRecpiesByIngredients(BasicDBObject searchQuery)
    {
        System.out.println(searchQuery);
        DBCursor cursor = recipeColl.find(searchQuery);
        while (cursor.hasNext())
        {
            System.out.println(JsonPrettyPrinter.toJsonPrettyPrint(cursor.next()));
            //ystem.out.println(cursor.next().toString());
        }
    }

    public void findRecipe(BasicDBObject searchQuery)
    {
        DBCursor cursor = recipeColl.find(searchQuery);
        while (cursor.hasNext())
        {
            System.out.println(JsonPrettyPrinter.toJsonPrettyPrint(cursor.next()));
        }
    }

    public ArrayList<DBObject> getRecipesOfUser(BasicDBObject selquery)
    {
        BasicDBObject colQuery = new BasicDBObject()
                .append("_id",          0)
                .append("recipes", 1);
        DBCursor cursor = userColl.find(selquery, colQuery);
        BasicBSONList recipeIDs = (BasicBSONList)cursor.next().get("recipes");
        ArrayList<DBObject> results = new ArrayList<DBObject>();
        for (Object recipeID:recipeIDs)
        {
            results.add(recipeColl.find(new BasicDBObject("_id", recipeID)).one());
        }
        return results;
    }

    public BasicDBList getLikesOfUser(BasicDBObject selquery)
    {
        BasicDBObject colQuery = new BasicDBObject()
                .append("_id",          0)
                .append("likes", 1);
        return (BasicDBList)userColl.find(selquery, colQuery).next().get("likes");
    }

    public String getFavIngredient(BasicDBObject selQuery)
    {
        List<DBObject> aggregate = new ArrayList<DBObject>();
        BasicDBObject colQuery = new BasicDBObject()
                .append("ingredients.name", 1)
                .append("_id", 0);
        BasicDBObject group = new BasicDBObject()
                .append("_id", "$ingredients.name")
                .append("sum", new BasicDBObject("$sum", 1));
        BasicDBObject sort = new BasicDBObject()
                .append("sum", -1);
        //aggregate.add(new BasicDBObject("$match", selQuery));
        aggregate.add(new BasicDBObject("$unwind", "$ingredients"));
        aggregate.add(new BasicDBObject("$project", colQuery));
        aggregate.add(new BasicDBObject("$group", group));
        aggregate.add(new BasicDBObject("$sort", sort));
        for (DBObject obj : recipeColl.aggregate(aggregate).results()) {
            return obj.get("_id").toString();
        }
        return "";
    }

    public String getFavCourse(BasicDBObject selQuery)
    {
        List<DBObject> aggregate = new ArrayList<DBObject>();
        BasicDBObject colQuery = new BasicDBObject()
                .append("courses", 1)
                .append("_id", 0);
        BasicDBObject group = new BasicDBObject()
                .append("_id", "$courses")
                .append("sum", new BasicDBObject("$sum", 1));
        BasicDBObject sort = new BasicDBObject()
                .append("sum", -1);
        //aggregate.add(new BasicDBObject("$match", selQuery));
        aggregate.add(new BasicDBObject("$unwind", "$courses"));
        aggregate.add(new BasicDBObject("$project", colQuery));
        aggregate.add(new BasicDBObject("$group", group));
        aggregate.add(new BasicDBObject("$sort", sort));
        for (DBObject obj : recipeColl.aggregate(aggregate).results()) {
            return obj.get("_id").toString();
        }
        return "";
    }

    public DBCursor searchFavoriteRecipe(String favoriteIngredient, String favoriteCourse)
    {
        ArrayList<BasicDBObject> ors = new ArrayList<BasicDBObject>();
        ors.add(new BasicDBObject("ingredients.name", favoriteIngredient));
        ors.add(new BasicDBObject("courses", favoriteCourse));

        BasicDBObject selQuery = new BasicDBObject("$or", ors);
        return recipeColl.find(selQuery);
    }
}
