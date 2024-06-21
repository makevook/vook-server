package vook.server.api.app.infra.search.common;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.IndexesQuery;
import com.meilisearch.sdk.model.Results;

import java.util.Arrays;

public abstract class MeilisearchService {

    protected final Client client;

    protected MeilisearchService(MeilisearchProperties properties) {
        this.client = new Client(new Config(properties.getHost(), properties.getApiKey()));
    }

    protected void clearAll(String uidPrefix) {
        Results<Index> indexes = client.getIndexes(new IndexesQuery() {{
            setLimit(Integer.MAX_VALUE);
        }});
        Arrays.stream(indexes.getResults())
                .map(Index::getUid)
                .filter(uid -> uid.startsWith(uidPrefix))
                .forEach(client::deleteIndex);
    }
}
