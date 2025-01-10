package generation.invoices.service.implementation;

import java.util.List;

import org.dalesbred.Database;

import generation.invoices.dao.InvoiceDao;
import generation.invoices.dto.AddInvoiceDto;
import generation.invoices.dto.AmountDto;
import generation.invoices.dto.OverdueFeeDto;
import generation.invoices.entity.Invoice;
import generation.invoices.entity.ResponseEntity;
import generation.invoices.repository.InvoiceRepository;
import generation.invoices.service.InvoiceGeneration;

public class InvoiceGenerationImplementation implements InvoiceGeneration{
	 
	private InvoiceDao invoiceDao;
	
	public InvoiceGenerationImplementation(Database database) {
		this.invoiceDao = new InvoiceRepository(database);
		invoiceDao.createTable();
	}
	
	@Override
	public ResponseEntity addInvoice(AddInvoiceDto invoice) {
		Integer id = invoiceDao.addInvoice(invoice);
		return new ResponseEntity(id);
		
	}

	@Override
	public List<Invoice> getInvoices() {
		return invoiceDao.getInvoices();
	}

	@Override
	public Invoice payInvoice(AmountDto amount) {
		return invoiceDao.payInvoice(amount);
	}

	@Override
	public List<Invoice> getOverdueInvoice(OverdueFeeDto overdueFee) {
		return invoiceDao.getOverdueInvoice(overdueFee);
	}
	
	
}
