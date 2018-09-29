package me.jmll.utm.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import me.jmll.utm.model.OptionsDoc;
import me.jmll.utm.rest.exception.ResourceNotFoundException;
import me.jmll.utm.service.FileService;
import me.jmll.utm.view.DownloadView;

@Controller
public class FileRest {
	@Autowired
	FileService fileService;
	@RequestMapping(value = "/api/v1/file",
			method = RequestMethod.OPTIONS)
	@ResponseBody
	public ResponseEntity<?> showOptions(){
		OptionsDoc options = new OptionsDoc();
		options.addOption(HttpMethod.DELETE, "Elimina un archivo en el parámetro 'path'");
		options.addOption(HttpMethod.GET, "Descarga un archivo en el parámetro 'path'"); 
		options.addOption(HttpMethod.OPTIONS, "Documentación de recursos");
		options.addOption(HttpMethod.POST, "Sube un archivo en el parámetro 'path'");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,DELETE,GET,POST");
		return new ResponseEntity<>(headers, HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value = "/api/v1/file",
			method = RequestMethod.GET)
	@ResponseBody
	public View downloadFile(String path){
		File file = new File(path);
		if (!file.exists()) throw new ResourceNotFoundException();
		fileService.getFile(path);
		Path paths = Paths.get(path);
		try {
			return new DownloadView(path, Files.probeContentType(paths), Files.readAllBytes(paths));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResourceNotFoundException();
		}		
	}
	
	@RequestMapping(value = "/api/v1/file",
			method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.ACCEPTED)
	public String deleteFile(String path){
		File file = new File(path);
		if (!file.exists()) throw new ResourceNotFoundException();
		String name = file.getName();
		fileService.delete(path);
		return name;
	}
	
	@RequestMapping(value = "/api/v1/file",
			method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> uploadFile(MultipartFile file, String name, String dir){
		if(file.isEmpty())return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		fileService.uploadFile(file, name, dir);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
}