package generation.invoices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverdueFeeDto {
	private float lateFee;
	private Integer overdueDays;
	
}
