package org.littlestar.event_central2.listener;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.littlestar.event_central2.EventListener;
import org.littlestar.event_central2.EventObserver;
import org.littlestar.event_central2.EventState;
import org.littlestar.event_central2.ListenerType;
import org.littlestar.event_central2.Severity;
import org.littlestar.event_central2.entity.Event;
import org.littlestar.event_central2.utils.PduHelper;
import org.littlestar.event_central2.utils.PduJsonWrap;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

public class SnmpTrapListener implements EventListener, CommandResponder {
	private Snmp snmp;
	private TransportMapping<?> transport;
	private Address address;
	private String protocol;
	private List<EventObserver> observers = new Vector<EventObserver>();
	
	public SnmpTrapListener() {
		address = GenericAddress.parse("udp:0.0.0.0/162");
	}
	
	public SnmpTrapListener(Address addr) {
		address = addr;
	}
	
	@Override
	public void addEventObserver(EventObserver observer) {
		observers.add(observer);
	}

	@Override
	public void removeEventObserver(EventObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void onEventRecived(Event e) {
		for (EventObserver o : observers) {
			o.eventReceived(e);
		}
	}

	@Override
	public void startup() throws Exception {
		ThreadPool threadPool = ThreadPool.create("snmp-trap-listener-pool", 6);
		MultiThreadedMessageDispatcher dispatcher = new MultiThreadedMessageDispatcher(threadPool,
				new MessageDispatcherImpl());
		if (address instanceof UdpAddress) {
			transport = new DefaultUdpTransportMapping((UdpAddress) address);
			protocol = "udp";
		} else {
			transport = new DefaultTcpTransportMapping((TcpAddress) address);
			protocol = "tcp";
		}
		snmp = new Snmp(dispatcher, transport);
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
		SecurityProtocols.getInstance().addDefaultProtocols();
		USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
		SecurityModels.getInstance().addSecurityModel(usm);
		snmp.addCommandResponder(this);
		snmp.listen();
	}

	@Override
	public void shutdown() throws Exception {
		try {
			transport.close();
		} catch (Exception e) {
		}
		try {
			snmp.close();
		} catch (Exception e) {}
	}
	
	@Override
	public boolean isListening() {
		if (transport != null) {
			return transport.isListening();
		}
		return false;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public int getPort() {
		int port = 0;
		String sAddr = address.toString();
		String sPort = sAddr.substring(sAddr.lastIndexOf('/') + 1, sAddr.length());
		try {
			port = Integer.parseInt(sPort);
		} catch (Throwable e) {}
		return port;
	}
	
	@Override
	public String getName() {
		return "snmp-trap-listener";
	}
	
	@Override
	public ListenerType getType() {
		return ListenerType.SNMP;
	}
	
	@Override
	public void processPdu(CommandResponderEvent event) {
		Date timestamp = new Date();
		String src = event.getPeerAddress().toString();
		src = src.substring(0, src.lastIndexOf('/')).trim();
		PDU pdu = event.getPDU();
		
		String data = new PduJsonWrap(pdu).toJson();
		Event e = new Event();
		e.setCreated(timestamp);
		e.setSeverity(Severity.CRITICAL.toString());
		e.setType(ListenerType.SNMP.toString());
		e.setSource(src);
		e.setState(EventState.OPEN.toString());
		e.setDigest(PduHelper.getDigest(pdu));
		e.setData(data);
		onEventRecived(e);
	}
}