import dataStructures.Company;
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

public class CompanyController {

    @FXML private static ObservableList<Company> dList = FXCollections.observableArrayList();

    private static Connection con;

    @FXML private TableView<Company> companiesTable;
    @FXML private TableColumn<Company, String> idCol;
    @FXML private TableColumn<Company, String> loginCol;
    @FXML private TableColumn<Company, String> passCol;
    @FXML private TableColumn<Company, String> nameCol;
    @FXML private TableColumn<Company, String> addressCol;
    @FXML private TableColumn<Company, String> lastNameCol;
    @FXML private TableColumn<Company, String> phoneCol;
    @FXML private TableColumn<Company, String> emailCol;

    @FXML private TextField idField;
    @FXML private TextField loginField;
    @FXML private TextField passField;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;

    @FXML private Button loadButton;


    @FXML
    public static void initializeCompany (Connection cn) throws IOException {
        con = cn;
        CompanyController controller = new CompanyController();
        controller.renderDisplay();
    }


    public void loadFromDatabase()
    {
        try {
            String query = "select id, login, pass, name, lastName, address, phone, email from company";
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            dList.clear();
            while (rs.next())
            {
                Company cmp = new Company(rs.getString("login"),rs.getString("pass"),rs.getString("id"),rs.getString("name"),rs.getString("address"),rs.getString("phone"),rs.getString("lastName"),rs.getString("email"));
                dList.add(cmp);
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
        companiesTable.setEditable(true);

        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        loginCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());


        this.loadButton.setDisable(true);
        idCol.setCellValueFactory(new PropertyValueFactory<Company, String>("id"));
        loginCol.setCellValueFactory(new PropertyValueFactory<Company, String>("login"));
        passCol.setCellValueFactory(new PropertyValueFactory<Company, String>("pass"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Company, String>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Company, String>("address"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Company, String>("lastName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Company, String>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Company, String>("email"));

        companiesTable.setItems(dList);
    }

    public void renderDisplay() throws IOException {
        Parent window1;
        Stage mainStage;
        window1 = FXMLLoader.load(getClass().getResource("xml/Company.fxml"));
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

    public void changeIdCellEvent(TableColumn.CellEditEvent<Company, String> edittedCell)throws SQLException {
        String query = "select id from company where id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, (idField.getText()));
        ResultSet rs = st.executeQuery();
        boolean exists = false;
        System.out.println(rs.next());
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
                Company personSelected = companiesTable.getSelectionModel().getSelectedItem();
                String queryUpd = "update company set id = ? where id = ?";
                st = con.prepareStatement(queryUpd);
                st.setString(1, edittedCell.getNewValue());
                st.setString(2, personSelected.getId());
                st.executeUpdate();
                loadFromDatabase();
            }
            catch (SQLException e ){
                System.out.println(e);
            }
        }
    }

    public void changeLoginCellEvent(TableColumn.CellEditEvent<Company, String> edittedCell) throws SQLException {
        Company personSelected = companiesTable.getSelectionModel().getSelectedItem();
        String query = "update company set login = ? where id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, edittedCell.getNewValue());
        st.setString(2, personSelected.getId());
        st.executeUpdate();
    }

    public void changePassCellEvent(TableColumn.CellEditEvent<Company, String> edittedCell) throws SQLException {
        Company personSelected = companiesTable.getSelectionModel().getSelectedItem();
        String query = "update company set pass = ? where id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, edittedCell.getNewValue());
        st.setString(2, personSelected.getId());
        st.executeUpdate();
    }
    public void changeLastNameCellEvent(TableColumn.CellEditEvent<Company, String> edittedCell) throws SQLException {
        Company personSelected = companiesTable.getSelectionModel().getSelectedItem();
        String query = "update company set lastName = ? where id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, edittedCell.getNewValue());
        st.setString(2, personSelected.getId());
        st.executeUpdate();
    }
    public void changeNameCellEvent(TableColumn.CellEditEvent<Company, String> edittedCell) throws SQLException {
        Company personSelected = companiesTable.getSelectionModel().getSelectedItem();
        String query = "update company set name = ? where id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, edittedCell.getNewValue());
        st.setString(2, personSelected.getId());
        st.executeUpdate();
    }

    public void changeAddressCellEvent(TableColumn.CellEditEvent<Company, String> edittedCell) throws SQLException {
        Company personSelected = companiesTable.getSelectionModel().getSelectedItem();
        String query = "update company set address = ? where id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, edittedCell.getNewValue());
        st.setString(2, personSelected.getId());
        st.executeUpdate();
    }

    public void changeTelNrCellEvent(TableColumn.CellEditEvent<Company, String> edittedCell) throws SQLException {
        Company personSelected = companiesTable.getSelectionModel().getSelectedItem();
        String query = "update company set phone = ? where id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, edittedCell.getNewValue());
        st.setString(2, personSelected.getId());
        st.executeUpdate();
    }

    public void changeEmailCellEvent(TableColumn.CellEditEvent<Company, String> edittedCell) throws SQLException {
        Company personSelected = companiesTable.getSelectionModel().getSelectedItem();
        String query = "update company set email = ? where id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, edittedCell.getNewValue());
        st.setString(2, personSelected.getId());
        st.executeUpdate();
    }

   

    public void deleteCompany()
    {
        ObservableList<Company> selectedRows;

        selectedRows = companiesTable.getSelectionModel().getSelectedItems();

        try {
            String query = "delete from company where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            for (Company cmp : selectedRows) {
                st.setString(1,cmp.getId());
                st.executeUpdate();
            }
            loadData();
        }
        catch (Exception ignored) { }
    }


    public void addCompany()throws SQLException {
        if(idField.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding new employee");
            alert.setHeaderText("Null ID");
            alert.setContentText("ID can't be null when adding new employee!");
            alert.showAndWait();
        }
        else {
            String query = "select id from company where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, (idField.getText()));
            ResultSet rs = st.executeQuery();
            boolean exists = false;
            while (rs.next()) {
                exists = true;
            }

            if (!exists) {
                String queryUpd = "insert into company values (?, ?, ?, ?, ?, ?, ?, ?)";
                st = con.prepareStatement(queryUpd);
                st.setString(1, idField.getText());
                st.setString(2, loginField.getText());
                st.setString(3,  passField.getText());
                st.setString(4, nameField.getText());
                st.setString(5, addressField.getText());
                st.setString(6, phoneField.getText());
                st.setString(7, lastNameField.getText());
                st.setString(8, emailField.getText());
                st.executeUpdate();
                loadFromDatabase();
                idField.setText("");
                loginField.setText("");
                passField.setText("");
                nameField.setText("");
                lastNameField.setText("");
                phoneField.setText("");
                emailField.setText("");
                addressField.setText("");
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

    public static ObservableList<Company> getList(){
        return dList;
    }


}
