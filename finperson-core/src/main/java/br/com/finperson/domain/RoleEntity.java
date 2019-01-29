package br.com.finperson.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import br.com.finperson.domain.enumm.RoleEnum;
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
public class RoleEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Builder
	public RoleEntity(Long id, RoleEnum role) {
		super(id);
		this.role = role;
	}

	@Column
	@Enumerated(EnumType.STRING)
	private RoleEnum role;

}
