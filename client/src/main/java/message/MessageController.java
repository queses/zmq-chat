package message;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageController implements Initializable {
    @FXML
    Text text;
    @FXML
    Text userName;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        text.setText("");
    }

    public void updateItem(Message message) {
        text.setText(message.text);
        userName.setText(message.username);
    }
}
