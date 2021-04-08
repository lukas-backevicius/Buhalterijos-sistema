package dataStructures;

import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class Category {
    private SimpleStringProperty id;
    private SimpleStringProperty responsiblePersonFirstName;
    private SimpleStringProperty responsiblePersonLastName;
    private SimpleStringProperty name;
    private SimpleStringProperty parentCategoryId;


    public Category(String id, String responsiblePersonFirstName, String responsiblePersonLastName, String name, String parentCategoryId){
        this.id = new SimpleStringProperty(id);
        this.responsiblePersonFirstName = new SimpleStringProperty(responsiblePersonFirstName);
        this.responsiblePersonLastName = new SimpleStringProperty(responsiblePersonLastName);
        this.name = new SimpleStringProperty(name);
        this.parentCategoryId = new SimpleStringProperty(parentCategoryId);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getResponsiblePersonFirstName() {
        return responsiblePersonFirstName.get();
    }

    public void setResponsiblePersonFirstName(String responsiblePersonFirstName) {
        this.responsiblePersonFirstName.set(responsiblePersonFirstName);
    }

    public String getResponsiblePersonLastName() {
        return responsiblePersonLastName.get();
    }

    public void setResponsiblePersonLastName(String responsiblePersonLastName) {
        this.responsiblePersonLastName.set(responsiblePersonLastName);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getParentCategoryId() {
        return parentCategoryId.get();
    }


    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId.set(parentCategoryId);
    }
}
