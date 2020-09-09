package hust.sse.vini.userpart.user;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer userId;
    @Column(length=32,unique = true,nullable = false)
    private String userName;
    @Column(nullable = false)
    private String passwordHash;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean gender;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte[] thumbnail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date birthday;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String location;
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
}
