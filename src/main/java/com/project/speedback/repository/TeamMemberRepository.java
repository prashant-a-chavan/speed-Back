package com.project.speedback.repository;

import com.project.speedback.entity.TeamMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
  List<TeamMember> findByNameIn(List<String> names);
}
