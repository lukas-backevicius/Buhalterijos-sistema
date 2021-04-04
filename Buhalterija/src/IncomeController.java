import dataStructures.Category;
import dataStructures.Employee;
import dataStructures.Expense;
import dataStructures.Income;
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

public class IncomeController {

    @FXML private static ObservableList<Income>  dList = FXCollections.observableArrayList();

    private static Connection con;

    @FXML private TableView<Income> incomesTable;
    @FXML private TableColumn<Income, String> idCol;
    @FXML private TableColumn<Income, String> categoryIdCol;
    @FXML private TableColumn<Income, String> howUsedCol;
    @FXML private TableColumn<Income, String> amountCol;


    @FXML private TextField idField;
    @FXML private TextField categoryIdField;
    @FXML private TextField howUsedField;
    @FXML private TextField amountField;

    @FXML private Button loadButton;

    @FXML
    public static void initializeIncome (Connection cn) throws IOException {
        con = cn;
        IncomeController controller = new IncomeController();
        controller.renderDisplay();
    }

    public void loadFromDatabase()
    {
        try {
            String query = "select id, categoryId, howUsed, amount from income";
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            dList.clear();
            while (rs.next())
            {
                Income row = new Income(rs.getString("id"),rs.getString("categoryId"),rs.getString("howUsed"),rs.getString("amount"));
                dList.add(row);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }

    public void loadData()
    {
        loadFromDatabase();

        incomesTable.setEditable(true);

        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        categoryIdCol.setCellFactory(TextFieldTableCell.forTableColumn());
        howUsedCol.setCellFactory(TextFieldTableCell.forTableColumn());
        amountCol.setCellFactory(TextFieldTableCell.forTableColumn());

        this.loadButton.setDisable(true);
        idCol.setCellValueFactory(new PropertyValueFactory<Income, String>("id"));
        categoryIdCol.setCellValueFactory(new PropertyValueFactory<Income, String>("categoryId"));
        howUsedCol.setCellValueFactory(new PropertyValueFactory<Income, String>("howUsed"));
        amountCol.setCellValueFactory(new PropertyValueFactory<Income, String>("amount"));

        incomesTable.setItems(dList);
    }

    public void renderDisplay() throws IOException {
        Parent window1;
        Stage mainStage;
        window1 = FXMLLoader.load(getClass().getResource("xml/Income.fxml"));
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

    public void changeIdCellEvent(TableColumn.CellEditEvent<Income, String> edittedCell) throws SQLException {
        String query = "select id from income where id = ?";
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
                Income selected = incomesTable.getSelectionModel().getSelectedItem();
                String queryUpd = "update income set id = ? where id = ?";
                st = con.prepareStatement(queryUpd);
                st.setString(1, edittedCell.getNewValue());
                st.setString(2, selected.getId());
                st.executeUpdate();
                loadFromDatabase();
            }
            catch (SQLException e ){
                System.out.println(e);
            }
        }
    }

    public void changeCategoryIdCellEvent(TableColumn.CellEditEvent<Income, String> edittedCell)
    {
        try{
            Income selected =  incomesTable.getSelectionModel().getSelectedItem();
            String queryUpd = "update income set categoryId = ? where id = ?";
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

    public void changeHowUsedCellEvent(TableColumn.CellEditEvent<Income, String> edittedCell)
    {
        try{
            Income selected =  incomesTable.getSelectionModel().getSelectedItem();
            String queryUpd = "update income set howUsed = ? where id = ?";
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

    public void changeAmountCellEvent(TableColumn.CellEditEvent<Income, String> edittedCell)
    {
        try{
            Income selected =  incomesTable.getSelectionModel().getSelectedItem();
            String queryUpd = "update income set amount = ? where id = ?";
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




    public void deleteIncome()
    {
        if(idField.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error deleting new income");
            alert.setHeaderText("Null ID");
            alert.setContentText("Please input ID to delete income");
            alert.showAndWait();
        }
        else {
            ObservableList<Income> selectedRows;
            selectedRows = incomesTable.getSelectionModel().getSelectedItems();
            try {
                String query = "delete from income where id = ?";
                PreparedStatement st = con.prepareStatement(query);
                for (Income row : selectedRows) {
                    st.setString(1, row.getId());
                    st.executeUpdate();
                }
                loadData();
            } catch (Exception ignored) {
            }
        }
    }


    public void addIncome() throws SQLException {
        if(idField.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding new income");
            alert.setHeaderText("Null ID");
            alert.setContentText("ID can't be null when adding new Income!");
            alert.showAndWait();
        }
        else if(amountField.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding new income");
            alert.setContentText("Please input income");
            alert.showAndWait();
        }

        else {
            String query = "select id from income where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, (idField.getText()));
            ResultSet rs = st.executeQuery();
            boolean exists = false;
            while (rs.next()) {
                exists = true;
            }
            if (!exists) {
                String queryUpd = "insert into income values (?, ?, ?, ?)";
                st = con.prepareStatement(queryUpd);
                st.setString(1, idField.getText());
                st.setString(2, categoryIdField.getText());
                st.setString(3, howUsedField.getText());
                st.setString(4, amountField.getText());
                st.executeUpdate();
                loadFromDatabase();
                idField.setText("");
                categoryIdField.setText("");
                howUsedField.setText("");
                amountField.setText("");
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Duplicate ID");
                alert.setContentText("Another record with the same ID exists!");
                alert.showAndWait();
            }
        }
    }

}

