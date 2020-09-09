package hust.sse.vini.userpart.friend;

import com.fasterxml.jackson.annotation.JsonInclude;

public class PersonalFriend {
    private Integer userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String alias;

    private String groupName="我的Vini";

    public PersonalFriend() {
    }

    public PersonalFriend(Integer userId, String alias, String groupName) {
        this.userId = userId;
        this.alias = alias;
        this.groupName = groupName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
