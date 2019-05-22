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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reminder")
public class ReminderEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Builder
	public ReminderEntity(Long id, @NotBlank AccountingEntryEntity accountingEntry,
			@NotBlank ReminderTimeEntity reminderTime) {
		super(id);
		this.accountingEntry = accountingEntry;
		this.reminderTime = reminderTime;
	}

	@NotBlank	
	@OneToOne
	@JoinColumn(name = "accounting_entry_id")
	private AccountingEntryEntity accountingEntry;

	@NotBlank
	@OneToOne
	@JoinColumn(name = "reminder_time_id")
	private ReminderTimeEntity reminderTime;
	
}
