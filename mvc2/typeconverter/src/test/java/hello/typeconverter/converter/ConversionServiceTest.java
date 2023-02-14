package hello.typeconverter.converter;

import static org.assertj.core.api.Assertions.assertThat;

import hello.typeconverter.type.IpPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;

public class ConversionServiceTest {

    @Test
    void ConversionService() {
        // 등록
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());
        IpPort ipPort = new IpPort("127.0.0.1", 8080);

        // 검증
        Assertions.assertAll(() -> {
            assertThat(conversionService.convert(10, String.class)).isEqualTo("10");
            assertThat(conversionService.convert("10", Integer.class)).isEqualTo(10);
            assertThat(conversionService.convert("127.0.0.1:8080", IpPort.class)).isEqualTo(ipPort);
            assertThat(conversionService.convert(ipPort, String.class)).isEqualTo("127.0.0.1:8080");
        });
    }
}
