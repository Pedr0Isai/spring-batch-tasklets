package com.batch.repository;

import java.math.BigInteger;

import org.springframework.data.repository.CrudRepository;

import com.batch.model.Persona;

public interface IPersonaDAO extends CrudRepository<Persona, BigInteger>{
	
}
