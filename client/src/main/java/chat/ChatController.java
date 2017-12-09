package chat;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import chat.message.Message;
import chat.message.MessageCellFactory;
import utils.PubSub;
import zero.ZeroClient;

import java.net.URL;
import java.util.ResourceBundle;

//  Freelance client - Model 1
//  Uses REQ socket to query one or more services
public class ChatController implements Initializable {
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
        Dialog<String> dialog = new Dialog<>();
        messages = FXCollections.observableArrayList();
        messagesList.setCellFactory((listView) -> new MessageCellFactory());
        messagesList.setItems(messages);

        ChatPubSub.getInstance().subscribe(ChatConstants.EVENT_MESSAGE_RECV, message -> {
            onMessage(message.toString());
        });
    }

//    @FXML
//    public void exitApplication(ActionEvent event) {
//        Platform.exit();
//    }

    public void onClose() {
        ChatPubSub.getInstance().publish(ChatConstants.EVENT_CLOSE, "");
        Platform.exit();
    }

    public void onSendAction() {
        String messageText = chatInput.getCharacters().toString();
        Message message = new Message(messageText, userName);
        message.setCurrentDate();
        String json = new Gson().toJson(message);
        System.out.println(json);
        ChatPubSub.getInstance().publish(ChatConstants.EVENT_MESSAGE_SENT, json);
        chatInput.clear();
    }

    public void onMessage(String eventText) {
        final String message = eventText.replace(String.format("[%s]", ChatConstants.MAIN_TOPIC), "");
        System.out.println("@ " + message);
        noMessagesLabel.setVisible(false);
        Message messageObj = new Gson().fromJson(message, Message.class);
        messages.add(messageObj);
//        messages.add(new Gson().fromJson(message, Message.class));
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
