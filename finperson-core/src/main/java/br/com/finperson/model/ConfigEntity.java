package br.com.finperson.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "config")
public class ConfigEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Builder
	public ConfigEntity(Long id, String field, String value, UserEntity user) {
		super(id);
		this.field = field;
		this.value = value;
		this.user = user;
	}

	@NotBlank
	private String field;

	@NotBlank
	private String value;

	@NotBlank
	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

}
