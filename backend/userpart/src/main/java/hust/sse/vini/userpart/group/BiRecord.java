package hust.sse.vini.userpart.group;

import javax.persistence.*;

@Entity
@Table(name = "person_group")
public class BiRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "group_relation_generator")
    @SequenceGenerator(name = "group_relation_generator",sequenceName = "group_relation_seq")
    private Integer id;
    @Column(nullable = false)
    private Integer memberId;
    private String memberAlias;
    @Column(nullable = false)
    private Integer groupId;
    private String groupAlias;
    @Column(nullable = false)
    private boolean confirmed = false;

    public BiRecord(Integer memberId, Integer groupId, boolean confirmed) {
        this.memberId = memberId;
        this.groupId = groupId;
        this.confirmed = confirmed;
    }

    public BiRecord() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberAlias() {
        return memberAlias;
    }

    public void setMemberAlias(String memberAlias) {
        this.memberAlias = memberAlias;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupAlias() {
        return groupAlias;
    }

    public void setGroupAlias(String groupAlias) {
        this.groupAlias = groupAlias;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
