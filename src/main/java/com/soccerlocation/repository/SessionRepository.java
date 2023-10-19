package com.soccerlocation.repository;

import com.soccerlocation.domain.Member;
import com.soccerlocation.domain.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session,Long> {

    Optional<Session> findByAccessToken(String accessToken);
}
