package me.jmll.utm.rest;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import me.jmll.utm.form.UserForm;
import me.jmll.utm.model.Link;
import me.jmll.utm.model.OptionsDoc;
import me.jmll.utm.model.User;
import me.jmll.utm.model.UserLinkListResource;
import me.jmll.utm.model.UserResource;
import me.jmll.utm.rest.exception.ResourceNotFoundException;
import me.jmll.utm.service.UserService;

@Controller
public class UserRest {
	@Autowired
	UserService userService;

	/**
	 * Recurso para obtener colección de entidades. 
	 * 1 (a) Obtener lista de links de Usuarios en JSON
	 * en lugar de información del
	 *  usuario {"_links": links, "data": userData}
	 */
	@RequestMapping(value = "user", 
			method = RequestMethod.GET,
			produces = { "application/json", "text/json" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getUsersJSON() {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(builder.path("/").build().toString(), "api"));
		links.add(new Link(builder.path("/user/").build().toString(), "self"));
		
		List<Link> data = new ArrayList<Link>();
		userService.getUsers().forEach(user -> data.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/user/" + user.getUsername())
				.build().toString(), user.getUsername())));
		
		Map<String, Object> response = new Hashtable<>(2);
		response.put("_links", links);
		response.put("data", data);
		return response;
}
	
	/**
	 * Recurso para obtener colección de entidades. 
	 * 1 (b) Obtener lista de links de Usuarios en XML
	 * en lugar de información del usuario (UserLinkListResource)
	 */
	@RequestMapping(value = "user", 
			method = RequestMethod.GET,
			produces = { "application/xml", "text/xml" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public UserLinkListResource getUsersXML() {
		// Escribe tu código aquí {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		UserLinkListResource userLinksResource = new UserLinkListResource();
		userLinksResource.addLink(new Link(builder.path("/").build().toString(), "api"));
		userLinksResource.addLink(new Link(builder.path("/user/").build().toString(), "self"));
		userService.getUsers().forEach(user -> userLinksResource.addUserLink(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping()
				.path("/user/" + user.getUsername()).build().toString(), user.getUsername())));
		return userLinksResource;
	}
	
	/**
	 * 2 (a) Recurso para obtener entidad usuario
	 * en JSON {"_links": links, "data": userData}
	 */
	@RequestMapping(value = "user/{username}", 
			method = RequestMethod.GET, 
			produces = { "application/json", "text/json" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getUserJSON(@PathVariable("username") String username) {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
		
		if (this.userService.getUser(username) == null)
			throw new ResourceNotFoundException("User was not found");
		
		List<Link> links = new ArrayList<Link>();
		links.add(new Link(builder.path("/user/").build().toString(), "user"));
		links.add(new Link(builder.path(username).build().toString(), "self"));
		
		Map<String, Object> response = new Hashtable<>(2);
		response.put("_links", links);
		response.put("data", userService.getUser(username));
		return response;
	}
	
	/**
	 * 2 (b) Recurso para obtener entidad usuario
	 * en XML (UserResource)
	 */
	@RequestMapping(value = "user/{username}", 
			method = RequestMethod.GET, 
			produces = { "application/xml", "text/xml" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public UserResource getUserXML(@PathVariable("username") String username) {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();

		if (this.userService.getUser(username) == null)
			throw new ResourceNotFoundException("User was not found");

		UserResource resource = new UserResource();
		resource.addLink(new Link(builder.path("/user/").build().toString(), "user"));
		resource.addLink(new Link(builder.path(username).build().toString(), "self"));
		resource.setUser(userService.getUser(username));
		return resource;
}

	/**
	 * Recurso para eliminar entidad
	 */
	@RequestMapping(value = "user/{username}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable("username") String username) {
		if (this.userService.getUser(username) == null)
			throw new ResourceNotFoundException("User was not found");
		this.userService.deleteUser(username);
	}

	/**
	 * Recurso para crear entidad
	 */
	@RequestMapping(value = "user", method = RequestMethod.POST)
	public ResponseEntity<User> create(@RequestBody UserForm form) {
		User newUser = this.userService.createUser(form.getUsername(), form.getPassword(), form.getFullName());

		String uri = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/user/{username}")
				.buildAndExpand(newUser.getUsername()).toString();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", uri);

		return new ResponseEntity<>(newUser, headers, HttpStatus.CREATED);
	}

	/**
	 * Recurso para actualizar entidad
	 */
	@RequestMapping(value = "user/{username}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable("username") String username, @RequestBody UserForm form) {
		User user = this.userService.getUser(username);
		if (user == null)
			throw new ResourceNotFoundException("User was not found");
		user.setFullName(form.getFullName());
		user.setPassword(form.getPassword());
		user.setUsername(form.getUsername());
		this.userService.updateUser(user);
	}

	@RequestMapping(value = "user", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> userIndex() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,GET,POST");
		
		Map<HttpMethod, String> methods = new Hashtable<>(3);
		methods.put(HttpMethod.GET, "Lists users available.");
		methods.put(HttpMethod.OPTIONS, "Resource documentation.");
		methods.put(HttpMethod.POST, "Creates specified user with form (username, password, fullName).");
		
		OptionsDoc options = new OptionsDoc();
		options.setMethods(methods);
		
		return new ResponseEntity<>(options, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "user/{username}", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> userOptions(@PathVariable("username") String username) {
		if (this.userService.getUser(username) == null)
			throw new ResourceNotFoundException("User was not found");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,GET,PUT,DELETE");
		
		Map<HttpMethod, String> methods = new Hashtable<>(4);
		methods.put(HttpMethod.GET, "Displays specified user's information.");
		methods.put(HttpMethod.OPTIONS, "Resource documentation.");
		methods.put(HttpMethod.PUT, "Updates specified user's information with form (username, password, fullName).");
		methods.put(HttpMethod.DELETE, "Deletes specified user.");
		
		OptionsDoc options = new OptionsDoc();
		options.setMethods(methods);
		
		return new ResponseEntity<>(options, headers, HttpStatus.OK);
	}

}
