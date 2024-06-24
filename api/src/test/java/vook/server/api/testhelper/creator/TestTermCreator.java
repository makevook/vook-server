package vook.server.api.testhelper.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vook.server.api.domain.term.model.Term;
import vook.server.api.domain.term.service.TermService;
import vook.server.api.domain.term.service.data.TermCreateCommand;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class TestTermCreator {

    private final TermService termService;

    public Term createTerm() {
        return termService.create(TermCreateCommand.builder()
                .term("testTerm")
                .meaning("testMeaning")
                .synonyms(List.of("testSynonym"))
                .build());
    }
}
