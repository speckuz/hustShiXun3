package hust.sse.vini.userinterest;

import javax.persistence.*;

@Entity
public class UserInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userInterestId;
    @Column(nullable = false)
    private Integer userId;

    private String singleInterest;

    public Integer getUserInterestId() {
        return userInterestId;
    }

    public void setUserInterestId(Integer userInterestId) {
        this.userInterestId = userInterestId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSingleInterest() {
        return singleInterest;
    }

    public void setSingleInterest(String singleInterest) {
        this.singleInterest = singleInterest;
    }
}
