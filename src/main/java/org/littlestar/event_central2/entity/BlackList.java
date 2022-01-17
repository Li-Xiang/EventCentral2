package org.littlestar.event_central2.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.google.gson.Gson;

@Entity
//@javax.persistence.Cacheable(true)
//@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY)
@IdClass(BlackListPk.class)
@Table(name = "blacklist")
public class BlackList {
	@Id
	private String category;
	@Id
	private String keyword;

	public BlackList() {}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
