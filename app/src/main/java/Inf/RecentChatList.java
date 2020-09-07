package Inf;

public class RecentChatList {
    private String userName;
    private String recentText;
    private String id;
    public RecentChatList(String id,String userName,String recentText){
        this.id = id;
        this.userName = userName;
        this.recentText = recentText;
    }


    public String getId() {
        return id;
    }
    public String getUserName() {
        return userName;
    }

    public String getRecentText() {
        return recentText;
    }
}
