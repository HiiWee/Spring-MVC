package hello.typeconverter.type;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * IP 및 PORT를 입력하면 해당 객체로 변환
 * ex: 127.0.0.1:8080 -> IpPort로 변환 혹은 반대도 가능하도록
 */
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpPort {
    private String ip;
    private int port;

    public IpPort(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
    }
}
