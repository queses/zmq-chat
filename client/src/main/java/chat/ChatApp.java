package chat;

import java.io.IOException;

import chat.ChatController;
import chat.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import utils.PubSub;
import zero.ZeroClient;

public class ChatApp extends Application {
    private ChatWorker chatWorker;
    private ChatController chatController;
    private LoginController loginController;
    private Scene loginScene, chatScene;
    private Stage primaryStage;

    @Override
    public void start(Stage loPrimaryStage) throws Exception {
        createLoginController();
        createChatController();
        chatWorker = new ZeroClient();
        chatWorker.init();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat/fxml/chat.fxml"));
        Parent root = loader.load();
        chatController = loader.getController();
        chatScene = new Scene(root);
    }

    public void createLoginController() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat/fxml/login.fxml"));
        Parent root = loader.load();
        loginController = loader.getController();

        ChatPubSub.getInstance().subscribe(ChatConstants.EVENT_LOGIN, (nameObj) -> {
            onEnterUsername(nameObj.toString());
        });

        loginScene = new Scene(root);
    }

    public void onEnterUsername(String name) {
        chatController.setUserName(name);
        primaryStage.setScene(chatScene);
    }

}