package nl.saxion.ADT;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mike on 5-3-2015.
 */
public class FindApl {
    private DatabaseHandler dh;

    public  FindApl(DatabaseHandler dh)
    {
        this.dh =  dh;
        //findRecpiesByIngredients(getIngredientsList("UI" ));
        //findRecipesByName("Ganzen schotel");
        findTopNRecpies(3);
        //findRecipeByUser("boomhoo");
        //findUserPreferences("boomhoo");
        //findRecpiesByIngredients(getIngredientsList("UI" , "Ganzen" ));
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

    public void findTopNRecpies(int numberOfItems)
    {
        System.out.println("De " + numberOfItems + " beste recepten");
        print("------------------------------------------------");
        for(DBObject recipe :dh.findTopNRecipe(numberOfItems))
        {

            print("Naam recept: " + recipe.get("name"));
            print("Gemiddeld: "+ recipe.get("avg"));
            print("Aantal beoordelingn" +  recipe.get("numberOfReviews"));
            print("------------------------------------------------");

        }
    }

    public void findRecpiesByIngredients(ArrayList<BasicDBObject> ingredients)
    {
        ArrayList<String> ingredientList = new ArrayList<String>();

        for(BasicDBObject ingredient :  ingredients)
        {
            if(ingredient.containsField("name"))
            {

                ingredientList.add(ingredient.getString("name"));
            }

        }
        BasicDBObject query  = new BasicDBObject();
        BasicDBObject queryPart = new BasicDBObject();
        query.append("ingredients.name" ,queryPart);
        queryPart.append("$in", ingredientList);

        dh.findRecpiesByIngredients(query);
    }

    public void findRecipesByName(String name)
    {
        //name = name.substring(1 , 4);
        BasicDBObject selquery = new BasicDBObject()
                .append("name", java.util.regex.Pattern.compile(name));
        System.out.print(selquery.toString());
        dh.findRecipe(selquery);
    }

    public void findRecipeByUser(String username)
    {
        BasicDBObject selquery = new BasicDBObject()
                .append("_id",              username);
        for (DBObject recipe:dh.getRecipesOfUser(selquery))
        {
            printRecipe(recipe);
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
                .append("_id", new BasicDBObject("$in", likes));
        String favoriteIngredient = dh.getFavIngredient(selQuery);
        String favoriteCourse = dh.getFavCourse(selQuery);

        // Now we have the most favorite ingredient and the most favorite course.
        // The last thing we have to do is querying on this.
        for (DBObject recipe:dh.searchFavoriteRecipe(favoriteIngredient, favoriteCourse))
        {
            printRecipe(recipe);
            break;
        }
    }

    public void printRecipe(DBObject recipe)
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
        for (BasicDBObject item : getCommentTree((ArrayList<BasicDBObject>) recipe.get("comments"))) {
            printCommentTree(item);
        }
    }

    public void print(String print)
    {
        System.out.println(print);
    }

    private ArrayList<BasicDBObject> getCommentTree(ArrayList<BasicDBObject> list)
    {
        ArrayList<DBObject> comments = new ArrayList<DBObject>();
        ArrayList<DBObject> tempList;
        BasicDBObject children = new BasicDBObject();  // This will be the map. The key will be the parentid and the value a listwith DBObjects
        int startDepth = 0;
        Pattern r = Pattern.compile("(\\w+)$");
        for (BasicDBObject comment:list)
        {
            if (Integer.parseInt(comment.get("depth").toString()) == startDepth){
                comments.add(comment);
            } else {
                Matcher match = r.matcher(comment.get("path").toString());
                if (match.find()){
                    if (children.get(match.group(1)) == null) {
                        tempList = new ArrayList<DBObject>();
                    } else {
                        tempList = (ArrayList<DBObject>) children.get(match.group(1));
                    }
                    tempList.add(comment);
                    children.append(match.group(1), tempList);
                }
            }
        }
        return assemble(comments, children);
    }

    private ArrayList<BasicDBObject> assemble(ArrayList<DBObject> comments, BasicDBObject children)
    {
        ArrayList<BasicDBObject> returnList = new  ArrayList<BasicDBObject>();
        for (DBObject comment:comments){
            BasicDBObject returnObject = new BasicDBObject("comment", comment);
            ArrayList<DBObject> temp = (ArrayList<DBObject>) children.get(comment.get("_id").toString());
            if (temp != null) {
                returnObject.append("children", assemble(temp, children));
            }
            returnList.add(returnObject);
        }
        return returnList;
    }

    private void printCommentTree(BasicDBObject item)
    {
        BasicDBObject comment = (BasicDBObject) item.get("comment");
        String indent = "";
        for (int i = 0; i < comment.getInt("depth"); i++){
            indent += "    ";
        }
        print(indent + comment.get("body"));
        if (item.get("children") != null)
        {
            for (BasicDBObject child : (ArrayList<BasicDBObject>) item.get("children")) {
                printCommentTree(child);
            }
        }
    }
}
