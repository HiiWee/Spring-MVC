package hello.upload.controller;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ItemForm {
    private Long itemId;
    private String itemName;
    private MultipartFile attachFile;
    // List 타입으로 받을 수 있음
    private List<MultipartFile> imageFiles;
}
