package message;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class MessageCellFactory extends ListCell<Message> {
    private Node graphic;
    private final MessageController controller;

    public MessageCellFactory() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/message.fxml"));
        try {
            graphic = loader.load();
        } catch (Exception err) {
            System.out.println(err.toString());
            throw new RuntimeException(err);
        }
        controller = loader.getController();
    }

    @Override
    protected void updateItem(Message item, boolean isEmpty) {
        super.updateItem(item, isEmpty);
        if (isEmpty || item == null) {
            setGraphic(null);
        } else {
            controller.updateItem(item);
            setGraphic(graphic);
        }
    }
}
