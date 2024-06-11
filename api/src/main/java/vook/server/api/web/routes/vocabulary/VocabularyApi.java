package vook.server.api.web.routes.vocabulary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import vook.server.api.web.auth.data.VookLoginUser;
import vook.server.api.web.common.CommonApiResponse;
import vook.server.api.web.routes.vocabulary.reqres.VocabularyResponse;

import java.util.List;

@Tag(name = "vocabulary", description = "용어집 API")
public interface VocabularyApi {

    @Operation(
            summary = "용어집 조회",
            security = {
                    @SecurityRequirement(name = "AccessToken")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VocabularyApiVocabulariesResponse.class)
                    )
            ),
    })
    CommonApiResponse<List<VocabularyResponse>> vocabularies(VookLoginUser user);

    class VocabularyApiVocabulariesResponse extends CommonApiResponse<List<VocabularyResponse>> {
    }
}
