package me.jmll.utm.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Notification implements Serializable {
	private static long SerialVersionUID;
	private String subject;
	private String message;
	private List<String> toAddress;
	private List<String> ccAddress;
	private String messageId;
	private String status;
	
	public Notification(){
		messageId = UUID.randomUUID().toString();
	}
	
	public Notification(String subject, String message, List<String> toAddress, List<String> ccAddress){
		this.subject = subject;
		this.message = message;
		this.toAddress = toAddress;
		this.ccAddress = ccAddress;
		messageId = UUID.randomUUID().toString();
	}
	
	public Notification(String subject, String message, List<String> toAddress){
		this.subject = subject;
		this.message = message;
		this.toAddress = toAddress;
		messageId = UUID.randomUUID().toString();
	}

	public String getSubject() {return subject;}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {return message;}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getToAddress() {return toAddress;}

	public void setToAddress(List<String> toAddress) {
		this.toAddress = toAddress;
	}

	public List<String> getCcAddress() {return ccAddress;}

	public void setCcAddress(List<String> ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String getMessageId() {return messageId;}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getStatus() {return status;}

	public void setStatus(String status) {
		this.status = status;
	}	
}