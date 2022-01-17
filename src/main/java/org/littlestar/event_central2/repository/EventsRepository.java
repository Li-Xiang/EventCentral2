package org.littlestar.event_central2.repository;

import org.littlestar.event_central2.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventsRepository extends JpaRepository<Event, Integer> {
	/**
	 * 根据事件状态过滤。
	 * @param pageable
	 * @param state
	 * @return
	 */
	@Query( value=
			"SELECT e.id, "+
			"       e.created, "+
			"       e.severity, "+ 
			"       e.type, "+ 
			"       IF(h.name IS NULL,e.source,concat(h.name,' (',e.source,')')) source, "+ 
			"       e.state, "+ 
			"       e.data, "+ 
			"       e.digest "+ 
			"FROM events e LEFT JOIN hosts h ON e.source = h.addr " +
			"WHERE state=:state",
			countQuery="select id from events where state=:state",
			nativeQuery = true)
	public Page<Event> getEventsByState(Pageable pageable, @Param("state") String state);
	
	@Query( value=
			"SELECT e.id, "+
			"       e.created, "+
			"       e.severity, "+ 
			"       e.type, "+ 
			"       IF(h.name IS NULL,e.source,concat(h.name,' (',e.source,')')) source, "+ 
			"       e.state, "+ 
			"       e.data, "+ 
			"       e.digest "+ 
			"FROM events e LEFT JOIN hosts h ON e.source = h.addr ",
			countQuery="select id from events",
			nativeQuery = true)
	public Page<Event> getEvents(Pageable pageable);
}