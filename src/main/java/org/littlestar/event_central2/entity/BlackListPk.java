package org.littlestar.event_central2.entity;

import java.io.Serializable;
import java.util.Objects;

public class BlackListPk implements Serializable {
	private static final long serialVersionUID = -2857602242043272759L;
	private String category;
	private String keyword;

	public BlackListPk() {}

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
    public int hashCode() {
        return Objects.hash(getCategory(), getKeyword());
    }
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof BlackListPk)) {
			return false;
		}
		BlackListPk that = (BlackListPk) o;
		return Objects.equals(getCategory(), that.getCategory()) && Objects.equals(getKeyword(), that.getKeyword());
	}

}
