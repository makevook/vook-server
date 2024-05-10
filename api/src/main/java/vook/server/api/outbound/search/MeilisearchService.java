package vook.server.api.outbound.search;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.IndexesQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.TaskInfo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vook.server.api.model.Glossary;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class MeilisearchService implements SearchService, SearchClearable {

    @Value("${service.meilisearch.host:}")
    private String host;

    @Value("${service.meilisearch.apiKey:}")
    private String apiKey;

    private Client client;

    @PostConstruct
    public void postConstruct() {
        this.client = new Client(new Config(host, apiKey));
    }

    @Override
    public void clearAll() {
        Results<Index> indexes = client.getIndexes(new IndexesQuery() {{
            setLimit(Integer.MAX_VALUE);
        }});
        Arrays.stream(indexes.getResults()).forEach(index -> {
            client.deleteIndex(index.getUid());
        });
    }

    @Override
    public void createGlossary(Glossary glossary) {
        TaskInfo index = client.createIndex(getIndexUid(glossary));
        client.waitForTask(index.getTaskUid());
    }

    @NotNull
    private static String getIndexUid(Glossary glossary) {
        return glossary.getUid();
    }
}
