package me.jmll.utm.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import me.jmll.utm.model.Link;
import me.jmll.utm.model.Notification;
import me.jmll.utm.model.NotificationLinkListResource;
import me.jmll.utm.model.OptionsDoc;
import me.jmll.utm.service.NotificationService;


@Controller
public class NotificationRest {
	static final Logger logger = LogManager.getLogger();
	@Autowired
	NotificationService notificationService;
	@RequestMapping(value = "/api/v1/notify",
			method = RequestMethod.OPTIONS)
	@ResponseBody
	public ResponseEntity<?> showOptions(){
		OptionsDoc options = new OptionsDoc();
		options.addOption(HttpMethod.GET, "Lists notifications submitted");
		options.addOption(HttpMethod.OPTIONS, "Resource documentation");
		options.addOption(HttpMethod.POST, "submits notification to send");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Allow", "OPTIONS,GET,POST");
		return new ResponseEntity<>(headers, HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value = "/api/v1/notify",
			method = RequestMethod.GET,
			produces = { "application/json", "text/json"})
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Map<String, Object> getNotificationsJSON(){
		List<Notification> notifications = new ArrayList<Notification>();
		notifications = notificationService.getNotifications();
		List<Link> _links = new ArrayList<Link>();
		_links.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/")
		 .build().toString(), "api"));
		_links.add(new Link(ServletUriComponentsBuilder.fromCurrentServletMapping().path("/notify/")
		 .build().toString(), "self"));
		Map<String, Object> response = new Hashtable<>(2);
		response.put("_links", _links);
		response.put("data", notifications);
		return response;
	}
	
	
	@RequestMapping(value = "/api/v1/notify", 
			method = RequestMethod.GET, 
			produces = { "application/xml", "text/xml" })
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public NotificationLinkListResource getNotificationsXML(){
		List<Notification> notifications = new ArrayList<Notification>();
		notifications = notificationService.getNotifications();
		NotificationLinkListResource notificationLinksResource = new NotificationLinkListResource();
		List<Link> _links = new ArrayList<Link>();
		_links.add(new Link(ServletUriComponentsBuilder
		 .fromCurrentServletMapping().path("/")
		 .build().toString(), "api"));
		_links.add(new Link(ServletUriComponentsBuilder
		 .fromCurrentServletMapping().path("/notify/")
		 .build().toString(), "self"));
		notificationLinksResource.setLinks(_links);
		notificationLinksResource.setNotifications(notifications);
		return notificationLinksResource;
	}
	
	@RequestMapping(value = "/api/v1/notify", 
			method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<?> notify(String subject, String message, String toAddress, String ccAddress){
		notificationService.notify(subject, message, Arrays.asList(toAddress), Arrays.asList(ccAddress));
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}