import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

//  Freelance client - Model 1
//  Uses REQ socket to query one or more services
public class Main extends Application {
    private ChatController chatController;
    private LoginController loginController;
    private Scene loginScene, chatScene;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage loPrimaryStage) throws Exception {
        createChatController();
        createLoginController();
        primaryStage = loPrimaryStage;
        primaryStage.setTitle("Zero Chat");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        chatController.onClose();
    }

    public void createChatController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        chatController = loader.getController();
        chatScene = new Scene(root);
    }

    public void createLoginController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        loginController = loader.getController();
        loginController.setApp(this);
        loginScene = new Scene(root);
    }

    public void onEnterUsername(String name) {
        chatController.setUserName(name);
        primaryStage.setScene(chatScene);
    }

}