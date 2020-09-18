package hust.sse.vini.userpart.group;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "vini_groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "group_generator")
    @SequenceGenerator(name = "group_generator",initialValue = 1,allocationSize = 1,sequenceName = "group_seq")
    private Integer id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(nullable = false)
    private String viniGroupName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer founderId;
    @ElementCollection
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> members;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getViniGroupName() {
        return viniGroupName;
    }

    public void setViniGroupName(String viniGroupName) {
        this.viniGroupName = viniGroupName;
    }

    public Integer getFounderId() {
        return founderId;
    }

    public void setFounderId(Integer founderId) {
        this.founderId = founderId;
    }

    public List<Integer> getMembers() {
        return members;
    }

    public void setMembers(List<Integer> members) {
        this.members = members;
    }
}
