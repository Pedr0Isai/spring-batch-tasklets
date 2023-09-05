package com.batch.service;

import java.util.List;

import com.batch.model.Persona;

public interface IPersonaService {

	void saveAll(List<Persona> personaList);
	
}
