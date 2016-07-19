/**
 * Created by admin on 4/19/16.
 *
 */
import java.util.ArrayList;


public class User {

    private String name;
    String password;
    int id = 1;

    ArrayList<Message> messages = new ArrayList<>();



    public String getName() {
        return name;
    }



    public User(String name, String password){
        this.name = name;
        this.password = password;


    }


}







