package vook.server.api.app.contexts.demo.application.data;

import com.meilisearch.sdk.SearchRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
@Builder
public class DemoTermSearchParams {

    private static final String DEFAULT_HIGHLIGHT_PRE_TAG = "<em>";
    private static final String DEFAULT_HIGHLIGHT_POST_TAG = "</em>";

    private String query;
    private boolean withFormat;
    private String highlightPreTag;
    private String highlightPostTag;
    private List<String> sort;

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
