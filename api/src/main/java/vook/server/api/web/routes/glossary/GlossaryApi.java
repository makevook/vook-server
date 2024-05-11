package vook.server.api.web.routes.glossary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import vook.server.api.web.common.CommonApiResponse;

import java.util.List;

@Tag(name = "glossary", description = "용어집 API")
public interface GlossaryApi {

    @Operation(summary = "용어집 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RetrieveApiResponse.class)
                    )
            ),
    })
    CommonApiResponse<List<RetrieveResponse>> retrieve();

    class RetrieveApiResponse extends CommonApiResponse<List<RetrieveResponse>> {
    }

    @Operation(summary = "용어집 내 용어 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RetrieveTermsApiResponse.class)
                    )
            ),
    })
    CommonApiResponse<List<RetrieveTermsResponse>> retrieveTerms(
            String glossaryUid,
            @ParameterObject Pageable pageable
    );

    class RetrieveTermsApiResponse extends CommonApiResponse<List<RetrieveTermsResponse>> {
    }

}
