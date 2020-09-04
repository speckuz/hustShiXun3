package hust.sse.vini.userinterest;

import java.util.List;

public class UserInterests {
    private Integer userId;

    private List<String> interests;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
