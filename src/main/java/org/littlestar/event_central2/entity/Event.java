package org.littlestar.event_central2.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.Gson;

@Entity
@Table(name = "events") //, schema = "public"
public class Event {
	private int id;
	private Date created;
	private String severity;
	private String type;
	private String source;
	private String state;
	private String data;
	private String digest;

	public Event() {}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id", nullable = false)  
	public int getId() {
		return id;
	}

	public void setId(int eid) {
		this.id = eid;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}
	
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
