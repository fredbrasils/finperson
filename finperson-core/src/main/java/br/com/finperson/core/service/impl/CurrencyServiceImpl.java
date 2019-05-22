package br.com.finperson.core.service.impl;

import org.springframework.stereotype.Service;

import br.com.finperson.core.repository.CurrencyRepository;
import br.com.finperson.core.service.CurrencyService;
import br.com.finperson.model.CurrencyEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CurrencyServiceImpl extends BaseServiceImpl<CurrencyEntity,Long> implements CurrencyService{
	
	private CurrencyRepository currencyRepository;	
	
	public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
		super(currencyRepository);
		this.currencyRepository = currencyRepository;
	}	
	

}
