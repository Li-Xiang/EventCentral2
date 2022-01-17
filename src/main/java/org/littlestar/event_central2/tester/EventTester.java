/*
 * Copyright 2019 Li Xiang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.littlestar.event_central2.tester;

import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.PDU;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

public class EventTester {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventTester.class);
	
	public static PDU newTestTrapPDU(String msg) throws Exception {
		List<VariableBinding> vbs = new Vector<>();
		System.out.print(msg);
		OID testOid = new OID("1.3.6.2.1.5.104");
		OctetString value = new OctetString(msg);
		VariableBinding vb = new VariableBinding(testOid, value);
		vbs.add(vb);
		PDU pdu = new PDU();
		pdu.setType(PDU.NOTIFICATION);
		pdu.setVariableBindings(vbs);
		return pdu;
	}
	
	public static void sendSnmpTrap(String protocol, String tAddr, String msg) throws Exception {
		SnmpTester snmpTester = new SnmpTester(protocol);
		PDU pdu = newTestTrapPDU(msg);
		snmpTester.send(pdu, tAddr);
	}

	public static void main(String[] args) throws Throwable {
		if (args.length < 3) {
			LOGGER.error("必须参数: {tcp|udp} {target-address} {msg}");
		}
		String protocol = args[0];
		String tAddr = args[1];
		String msg = args[2];
		System.out.println(protocol);
		System.out.println(tAddr);
		System.out.println(msg);
		EventTester.sendSnmpTrap(protocol, tAddr, msg);
	}
}
