package com.crpf.forms.repository;

import com.crpf.forms.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    List<FamilyMember> findAllByActiveTrue();
    Optional<FamilyMember> findByIdAndActiveTrue(Long id);

    boolean existsByForceNoAndMemberNameAndRelationshipAndActiveTrue(
            String forceNo, String memberName, String relationship);

    boolean existsByForceNoAndMemberNameAndRelationshipAndIdNotAndActiveTrue(
            String forceNo, String memberName, String relationship, Long id);
}
