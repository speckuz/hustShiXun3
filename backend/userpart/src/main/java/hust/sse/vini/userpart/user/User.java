package hust.sse.vini.userpart.user;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_generator")
    @SequenceGenerator(name = "user_generator",sequenceName = "user_seq")
    private Integer userId;
    @Column(length=32,unique = true,nullable = false)
    private String userName;

    private String nickname;
    @Column(nullable = false)
    private String passwordHash;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean gender;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte[] thumbnail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date birthday;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String location;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String signature;
    @ElementCollection
    private List<String> interests = new ArrayList<>();

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
