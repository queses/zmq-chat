package chat.message;

import java.util.Date;

public class Message {
    public String text;
    public String username;
    public Date date;

    public Message(String text, String username) {
        this.text = text;
        this.username = username;
    }

    public void setCurrentDate() {
        this.date = new Date();
    }
}
