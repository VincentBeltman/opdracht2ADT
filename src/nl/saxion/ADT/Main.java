package nl.saxion.ADT;

public class Main {
    public static void main(String[] args) {
	// write your code here
        DatabaseHandler dh = DatabaseHandler.getIntance();
        dh.connect();
        //new InsertApl(dh);
        new FindApl(dh);
    }
}
