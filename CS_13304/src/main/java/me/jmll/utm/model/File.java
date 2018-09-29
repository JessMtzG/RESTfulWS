package me.jmll.utm.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class File implements Serializable {
	private long SerialVersionUID;
	private String name;
	private String path;
	private String fullPath;
	private long sizeBytes;
	private Link _link;
	
	public long getSerialVersionUID() {return SerialVersionUID;}
	public void setSerialVersionUID(long serialVersionUID) {SerialVersionUID = serialVersionUID;}
	public String getName() {return name;}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {return path;}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFullPath() {return fullPath;}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	public long getSizeBytes() {return sizeBytes;}
	public void setSizeBytes(long sizeBytes) {
		this.sizeBytes = sizeBytes;
	}
	public Link get_link() {return _link;}
	public void set_link(Link _link) {
		this._link = _link;
	}
}