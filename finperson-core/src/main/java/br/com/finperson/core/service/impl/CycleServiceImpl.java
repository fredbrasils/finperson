package br.com.finperson.core.service.impl;

import org.springframework.stereotype.Service;

import br.com.finperson.core.repository.CycleRepository;
import br.com.finperson.core.service.CycleService;
import br.com.finperson.model.CycleEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CycleServiceImpl extends BaseServiceImpl<CycleEntity,Long> implements CycleService{
	
	private CycleRepository cycleRepository;	
	
	public CycleServiceImpl(CycleRepository cycleRepository) {
		super(cycleRepository);
		this.cycleRepository = cycleRepository;
	}	
	

}
