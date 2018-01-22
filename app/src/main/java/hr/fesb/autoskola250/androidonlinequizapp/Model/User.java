package hr.fesb.autoskola250.androidonlinequizapp.Model;

/**
 * Created by Antun on 18.1.2018..
 */

public class User {
    private String userName;
    private String pasword;
    private String email;

    public User(){

    }

    public User(String userName, String pasword, String email){
        this.userName = userName;
        this.pasword = pasword;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPasword() {
        return pasword;
    }

    public String getEmail() {
        return email;
    }
}
