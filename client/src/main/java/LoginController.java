import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Main app;

    @FXML
    TextField nameField;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }

    public void onEnterAction() {
        String userName = nameField.getText();
        System.out.println(userName);
        if (this.app != null) {
            app.onEnterUsername(userName);
        }
    }

    public void setApp(Main app) {
        this.app = app;
    }

}
