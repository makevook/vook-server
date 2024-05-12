package vook.server.api.web.routes.term;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import vook.server.api.model.Glossary;
import vook.server.api.outbound.search.SearchParams;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class SearchRequest {

    @Schema(description = "용어집 UID", requiredMode = REQUIRED, example = "0e840100-9302-4f8b-8110-ffd321c3ccb9")
    private String glossaryUid;

    @Schema(description = "검색 쿼리", requiredMode = REQUIRED, example = "하이브리드앱")
    private String query;

    @Schema(description = "포맷 적용 여부", defaultValue = "false")
    private boolean withFormat;

    @Schema(description = "하이라이트 시작 태그, 포맷 적용 여부가 true일 때만 적용 됨", defaultValue = "<em>")
    private String highlightPreTag;

    @Schema(description = "하이라이트 종료 태그, 포맷 적용 여부가 true일 때만 적용 됨", defaultValue = "</em>")
    private String highlightPostTag;

    public SearchParams toSearchParam(Glossary glossary) {
        return SearchParams.builder()
                .glossary(glossary)
                .query(query)
                .withFormat(withFormat)
                .highlightPreTag(highlightPreTag)
                .highlightPostTag(highlightPostTag)
                .build();
    }
}
