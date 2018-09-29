package me.jmll.utm.rest;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.jmll.utm.model.FileLinkListResource;
import me.jmll.utm.model.Link;
import me.jmll.utm.model.OptionsDoc;
import me.jmll.utm.rest.exception.ResourceNotFoundException;
import me.jmll.utm.service.FileService;

@Controller
public class DirectoryRest {
	@Autowired
	FileService fileService;
	@RequestMapping(value = "/api/v1/directory",
			method = RequestMethod.OPTIONS)
	@ResponseBody
	public ResponseEntity<?> showOptions(){
		OptionsDoc options = new OptionsDoc();
		options.addOption(HttpMethod.GET, "Lists specified directory contents in parameter 'dir'");
		options.addOption(HttpMethod.OPTIONS, "Resource documentation");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,GET");
		return new ResponseEntity<>(headers, HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value = "/api/v1/directory",
			method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getFilesJSON(String dir){
		File file = new File(dir);
		if (!file.exists()) throw new ResourceNotFoundException();
		Path path = Paths.get(dir);
		List<Path> files = new ArrayList<Path>();
		files = fileService.walkDir(path, files);
		List<Link> _links = new ArrayList<Link>();
		 _links.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/")
				 .build().toString(), "api"));
		 _links.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/directory")
				 .build().toString(), "self"));
		 Map<String, Object> response = new Hashtable<>(2);
		 response.put("_links", _links);
		 response.put("data", files);
		 return response;
	}
	
	@RequestMapping(value = "/api/v1/directory",
			method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public FileLinkListResource getFilesXML(String dir){
		File file = new File(dir);
		if (!file.exists()) throw new ResourceNotFoundException();
		FileLinkListResource filesLinksResource = new FileLinkListResource();
		Path path = Paths.get(dir);
		List<Path> paths = new ArrayList<Path>();
		paths = fileService.walkDir(path, paths);
		List<me.jmll.utm.model.File> files = new ArrayList<me.jmll.utm.model.File>();
		List<Link> _links = new ArrayList<Link>();
		_links.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/")
		 .build().toString(), "api"));
		_links.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/directory")
		 .build().toString(), "self"));
		filesLinksResource.setLinks(_links);
		filesLinksResource.setFiles(files);
		return filesLinksResource;	
	}	
}