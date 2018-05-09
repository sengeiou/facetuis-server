package com.facetuis.server.dao.user;

import com.facetuis.server.model.user.UserCommision;
import com.facetuis.server.model.user.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCommisionRepository extends JpaRepository<UserCommision,String> {

    UserCommision findByUserId(String userId);

    @Query(value = "select sum(inviting_cash) from t_user_commision where user_id = ?1  and to_days(create_time) = to_days(now())",nativeQuery = true)
    Long invitingToday(String userId);

    @Query(value = "select sum(inviting_cash) from t_user_commision where user_id = ?1  and TO_DAYS( NOW( ) ) - TO_DAYS( create_time) = 1",nativeQuery = true)
    Long invitingYesterday(String userId);
}
