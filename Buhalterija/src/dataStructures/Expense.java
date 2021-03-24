package dataStructures;

import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

import java.io.Serializable;

@Data

public class Expense {
    private SimpleStringProperty id;
    private SimpleStringProperty categoryId;
    private SimpleStringProperty howAcquired;
    private SimpleStringProperty amount;

    public Expense(String id, String categoryId, String howAcquired, String amount){
        this.id = new SimpleStringProperty(id);
        this.categoryId = new SimpleStringProperty(categoryId);
        this.howAcquired = new SimpleStringProperty(howAcquired);
        this.amount = new SimpleStringProperty(amount);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getCategoryId() {
        return categoryId.get();
    }


    public void setCategoryId(String categoryId) {
        this.categoryId.set(categoryId);
    }

    public String getHowAcquired() {
        return howAcquired.get();
    }

    public void setHowAcquired(String howAcquired) {
        this.howAcquired.set(howAcquired);
    }

    public String getAmount() {
        return amount.get();
    }


    public void setAmount(String amount) {
        this.amount.set(amount);
    }
}
