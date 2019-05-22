package br.com.finperson.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "idiom")
public class IdiomEntity extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Builder
	public IdiomEntity(Long id, String name, String lang) {
		super(id);
		this.name = name;
		this.lang = lang;
	}

	@NotBlank
	private String name;
	
	@NotBlank
	private String lang;

}
