package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Item {

    private Long id;

    @NotBlank(message = "공백x")   // 빈캆 + 공백만 있는 경우 허용 x
    private String itemName;

    @NotNull    // null x
    @Range(min = 1000, max = 1000000)   // 범위 안의 값이어야 함
    private Integer price;

    @NotNull
    @Max(9999)  // 최대값 설정
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
