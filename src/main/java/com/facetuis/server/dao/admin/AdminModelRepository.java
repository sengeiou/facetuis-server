package com.facetuis.server.dao.admin;

import com.facetuis.server.model.admin.AdminModel;
import com.facetuis.server.model.admin.AdminUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminModelRepository  extends JpaRepository<AdminModel,String> {
}
