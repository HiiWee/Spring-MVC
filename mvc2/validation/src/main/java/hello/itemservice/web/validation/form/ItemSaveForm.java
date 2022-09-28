package hello.itemservice.web.validation.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemSaveForm {

    // 저장할때는 ID는 필요 없으므로 뺌
    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)   // 범위 안의 값이어야 함
    private Integer price;

    @NotNull
    @Max(value = 9999)
    private Integer quantity;
}
