package com.batch.steps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemDescompressStep implements Tasklet {
	
	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		log.info("------------> Inicio del paso de DESCOMPRESIÓN <------------");
		
		Resource resource = resourceLoader.getResource("classpath:files/personas.zip");
		String filePath = resource.getFile().getAbsolutePath();
		
		/** Guardamos el archivo ZIP en la variable zip */
		ZipFile zip = new ZipFile(filePath);
		
		/** Creamos la carpeta de destino */
		File destDir = new File(resource.getFile().getParent(), "destino");
		
		/** Si la carpeta no existe créala */
		if(!destDir.exists()) {
			destDir.mkdir();
		}
		
		//** Se obtienen todas los elementos que contenga el zip */
		Enumeration<? extends ZipEntry> entries = zip.entries();
		
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			File file = new File(destDir, entry.getName());
			if(file.isDirectory()) {
				file.mkdir();
			}else {
				InputStream inputStream = zip.getInputStream(entry);
				FileOutputStream outputStream = new FileOutputStream(file);
				
				byte[] buffer = new byte[1024];
				int length;
				
				while ((length = inputStream.read(buffer))> 0) {
					outputStream.write(buffer, 0, length);
				}
				outputStream.close();
				inputStream.close();
			}
		}
		zip.close();
		
		log.info("------------> Fin del paso de DESCOMPRESIÓN <------------");
		
		return RepeatStatus.FINISHED;
	}

}
