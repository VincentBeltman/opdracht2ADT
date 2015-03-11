package nl.saxion.ADT;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Vincent on 4-3-2015.
 */
public class InsertApl {
    private DatabaseHandler dh;
    public InsertApl(DatabaseHandler dh)
    {
        this.dh = dh;

        // Add user
        String username  = "boomhoo";
        //addUser(username);

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
        ObjectId recipeID = addRecipe("Ganzen schotel", 5, courses, 5, 12, types, ingredients,
                "Het vlees van 3 ganzen in één schotel", procedures, username);

        //addUser(username);
        // Add review
        addReview(username, 4, recipeID);

        // Add comment
        addComment(username, "Dit is een comment :D", new ObjectId("54f88f063674acb7bfabf910"), new ObjectId("54fc59831822ad7f85691953"));

        // Add like
        // addLikeWithUpdate(false, new ObjectId("54fc59831822ad7f85691953"), "boomhoo", new ObjectId("54f88f063674acb7bfabf910"));
        // Add review
        // addReview("boomhoo",4,new ObjectId("54f88f063674acb7bfabf910"));
    }

    public void addReview(String username, int review, ObjectId recipeID)
    {
        BasicDBObject addQuery = new BasicDBObject()
                .append("username",          username)
                .append("review",           review);
        addQuery = new BasicDBObject()
                .append("reviews",          addQuery);
        addQuery = new BasicDBObject()
                .append("$addToSet",        addQuery);
        dh.addReviewToRecipe(addQuery, recipeID);
    }

    public void addUser(String username)
    {
        // { "_id" : "Mike de Boer" , "likes" : [ ] , "recipes" : [ ]}
        BasicDBObject addQuery = new BasicDBObject()
                .append("_id",              username)
                .append("likes",            new ArrayList<BasicDBObject>())
                .append("recipes", new ArrayList<ObjectId>());
        dh.addUser(addQuery);
    }

    public void addLikeWithUpdate(boolean like, ObjectId commentID, String username, ObjectId recipeID)
    {
        // { "$addToSet" : { "likes" : { "commentID" : 10 , "like" : true}}}
        BasicDBObject addQuery = new BasicDBObject()
                .append("commentID", commentID)
                .append("like",             like);
        addQuery = new BasicDBObject()
                .append("likes",            addQuery);
        addQuery = new BasicDBObject()
                .append("$addToSet",        addQuery);
        dh.addLikeToUserWithUpdate(addQuery, username);
        String likeOrDislike = like ? "comments.$.likes":"comments.$.dislikes";
        BasicDBObject updQuery = new BasicDBObject()
                .append("$inc",             new BasicDBObject(likeOrDislike, 1));
        dh.addLikeToRecipeWithUpdate(updQuery, recipeID, commentID);
    }

    public ObjectId addRecipe(String name, int personCount, List<String> courses, int difficulty,
                          int preparationTime, List<String> types, List<BasicDBObject> ingredients,
                          String description, List<String> procedures, String username)
    {
        // { "name" : "Ganzen schotel" , "personCount" : 5 , "courses" : [ "Main"] , "difficulty" : 5 , "preparationTime" : 12 , "types" : [ "French"] , "ingredients" : [ { "name" : "Ganzen" , "amount" : 3 , "unit" : "Stuks"}] , "description" : "Het vlees van 3 ganzen in één schotel" , "procedures" : [ "Pak een gans" , "Pak een gans" , "Pak een gans" , "Kook het"] , "reviews" : [ ] , "comments" : [ ]}
        BasicDBObject addQuery = new BasicDBObject()
                .append("name",             name)
                .append("personCount",      personCount)
                .append("courses",          courses)
                .append("difficulty",       difficulty)
                .append("preparationTime",  preparationTime)
                .append("types",            types)
                .append("ingredients",      ingredients)
                .append("description",      description)
                .append("procedures",       procedures)
                .append("reviews",          new ArrayList<String>())
                .append("comments",         new ArrayList<String>());
        ObjectId recipeID = dh.addRecipe(addQuery);
        addRecipeToUserWithUpdate(username, recipeID);
        return recipeID;
    }

    public ObjectId addComment(String username, String body, ObjectId recipeID, ObjectId parentID)
    {
        String path = null;
        int depth = 0;
        ObjectId id = new ObjectId();
        if (parentID != null){
            BasicDBObject selQuery = new BasicDBObject()
                .append("comments._id",              parentID);
            DBObject obj = dh.findCommentPath(selQuery);
            Object temp = obj.get("path");
            path = temp == null ? parentID + "":temp.toString() + ":" + parentID;
            depth = Integer.parseInt(obj.get("depth").toString()) + 1;
        }
        BasicDBObject addQuery = new BasicDBObject()
                .append("_id",              id)
                .append("username",         username)
                .append("body",             body)
                .append("created_at",       new Date())
                .append("path" ,            path)
                .append("depth",            depth)
                .append("likes",            0)
                .append("dislikes",         0);
        addQuery = new BasicDBObject()
                .append("comments",         addQuery);
        addQuery = new BasicDBObject()
                .append("$addToSet",        addQuery);
        dh.addCommentToRecipe(addQuery, recipeID);
        print(id + "");
        return id;
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
