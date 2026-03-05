package com.project.Ums.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLogAspect {

    private final ActivityLogService logService;
    private final HttpServletRequest request;

    @AfterReturning("@annotation(logActivity)")
    public void logSuccess(JoinPoint joinPoint, LogActivity logActivity) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        ActivityLog log = new ActivityLog();
        log.setUsername(auth != null ? auth.getName() : "ANONYMOUS");
        log.setAction(logActivity.action());
        log.setMethodName(joinPoint.getSignature().getName());
        log.setDescription(logActivity.description());
        log.setIpAddress(request.getRemoteAddr());
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setSuccess(true);

        logService.saveLog(log);
    }

    @AfterThrowing(pointcut="@annotation(logActivity)", throwing="ex")
    public void logFailure(JoinPoint joinPoint, LogActivity logActivity, Throwable ex) {
        ActivityLog log = new ActivityLog();
        log.setUsername("UNKNOWN");
        log.setAction(logActivity.action());
        log.setErrorMessage(ex.getMessage());
        logService.saveLog(log);
    }
}