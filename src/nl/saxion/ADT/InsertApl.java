package nl.saxion.ADT;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Vincent on 4-3-2015.
 */
public class InsertApl {
    private DatabaseHandler dh;
    public InsertApl(DatabaseHandler dh)
    {
        this.dh = dh;
        addUser("Mike De boer", new Date(2012, 12, 12));
        addLikeWithUpdate(true, 10, "Mike De boer", 0);
    }

    public void addUser(String username, Date date)
    {
        BasicDBObject addQuery = new BasicDBObject();
        addQuery.append("_id", username)
                .append("dob", date)
                .append("likes", new ArrayList<BasicDBObject>())
                .append("recipes", new ArrayList<ObjectId>());
        dh.addUser(addQuery);
    }

    public void addLikeWithUpdate(boolean like, int commentID, String username, int recipeID)
    {
        BasicDBObject addQuery = new BasicDBObject();
        addQuery.append("commentID", commentID)
                .append("like", like);
        addQuery = new BasicDBObject("$addToSet", addQuery);
        addQuery = new BasicDBObject("$set", addQuery);
        print(addQuery.toString());
        dh.addLikeWithUpdate(addQuery, username);
    }

    public void print(String print)
    {
        System.out.println(print);
    }
}
