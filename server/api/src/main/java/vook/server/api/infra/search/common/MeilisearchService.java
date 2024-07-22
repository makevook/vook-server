package vook.server.api.infra.search.common;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.IndexesQuery;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.model.TypoTolerance;

import java.util.Arrays;

public abstract class MeilisearchService {

    protected final Client client;

    protected MeilisearchService(MeilisearchProperties properties) {
        this.client = new Client(new Config(properties.getHost(), properties.getApiKey()));
    }

    protected void clearAllByPrefix(String uidPrefix) {
        Results<Index> indexes = client.getIndexes(new IndexesQuery() {{
            setLimit(Integer.MAX_VALUE);
        }});
        Arrays.stream(indexes.getResults())
                .map(Index::getUid)
                .filter(uid -> uid.startsWith(uidPrefix))
                .forEach(client::deleteIndex);
    }

    protected void createIndex(String indexUid, String primaryKeyName) {
        TaskInfo indexCreateTask = client.createIndex(indexUid, primaryKeyName);
        client.waitForTask(indexCreateTask.getTaskUid());

        // 용어, 동의어, 뜻에 대해서만 검색
        TaskInfo updateSearchableTask = client.index(indexUid).updateSearchableAttributesSettings(new String[]{
                "term",
                "synonyms",
                "meaning"
        });
        client.waitForTask(updateSearchableTask.getTaskUid());

        // 용어, 동의어, 뜻, 생성일시에 대해 정렬 가능
        TaskInfo updateSortableTask = client.index(indexUid).updateSortableAttributesSettings(new String[]{
                "term",
                "synonyms",
                "meaning",
                "createdAt"
        });
        client.waitForTask(updateSortableTask.getTaskUid());

        client.index(indexUid).updateRankingRulesSettings(new String[]{
                "sort",
                "words",
                "typo",
                "proximity",
                "attribute",
                "exactness"
        });

        // 오타 용인을 비활성화 하여도 띄어쓰기에 대해서는 검색이 됨으로 비활성화 함
        TypoTolerance typoTolerance = new TypoTolerance();
        typoTolerance.setEnabled(false);
        TaskInfo updateTypoTask = client.index(indexUid).updateTypoToleranceSettings(typoTolerance);
        client.waitForTask(updateTypoTask.getTaskUid());
    }
}
