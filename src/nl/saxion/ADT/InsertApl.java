package nl.saxion.ADT;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 4-3-2015.
 */
public class InsertApl {
    private DatabaseHandler dh;
    public InsertApl(DatabaseHandler dh)
    {
        this.dh = dh;
        // Add users
        String[] usernames = new String[7];
        String mike = "Mike";
        usernames[0] = mike;
        String vincent = "Vincent1995";
        usernames[1] = vincent;
        String evert = "Evert Stroet";
        usernames[2] = evert;
        String jan = "Jan Duipmans";
        usernames[3] = jan;
        String jaap = "Jaapie";
        usernames[4] = jaap;
        String nobody = "nobody";
        usernames[5] = nobody;
        String prakken = "lekker_prakje";
        usernames[6] = prakken;
        addUser(mike);
        addUser(vincent);
        addUser(evert);
        addUser(jan);
        addUser(jaap);
        addUser(nobody);
        addUser(prakken);

        // Mike adds a first recipe 1
        String username = mike;
        RecipeHelper rh = new RecipeHelper(this);
        rh.setName("Kipsalade");
        rh.setPersonCount(13);
        rh.addCourse("Toetje");
        rh.setDifficulty(2);
        rh.setPreparationTime(30);
        rh.addTypes("Nederlands");
        rh.addIngredients(parseIngredient("Boter", 50, "gram"));
        rh.addIngredients(parseIngredient("Slasaus", 500, "ml"));
        rh.addIngredients(parseIngredient("Kip", 500, "gram"));
        rh.addIngredients(parseIngredient("Eieren", 2, "Stuks"));
        rh.addIngredients(parseIngredient("Bonen", 200, "gram"));
        rh.setDescription("Een lekkere salade van kip en bonen");
        rh.addProcedures("Snij de kip in stukken");
        rh.addProcedures("Maak deze gaar in een pan met boter");
        rh.addProcedures("Pak de slasaus, de eieren en de bonen en roer dit door elkaar");
        rh.addProcedures("Als de kip gaar is laat dit dan afkoelen in de koelkast");
        rh.addProcedures("Als de kip koud is, voeg alles door elkaar en blijf roeren tot het een geheel is");
        ObjectId recipeId = rh.addRecipe(username);


        // Reviews toevoegen
        int till = (int)(Math.random()*5) + 1;
        for (int i = 0; i < till;i++)
        {
            int review = (int)(Math.random()*5) + 1;
            while (username.equals(mike)){
                username = usernames[(int)(Math.random()*5) + 1];
            }
            addReview(username, review, recipeId);
        }

        // Last reviewer adds a random comment
        ObjectId commentId = addComment(username, "Een random comment", recipeId, null); // Null if it is a parent
        // Add like or dislike by vincent
        addLikeWithUpdate(((int)(Math.random()*2) + 1) == 1, commentId, vincent, recipeId);





        // Vincent adds a recipe 2
        username = vincent;
        rh = new RecipeHelper(this);
        rh.setName("Ganzen schotel");
        rh.setPersonCount(4);
        rh.addCourse("Dinner");
        rh.setDifficulty(1);
        rh.setPreparationTime(60);
        rh.addTypes("Frans");
        rh.addIngredients(parseIngredient("Gans", 3, "stuks"));
        rh.setDescription("Drie ganzen in één schotel");
        rh.addProcedures("Pak een gans");
        rh.addProcedures("Pak een gans");
        rh.addProcedures("Pak een gans");
        rh.addProcedures("Kook het");
        recipeId = rh.addRecipe(username);

        // Reviews toevoegen
        till = (int)(Math.random()*5) + 1;
        for (int i = 0; i < till;i++)
        {
            int review = (int)(Math.random()*5) + 1;
            while (username.equals(mike)){
                username = usernames[(int)(Math.random()*5) + 1];
            }
            addReview(username, review, recipeId);
        }

        // Last reviewer adds a random comment
        commentId = addComment(username, "Een random comment", recipeId, null); // Null if it is a parent
        // Add like or dislike by vincent
        addLikeWithUpdate(((int)(Math.random()*2) + 1) == 1, commentId, vincent, recipeId);





        // Evert adds a recipe 3
        username = evert;
        rh = new RecipeHelper(this);
        rh.setName("Casa di mama");
        rh.setPersonCount(1);
        rh.addCourse("Dinner");
        rh.setDifficulty(1);
        rh.setPreparationTime(60);
        rh.addTypes("Italiaans");
        rh.addIngredients(parseIngredient("Pizza", 1, "stuks"));
        rh.setDescription("Een lekkere pizza kant en klaar uit de winkel");
        rh.addProcedures("Pak de pizza");
        rh.addProcedures("Volg de instructies op de achterkant");
        recipeId = rh.addRecipe(username);

        // Reviews toevoegen
        till = (int)(Math.random()*5) + 1;
        for (int i = 0; i < till;i++)
        {
            int review = (int)(Math.random()*5) + 1;
            while (username.equals(mike)){
                username = usernames[(int)(Math.random()*5) + 1];
            }
            addReview(username, review, recipeId);
        }

        // Last reviewer adds a random comment
        commentId = addComment(username, "Een random comment", recipeId, null); // Null if it is a parent
        // Add like or dislike by vincent
        addLikeWithUpdate(((int)(Math.random()*2) + 1) == 1, commentId, vincent, recipeId);





        // Evert adds a recipe 3
        username = jan;
        rh = new RecipeHelper(this);
        rh.setName("Spaghetti");
        rh.setPersonCount(1);
        rh.addCourse("Dinner");
        rh.addCourse("Toetje");
        rh.setDifficulty(5);
        rh.setPreparationTime(12345);
        rh.addTypes("Italiaans");
        rh.addTypes("Frans");
        rh.addTypes("Ik ben de naam vergeten");
        rh.addIngredients(parseIngredient("Spaghetti", 1, "pak"));
        rh.setDescription("Ik weet niet hoe ik spaghetti moet klaar maken");
        rh.addProcedures("Zoek maar iets anders");
        rh.addProcedures("Want");
        rh.addProcedures("ik");
        rh.addProcedures("weet");
        rh.addProcedures("het");
        rh.addProcedures("niet");
        recipeId = rh.addRecipe(username);

        // Reviews toevoegen
        till = (int)(Math.random()*5) + 1;
        for (int i = 0; i < till;i++)
        {
            int review = (int)(Math.random()*5) + 1;
            while (username.equals(mike)){
                username = usernames[(int)(Math.random()*5) + 1];
            }
            addReview(username, review, recipeId);
        }

        // Last reviewer adds a random comment
        commentId = addComment(username, "Een random comment", recipeId, null); // Null if it is a parent
        // Add like or dislike by vincent
        addLikeWithUpdate(((int)(Math.random()*2) + 1) == 1, commentId, vincent, recipeId);





        // Evert adds a recipe 3
        username = prakken;
        rh = new RecipeHelper(this);
        rh.setName("Boerenkool met worst");
        rh.setPersonCount(8);
        rh.addCourse("Dinner");
        rh.setDifficulty(2);
        rh.setPreparationTime(60);
        rh.addTypes("Nederlands");
        rh.addIngredients(parseIngredient("Boerenkool", 2, "kilo"));
        rh.addIngredients(parseIngredient("Worst", 2, "stuks"));
        rh.addIngredients(parseIngredient("Aardappelen", 2, "kilo"));
        rh.setDescription("Een lekkere boerenkool stampot");
        rh.addProcedures("Kook de boerenkool in een grote pan");
        rh.addProcedures("Kook tegelijkertijd de aaradappelen (geschild) in een aparte pan");
        rh.addProcedures("Kook de wordt ook in een aparte pan");
        rh.addProcedures("Etc..");
        recipeId = rh.addRecipe(username);

        // Reviews toevoegen
        till = (int)(Math.random()*5) + 1;
        for (int i = 0; i < till;i++)
        {
            int review = (int)(Math.random()*5) + 1;
            while (username.equals(mike)){
                username = usernames[(int)(Math.random()*5) + 1];
            }
            addReview(username, review, recipeId);
        }

        // Hier wordt handmatig een comment tree opgezet om te testen.
        // Eerst een parent
        ObjectId parrentID1 = addComment(username, "Parent comment", recipeId, null);
        // Voegen wij twee childs aan toe
        addComment(username, "Comment depth 1", recipeId, parrentID1);
        commentId = addComment(username, "Comment depth 1", recipeId, parrentID1);
        // Ook weer één
        commentId = addComment(username, "Comment depth 2", recipeId, commentId);
        // Nog één keer
        commentId = addComment(username, "Comment depth 3", recipeId, commentId);
        // Dan nog een parent
        parrentID1 = addComment(username, "Parent comment", recipeId, null);
        // Hier ook één aan toevogen
        addComment(username, "Comment depth 1", recipeId, parrentID1);
        // Deze tree gaat er als volgt ongeveer uitzien. (volgorde kan verschillen)
        /*  Parent comment
                Comment depth 1
                Comment depth 1
                    Comment depth 2
                        Comment depth 3
            Parent comment
                Comment depth 1
         */
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
                .append("commentID",        commentID)
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
            Object temp = null;
            if(obj != null)
            {
                temp =  obj.get("path");
                depth = Integer.parseInt(obj.get("depth").toString()) + 1;
            }

            path = temp == null ? parentID + "":temp.toString() + ":" + parentID;

        }
        BasicDBObject addQuery = new BasicDBObject()
                .append("_id",              id)
                .append("username",         username)
                .append("body",             body)
                .append("path" ,            path)
                .append("depth",            depth)
                .append("likes",            0)
                .append("dislikes",         0);
        addQuery = new BasicDBObject()
                .append("comments",         addQuery);
        addQuery = new BasicDBObject()
                .append("$addToSet",        addQuery);
        dh.addCommentToRecipe(addQuery, recipeID);
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
