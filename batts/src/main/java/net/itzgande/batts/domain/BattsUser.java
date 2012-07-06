package net.itzgande.batts.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="users")
public class BattsUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String openId;
	
	@DBRef
	private Household household;
	
	private Date created;
	
	private Date lastAccess;
	
	private int accessCount;

	public BattsUser() {
	}

	public BattsUser(String openId, Date created) {
		super();
		this.openId = openId;
		this.created = created;
	}
	
	@Override
	public String toString() {
		return BattsUser.class.getSimpleName()+":[openId="+openId+
				", household="+household+
				"]";
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Household getHousehold() {
		return household;
	}

	public void setHousehold(Household household) {
		this.household = household;
	}

	public Date getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	public int getAccessCount() {
		return accessCount;
	}

	public void setAccessCount(int accessCount) {
		this.accessCount = accessCount;
	}

	public Date getCreated() {
		return created;
	}
}
