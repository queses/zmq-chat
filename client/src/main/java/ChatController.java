import com.google.gson.Gson;
import com.google.gson.internal.Excluder;
import com.sun.javafx.collections.ImmutableObservableList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import message.Message;
import message.MessageCellFactory;
import utils.GsonTypes;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

//  Freelance client - Model 1
//  Uses REQ socket to query one or more services
public class ChatController implements Initializable {

    private ZeroClient zeroClient;

    @FXML
    private TextField chatInput;
    @FXML
    private ListView<Message> messagesList;
    private ObservableList<Message> messages;
    @FXML
    private Text noMessagesLabel;

    private String userName = "User";

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        zeroClient = new ZeroClient(this);
        Dialog<String> dialog = new Dialog<>();
        messages = FXCollections.observableArrayList();
        messagesList.setCellFactory((listView) -> new MessageCellFactory());
        messagesList.setItems(messages);
    }

//    @FXML
//    public void exitApplication(ActionEvent event) {
//        Platform.exit();
//    }

    void onClose() {
        zeroClient.close();
        Platform.exit();
    }

    public void onSendAction() {
        String messageText = chatInput.getCharacters().toString();
        Message message = new Message(messageText, userName);
        String json = new Gson().toJson(message);
        System.out.println(json);
        zeroClient.send(json);
        chatInput.clear();
    }

    void onSocketMessage(String eventText) {
        System.out.println("@ " + eventText);
        noMessagesLabel.setVisible(false);
        messages.add(new Gson().fromJson(eventText, Message.class));
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
