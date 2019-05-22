package br.com.finperson.core.service.impl;

import org.springframework.stereotype.Service;

import br.com.finperson.core.repository.ReminderRepository;
import br.com.finperson.core.service.ReminderService;
import br.com.finperson.model.ReminderEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReminderServiceImpl extends BaseServiceImpl<ReminderEntity,Long> implements ReminderService{
	
	private ReminderRepository reminderRepository;	
	
	public ReminderServiceImpl(ReminderRepository reminderRepository) {
		super(reminderRepository);
		this.reminderRepository = reminderRepository;
	}	
	

}
