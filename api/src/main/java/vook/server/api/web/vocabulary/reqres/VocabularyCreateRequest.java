package vook.server.api.web.vocabulary.reqres;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VocabularyCreateRequest {

    @NotBlank
    @Size(min = 1, max = 20)
    private String name;
}
