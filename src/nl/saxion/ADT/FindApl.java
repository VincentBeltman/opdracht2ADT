package nl.saxion.ADT;

import com.mongodb.BasicDBObject;
import org.bson.BSON;

import java.util.ArrayList;

/**
 * Created by Mike on 5-3-2015.
 */
public class FindApl {
    private DatabaseHandler dh;

    public  FindApl(DatabaseHandler dh)
    {
        this.dh =  dh;
        findRecpiesByIngredients(getIngredientsList("UI" , "Ganzen" ));

    }

    public ArrayList getIngredientsList(String ... names )
    {
        ArrayList<BasicDBObject> result = new ArrayList<BasicDBObject>();
        for(String s : names)
        {
            result.add(new BasicDBObject().append("name", s));
        }

        return  result;
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
}
