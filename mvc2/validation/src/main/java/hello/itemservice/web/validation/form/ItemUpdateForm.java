package hello.itemservice.web.validation.form;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemUpdateForm {

    // 수정할때는 ID가 있어야 수정가능 하므로 ID 넣음
    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)   // 범위 안의 값이어야 함
    private Integer price;

    // 수정에서 수량은 자유롭게 변경할 수 있다.
    private Integer quantity;

}
