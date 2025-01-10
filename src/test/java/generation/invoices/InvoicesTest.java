package generation.invoices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import org.dalesbred.Database;
import org.dalesbred.junit.TestDatabaseProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import generation.invoices.dto.AddInvoiceDto;
import generation.invoices.dto.AmountDto;
import generation.invoices.dto.OverdueFeeDto;
import generation.invoices.entity.Invoice;
import generation.invoices.entity.ResponseEntity;
import generation.invoices.service.implementation.InvoiceGenerationImplementation;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class InvoicesTest {
    private static Database db = TestDatabaseProvider.databaseForProperties("db.properties");
    private static InvoiceGenerationImplementation service;

    @BeforeAll
    public static void setup() {
        service = new InvoiceGenerationImplementation(db);
    }

    @AfterAll
    public static void after() {
        db.update("TRUNCATE TABLE invoices RESTART IDENTITY");
    }

    @Test
    void addInvoice() {
        AddInvoiceDto invoiceDto = new AddInvoiceDto();
        invoiceDto.setDueDate("2025-01-01");
        invoiceDto.setAmount(199.99F);

        ResponseEntity response = service.addInvoice(invoiceDto);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isGreaterThan(0);
    }

    @Test
    void getInvoices() {
        AddInvoiceDto invoiceDto = new AddInvoiceDto();
        invoiceDto.setDueDate("2025-01-01");
        invoiceDto.setAmount(199.99F);
        service.addInvoice(invoiceDto);

        List<Invoice> invoices = service.getInvoices();
        assertThat(invoices).isNotNull();
        assertThat(invoices.size()).isGreaterThan(0);
    }

    @Test
    void payInvoice() {
        AmountDto amountDto = new AmountDto();
        amountDto.setId(1);
        amountDto.setAmount(100.0F);

        Invoice invoice = service.payInvoice(amountDto);
        assertThat(invoice).isNotNull();
        assertThat(invoice.getAmount()).isCloseTo(99.99F, within(0.01F));
    }

    @Test
    void getOverdueInvoice() {
        AddInvoiceDto invoiceDto = new AddInvoiceDto();
        invoiceDto.setDueDate("2025-01-01");
        invoiceDto.setAmount(199.99F);
        service.addInvoice(invoiceDto);

        OverdueFeeDto overdueFeeDto = new OverdueFeeDto();
        overdueFeeDto.setLateFee(100);
        overdueFeeDto.setOverdueDays(9);

        List<Invoice> overdueInvoices = service.getOverdueInvoice(overdueFeeDto);
        assertThat(overdueInvoices).isNotNull();
        assertThat(overdueInvoices.size()).isGreaterThan(0);

        Invoice overdueInvoice = overdueInvoices.get(0);
        assertThat(overdueInvoice.getAmount()).isEqualTo(299.99F); 
        assertThat(overdueInvoice.getDueDate()).isEqualTo("2025-01-10");
    }
}