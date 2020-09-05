package Inf;

public class RecentChatList {
    private String userName;
    private String recentText;
    public RecentChatList(String userName,String recentText){
        this.userName = userName;
        this.recentText = recentText;
    }

    public String getUserName() {
        return userName;
    }

    public String getRecentText() {
        return recentText;
    }
}
