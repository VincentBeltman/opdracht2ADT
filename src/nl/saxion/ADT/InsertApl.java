package nl.saxion.ADT;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vincent on 4-3-2015.
 */
public class InsertApl {
    private DatabaseHandler dh;
    public InsertApl(DatabaseHandler dh)
    {
        this.dh = dh;

        // Add recipe
        List<String> courses = new ArrayList<String>();
        courses.add("Main");
        List<String> types = new ArrayList<String>();
        types.add("French");
        List<String> procedures = new ArrayList<String>();
        procedures.add("Pak een gans");
        procedures.add("Pak een gans");
        procedures.add("Pak een gans");
        procedures.add("Kook het");
        List<BasicDBObject> ingredients = new ArrayList<BasicDBObject>();
        ingredients.add(parseIngredient("Ganzen", 3, "Stuks"));
        addRecipe("Ganzen schotel", 5, courses, 5, 12, types, ingredients,
                "Het vlees van 3 ganzen in één schotel", procedures, "Mike de Boer");
    }

    public void addReview()
    {

    }

    public void addUser(String username, Date date)
    {
        // { "_id" : "Mike de Boer" , "dob" : { "$date" : "3113-01-11T23:00:00.000Z"} , "likes" : [ ] , "recipes" : [ ]}
        BasicDBObject addQuery = new BasicDBObject();
        addQuery.append("_id",              username)
                .append("dob",              date)
                .append("likes",            new ArrayList<BasicDBObject>())
                .append("recipes",          new ArrayList<ObjectId>());
        dh.addUser(addQuery);
    }

    public void addLikeWithUpdate(boolean like, int commentID, String username, int recipeID)
    {
        // { "$addToSet" : { "likes" : { "commentID" : 10 , "like" : true}}}
        BasicDBObject addQuery = new BasicDBObject();
        addQuery.append("commentID",        commentID)
                .append("like",             like);
        addQuery = new BasicDBObject("likes", addQuery);
        addQuery = new BasicDBObject("$addToSet", addQuery);
        dh.addLikeToUserWithUpdate(addQuery, username);
        dh.addLikeToRecipeWithUpdate(addQuery, recipeID);
    }

    public void addRecipe(String name, int personCount, List<String> courses, int difficulty,
                          int preparationTime, List<String> types, List<BasicDBObject> ingredients,
                          String description, List<String> procedures, String username)
    {
        // { "name" : "Ganzen schotel" , "personCount" : 5 , "courses" : [ "Main"] , "difficulty" : 5 , "preparationTime" : 12 , "types" : [ "French"] , "ingredients" : [ { "name" : "Ganzen" , "amount" : 3 , "unit" : "Stuks"}] , "description" : "Het vlees van 3 ganzen in één schotel" , "procedures" : [ "Pak een gans" , "Pak een gans" , "Pak een gans" , "Kook het"] , "reviews" : [ ] , "comments" : [ ]}
        BasicDBObject addQuery = new BasicDBObject();
        addQuery.append("name", name)
                .append("personCount",      personCount)
                .append("courses",          courses)
                .append("difficulty",       difficulty)
                .append("preparationTime",  preparationTime)
                .append("types",            types)
                .append("ingredients",      ingredients)
                .append("description",      description)
                .append("procedures",       procedures)
                .append("reviews",          new ArrayList<String>())
                .append("comments", new ArrayList<String>());
        print(addQuery.toString());
        addRecipeToUserWithUpdate(username, dh.addRecipe(addQuery));
    }

    private BasicDBObject parseIngredient(String name, int amount, String unit)
    {
        return new BasicDBObject()
                .append("name",             name)
                .append("amount",           amount)
                .append("unit",             unit);
    }

    private void addRecipeToUserWithUpdate(String username, ObjectId recipeID)
    {
        // { "$push" : { "recipes" : 10}}
        BasicDBObject addQuery = new BasicDBObject("$push", new BasicDBObject("recipes", recipeID));
        dh.addRecipeToUserWithUpdate(addQuery, username);
    }

    public void print(String print)
    {
        System.out.println(print);
    }
}
