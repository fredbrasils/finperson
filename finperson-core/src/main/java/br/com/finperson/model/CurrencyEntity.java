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
@Table(name = "currency")
public class CurrencyEntity extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	@Builder
	public CurrencyEntity(Long id, String name, String symbol) {
		super(id);
		this.name = name;
		this.symbol = symbol;
	}

	@NotBlank
	private String name;
	
	@NotBlank
	private String symbol;

}
