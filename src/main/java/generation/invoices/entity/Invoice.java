package generation.invoices.entity;

import generation.invoices.enums.InvoiceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
	private Integer id;
	private float amount;
	private String dueDate;
	private float paidAmount;
	private InvoiceEnum status;
}
