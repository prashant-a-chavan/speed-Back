package com.project.speedBack.repository;

import com.project.speedBack.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByNameIn(List<String> names);
}
