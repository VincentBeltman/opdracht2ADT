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
        findRecpiesByIngredients(getIngredientsList("UI" ));

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
        ArrayList<BasicDBObject> orList = new ArrayList<BasicDBObject>();
        for(BasicDBObject ingredient :  ingredients)
        {
            if(ingredient.containsField("name"))
            {
                BasicDBObject newObject = new BasicDBObject().append("ingredients.name" , ingredient.get("name"));
                orList.add(newObject);
            }

        }
        BasicDBObject query  = new BasicDBObject();
        query.append("$or" , orList);
        dh.findRecpiesByIngredients(query);




    }
}
