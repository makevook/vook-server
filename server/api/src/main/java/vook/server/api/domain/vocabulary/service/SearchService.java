package vook.server.api.domain.vocabulary.service;

import lombok.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface SearchService {
    
    Result search(Params params);

    @Builder
    record Params(
            List<String> vocabularyUids,
            List<String> queries,
            boolean withFormat,
            String highlightPreTag,
            String highlightPostTag
    ) {
    }

    @Builder
    record Result(
            List<Result.Record> records
    ) {
        public record Record(
                String vocabularyUid,
                String query,
                ArrayList<HashMap<String, Object>> hits
        ) {
        }
    }
}
