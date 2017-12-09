import java.util.Scanner;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

//  Freelance client - Model 1
//  Uses REQ socket to query one or more services
public class Main extends Application {
    MainController mainController;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        mainController = loader.getController();
        primaryStage.setTitle("Zero Chat");
        primaryStage.setScene(new Scene(root, 645, 375));
        primaryStage.show();
    }

    @Override
    public void stop() {
        mainController.onClose();
    }

}