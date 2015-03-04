package nl.saxion.ADT;

public class Main {

    public static void main(String[] args) {
	// write your code here
        DatabaseHandler db = DatabaseHandler.getIntance();
        db.connect();
    }
}
