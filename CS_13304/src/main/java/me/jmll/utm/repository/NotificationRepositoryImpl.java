package me.jmll.utm.repository;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import me.jmll.utm.model.Notification;

@Repository("NotificationRepository")
public class NotificationRepositoryImpl implements NotificationRepository {
	
	private static Map<String, Notification> notificationDB;
	
	public List<Notification> getNotifications(){
		return (List<Notification>) notificationDB.values();
	}
	
	public Notification getNotification(String Id){
		return notificationDB.get(Id);
	}
	
	public Notification createNotification(String Id, String subject, String message,
			List<String> toAddress, List<String> ccAddress){
		Notification notification = new Notification(subject, message, toAddress, ccAddress);
		notificationDB.put(notification.getMessageId(), notification);
		return notification;
	}
	
	public Notification updateNotification(String Id, Notification notification){
		notificationDB.replace(Id, notification);
		return notification;
	}
	
	public void addNotification(Notification notification){
		notificationDB.put(notification.getMessageId(), notification);
	}
	
}