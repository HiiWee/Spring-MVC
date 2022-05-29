package hello.servlet.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */
public class MemberRepository {

    // 여기서 static 은 굳이 없어도 싱글톤 보장됨 (객체가 하나만 생성되는것이 보장되므로)
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    // 싱글톤으로 만들기
    private static final MemberRepository instance = new MemberRepository();

    public static MemberRepository getInstance() {
        return instance;
    }

    // 싱글톤으로 만들 때 private으로 생성자를 막아야함
    private MemberRepository() {
    }

    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public List<Member> findAll() {
        // store내 value List를 건들지 않기 위해 (다만 내부 Member객체를 수정하면 실제로도 변경됨)
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
