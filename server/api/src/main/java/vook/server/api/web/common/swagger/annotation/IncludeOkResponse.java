package vook.server.api.web.common.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IncludeOkResponse {

    /**
     * @return {@link Class} 응답 Schema 클래스
     */
    Class<?> implementation() default Void.class;
}
