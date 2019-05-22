package br.com.finperson.core.service.impl;

import org.springframework.stereotype.Service;

import br.com.finperson.core.repository.ReminderTimeRepository;
import br.com.finperson.core.service.ReminderTimeService;
import br.com.finperson.model.ReminderTimeEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReminderTimeServiceImpl extends BaseServiceImpl<ReminderTimeEntity,Long> implements ReminderTimeService{
	
	private ReminderTimeRepository reminderTimeRepository;	
	
	public ReminderTimeServiceImpl(ReminderTimeRepository reminderTimeRepository) {
		super(reminderTimeRepository);
		this.reminderTimeRepository = reminderTimeRepository;
	}	
	

}
