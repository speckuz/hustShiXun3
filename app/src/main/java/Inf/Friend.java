package Inf;

public class Friend {
    private String friendName;
    private String friendText;
    public Friend(String friendName,String friendText){
        this.friendName = friendName;
        this.friendText = friendText;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getFriendText() {
        return friendText;
    }
}
