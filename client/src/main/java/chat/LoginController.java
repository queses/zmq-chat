package chat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private ChatApp app;

    @FXML
    TextField nameField;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }

    public void onEnterAction() {
        String userName = nameField.getText();
        System.out.println(userName);
        ChatPubSub.getInstance().publish(ChatConstants.EVENT_LOGIN, userName);
    }

    public void setApp(ChatApp app) {
        this.app = app;
    }

}
