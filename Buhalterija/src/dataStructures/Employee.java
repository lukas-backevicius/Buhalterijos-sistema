package dataStructures;

import javafx.beans.property.SimpleStringProperty;


public class Employee extends User {
    private  SimpleStringProperty id, firstName, lastName;
    private SimpleStringProperty phone;
    private SimpleStringProperty email;

    public Employee(String login, String pass, String id, String firstName, String lastName, String phone, String email){
        super(login,pass);
        this.id = new SimpleStringProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
    }

    public String getLogin(){
        return login.get();
    }

    public String getPass(){
        return pass.get();
    }

    public String getId(){
        return id.get();
    }

    public String getFirstName(){
        return firstName.get();
    }

    public String getLastName(){
        return lastName.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getEmail(){
        return email.get();
    }

    public void setLogin(String login){
        this.login = new SimpleStringProperty(login);
    }

    public void setPass(String pass){
        this.pass.set(pass);
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
