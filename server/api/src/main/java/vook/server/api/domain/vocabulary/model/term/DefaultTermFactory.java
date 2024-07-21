package vook.server.api.domain.vocabulary.model.term;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import vook.server.api.domain.common.model.Synonym;
import vook.server.api.domain.vocabulary.exception.TermLimitExceededException;
import vook.server.api.domain.vocabulary.exception.VocabularyNotFoundException;
import vook.server.api.domain.vocabulary.model.vocabulary.Vocabulary;
import vook.server.api.domain.vocabulary.model.vocabulary.VocabularyRepository;
import vook.server.api.globalcommon.annotation.ModelFactory;

import java.util.List;
import java.util.UUID;

@ModelFactory
@RequiredArgsConstructor
public class DefaultTermFactory implements TermFactory {

    private static final int MAX_VOCABULARY_SIZE = 100;

    private final VocabularyRepository vocabularyRepository;

    @Override
    public Term create(@Valid @NotNull CreateCommand request) {
        Vocabulary vocabulary = getVocabulary(request.vocabularyUid());
        int termCount = vocabulary.termCount();
        if (termCount >= MAX_VOCABULARY_SIZE) {
            throw new TermLimitExceededException();
        }

        Term term = Term.builder()
                .uid(UUID.randomUUID().toString())
                .term(request.termInfo().term())
                .meaning(request.termInfo().meaning())
                .synonym(Synonym.from(request.termInfo().synonyms()))
                .vocabulary(vocabulary)
                .build();
        vocabulary.addTerm(term);

        return term;
    }

    @Override
    public List<Term> createForBatchCreate(@Valid @NotNull CreateForBatchCommand request) {
        Vocabulary vocabulary = getVocabulary(request.vocabularyUid());
        int savedCount = vocabulary.termCount();
        int count = request.termInfos().size();
        if (savedCount + count > MAX_VOCABULARY_SIZE) {
            throw new TermLimitExceededException();
        }

        return request.termInfos().stream()
                .map(termInfo -> Term.builder()
                        .uid(UUID.randomUUID().toString())
                        .term(termInfo.term())
                        .meaning(termInfo.meaning())
                        .synonym(Synonym.from(termInfo.synonyms()))
                        .vocabulary(vocabulary)
                        .build()
                )
                .peek(vocabulary::addTerm)
                .toList();
    }

    @Override
    public Term createForUpdate(@Valid @NotNull UpdateCommand request) {
        return Term.builder()
                .term(request.termInfo().term())
                .meaning(request.termInfo().meaning())
                .synonym(Synonym.from(request.termInfo().synonyms()))
                .build();
    }

    private Vocabulary getVocabulary(String vocabularyUid) {
        return vocabularyRepository.findByUid(vocabularyUid).orElseThrow(VocabularyNotFoundException::new);
    }
}
