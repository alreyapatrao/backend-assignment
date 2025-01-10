package generation.invoices.dao;

import java.util.List;

import generation.invoices.dto.AddInvoiceDto;
import generation.invoices.dto.AmountDto;
import generation.invoices.dto.OverdueFeeDto;
import generation.invoices.entity.Invoice;

public interface InvoiceDao {
	
	void createTable();
	
	int addInvoice(AddInvoiceDto invoice);

	List<Invoice> getInvoices();

	Invoice payInvoice(AmountDto amount);

	List<Invoice> getOverdueInvoice(OverdueFeeDto overdueFee);

}
