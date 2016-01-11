package org.c.lins.auth.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	public String aliasName;
	public String loginName;
	public String hashPassword;
	public String salt;
	public String email;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
