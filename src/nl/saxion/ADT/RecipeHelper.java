package nl.saxion.ADT;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 14-3-2015.
 */
public class RecipeHelper {
    private InsertApl apl;
    private String name;
    private int personCount;
    private int difficulty;
    private int preparationTime;
    private List<String> courses = new ArrayList<String>();
    private List<String> types = new ArrayList<String>();
    private List<String> procedures = new ArrayList<String>();
    private List<BasicDBObject> ingredients = new ArrayList<BasicDBObject>();
    private String description = "";


    public RecipeHelper(InsertApl apl) {
        this.apl = apl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void addCourse(String course) {
        courses.add(course);
    }

    public void addTypes(String type) {
        types.add(type);
    }

    public void addProcedures(String procedure) {
        procedures.add("procedure");
    }

    public void addIngredients(BasicDBObject ingredient) {
        ingredients.add(ingredient);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectId addRecipe(String username){
        return apl.addRecipe(name,personCount, courses, difficulty, preparationTime, types, ingredients, description, procedures, username);
    }
}
