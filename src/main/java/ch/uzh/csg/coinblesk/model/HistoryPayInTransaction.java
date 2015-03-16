package ch.uzh.csg.coinblesk.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HistoryPayInTransaction extends AbstractHistory {
	private static final long serialVersionUID = 7289814636424185782L;

	public HistoryPayInTransaction() {
	}

	public HistoryPayInTransaction(Date timestamp, BigDecimal amount) {
		this.timestamp = timestamp;
		this.amount = amount;
	}

	@Override
	public String toString() {
		DecimalFormat DisplayFormatBTC = new DecimalFormat("#.########");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss", Locale.getDefault());
		sdf.setTimeZone(TimeZone.getDefault());
		
		StringBuilder sb = new StringBuilder();
		sb.append(sdf.format(getTimestamp()));
		sb.append("\n");
		sb.append("PayIn Transaction from BTC Network: ");
		sb.append(DisplayFormatBTC.format(getAmount()) + " BTC");
		return sb.toString();
	}
	
}
