package hello.upload.controller;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName,
                           @RequestParam MultipartFile file, HttpServletRequest request) throws IOException {
        log.info("request={}", request);
        log.info("itemName={}", itemName);
        log.info("multipartFile={}", file);

        if (!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("파일 저장 fullPath={}", fullPath);
            file.transferTo(new File(fullPath));
        }
        return "upload-form";
    }

    // @ModelAttribute에서 MultipartFile 주입받기
    @Getter
    @ToString
    static class MyFile {
        private final String itemName;
        private final MultipartFile file;

        public MyFile(final String itemName, final MultipartFile file) {
            this.itemName = itemName;
            this.file = file;
        }
    }

    @GetMapping("/uploadV2")
    public String newFileV2() {
        return "upload-formV2";
    }

    @PostMapping("/uploadV2")
    public String saveFileV2(@ModelAttribute MyFile myFile) throws IOException {
        log.info("myFile={}", myFile);
        MultipartFile file = myFile.getFile();
        if (!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("파일 저장 fullPath={}", fullPath);
            file.transferTo(new File(fullPath));
        }
        return "upload-formV2";
    }
}
