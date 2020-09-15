package hust.sse.vini.userpart.friend;

import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
public class FriendRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "friend_relation_generator")
    @SequenceGenerator(name = "friend_relation_generator",sequenceName = "friend_relation_seq")
    private Integer friendRelationId;

    @Column(nullable = false)
    private Integer userUserId;

    @Column(nullable = false)
    private Integer friendUserId;

    private String friendAlias;

    @Column(nullable = false)
    private String friendGroupName="我的Vini";

    private Boolean pendingConfirm=false;

    public Integer getFriendRelationId() {
        return friendRelationId;
    }

    public void setFriendRelationId(Integer friendRelationId) {
        this.friendRelationId = friendRelationId;
    }

    public Integer getUserUserId() {
        return userUserId;
    }

    public void setUserUserId(Integer userUserId) {
        this.userUserId = userUserId;
    }

    public Integer getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(Integer friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getFriendAlias() {
        return friendAlias;
    }

    public void setFriendAlias(String friendAlias) {
        this.friendAlias = friendAlias;
    }

    public String getFriendGroupName() {
        return friendGroupName;
    }

    public void setFriendGroupName(String friendGroupName) {
        this.friendGroupName = friendGroupName;
    }

    public Boolean getPendingConfirm() {
        return pendingConfirm;
    }

    public void setPendingConfirm(Boolean pendingConfirm) {
        this.pendingConfirm = pendingConfirm;
    }
}
