package binhdang.ueh.chatify;

public class Message {
    String message;
    String time;
    String senderName;

    public Message(String message, String time, String senderName) {
        this.message = message;
        this.time = time;
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
