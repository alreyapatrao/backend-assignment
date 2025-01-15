package generation.invoices.repository;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dalesbred.Database;
import org.dalesbred.query.SqlQuery;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import generation.invoices.dao.InvoiceDao;
import generation.invoices.dto.AddInvoiceDto;
import generation.invoices.dto.AmountDto;
import generation.invoices.dto.OverdueFeeDto;
import generation.invoices.entity.Invoice;
import generation.invoices.enums.InvoiceEnum;

public class InvoiceRepository implements InvoiceDao {

	private Database database;

	private static final String DATA_SOURCE = "db/table.sql";

	private String generateTableIfExists() throws IOException {
		URL url = Resources.getResource(DATA_SOURCE);
		String tables = Resources.toString(url, Charsets.UTF_8);
		return tables;
	}

	public InvoiceRepository(Database database) {
		this.database = database;
	}

	@Override
	public void createTable() {
		try {
			String tables = generateTableIfExists();
			database.update(tables);

		} catch (Exception e) {
			System.err.println(e);
		}

	}

	@Override
	public int addInvoice(AddInvoiceDto invoice) {
		final String ADD_INVOICE = "INSERT into Invoices(amount , due_date) VALUES (:amount , :due_date) returning id";
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("amount", invoice.getAmount());
		valueMap.put("due_date", invoice.getDueDate());
		return database.findUniqueInt(SqlQuery.namedQuery(ADD_INVOICE, valueMap));
	}

	@Override
	public List<Invoice> getInvoices() {
		final String GET_INVOICES = "SELECT * FROM Invoices";
		return database.findAll(Invoice.class, SqlQuery.query(GET_INVOICES));
	}

	@Override
	public Invoice payInvoice(AmountDto amount) {
		int id = amount.getId();
		final String GET_INVOICE = "SELECT * FROM Invoices WHERE id = :id";
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("id", id);
		Invoice invoice = database.findUnique(Invoice.class, SqlQuery.namedQuery(GET_INVOICE, valueMap));
		System.out.println(amount.getAmount());
		System.out.println(invoice.getPaidAmount());

		InvoiceEnum status = amount.getAmount() == invoice.getAmount() ? InvoiceEnum.PAID : InvoiceEnum.PENDING;
		valueMap.put("status", status);
		valueMap.put("amount", invoice.getAmount() - amount.getAmount());
		valueMap.put("paid_amount", invoice.getPaidAmount() + amount.getAmount());
		final String UPDATE_INVOICE = "UPDATE Invoices SET amount = :amount , paid_amount = :paid_amount , status = :status WHERE id = :id";

		database.update(SqlQuery.namedQuery(UPDATE_INVOICE, valueMap));
		return database.findUnique(Invoice.class, SqlQuery.namedQuery(GET_INVOICE, valueMap));
	}

	@Override
	public List<Invoice> getOverdueInvoice(OverdueFeeDto overdueFee) {
		final String DATA = "SELECT * FROM Invoices WHERE TO_DATE(due_date, 'YYYY-MM-DD') < CURRENT_DATE AND status = :status";
		Map<String, Object> valueMap = new HashMap<>();
		valueMap.put("status", InvoiceEnum.PENDING);

		List<Invoice> overdueInvoices = database.findAll(Invoice.class, SqlQuery.namedQuery(DATA, valueMap));
		for (Invoice invoice : overdueInvoices) {
			InvoiceEnum updatedState = invoice.getPaidAmount() == 0.0 ? InvoiceEnum.VOID : InvoiceEnum.PAID;
			float updatedAmount = invoice.getAmount() + overdueFee.getLateFee();
			String updatedDate = calculateDueDate(invoice.getDueDate(), overdueFee.getOverdueDays());
			final String UPDATE_INVOICE = "UPDATE Invoices SET status = :status where id = :id";
			Map<String, Object> updatedValueMap = new HashMap<>();

			updatedValueMap.put("id", invoice.getId());
			updatedValueMap.put("status", updatedState);
			database.update(SqlQuery.namedQuery(UPDATE_INVOICE, updatedValueMap));

			AddInvoiceDto updatedInvoice = new AddInvoiceDto();
			updatedInvoice.setAmount(updatedAmount);
			updatedInvoice.setDueDate(updatedDate);

			int id = addInvoice(updatedInvoice);
			invoice.setId(id);
			invoice.setStatus(InvoiceEnum.PENDING);
			invoice.setAmount(updatedAmount);
			invoice.setDueDate(updatedDate);
		}

		return overdueInvoices;
	}

	private String calculateDueDate(String dueDate, Integer overdueDays) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(dueDate, dateTimeFormatter);
		return date.plusDays(overdueDays).toString();

	}

}
