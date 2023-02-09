package hello.typeconverter.converter;


import static org.assertj.core.api.Assertions.assertThat;

import hello.typeconverter.type.IpPort;
import org.junit.jupiter.api.Test;

class ConverterTest {

    @Test
    void StringToInteger() {
        // given
        StringToIntegerConverter converter = new StringToIntegerConverter();

        // when
        Integer result = converter.convert("10");

        // then
        assertThat(result).isEqualTo(10);
    }

    @Test
    void IntegerToString() {
        // given
        IntegerToStringConverter converter = new IntegerToStringConverter();

        // when
        String result = converter.convert(10);

        // then
        assertThat(result).isEqualTo("10");
    }

    @Test
    void stringToIpPort() {
        // given
        IpPortToStringConverter converter = new IpPortToStringConverter();
        IpPort source = new IpPort("127.0.0.1", 8080);

        // when
        String result = converter.convert(source);

        // then
        assertThat(result).isEqualTo("127.0.0.1:8080");
    }

    @Test
    void ipPortToString() {
        // given
        StringToIpPortConverter converter = new StringToIpPortConverter();
        String source = "127.0.0.1:8080";

        // when
        IpPort result = converter.convert(source);

        // then
        assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080));
    }
}
