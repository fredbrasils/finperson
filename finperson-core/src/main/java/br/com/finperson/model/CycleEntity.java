package br.com.finperson.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import br.com.finperson.model.enumm.CycleTypeEnum;
import br.com.finperson.model.enumm.DaysWeekEnum;
import br.com.finperson.model.enumm.MonthEnum;
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
@Table(name = "cycle")
public class CycleEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private LocalDateTime date;

	@NotBlank
	@Column
	@Enumerated(EnumType.STRING)
	private CycleTypeEnum cycleType;

	@Column
	@Enumerated(EnumType.STRING)
	private DaysWeekEnum daysWeek;

	private Integer day;

	@Column
	@Enumerated(EnumType.STRING)
	private MonthEnum month;

	private Boolean weekend;

	private LocalDateTime expirationDate;
}
