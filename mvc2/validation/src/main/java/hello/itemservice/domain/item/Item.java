package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
// 제약이 많고 복잡하다.
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총 합이 10000원을 넘겨야 합니다.")
public class Item {

//    @NotNull(groups = UpdateCheck.class)
    private Long id;

//    @NotBlank(message = "공백x", groups = {SaveCheck.class, UpdateCheck.class})   // 빈캆 + 공백만 있는 경우 허용 x
    private String itemName;

//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})    // null x
//    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})   // 범위 안의 값이어야 함
    private Integer price;

//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
//    @Max(value = 9999, groups = SaveCheck.class)  // 최대값 설정
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
