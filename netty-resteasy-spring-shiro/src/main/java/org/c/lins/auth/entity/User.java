package org.c.lins.auth.entity;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

//@Entity
public class User {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	public String aliasName;
	public String loginName;
	public String hashPassword;
	public String salt;
	public String email;

	// 多对多定义
//	@ManyToMany
//	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
//	// Fecth策略定义
//	@Fetch(FetchMode.JOIN)
//	// 集合按id排序
//	@OrderBy("id ASC")

	private List<Role> roles = Lists.newArrayList(); // 有序的关联对象集合

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
