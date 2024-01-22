package com.yohanii.lostandfound.aop;

import com.yohanii.lostandfound.domain.user.User;
import com.yohanii.lostandfound.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
@Aspect
public class LoginUserAOP {

    @Pointcut("execution(* com.yohanii.lostandfound.web.post..*(..))")
    private void webPost() {}

    @Pointcut("execution(* com.yohanii.lostandfound.web.profile..*(..))")
    private void webProfile() {}

    @Pointcut("execution(* com.yohanii.lostandfound.web.user..*(..))")
    private void webUser() {}

    @Pointcut("execution(* com.yohanii.lostandfound.web.HomeController..*(..))")
    private void webHome() {}

    @Around("webPost() || webProfile() || webUser() || webHome()")
    public Object getLoginUser(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        log.info("LoginUserAOP.getLoginUser");

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Model model) {
                if (session != null) {
                    model.addAttribute("user", session.getAttribute(SessionConst.LOGIN_USER));
                }
            }
        }

        return joinPoint.proceed();
    }
}
