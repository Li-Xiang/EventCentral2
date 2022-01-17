package org.littlestar.event_central2.repository;

import java.util.List;

import org.littlestar.event_central2.entity.BlackList;
import org.littlestar.event_central2.entity.BlackListPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList, BlackListPk> {
	//@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	public List<BlackList> findByCategory(String category);
	
	//@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	public List<BlackList> findByKeyword(String keyword);
}
