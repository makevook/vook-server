package vook.server.api.infra.search.vocabulary;

import com.meilisearch.sdk.IndexSearchRequest;
import com.meilisearch.sdk.MultiSearchRequest;
import com.meilisearch.sdk.model.MultiSearchResult;
import com.meilisearch.sdk.model.Results;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vook.server.api.domain.vocabulary.service.SearchService;
import vook.server.api.infra.search.common.MeilisearchProperties;
import vook.server.api.infra.search.common.MeilisearchService;

import java.util.Arrays;

import static com.meilisearch.sdk.IndexSearchRequest.builder;

@Service
public class MeilisearchSearchService extends MeilisearchService implements SearchService {

    public MeilisearchSearchService(MeilisearchProperties properties) {
        super(properties);
    }

    @Override
    public Result search(Params params) {
        MultiSearchRequest request = new RequestBuilder(params).buildMultiSearchRequest();
        Results<MultiSearchResult> results = this.client.multiSearch(request);
        return new ResultBuilder(results).build();
    }

    private record RequestBuilder(Params params) {

        private static final String DEFAULT_HIGHLIGHT_PRE_TAG = "<em>";
        private static final String DEFAULT_HIGHLIGHT_POST_TAG = "</em>";

        public MultiSearchRequest buildMultiSearchRequest() {
            IndexSearchRequest.IndexSearchRequestBuilder builder = builder();
            if (params.withFormat()) {
                builder.attributesToHighlight(new String[]{"*"});
                builder.highlightPreTag(StringUtils.hasText(params.highlightPreTag()) ? params.highlightPreTag() : DEFAULT_HIGHLIGHT_PRE_TAG);
                builder.highlightPostTag(StringUtils.hasText(params.highlightPostTag()) ? params.highlightPostTag() : DEFAULT_HIGHLIGHT_POST_TAG);
            }
            builder.limit(Integer.MAX_VALUE);

            MultiSearchRequest request = new MultiSearchRequest();
            params.vocabularyUids().forEach(uid -> {
                params.queries().forEach(query -> {
                    request.addQuery(builder.indexUid(uid).q(query).build());
                });
            });
            return request;
        }
    }

    private record ResultBuilder(
            Results<MultiSearchResult> results
    ) {
        public Result build() {
            return new Result(
                    Arrays.stream(results.getResults())
                            .map(result -> new Result.Record(result.getIndexUid(), result.getQuery(), result.getHits()))
                            .toList()
            );
        }
    }
}
