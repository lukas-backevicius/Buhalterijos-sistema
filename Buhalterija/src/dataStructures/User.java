package dataStructures;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class User {
    StringProperty login;
    StringProperty pass;


    public User(String login, String pass) {
        this.login = new SimpleStringProperty(login);
        this.pass = new SimpleStringProperty(pass);
    }

    public String getPrisijugimas(){
        return login.get();
    }

    public String getSlaptazodis(){
        return pass.get();
    }
}
