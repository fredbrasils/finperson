package br.com.finperson.core.service.impl;

import org.springframework.stereotype.Service;

import br.com.finperson.core.repository.IdiomRepository;
import br.com.finperson.core.service.IdiomService;
import br.com.finperson.model.IdiomEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IdiomServiceImpl extends BaseServiceImpl<IdiomEntity,Long> implements IdiomService{
	
	private IdiomRepository idiomRepository;	
	
	public IdiomServiceImpl(IdiomRepository idiomRepository) {
		super(idiomRepository);
		this.idiomRepository = idiomRepository;
	}	
	

}
