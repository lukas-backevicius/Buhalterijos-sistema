import dataStructures.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Scanner;


public class MainController {

    private static Connection con;
    @FXML
    public Button closeButton;

    public static void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/buhalterija", "buhalterija", "test");
            System.out.println("Connected to database!");
             }
        catch (Exception e)
             {
                 System.out.println(e);
            System.out.println("Couldn't connect to database!");
             }
    }

    @FXML
    private void toEmployee() throws IOException{
        EmployeeController.initializeEmpoyee(con);

    }

    @FXML
    private void toCompany() throws IOException{
        CompanyController.initializeCompany(con);
    }

    @FXML
    private void toCategory() throws IOException{
        CategoryController.initializeCategory(con);
    }

    @FXML
    private void toIncome() throws IOException{
        IncomeController.initializeIncome(con);
    }

    @FXML
    private void toExpense() throws IOException{
        ExpenseController.initializeExpense(con);
    }


    @FXML
    public void showAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("About");
        alert.setContentText("Program created by: Lukas Backevičius, Prif-18/4, 2020");
        alert.showAndWait();
    }

    @FXML
    public void showEdit(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About edit");
        alert.setHeaderText("How to edit?");
        alert.setContentText("Double click on any field to begin editing. When finished press enter!");
        alert.showAndWait();
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event)  {
        try {
            con.close();
            System.out.println("Connection to database closed!");
        }
        catch (SQLException e)
        {
            System.out.println("Couldn't close connection to database!");
        }

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    public void readFile(){
        try {
            String current = "";
            String query;
            PreparedStatement st;
            File myObj = new File("sampleData.txt");
            int alertType = 0;
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String[] enteredValues = myReader.nextLine().split(";");

                if (enteredValues.length == 1) {
                    current = enteredValues[0];
                }

                if (current.equals("employee") && enteredValues.length != 1) {
                    query = "select id from employee where id = ?";
                    st = con.prepareStatement(query);
                    st.setString(1, enteredValues[0]);
                    ResultSet rs = st.executeQuery();
                    boolean exists = false;
                    while (rs.next()) {
                        exists = true;
                    }
                    if (!exists) {
                        query = "insert into employee values (?, ?, ?, ?, ?, ?, ?)";
                        st = con.prepareStatement(query);
                        st.setString(1, enteredValues[0]);
                        st.setString(2, enteredValues[1]);
                        st.setString(3, enteredValues[2]);
                        st.setString(4, enteredValues[3]);
                        st.setString(5, enteredValues[4]);
                        st.setString(6, enteredValues[5]);
                        st.setString(7, enteredValues[6]);
                        st.executeUpdate();
                    } else {
                        alertType = 1;
                    }
                }

                if (current.equals("company") && enteredValues.length != 1) {
                    query = "select id from company where id = ?";
                    st = con.prepareStatement(query);
                    st.setString(1, enteredValues[0]);
                    ResultSet rs = st.executeQuery();
                    boolean exists = false;
                    while (rs.next()) {
                        exists = true;
                    }
                    if (!exists) {
                        query = "insert into company values (?, ?, ?, ?, ?, ?, ?, ?)";
                        st = con.prepareStatement(query);
                        st.setString(1, enteredValues[0]);
                        st.setString(2, enteredValues[1]);
                        st.setString(3, enteredValues[2]);
                        st.setString(4, enteredValues[3]);
                        st.setString(5, enteredValues[4]);
                        st.setString(6, enteredValues[5]);
                        st.setString(7, enteredValues[6]);
                        st.setString(8, enteredValues[7]);
                        st.executeUpdate();
                    } else {
                        alertType = 1;
                    }
                }
            }
            myReader.close();

            if (alertType == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText("Success!");
            alert.setContentText("Data was successfully loaded!");
            alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Error!");
                alert.setContentText("Data with the same ID already exists in database table!");
                alert.showAndWait();
            }
        } catch (FileNotFoundException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error!");
            alert.setContentText("File was not found!");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void printFile() {
        try {
            PrintWriter myWriter = new PrintWriter("savedData.txt");
            myWriter.println("employee;");
            ObservableList<Employee> darbList = EmployeeController.getList();
            ObservableList<Company> imList = CompanyController.getList();

            for(Employee d : darbList) {
                myWriter.println(d.getLogin() + ";" + d.getPass() + ";" + d.getId() + ";" + d.getFirstName() + ";" + d.getLastName() + ";" + d.getPhone() + ";" + d.getEmail() + ";");
            }

            myWriter.println("category;");

            for(Company i : imList) {
                myWriter.println( i.getLogin() + ";" + i.getPass() + ";" + i.getId() + ";" + i.getName() + ";" + i.getAddress() + ";" + i.getPhone() + ";" + i.getLastName()+ ";" +  i.getEmail() + ";");
            }
            myWriter.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText("Success!");
            alert.setContentText("Data was successfully written to file!");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("Error!");
            alert.setContentText("Some kind of error occured!");
            alert.showAndWait();
            e.printStackTrace();
        }
    }


}
