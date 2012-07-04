package net.itzgande.batts.domain;

import java.io.Serializable;

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

	public BattsUser() {
	}

	public BattsUser(String openId) {
		super();
		this.openId = openId;
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
}
