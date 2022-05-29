package hello.servlet.domain.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

    private Long id;
    private String username;
    private int age;

    public Member() {
    }

    // id는 회원 저장소에서(DB) 저장하면서 자동할당
    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
