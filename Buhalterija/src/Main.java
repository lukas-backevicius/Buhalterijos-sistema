import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {
    public static Stage parentWindow;

    @Override
    public void start(Stage stage) throws Exception{
        parentWindow = stage;
        Parent root = FXMLLoader.load(getClass().getResource("xml/Main.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Accounting System");
        stage.show();
        MainController.connectDatabase();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
