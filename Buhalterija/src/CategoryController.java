import dataStructures.Category;
import dataStructures.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryController {

    @FXML private static ObservableList<Category> dList = FXCollections.observableArrayList();

    private static Connection con;

    @FXML private TableView<Category> categoriesTable;
    @FXML private TableColumn<Category, String> idCol;
    @FXML private TableColumn<Category, String> responsiblePersonFirstNameCol;
    @FXML private TableColumn<Category, String> responsiblePersonLastNameCol;
    @FXML private TableColumn<Category, String> nameCol;
    @FXML private TableColumn<Category, String> parentCategoryIdCol;

    @FXML private TextField idField;
    @FXML private TextField responsiblePersonFirstNameField;
    @FXML private TextField responsiblePersonLastNameField;
    @FXML private TextField nameField;
    @FXML private TextField parentCategoryIdField;

    @FXML private Button loadButton;
    @FXML private Button deleteButton;

    @FXML
    public static void initializeCategory (Connection cn) throws IOException {
        con = cn;
        CategoryController cat = new CategoryController();
        cat.renderDisplay();
    }


    public void loadFromDatabase()
    {
        try {
            String query = "select id, responsiblePersonFirstName, responsiblePersonLastName, name, parentCategoryId from category";
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            dList.clear();
            while (rs.next())
            {
                Category row = new Category(rs.getString("id"),rs.getString("responsiblePersonFirstName"),rs.getString("responsiblePersonLastName"),rs.getString("name"),rs.getString("parentCategoryId"));
                dList.add(row);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }

    @FXML
    public void initialize(){
        categoriesTable.getSelectionModel().selectedItemProperty().addListener((obj) ->
                deleteButton.setDisable(obj == null)
        );
    }

    public void loadData()
    {
        loadFromDatabase();

        categoriesTable.setEditable(true);

        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        responsiblePersonFirstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        responsiblePersonLastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        parentCategoryIdCol.setCellFactory(TextFieldTableCell.forTableColumn());

        this.loadButton.setDisable(true);
        this.deleteButton.setDisable(true);
        idCol.setCellValueFactory(new PropertyValueFactory<Category, String>("id"));
        responsiblePersonFirstNameCol.setCellValueFactory(new PropertyValueFactory<Category, String>("responsiblePersonFirstName"));
        responsiblePersonLastNameCol.setCellValueFactory(new PropertyValueFactory<Category, String>("responsiblePersonLastName"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
        parentCategoryIdCol.setCellValueFactory(new PropertyValueFactory<Category, String>("parentCategoryId"));

        categoriesTable.setItems(dList);
    }

    public void renderDisplay() throws IOException {
        Parent window1;
        Stage mainStage;
        window1 = FXMLLoader.load(getClass().getResource("xml/Category.fxml"));
        mainStage = Main.parentWindow;
        mainStage.getScene().setRoot(window1);
    }

    public void backToMain() throws IOException {
        Parent window1;
        Stage mainStage;
        window1 = FXMLLoader.load(getClass().getResource("xml/Main.fxml"));
        mainStage = Main.parentWindow;
        mainStage.getScene().setRoot(window1);

    }
    public void changeIdCellEvent(TableColumn.CellEditEvent<Category, String> edittedCell) throws SQLException {
        String query = "select id from category where id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, (idField.getText()));
        ResultSet rs = st.executeQuery();
        boolean exists = false;
        while (rs.next()) {
            exists = true;
        }

        if(exists)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Duplicate ID");
            alert.setContentText("Another record with the same ID exists!");
            alert.showAndWait();
        }
        else {
            try {
                Category categorySelected = categoriesTable.getSelectionModel().getSelectedItem();
                String queryUpd = "update category set id = ? where id = ?";
                st = con.prepareStatement(queryUpd);
                st.setString(1, edittedCell.getNewValue());
                st.setString(2, categorySelected.getId());
                st.executeUpdate();
                loadFromDatabase();
            }
            catch (SQLException e ){
                System.out.println(e);
            }
        }
    }

    public void changeResponsiblePersonFirstNameCellEvent(TableColumn.CellEditEvent<Category, String> edittedCell)
    {
        try{
        Category selected =  categoriesTable.getSelectionModel().getSelectedItem();
        String queryUpd = "update category set responsiblePersonFirstName = ? where id = ?";
        PreparedStatement st = con.prepareStatement(queryUpd);
        st.setString(1, edittedCell.getNewValue());
        st.setString(2, selected.getId());
        st.executeUpdate();
        loadFromDatabase();
        }
        catch (SQLException e ){
            System.out.println(e);
        }
    }

    public void changeResponsiblePersonLastNameCellEvent(TableColumn.CellEditEvent<Category, String> edittedCell)
    {
        try{
            Category selected =  categoriesTable.getSelectionModel().getSelectedItem();
            String queryUpd = "update category set responsiblePersonLastName = ? where id = ?";
            PreparedStatement st = con.prepareStatement(queryUpd);
            st.setString(1, edittedCell.getNewValue());
            st.setString(2, selected.getId());
            st.executeUpdate();
            loadFromDatabase();
        }
        catch (SQLException e ){
            System.out.println(e);
        }
    }

    public void changeNameCellEvent(TableColumn.CellEditEvent<Category, String> edittedCell)
    {
        try{
            Category selected =  categoriesTable.getSelectionModel().getSelectedItem();
            String queryUpd = "update category set name = ? where id = ?";
            PreparedStatement st = con.prepareStatement(queryUpd);
            st.setString(1, edittedCell.getNewValue());
            st.setString(2, selected.getId());
            st.executeUpdate();
            loadFromDatabase();
        }
        catch (SQLException e ){
            System.out.println(e);
        }
    }

    public void changeParentCategoryIdCellEvent(TableColumn.CellEditEvent<Category, String> edittedCell)
    {
        try{
            Category selected =  categoriesTable.getSelectionModel().getSelectedItem();
            String queryUpd = "update category set parentCategoryId = ? where id = ?";
            PreparedStatement st = con.prepareStatement(queryUpd);
            st.setString(1, edittedCell.getNewValue());
            st.setString(2, selected.getId());
            st.executeUpdate();
            loadFromDatabase();
        }
        catch (SQLException e ){
            System.out.println(e);
        }
    }



    public void deleteCategory()
    {
        ObservableList<Category> selectedRows;


        selectedRows = categoriesTable.getSelectionModel().getSelectedItems();
        try {
            String query = "delete from category where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            for (Category row : selectedRows) {
                st.setString(1,row.getId());
                st.executeUpdate();
            }
            loadData();
        }
        catch (Exception ignored) { }
    }


    public void addCategory() throws SQLException {
        if(idField.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding new Category");
            alert.setHeaderText("Null ID");
            alert.setContentText("ID can't be null when adding new Category!");
            alert.showAndWait();
        }
        else {
            String query = "select id from category where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, (idField.getText()));
            ResultSet rs = st.executeQuery();
            boolean exists = false;
            while (rs.next()) {
                exists = true;
            }
            if (!exists) {
                String queryUpd = "insert into category values (?, ?, ?, ?, ?)";
                st = con.prepareStatement(queryUpd);
                st.setString(1, idField.getText());
                st.setString(2, responsiblePersonFirstNameField.getText());
                st.setString(3, responsiblePersonLastNameField.getText());
                st.setString(4, nameField.getText());
                st.setString(5, parentCategoryIdField.getText());
                st.executeUpdate();
                loadFromDatabase();
                idField.setText("");
                responsiblePersonFirstNameField.setText("");
                responsiblePersonLastNameField.setText("");
                nameField.setText("");
                parentCategoryIdField.setText("");
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Duplicate ID");
                alert.setContentText("Another record with the same ID exists!");
                alert.showAndWait();
            }
        }
    }

}
