package vook.server.api.web.demo.reqres;

import io.swagger.v3.oas.annotations.media.Schema;
import vook.server.api.domain.demo.service.data.DemoTermSearchParams;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public record SearchTermRequest(
        @Schema(description = "검색 쿼리, 빈 문자열을 보낼경우 모든 데이터가 반환된다.", requiredMode = REQUIRED, example = "하이브리드앱")
        String query,

        @Schema(description = "포맷 적용 여부", defaultValue = "false")
        boolean withFormat,

        @Schema(description = "하이라이트 시작 태그, 포맷 적용 여부가 true일 때만 적용 됨", defaultValue = "<em>")
        String highlightPreTag,

        @Schema(description = "하이라이트 종료 태그, 포맷 적용 여부가 true일 때만 적용 됨", defaultValue = "</em>")
        String highlightPostTag,

        @Schema(
                description = "정렬 정보, null이면 관련도 기준으로 정렬 됨",
                allowableValues = {
                        "term:asc", "term:desc",
                        "synonyms:asc", "synonyms:desc",
                        "meaning:asc", "meaning:desc",
                        "createdAt:asc", "createdAt:desc"
                }
        )
        List<String> sort
) {
    public DemoTermSearchParams toSearchParam() {
        return DemoTermSearchParams.builder()
                .query(query)
                .withFormat(withFormat)
                .highlightPreTag(highlightPreTag)
                .highlightPostTag(highlightPostTag)
                .sort(sort)
                .build();
    }
}
