package binhdang.ueh.chatify;

public class ConversationBar {
    private String id;
    private String title;
    private String photoSrc;
    private String lastestMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoSrc() {
        return photoSrc;
    }

    public void setPhotoSrc(String photoSrc) {
        this.photoSrc = photoSrc;
    }

    public String getLastestMessage() {
        return lastestMessage;
    }

    public void setLastestMessage(String lastestMessage) {
        this.lastestMessage = lastestMessage;
    }

    public ConversationBar(String id, String title, String photoSrc, String lastestMessage){
        setId(id);
        setTitle(title);
        setPhotoSrc(photoSrc);
        setLastestMessage(lastestMessage);
    }
}
