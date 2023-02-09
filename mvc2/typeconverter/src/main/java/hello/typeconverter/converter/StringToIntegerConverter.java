package hello.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

/**
 * 문자 -> 정수 변환 컨버터
 */
@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(final String source) {
        log.info("convert source={}", source);
        return Integer.parseInt(source);
    }
}
