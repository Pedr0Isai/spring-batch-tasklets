package com.batch.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PERSONAS")
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private BigInteger id;
	
	private String nombre;
	
	private String apellido;
	
	private String edad;
	
	@Column(name = "FECHA_ALTA")
	private String fechaAlta;
	
}
