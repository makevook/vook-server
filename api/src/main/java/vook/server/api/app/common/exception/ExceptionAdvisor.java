package vook.server.api.app.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionAdvisor {
    // vook.server.api.app 패키지 이하의 모든 메서드에 대해 실행
    @Around("execution(* vook.server.api.app..*.*(..))")
    public Object convertException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ConstraintViolationException e) {
            throw new ParameterValidateException(e);
        }
    }
}
