package br.com.finperson.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import br.com.finperson.model.enumm.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
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
