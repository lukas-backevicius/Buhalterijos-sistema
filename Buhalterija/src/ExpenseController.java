

import dataStructures.Expense;
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

public class ExpenseController {

    @FXML private static ObservableList<Expense> dList  = FXCollections.observableArrayList();

    private static Connection con;

    @FXML private TableView<Expense> expensesTable;
    @FXML private TableColumn<Expense, String> idCol;
    @FXML private TableColumn<Expense, String> categoryIdCol;
    @FXML private TableColumn<Expense, String> howAcquiredCol;
    @FXML private TableColumn<Expense, String> amountCol;


    @FXML private TextField idField;
    @FXML private TextField categoryIdField;
    @FXML private TextField howAcquiredField;
    @FXML private TextField amountField;

    @FXML private Button loadButton;

    @FXML
    public static void initializeExpense (Connection cn) throws IOException {
        con = cn;
        ExpenseController controller = new ExpenseController();
        controller.renderDisplay();
    }


    public void loadFromDatabase()
    {
        try {
            String query = "select id, categoryId, howAcquired, amount from expense";
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            dList.clear();
            while (rs.next())
            {
                Expense row = new Expense(rs.getString("id"),rs.getString("categoryId"),rs.getString("howAcquired"),rs.getString("amount"));
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

        expensesTable.setEditable(true);

        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        categoryIdCol.setCellFactory(TextFieldTableCell.forTableColumn());
        howAcquiredCol.setCellFactory(TextFieldTableCell.forTableColumn());
        amountCol.setCellFactory(TextFieldTableCell.forTableColumn());

        this.loadButton.setDisable(true);
        idCol.setCellValueFactory(new PropertyValueFactory<Expense, String>("id"));
        categoryIdCol.setCellValueFactory(new PropertyValueFactory<Expense, String>("categoryId"));
        howAcquiredCol.setCellValueFactory(new PropertyValueFactory<Expense, String>("howAcquired"));
        amountCol.setCellValueFactory(new PropertyValueFactory<Expense, String>("amount"));

        expensesTable.setItems(dList);
    }

    public void renderDisplay() throws IOException {
        Parent window1;
        Stage mainStage;
        window1 = FXMLLoader.load(getClass().getResource("xml/Expense.fxml"));
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

    public void changeIdCellEvent(TableColumn.CellEditEvent<Expense, String> edittedCell) throws SQLException {
        String query = "select id from expense where id = ?";
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
                Expense selected = expensesTable.getSelectionModel().getSelectedItem();
                String queryUpd = "update expense set id = ? where id = ?";
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

    public void changeCategoryIdCellEvent(TableColumn.CellEditEvent<Expense, String> edittedCell)
    {
        try{
            Expense selected =  expensesTable.getSelectionModel().getSelectedItem();
            String queryUpd = "update expense set categoryId = ? where id = ?";
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

    public void changeHowAcquiredCellEvent(TableColumn.CellEditEvent<Expense, String> edittedCell)
    {
        try{
            Expense selected =  expensesTable.getSelectionModel().getSelectedItem();
            String queryUpd = "update expense set howAcquired = ? where id = ?";
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

    public void changeAmountCellEvent(TableColumn.CellEditEvent<Expense, String> edittedCell)
    {
        try{
            Expense selected =  expensesTable.getSelectionModel().getSelectedItem();
            String queryUpd = "update expense set amount = ? where id = ?";
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




    public void deleteExpense()
    {
        ObservableList<Expense> selectedRows;

        selectedRows = expensesTable.getSelectionModel().getSelectedItems();
        try {
            String query = "delete from expense where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            for (Expense row : selectedRows) {
                st.setString(1,row.getId());
                st.executeUpdate();
            }
            loadData();
        }
        catch (Exception ignored) { }
    }


    public void addExpense() throws SQLException {
        if(idField.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding new Expense");
            alert.setHeaderText("Null ID");
            alert.setContentText("ID can't be null when adding new Expense!");
            alert.showAndWait();
        }
        else {
            String query = "select id from expense where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, (idField.getText()));
            ResultSet rs = st.executeQuery();
            boolean exists = false;
            while (rs.next()) {
                exists = true;
            }
            if (!exists) {
                String queryUpd = "insert into expense values (?, ?, ?, ?)";
                st = con.prepareStatement(queryUpd);
                st.setString(1, idField.getText());
                st.setString(2, categoryIdField.getText());
                st.setString(3, howAcquiredField.getText());
                st.setString(4, amountField.getText());
                st.executeUpdate();
                loadFromDatabase();
                idField.setText("");
                categoryIdField.setText("");
                howAcquiredField.setText("");
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
