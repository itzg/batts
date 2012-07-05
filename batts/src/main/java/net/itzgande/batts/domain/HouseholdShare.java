package net.itzgande.batts.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="shares")
public class HouseholdShare implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private int shareCode;
	
	private Date expires;
	
	@DBRef
	private Household household;
	
	@DBRef
	private BattsUser sharedBy;
	
	public HouseholdShare() {
	}

	public HouseholdShare(int shareCode, Date expires, Household household,
			BattsUser sharedBy) {
		super();
		this.shareCode = shareCode;
		this.expires = expires;
		this.household = household;
		this.sharedBy = sharedBy;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public int getShareCode() {
		return shareCode;
	}

	public Household getHousehold() {
		return household;
	}

	public BattsUser getSharedBy() {
		return sharedBy;
	}
}
