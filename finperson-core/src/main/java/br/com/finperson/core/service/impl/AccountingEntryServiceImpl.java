package br.com.finperson.core.service.impl;

import org.springframework.stereotype.Service;

import br.com.finperson.core.repository.AccountingEntryRepository;
import br.com.finperson.core.service.AccountingEntryService;
import br.com.finperson.model.AccountingEntryEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountingEntryServiceImpl extends BaseServiceImpl<AccountingEntryEntity,Long> implements AccountingEntryService{
	
	private AccountingEntryRepository accountingEntryRepository;	
	
	public AccountingEntryServiceImpl(AccountingEntryRepository accountingEntryRepository) {
		super(accountingEntryRepository);
		this.accountingEntryRepository = accountingEntryRepository;
	}	
	

}
