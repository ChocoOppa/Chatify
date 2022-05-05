package binhdang.ueh.chatify;

public class ConversationBar {
    private String id;
    private String conversationName;
    private String title;
    private String photoSrc;
    private String lastMessage;
    private String lastMessageTime;

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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public ConversationBar(String id, String conversationName, String title, String photoSrc, String lastMessage, String lastMessageTime){
        setId(id);
        setConversationName(conversationName);
        setTitle(title);
        setPhotoSrc(photoSrc);
        setLastMessage(lastMessage);
        setLastMessageTime(lastMessageTime);
    }
}
