package com.soccerlocation.repository;

import com.soccerlocation.domain.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member,Long> {

    Optional<Member> findByEmailAndPassword(String email, String password);
}
