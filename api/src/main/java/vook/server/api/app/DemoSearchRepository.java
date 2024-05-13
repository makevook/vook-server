package vook.server.api.app;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import vook.server.api.helper.QuerydslHelper;
import vook.server.api.model.demo.DemoTerm;
import vook.server.api.model.demo.QDemoTerm;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DemoSearchRepository {

    private final JPAQueryFactory queryFactory;

    public List<DemoTerm> searchDemoTerm(String demoGlossaryUid, Pageable pageable) {
        QDemoTerm demoTerm = QDemoTerm.demoTerm;

        // fetch join과 pagination을 같이 하면,
        // HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory
        // 에러가 발생, 이를 피하기 위해 pagination이 포함된 쿼리를 서브 쿼리로 하여 수행
        JPAQuery<Long> termIdQuery = queryFactory
                .select(demoTerm.id)
                .from(demoTerm)
                .where(demoTerm.demoGlossary.uid.eq(demoGlossaryUid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        QuerydslHelper
                .toOrderSpecifiers(demoTerm, pageable)
                .forEach(termIdQuery::orderBy);

        JPAQuery<DemoTerm> dataQuery = queryFactory
                .selectFrom(demoTerm)
                .leftJoin(demoTerm.synonyms).fetchJoin()
                .where(demoTerm.id.in(termIdQuery));

        // In 절에 들어간 입력순대로 출력되지 않음으로 정렬 조건을 다시 한번 설정
        QuerydslHelper
                .toOrderSpecifiers(demoTerm, pageable)
                .forEach(dataQuery::orderBy);

        return dataQuery.fetch();
    }
}
