package vook.server.api.infra.term;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import vook.server.api.domain.vocabulary.model.QTerm;
import vook.server.api.domain.vocabulary.model.Term;
import vook.server.api.globalcommon.helper.querydsl.QuerydslHelper;
import vook.server.api.usecases.term.RetrieveTermUseCase;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaTermSearchRepository implements RetrieveTermUseCase.TermSearchService {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Term> findAllBy(Pageable pageable) {
        QTerm term = QTerm.term1;

        JPAQuery<Term> dataQuery = queryFactory
                .selectFrom(term)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        QuerydslHelper.toOrderSpecifiers(term, pageable).forEach(dataQuery::orderBy);
        List<Term> result = dataQuery.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(term.count())
                .from(term);

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }
}
