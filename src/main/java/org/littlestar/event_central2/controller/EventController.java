package org.littlestar.event_central2.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.littlestar.event_central2.ListenerRunner;
import org.littlestar.event_central2.entity.Event;
import org.littlestar.event_central2.repository.EventsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
public class EventController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
	@Autowired
	private EventsRepository eventsRepository;
	
	@RequestMapping("/")
	public String index(Model model) {
		return "redirect:/dashboard";
	}
	
	@RequestMapping("/dashboard")
	public String dashboard(Model model) {
		return "dashboard";
	}
	
	@RequestMapping("/get-events")
	@ResponseBody
	public String getEvents(@RequestParam Map<String, Object> params) {
		String sortColumn = "created";
		Direction sortOrder = Sort.Direction.DESC;
		String showAllParam = params.getOrDefault("showAll", false).toString();
		String pageSizeParam = params.getOrDefault("pageSize", 10).toString();
		String currentPageParam = params.getOrDefault("currentPage", 0).toString();
		boolean showAll = Boolean.parseBoolean(showAllParam);
		int pageSize = Integer.parseInt(pageSizeParam);
		int currentPage = Integer.parseInt(currentPageParam);
		Page<Event> page;
		Pageable pageable = PageRequest.of(currentPage, pageSize, sortOrder, sortColumn);
		
		if (showAll) {
			page = eventsRepository.getEvents(pageable);
		} else {
			String state = "OPEN";
			page = eventsRepository.getEventsByState(pageable, state);
		}
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		EventPageResponse responseWrap = new EventPageResponse(page.getTotalElements(), page.toList());
		String responseJson = gson.toJson(responseWrap);
		return responseJson;
	}
	
	class EventPageResponse {
		final long total ;
		final List<Event> rows;
		EventPageResponse(long total, List<Event> rows) {
			this.total = total;
			this.rows = rows;
		}
	}
	
	@RequestMapping("/close-event")
	@ResponseBody
	public String closeEvent(@RequestParam Map<String, Object> params) {
		String error = "";
		boolean success = false;
		Object eIdParam = params.get("eid");
		int eId = -1;
		try {
			eId = Integer.parseInt(eIdParam.toString());
			Event event = eventsRepository.getById(eId);
			if(Objects.isNull(event)) {
				error += "event(" + eId + ") not found. ";
			} else {
				event.setState("CLOSED");
				eventsRepository.save(event);
				success = true;
			}
		} catch(Exception e) {
			error += e.getMessage();
			LOGGER.error("", e);
		}
		CloseEventResponse responseWrap = new CloseEventResponse(success, error);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String responseJson = gson.toJson(responseWrap);
		return responseJson;
	}
	
	class CloseEventResponse {
		final String error;
		final boolean success;
		CloseEventResponse(boolean success, String error) {
			this.success = success;
			this.error = error;
		}
	}
	
	@Autowired
	private ListenerRunner listenerRunner;
	@RequestMapping("/get-listener-info")
	@ResponseBody
	public String getListenerInfo() {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String responseJson = gson.toJson(listenerRunner.getListenerInfo());
		return responseJson;
	}
}
