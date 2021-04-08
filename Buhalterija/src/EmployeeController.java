import dataStructures.Employee;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeeController {
    @FXML
    private static ObservableList<Employee> dList = FXCollections.observableArrayList();

    private static Connection con;

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> idCol;
    @FXML
    private TableColumn<Employee, String> loginCol;
    @FXML
    private TableColumn<Employee, String> passCol;
    @FXML
    private TableColumn<Employee, String> firstNameCol;
    @FXML
    private TableColumn<Employee, String> lastNameCol;
    @FXML
    private TableColumn<Employee, String> phoneCol;
    @FXML
    private TableColumn<Employee, String> emailCol;

    @FXML
    private TextField idField;
    @FXML
    private TextField loginField;
    @FXML
    private TextField passField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private Button loadButton;
    @FXML
    private Button addNewEmployeeButton;

    private InvalidationListener changeListener;


    public boolean areAllFieldsFilled() {
        return !(
                idField.getText().equals("")
                        || loginField.getText().equals("")
                        || passField.getText().equals("")
                        || firstNameField.getText().equals("")
                        || lastNameField.getText().equals("")
                        || phoneField.getText().equals("")
                        || emailField.getText().equals("")
        );
    }

    @FXML
    public void initialize() {
        addNewEmployeeButton.setDisable(!areAllFieldsFilled());
        changeListener = (event) -> addNewEmployeeButton.setDisable(!areAllFieldsFilled());

        idField.textProperty().addListener(changeListener);
        loginField.textProperty().addListener(changeListener);
        passField.textProperty().addListener(changeListener);
        firstNameField.textProperty().addListener(changeListener);
        lastNameField.textProperty().addListener(changeListener);
        phoneField.textProperty().addListener(changeListener);
        emailField.textProperty().addListener(changeListener);

        firstNameField.setTextFormatter(pureTextFormatter());
        lastNameField.setTextFormatter(pureTextFormatter());
        idField.setTextFormatter(noSpacesFormatter());
        loginField.setTextFormatter(noSpacesFormatter());
        passField.setTextFormatter(noSpacesFormatter());
        phoneField.setTextFormatter(noSpacesFormatter());
        emailField.setTextFormatter(noSpacesFormatter());
    }

    private TextFormatter<String> pureTextFormatter() {
        return new TextFormatter<>(change -> {
            String text = change.getText();
            if (text.matches("[0-9]+") || text.matches("\\s")) {
                return null;
            }

            return change;
        });
    }

    private TextFormatter<String> noSpacesFormatter() {
        return new TextFormatter<>(change -> {
            String text = change.getText();
            if (text.matches("\\s")) {
                return null;
            }

            return change;
        });
    }

    @FXML
    public static void initializeEmpoyee(Connection cn) throws IOException {
        con = cn;
        EmployeeController controller = new EmployeeController();
        controller.renderDisplay();
    }


    public void loadFromDatabase() {
        try {
            String query = "select id, login, pass, firstName, lastName, phone, email from employee";
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            dList.clear();
            while (rs.next()) {
                Employee emp = new Employee(rs.getString("login"), rs.getString("pass"), rs.getString("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("phone"), rs.getString("email"));
                dList.add(emp);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void deleteEmployee() {
        ObservableList<Employee> selectedRows;

        selectedRows = employeeTable.getSelectionModel().getSelectedItems();

        try {
            String query = "delete from employee where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            for (Employee employee : selectedRows) {
                st.setString(1, employee.getId());
                st.executeUpdate();
            }
            loadData();
        } catch (Exception ignored) {
        }
    }


    public void addEmployee() throws SQLException {
        if (idField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error adding new employee");
            alert.setHeaderText("Null ID");
            alert.setContentText("ID can't be null when adding new employee!");
            alert.showAndWait();
        } else {
            String query = "select id from employee where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, (idField.getText()));
            ResultSet rs = st.executeQuery();
            boolean exists = false;
            while (rs.next()) {
                exists = true;
            }

            if (!exists) {
                String queryUpd = "insert into employee values (?, ?, ?, ?, ?, ?, ?)";
                st = con.prepareStatement(queryUpd);
                st.setString(1, idField.getText());
                st.setString(2, loginField.getText());
                st.setString(3, passField.getText());
                st.setString(4, firstNameField.getText());
                st.setString(5, lastNameField.getText());
                st.setString(6, phoneField.getText());
                st.setString(7, emailField.getText());
                st.executeUpdate();
                loadFromDatabase();
                idField.setText("");
                loginField.setText("");
                passField.setText("");
                firstNameField.setText("");
                lastNameField.setText("");
                phoneField.setText("");
                emailField.setText("");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Duplicate ID");
                alert.setContentText("Another record with the same ID exists!");
                alert.showAndWait();
            }
        }
    }

    public void loadData() {
        loadFromDatabase();
        employeeTable.setEditable(true);

        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        loginCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());


        this.loadButton.setDisable(true);
        idCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("id"));
        loginCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("login"));
        passCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("pass"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));

        employeeTable.setItems(dList);
    }


    public void renderDisplay() throws IOException {
        Parent window1;
        Stage mainStage;
        window1 = FXMLLoader.load(getClass().getResource("xml/Employee.fxml"));
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

    public void changeIdCellEvent(TableColumn.CellEditEvent<Employee, String> edittedCell) throws SQLException {
        String query = "select id from employee where id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, (idField.getText()));
        ResultSet rs = st.executeQuery();
        boolean exists = false;
        while (rs.next()) {
            exists = true;
        }

        if (exists) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Duplicate ID");
            alert.setContentText("Another record with the same ID exists!");
            alert.showAndWait();
        } else {
            try {
                Employee personSelected = employeeTable.getSelectionModel().getSelectedItem();
                String queryUpd = "update employee set id = ? where id = ?";
                st = con.prepareStatement(queryUpd);
                st.setString(1, edittedCell.getNewValue());
                st.setString(2, personSelected.getId());
                st.executeUpdate();
                loadFromDatabase();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public void changeLoginCellEvent(TableColumn.CellEditEvent<Employee, String> edittedCell) throws SQLException {
        Employee personSelected = employeeTable.getSelectionModel().getSelectedItem();
        String query = "update employee set login = ? where id = ?";
        PreparedStatement st = con.prepareStatement(query);
        st.setString(1, edittedCell.getNewValue());
        st.setString(2, personSelected.getId());
        st.executeUpdate();
        loadFromDatabase();
    }

    public void changePassCellEvent(TableColumn.CellEditEvent<Employee, String> edittedCell) {
        try {
            Employee personSelected = employeeTable.getSelectionModel().getSelectedItem();
            String query = "update employee set pass = ? where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, edittedCell.getNewValue());
            st.setString(2, personSelected.getId());
            st.executeUpdate();
            loadFromDatabase();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void changeFirstNameCellEvent(TableColumn.CellEditEvent<Employee, String> edittedCell) {
        try {
            Employee personSelected = employeeTable.getSelectionModel().getSelectedItem();
            String query = "update employee set firstName = ? where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, edittedCell.getNewValue());
            st.setString(2, personSelected.getId());
            st.executeUpdate();
            loadFromDatabase();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void changeLastNameCellEvent(TableColumn.CellEditEvent<Employee, String> edittedCell) {
        try {
            Employee personSelected = employeeTable.getSelectionModel().getSelectedItem();
            String query = "update employee set lastName = ? where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, edittedCell.getNewValue());
            st.setString(2, personSelected.getId());
            st.executeUpdate();
            loadFromDatabase();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void changePhoneCellEvent(TableColumn.CellEditEvent<Employee, String> edittedCell) {
        try {
            Employee personSelected = employeeTable.getSelectionModel().getSelectedItem();
            String query = "update employee set phone = ? where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, edittedCell.getNewValue());
            st.setString(2, personSelected.getId());
            st.executeUpdate();
            loadFromDatabase();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void changeEmailCellEvent(TableColumn.CellEditEvent<Employee, String> edittedCell) {
        try {
            Employee personSelected = employeeTable.getSelectionModel().getSelectedItem();
            String query = "update employee set email = ? where id = ?";
            PreparedStatement st = con.prepareStatement(query);
            st.setString(1, edittedCell.getNewValue());
            st.setString(2, personSelected.getId());
            st.executeUpdate();
            loadFromDatabase();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static ObservableList<Employee> getList() {
        return dList;
    }
}
