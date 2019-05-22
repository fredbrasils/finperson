package br.com.finperson.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import br.com.finperson.model.enumm.ReminderTimeEnum;
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
@Table(name = "reminder_time")
public class ReminderTimeEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Builder
	public ReminderTimeEntity(Long id, ReminderTimeEnum reminderTime, Long timeCount) {
		super(id);
		this.reminderTime = reminderTime;
		this.timeCount = timeCount;
	}

	@NotBlank
	@Column
	@Enumerated(EnumType.STRING)
	private ReminderTimeEnum reminderTime;

	@NotBlank
	private Long timeCount;
}
