package com.facetuis.server.dao.user;

import com.facetuis.server.model.user.UserCommision;
import com.facetuis.server.model.user.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommisionRepository extends JpaRepository<UserCommision,String> {

    UserCommision findByUserId(String userId);
}
