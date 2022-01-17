package org.littlestar.event_central2.utils;

import java.util.List;
import java.util.Vector;

import org.snmp4j.PDU;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PduJsonWrap {
	@SerializedName("variableBindings")
	@Expose
	protected List<VbPair> variableBindingPairs;

	@SerializedName("errorStatus")
	@Expose
	protected Integer errorStatus;

	@SerializedName("errorIndex")
	@Expose
	protected Integer errorIndex;

	@SerializedName("requestID")
	@Expose
	protected Integer requestID;

	@SerializedName("type")
	@Expose
	protected int type;

	public PduJsonWrap(PDU pdu) {
		type = pdu.getType();
		errorStatus = pdu.getErrorStatus();
		errorIndex = pdu.getErrorIndex();
		requestID = pdu.getRequestID().toInt();
		variableBindingPairs = toVariableBindingPairs(pdu.getVariableBindings());
	}

	public PDU getPDU() {
		PDU pdu = new PDU();
		pdu.setRequestID(new Integer32(requestID));
		pdu.setType(PDU.NOTIFICATION);
		pdu.setErrorStatus(errorStatus);
		pdu.setVariableBindings(toVariableBindings(variableBindingPairs));
		return pdu;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	private List<? extends VariableBinding> toVariableBindings(List<VbPair> variableBindings) {
		Vector<VariableBinding> vbs = new Vector<VariableBinding>();
		for (VbPair vbPair : variableBindings) {
			OID oid = new OID(vbPair.getOid());
			OctetString value = new OctetString(vbPair.getValue());
			vbs.add(new VariableBinding(oid, value));
		}
		return vbs;
	}

	private List<VbPair> toVariableBindingPairs(List<? extends VariableBinding> variableBindings) {
		Vector<VbPair> vbPairs = new Vector<VbPair>();
		for (VariableBinding vb : variableBindings) {
			vbPairs.add(new VbPair(vb));
		}
		return vbPairs;
	}

	class VbPair {
		@SerializedName("oid")
		@Expose
		public String oid;
		@SerializedName("variableValue")
		@Expose
		public String value;

		public VbPair(VariableBinding vb) {
			oid = vb.getOid().toString();
			value = vb.getVariable().toString().trim();
		}

		public String getValue() {
			return value;
		}

		public String getOid() {
			return oid;
		}
	}
}