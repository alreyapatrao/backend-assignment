package generation.invoices.service;

import java.util.List;

import generation.invoices.dto.AddInvoiceDto;
import generation.invoices.dto.AmountDto;
import generation.invoices.dto.OverdueFeeDto;
import generation.invoices.entity.Invoice;
import generation.invoices.entity.ResponseEntity;

public interface InvoiceGeneration {
	
	ResponseEntity addInvoice(AddInvoiceDto invoice);

	List<Invoice> getInvoices();

	Invoice payInvoice(AmountDto amount);

	List<Invoice> getOverdueInvoice(OverdueFeeDto overdueFee);
}
