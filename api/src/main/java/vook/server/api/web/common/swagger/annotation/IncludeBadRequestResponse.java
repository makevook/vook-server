package vook.server.api.web.common.swagger.annotation;

import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.MediaType;
import lombok.RequiredArgsConstructor;
import vook.server.api.web.common.swagger.ComponentRefConsts;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IncludeBadRequestResponse {

    /**
     * @return {@link Kind} 응답 내용 종류
     */
    Kind[] value() default {};

    /**
     * {@link IncludeBadRequestResponse} 응답 내용
     */
    @RequiredArgsConstructor
    enum Kind {

        INVALID_PARAMETER(new ExampleInfo(
                "유효하지 않은 파라미터",
                new Example().$ref(ComponentRefConsts.Example.INVALID_PARAMETER)
        )),

        VIOLATION_BUSINESS_RULE(new ExampleInfo(
                "비즈니스 규칙 위반",
                new Example().$ref(ComponentRefConsts.Example.VIOLATION_BUSINESS_RULE)
        ));

        private final ExampleInfo exampleInfo;

        public void applyExample(MediaType mediaType) {
            mediaType.getExamples().put(exampleInfo.name, exampleInfo.example);
        }
    }

    record ExampleInfo(
            String name,
            Example example
    ) {
    }
}
