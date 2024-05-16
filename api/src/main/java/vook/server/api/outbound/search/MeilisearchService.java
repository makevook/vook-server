package vook.server.api.outbound.search;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.IndexesQuery;
import com.meilisearch.sdk.model.Results;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

public abstract class MeilisearchService {

    @Value("${service.meilisearch.host:}")
    protected String host;

    @Value("${service.meilisearch.apiKey:}")
    protected String apiKey;

    protected Client client;

    @PostConstruct
    public void postConstruct() {
        this.client = new Client(new Config(host, apiKey));
    }

    protected void clearAll(String uidPrefix) {
        Results<Index> indexes = client.getIndexes(new IndexesQuery() {{
            setLimit(Integer.MAX_VALUE);
        }});
        Arrays.stream(indexes.getResults())
                .map(Index::getUid)
                .filter(uid -> uid.startsWith(uidPrefix))
                .forEach(uid -> {
                    client.deleteIndex(uid);
                });
    }
}
