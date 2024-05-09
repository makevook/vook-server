package vook.server.api.devhelper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.model.Member;
import vook.server.api.model.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final MemberRepository memberRepository;

    public void init() {
        memberRepository.deleteAllInBatch();

        memberRepository.save(Member.forCreateOf("vook"));
    }
}
