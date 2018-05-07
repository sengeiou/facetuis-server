package com.facetuis.server.dao.admin;

import com.facetuis.server.model.admin.AdminUsers;
import com.facetuis.server.model.order.OrderCommision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUsers,String> {

    public AdminUsers findByUserName (String userName);


}
