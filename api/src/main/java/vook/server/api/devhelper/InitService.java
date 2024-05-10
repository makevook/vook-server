package vook.server.api.devhelper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.model.Glossary;
import vook.server.api.model.GlossaryRepository;
import vook.server.api.model.Member;
import vook.server.api.model.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final MemberRepository memberRepository;
    private final GlossaryRepository glossaryRepository;

    public void init() {
        glossaryRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();

        Member vook = memberRepository.save(Member.forCreateOf("vook"));

        glossaryRepository.save(Glossary.forCreateOf("개발", vook));
        glossaryRepository.save(Glossary.forCreateOf("디자인", vook));
        glossaryRepository.save(Glossary.forCreateOf("마케팅", vook));
        glossaryRepository.save(Glossary.forCreateOf("실무", vook));
    }
}
