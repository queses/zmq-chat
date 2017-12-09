import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

//  Freelance client - Model 1
//  Uses REQ socket to query one or more services
public class MainController implements Initializable {

    private ZeroClient zeroClient;

    @FXML
    private TextField chatInput;
    @FXML
    private ListView<String> messagesList;
    @FXML
    private Label label;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        zeroClient = new ZeroClient(this);
//        messages = new Ob
//        messagesList.setItems()
    }

//    @FXML
//    public void exitApplication(ActionEvent event) {
//        Platform.exit();
//    }

    void onClose() {
        zeroClient.close();
        Platform.exit();
    }

    public void onClicked() {
        String messageText = chatInput.getCharacters().toString();
        System.out.println(messageText);
        zeroClient.send(messageText);
    }

    void onSocketMessage(String messageText) {
        System.out.println("@ " + messageText);
        label.setText(messageText);
    }

}
