package com.batch.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1")
public class BatchController {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;

	@PostMapping("/uploadFile")
	public ResponseEntity<?> receiveFile(@RequestParam("file") MultipartFile multipartFile){
		/**Se define path del archivo subido al endpoint*/
		String fileName = multipartFile.getOriginalFilename();
		String slash = File.separator;
		String uriStr = String.format("src%smain%sresources%sfiles%s%s", slash, slash, slash, slash, fileName);
		
		/**Se define path el cuerpo del response*/
		Map<String, String> response = new HashMap<>();
		response.put("archivo", fileName);
		response.put("estado", "No recibido");
		
		try {
			/**Se crea el directorio padre y se copia ahi el archivo zip*/
			Path path = Paths.get(uriStr);
			Files.createDirectories(path.getParent());
			Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			
			log.info("------------> Inicio del proceso Batch <------------");
			
			/**Definimos parametros iniciales para el Job*/
			JobParameters jobParameter = new JobParametersBuilder()
					.addString("fileName", fileName)
					.addDate("fecha", new Date())
					.toJobParameters();
			
			/**Ejecutamos el Job con el JobLauncher*/
			jobLauncher.run(job, jobParameter);
			response.put("estado", "Recibido");
			return ResponseEntity.ok(response);
		}catch(Exception e){
			log.error("Error al iniciar el proceso Batch: {}", e.getMessage(), e);
			throw new RuntimeException();
		}
	}
}
