package dataStructures;

import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

import java.io.Serializable;

@Data

public class Income {
    private SimpleStringProperty id;
    private SimpleStringProperty categoryId;
    private SimpleStringProperty howUsed;
    private SimpleStringProperty amount;

    public Income(String id, String categoryId, String howUsed, String amount) {
        this.id = new SimpleStringProperty(id);
        this.categoryId = new SimpleStringProperty(categoryId);
        this.howUsed = new SimpleStringProperty(howUsed);
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

    public String getHowUsed() {
        return howUsed.get();
    }


    public void setHowUsed(String howUsed) {
        this.howUsed.set(howUsed);
    }

    public String getAmount() {
        return amount.get();
    }


    public void setAmount(String amount) {
        this.amount.set(amount);
    }
}
