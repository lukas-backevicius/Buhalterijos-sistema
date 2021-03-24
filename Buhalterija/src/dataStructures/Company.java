package dataStructures;

import javafx.beans.property.SimpleStringProperty;

public class Company extends User{
    private SimpleStringProperty name;
    private SimpleStringProperty address;
    private SimpleStringProperty phone;
    private SimpleStringProperty id;
    private SimpleStringProperty lastName;
    private SimpleStringProperty email;

    public Company(String login, String pass, String id, String name, String address, String phone, String lastName
            , String email)
    {
        super(login,pass);
        this.id = new SimpleStringProperty( id);
        this.name = new SimpleStringProperty( name);
        this.address = new SimpleStringProperty( address);
        this.phone = new SimpleStringProperty( phone);
        this.lastName = new SimpleStringProperty( lastName);
        this.email = new SimpleStringProperty( email);
    }

    public String getLogin(){
        return login.get();
    }
    public void setLogin(String Login){
        this.login = new SimpleStringProperty(Login);
    }


    public String getPass(){
        return pass.get();
    }

    public void setPass(String Pass){
        this.pass.set(Pass);
    }

    public String getName() {
        return name.get();
    }


    public void setName(String name) {
        this.name.set(name);
    }

    public String getAddress() {
        return address.get();
    }


    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getId() {
        return id.get();
    }


    public void setId(String id) {
        this.id.set(id);
    }

    public String getLastName() {
        return lastName.get();
    }


    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getEmail() {
        return email.get();
    }


    public void setEmail(String email) {
        this.email.set(email);
    }
}
