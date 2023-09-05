package com.batch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.batch.model.Persona;
import com.batch.repository.IPersonaDAO;

@Service
@Transactional
public class PersonaServiceImpl implements IPersonaService {

	@Autowired
	private IPersonaDAO repository;
	
	@Override
	public void saveAll(List<Persona> personaList) {
		repository.saveAll(personaList);
	}

}
