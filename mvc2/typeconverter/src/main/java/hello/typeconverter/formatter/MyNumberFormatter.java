package hello.typeconverter.formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

@Slf4j
// Formatter의 String은 기본으로 변환해주므로, 객체를 넣어주면 된다.
public class MyNumberFormatter implements Formatter<Number> {

    @Override
    public Number parse(final String text, final Locale locale) throws ParseException {
        log.info("text={}, locale={}", text, locale);
        // "1,000 -> 1000
        NumberFormat format = NumberFormat.getInstance(locale);
        return format.parse(text);
    }

    @Override
    public String print(final Number object, final Locale locale) {
        log.info("object={}, locale-{}", object, locale);
        return NumberFormat.getInstance(locale)
                .format(object);
    }
}
