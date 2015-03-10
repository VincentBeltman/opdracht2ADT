package nl.saxion.ADT;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.BSON;
import org.bson.types.ObjectId;
import sun.misc.BASE64Decoder;

import java.util.ArrayList;

/**
 * Created by Mike on 5-3-2015.
 */
public class FindApl {
    private DatabaseHandler dh;

    public  FindApl(DatabaseHandler dh)
    {
        this.dh =  dh;
        //findRecpiesByIngredients(getIngredientsList("UI" ));
        //findRecipeByName("Ganzen schotel");
        //findRecipeByUser("boomhoo");
        findUserPreferences("boomhoo");
    }

    public ArrayList getIngredientsList(String ... names )
    {
        ArrayList<BasicDBObject> result = new ArrayList<BasicDBObject>();
        for(String s : names)
        {
            result.add(new BasicDBObject()
                    .append("name", s));
        }

        return  result;
    }

    public void findRecpiesByIngredients(ArrayList<BasicDBObject> ingredients)
    {
        ArrayList<BasicDBObject> orList = new ArrayList<BasicDBObject>();
        for(BasicDBObject ingredient :  ingredients)
        {
            if(ingredient.containsField("name"))
            {
                BasicDBObject newObject = new BasicDBObject()
                        .append("ingredients.name", ingredient.get("name"));
                orList.add(newObject);
            }

        }
        BasicDBObject query  = new BasicDBObject()
                .append("$or" ,            orList);
        dh.findRecpiesByIngredients(query);




    }

    public void findRecipeByName(String name)
    {
        BasicDBObject selquery = new BasicDBObject()
                .append("name",             name); //TODO: contains ipv is
        dh.findRecipe(selquery);
    }

    public void findRecipeByUser(String username)
    {
        BasicDBObject selquery = new BasicDBObject()
                .append("_id",              username);
        for (DBObject recipe:dh.getRecipesOfUser(selquery))
        {
            print("------------------------------------------------");
            print("Naam recept: " + recipe.get("name"));
            print("Aantal personen: " + recipe.get("personCount"));
            print("Moeilijkheid: " + recipe.get("difficulty"));
            print("Bereidingstijd: " + recipe.get("preparationTime"));
            print("Bescrhijving: " + recipe.get("description"));
            int avg = 0;
            for (Object review:((BasicDBList)recipe.get("reviews")))
            {
                DBObject temp = (DBObject)review;
                avg += Integer.parseInt(temp.get("review").toString());
            }
            print("Gemiddelde beoordeling: " + avg);
            print("Keukens:");
            for (Object course:(BasicDBList)recipe.get("courses"))
            {
                print(" -  " + course.toString());
            }
            print("Types:");
            for (Object type:((BasicDBList)recipe.get("types")))
            {
                print(" -  " + type.toString());
            }
            print("Ingredienten:");
            for (Object ingredient:((BasicDBList)recipe.get("ingredients")))
            {
                DBObject temp = (DBObject)ingredient;
                print(" -  " + temp.get("amount") + " " + temp.get("unit") + " " + temp.get("name"));
            }
            print("Stappen:");
            int count = 1;
            for (Object procedure:((BasicDBList)recipe.get("procedures")))
            {
                print(" " + count + ". " + procedure.toString());
                count++;
            }
            print("Reacties:");
        }
    }

    public void findUserPreferences(String username)
    {
        // First we get all of the likes of the user
        BasicDBObject selQuery = new BasicDBObject()
                .append("_id",              username);
        ArrayList<ObjectId> likes = new ArrayList<ObjectId>();
        ArrayList<ObjectId> dislikes = new ArrayList<ObjectId>();
        for (Object like:dh.getLikesOfUser(selQuery))
        {
            DBObject temp = (DBObject) like;
            if (temp.get("like").toString() == "true") {
                likes.add((ObjectId) temp.get("commentID"));
            } else{
                dislikes.add((ObjectId) temp.get("commentID"));
            }
        }
        // Now we have a list of likes an dislikes. We need to extract patterns now.
        selQuery = new BasicDBObject()
                .append("likes", new BasicDBObject("$in", likes));
        dh.getFavIngredient(selQuery);
    }

    public void print(String print)
    {
        System.out.println(print);
    }
}
