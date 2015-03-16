package ch.uzh.csg.coinblesk.responseobject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import ch.uzh.csg.coinblesk.model.HistoryPayInTransaction;
import ch.uzh.csg.coinblesk.model.HistoryPayInTransactionUnverified;
import ch.uzh.csg.coinblesk.model.HistoryPayOutTransaction;
import ch.uzh.csg.coinblesk.model.HistoryTransaction;

public class GetHistoryTransferObject extends TransferObject {
	
	private List<HistoryTransaction> transactionHistory;
	private List<HistoryPayInTransaction> payInTransactionHistory;
	private List<HistoryPayInTransactionUnverified> payInTransactionUnverifiedHistory;
	private List<HistoryPayOutTransaction> payOutTransactionHistory;
	
	private Long nofTransactions;
	private Long nofPayInTransactions;
	private Long nofPayInTransactionsUnverified;
	private Long nofPayOutTransactions;
	
	public GetHistoryTransferObject() {
	}
	
	public GetHistoryTransferObject(List<HistoryTransaction> transactions,
			List<HistoryPayInTransaction> payInTransactions,
			List<HistoryPayInTransactionUnverified> payInTransactionUnverifiedHistory,
			List<HistoryPayOutTransaction> payOutTransactions,
			Long nofTransactions,
			Long nofPayInTransactions,
			Long nofPayInTransactionsUnverified,
			Long nofPayOutTransactions) {
		this.transactionHistory = transactions;
		this.payInTransactionHistory = payInTransactions;
		this.payInTransactionUnverifiedHistory = payInTransactionUnverifiedHistory;
		this.payOutTransactionHistory = payOutTransactions;
		this.nofTransactions = nofTransactions;
		this.nofPayInTransactions = nofPayInTransactions;
		this.nofPayInTransactionsUnverified = nofPayInTransactionsUnverified;
		this.nofPayOutTransactions = nofPayOutTransactions;
	}

	public List<HistoryTransaction> getTransactionHistory() {
		return transactionHistory;
	}

	public void setTransactionHistory(List<HistoryTransaction> transactionHistory) {
		this.transactionHistory = transactionHistory;
	}

	public List<HistoryPayInTransaction> getPayInTransactionHistory() {
		return payInTransactionHistory;
	}

	public void setPayInTransactionHistory(List<HistoryPayInTransaction> payInTransactionHistory) {
		this.payInTransactionHistory = payInTransactionHistory;
	}
	
	
	public List<HistoryPayInTransactionUnverified> getPayInTransactionUnverifiedHistory() {
		return payInTransactionUnverifiedHistory;
	}

	public void setPayInTransactionUnverifiedHistory(List<HistoryPayInTransactionUnverified> payInTransactionUnverifiedHistory) {
		this.payInTransactionUnverifiedHistory = payInTransactionUnverifiedHistory;
	}
	

	public List<HistoryPayOutTransaction> getPayOutTransactionHistory() {
		return payOutTransactionHistory;
	}

	public void setPayOutTransactionHistory(List<HistoryPayOutTransaction> payOutTransactionHistory) {
		this.payOutTransactionHistory = payOutTransactionHistory;
	}
	
	public Long getNofTransactions() {
		return nofTransactions;
	}

	public void setNofTransactions(Long nofTransactions) {
		this.nofTransactions = nofTransactions;
	}

	public Long getNofPayInTransactions() {
		return nofPayInTransactions;
	}

	public void setNofPayInTransactions(Long nofPayInTransactions) {
		this.nofPayInTransactions = nofPayInTransactions;
	}
	
	public Long getNofPayInTransactionsUnverified() {
		return nofPayInTransactionsUnverified;
	}

	public void setNofPayInTransactionsUnverified(Long nofPayInTransactionsUnverified) {
		this.nofPayInTransactionsUnverified = nofPayInTransactionsUnverified;
	}

	public Long getNofPayOutTransactions() {
		return nofPayOutTransactions;
	}

	public void setNofPayOutTransactions(Long nofPayOutTransactions) {
		this.nofPayOutTransactions = nofPayOutTransactions;
	}
	
	@Override
	public JSONObject decode(String responseString) throws Exception {
		if(responseString == null) {
			return null;
		}
		super.decode(responseString);
		JSONObject o = (JSONObject) JSONValue.parse(responseString);
		decode(o);
		return o;
	}

