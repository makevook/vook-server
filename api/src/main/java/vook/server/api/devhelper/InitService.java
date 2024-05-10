package vook.server.api.devhelper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.model.Glossary;
import vook.server.api.model.GlossaryRepository;
import vook.server.api.model.Member;
import vook.server.api.model.MemberRepository;
import vook.server.api.outbound.search.SearchClearable;
import vook.server.api.outbound.search.SearchService;

@Service
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final MemberRepository memberRepository;
    private final GlossaryRepository glossaryRepository;
    private final SearchClearable searchClearable;

    private final SearchService searchService;

    public void init() {
        glossaryRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        searchClearable.clearAll();

        Member vook = memberRepository.save(Member.forCreateOf("vook"));

        Glossary devGlossary = glossaryRepository.save(Glossary.forCreateOf("개발", vook));
        searchService.createGlossary(devGlossary);
        Glossary designGlossary = glossaryRepository.save(Glossary.forCreateOf("디자인", vook));
        searchService.createGlossary(designGlossary);
        Glossary marketingGlossary = glossaryRepository.save(Glossary.forCreateOf("마케팅", vook));
        searchService.createGlossary(marketingGlossary);
        Glossary practiceGlossary = glossaryRepository.save(Glossary.forCreateOf("실무", vook));
        searchService.createGlossary(practiceGlossary);
    }
}
