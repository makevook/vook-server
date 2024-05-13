package vook.server.api.outbound.search;

import com.meilisearch.sdk.SearchRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;
import vook.server.api.model.demo.DemoGlossary;

@Getter
@Builder
public class DemoTermSearchParams {

    private static final String DEFAULT_HIGHLIGHT_PRE_TAG = "<em>";
    private static final String DEFAULT_HIGHLIGHT_POST_TAG = "</em>";

    private DemoGlossary demoGlossary;
    private String query;
    private boolean withFormat;
    private String highlightPreTag;
    private String highlightPostTag;

    public SearchRequest buildSearchRequest() {
        SearchRequest.SearchRequestBuilder builder = SearchRequest.builder();
        if (withFormat) {
            builder.attributesToHighlight(new String[]{"*"});
            builder.highlightPreTag(StringUtils.hasText(highlightPreTag) ? highlightPreTag : DEFAULT_HIGHLIGHT_PRE_TAG);
            builder.highlightPostTag(StringUtils.hasText(highlightPostTag) ? highlightPostTag : DEFAULT_HIGHLIGHT_POST_TAG);
        }

        return builder
                .q(query)
                .build();
    }
}
