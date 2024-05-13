package vook.server.api.app;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vook.server.api.model.demo.DemoGlossary;
import vook.server.api.model.demo.DemoGlossaryRepository;
import vook.server.api.model.demo.DemoTerm;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final DemoGlossaryRepository demoGlossaryRepository;
    private final DemoSearchRepository searchRepository;

    public List<DemoGlossary> findAllDemoGlossary() {
        return demoGlossaryRepository.findAll();
    }

    public Optional<DemoGlossary> findDemoGlossaryByUid(String demoGlossaryUid) {
        return demoGlossaryRepository.findByUid(demoGlossaryUid);
    }

    public List<DemoTerm> findAllDemoTermBy(DemoGlossary demoGlossary, Pageable pageable) {
        return searchRepository.searchDemoTerm(demoGlossary.getUid(), pageable);
    }
}
