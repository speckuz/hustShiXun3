package sse.hust.vini.group;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface BiRecordRepository extends CrudRepository<BiRecord, Integer> {
    BiRecord getByGroupIdAndConfirmedAndMemberId(Integer groupId, Boolean confirmed, Integer memberId);
    BiRecord getByGroupIdAndMemberId(Integer groupId, Integer memberId);
    @Transactional
    void deleteByGroupIdAndMemberId(Integer groupId, Integer memberId);
    @Transactional
    void deleteAllByGroupId(Integer groupId);
}
