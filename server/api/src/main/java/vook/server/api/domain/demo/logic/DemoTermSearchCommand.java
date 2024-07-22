package vook.server.api.domain.demo.logic;

import com.meilisearch.sdk.SearchRequest;
import lombok.Builder;
import org.springframework.util.StringUtils;

import java.util.List;

@Builder
public record DemoTermSearchCommand(
        String query,
        boolean withFormat,
        String highlightPreTag,
        String highlightPostTag,
        List<String> sort
) {

    private static final String DEFAULT_HIGHLIGHT_PRE_TAG = "<em>";
    private static final String DEFAULT_HIGHLIGHT_POST_TAG = "</em>";

    public SearchRequest buildSearchRequest() {
        SearchRequest.SearchRequestBuilder builder = SearchRequest.builder();
        if (withFormat) {
            builder.attributesToHighlight(new String[]{"*"});
            builder.highlightPreTag(StringUtils.hasText(highlightPreTag) ? highlightPreTag : DEFAULT_HIGHLIGHT_PRE_TAG);
            builder.highlightPostTag(StringUtils.hasText(highlightPostTag) ? highlightPostTag : DEFAULT_HIGHLIGHT_POST_TAG);
        }

        if (sort != null && !sort.isEmpty()) {
            builder.sort(sort.toArray(new String[0]));
        }

        return builder
                .q(query)
                .limit(Integer.MAX_VALUE)
                .build();
    }
}
