package Inf;

public class Moment {
    String id;
    String userName;
    String content;
    String time;

    public Moment(String userName,String id, String content, String time) {
        this.userName = userName;
        this.id = id;
        this.content = content;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
