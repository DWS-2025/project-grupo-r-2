package com.spartanwrath.repository;

import com.spartanwrath.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface MembershipRepository extends JpaRepository<Membership,Long> {

}
