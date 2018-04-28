package com.facetuis.server.model.user;

import com.facetuis.server.model.basic.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_user_commision")
public class UserComision extends BaseEntity {
}
