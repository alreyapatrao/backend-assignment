package generation.invoices.resources;

import java.util.List;

import org.dalesbred.Database;

import generation.invoices.dto.AddInvoiceDto;
import generation.invoices.dto.AmountDto;
import generation.invoices.dto.OverdueFeeDto;
import generation.invoices.entity.Invoice;
import generation.invoices.entity.ResponseEntity;
import generation.invoices.service.InvoiceGeneration;
import generation.invoices.service.implementation.InvoiceGenerationImplementation;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Path("/invoices")
public class InvoiceResource {
	
	private InvoiceGeneration invoiceGeneration;
	public InvoiceResource(Database database) {
		this.invoiceGeneration = new InvoiceGenerationImplementation(database);
	}
	
	@POST
	public ResponseEntity addInvoice(AddInvoiceDto invoice) {
		return invoiceGeneration.addInvoice(invoice);
	}
 
	@GET
	public List<Invoice> getInvoices(){
		return invoiceGeneration.getInvoices();
	}
	
	@POST
	@Path("/{id}/payments")
	public Invoice payInvoice(@PathParam("id") int id,AmountDto amount) {
		amount.setId(id);
		return invoiceGeneration.payInvoice(amount);
	}
	
	@POST	
	@Path("/process-overdue")
	public List<Invoice> getOverdueInvoice(OverdueFeeDto overdueFee) {
		return invoiceGeneration.getOverdueInvoice(overdueFee);
	}
}
