package vook.server.api.domain.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import vook.server.api.globalcommon.exception.ParameterValidateException;

@Aspect
@Component
public class ExceptionAdvisor {
    // vook.server.api.domain 패키지 이하의 모든 메서드에 대해 실행
    @Around("execution(* vook.server.api.domain..*.*(..))")
    public Object convertException(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ConstraintViolationException e) {
            throw new ParameterValidateException();
        }
    }
}
