package generation.invoices.dto;
import generation.invoices.enums.InvoiceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {
	private Integer id;
	private float amount;
	private String dueDate;
	private Integer paidAmount;
	private InvoiceEnum status;
}
