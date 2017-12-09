package chat.message;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class MessageController implements Initializable {
    @FXML
    Text text;
    @FXML
    Text userName;
    @FXML
    Text date;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        text.setText("");
    }

    public void updateItem(Message message) {
        text.setText(message.text);
        userName.setText(message.username);
        date.setText(new SimpleDateFormat("HH:mm").format(message.date));
    }
}
