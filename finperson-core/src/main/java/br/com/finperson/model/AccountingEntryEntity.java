package br.com.finperson.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.finperson.model.enumm.AccountingEntryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounting_entry")
public class AccountingEntryEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Builder
	public AccountingEntryEntity(Long id, AccountingEntryEntity mainAccountingEntry,
			Set<AccountingEntryEntity> accountingEntries, @NotBlank AccountingEntryTypeEnum accountingEntryType,
			CategoryEntity category, @NotBlank CycleEntity cycle, BigDecimal value, CurrencyEntity currency,
			UserEntity user) {
		super(id);
		this.mainAccountingEntry = mainAccountingEntry;
		this.accountingEntries = accountingEntries;
		this.accountingEntryType = accountingEntryType;
		this.category = category;
		this.cycle = cycle;
		this.value = value;
		this.currency = currency;
		this.user = user;
	}

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "main_accounting_id")
	AccountingEntryEntity mainAccountingEntry;
	
	@JsonManagedReference
	@OneToMany(mappedBy="mainAccountingEntry")
	Set<AccountingEntryEntity> accountingEntries;
	
	@NotBlank
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private AccountingEntryTypeEnum accountingEntryType;
	
	@OneToOne
	@JoinColumn(name = "category_id")
	private CategoryEntity category;
	
	@NotBlank
	@JoinColumn(name = "cycle_id")
	private CycleEntity cycle;
	
	private BigDecimal value;
	
	@OneToOne
	@JoinColumn(name = "currency_id")
	private CurrencyEntity currency;	

	@OneToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

}
