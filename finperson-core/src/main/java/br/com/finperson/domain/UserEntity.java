package br.com.finperson.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class UserEntity extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@Builder
	public UserEntity(Long id,String firstName, String lastName, String password, String email, Boolean enabled, Boolean nonLocked,
			Integer validationCode, Set<RoleEntity> roles) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.nonLocked = nonLocked;
		this.validationCode = validationCode;
		this.roles = roles;
	}
	
	
	@Column
    private String firstName;
    

	@Column
    private String lastName;
     
	@Column
    private String password;
	
	@Column 
    private String email;
	
	@Column 
    private Boolean enabled;
	
	@Column 
    private Boolean nonLocked;
	
	@Column 
    private Integer validationCode;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles;
}
