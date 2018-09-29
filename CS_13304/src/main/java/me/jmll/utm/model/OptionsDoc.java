package me.jmll.utm.model;

import java.util.HashMap;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.http.HttpMethod;

@XmlRootElement
public class OptionsDoc {

	 HashMap<HttpMethod, String> options;
	public OptionsDoc(){
		 options = new HashMap<HttpMethod, String>();
	 }
	 public void addOption(HttpMethod option, String description){
		 options.put(option, description);
	 }

}