	public void decode(JSONObject o) throws ParseException {
				
		setNofTransactions(toLongOrNull(o.get("nofTransactions")));
		setNofPayInTransactions(toLongOrNull(o.get("nofPayInTransactions")));
		setNofPayInTransactionsUnverified(toLongOrNull(o.get("nofPayInTransactionsUnverified")));
		setNofPayOutTransactions(toLongOrNull(o.get("nofPayOutTransactions")));

		JSONArray array1 = toJSONArrayOrNull(o.get("transactionHistory"));
		ArrayList<HistoryTransaction> transactionHistory = new ArrayList<HistoryTransaction>();
		if(array1!=null) {
			for(Object o2:array1) {
				JSONObject o3 = (JSONObject) o2;
				HistoryTransaction h1 = new HistoryTransaction();
				h1.decode(o3);
				transactionHistory.add(h1);
			}
		}
		setTransactionHistory(transactionHistory);

		JSONArray array2 = toJSONArrayOrNull(o.get("payInTransactionHistory"));
		ArrayList<HistoryPayInTransaction> payInTransactionHistory = new ArrayList<HistoryPayInTransaction>();
		if(array2!=null) {
			for(Object o2:array2) {
				JSONObject o3 = (JSONObject) o2;
				HistoryPayInTransaction h1 = new HistoryPayInTransaction();
				h1.decode(o3);
				payInTransactionHistory.add(h1);
			}
		}
		setPayInTransactionHistory(payInTransactionHistory);
		
		JSONArray array3 = toJSONArrayOrNull(o.get("payInTransactionUnverifiedHistory"));
		ArrayList<HistoryPayInTransactionUnverified> payInTransactionUnverifiedHistory = new ArrayList<HistoryPayInTransactionUnverified>();
		if(array3!=null) {
			for(Object o2:array3) {
				JSONObject o3 = (JSONObject) o2;
				HistoryPayInTransactionUnverified h1 = new HistoryPayInTransactionUnverified();
				h1.decode(o3);
				payInTransactionUnverifiedHistory.add(h1);
			}
		}
		setPayInTransactionUnverifiedHistory(payInTransactionUnverifiedHistory);
		
		JSONArray array4 = toJSONArrayOrNull(o.get("payOutTransactionHistory"));
		ArrayList<HistoryPayOutTransaction> payOutTransactionHistory = new ArrayList<HistoryPayOutTransaction>();
		if(array4!=null) {
			for(Object o2:array4) {
				JSONObject o3 = (JSONObject) o2;
				HistoryPayOutTransaction h1 = new HistoryPayOutTransaction();
				h1.decode(o3);
				payOutTransactionHistory.add(h1);
			}
		}
		setPayOutTransactionHistory(payOutTransactionHistory);
    }
	
	@Override
	public void encode(JSONObject jsonObject) throws Exception {
		super.encode(jsonObject);
		encodeThis(jsonObject);
	}

	public void encodeThis(JSONObject jsonObject) {
		if(nofTransactions!=null) {
			jsonObject.put("nofTransactions", nofTransactions);
		}
		if(nofPayInTransactions!=null) {
			jsonObject.put("nofPayInTransactions", nofPayInTransactions);
		}
		if(nofPayInTransactionsUnverified!=null) {
			jsonObject.put("nofPayInTransactionsUnverified", nofPayInTransactionsUnverified);
		}
		if(nofPayOutTransactions!=null) {
			jsonObject.put("nofPayOutTransactions", nofPayOutTransactions);
		}
		
		if(transactionHistory != null) {
			JSONArray array = new JSONArray();
			for(HistoryTransaction h: transactionHistory) {
				JSONObject o = new JSONObject();
				h.encode(o);
				array.add(o);
			}
			jsonObject.put("transactionHistory", array);
		}
		
		if(payInTransactionHistory != null) {
			JSONArray array = new JSONArray();
			for(HistoryPayInTransaction h: payInTransactionHistory) {
				JSONObject o = new JSONObject();
				h.encode(o);
				array.add(o);
			}
			jsonObject.put("payInTransactionHistory", array);
		}
		
		if(payInTransactionUnverifiedHistory != null) {
			JSONArray array = new JSONArray();
			for(HistoryPayInTransactionUnverified h: payInTransactionUnverifiedHistory) {
				JSONObject o = new JSONObject();
				h.encode(o);
				array.add(o);
			}
			jsonObject.put("payInTransactionUnverifiedHistory", array);
		}
		
		if(payOutTransactionHistory != null) {
			JSONArray array = new JSONArray();
			for(HistoryPayOutTransaction h: payOutTransactionHistory) {
				JSONObject o = new JSONObject();
				h.encode(o);
				array.add(o);
			}
			jsonObject.put("payOutTransactionHistory", array);
		}
    }
}
