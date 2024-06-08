package com.yohanii.lostandfound.component.admin.interceptor;

import com.yohanii.lostandfound.component.crud.entity.Member;
import com.yohanii.lostandfound.component.crud.entity.MemberAuth;
import com.yohanii.lostandfound.consts.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        if (session != null && session.getAttribute(SessionConst.LOGIN_MEMBER) != null && session.getAttribute(SessionConst.LOGIN_MEMBER) instanceof Member member) {
            if (member.getAuth().equals(MemberAuth.ADMIN)) {
                log.info("AdminInterceptor : MemberAuth is ADMIN");
                return true;
            }
        }

        response.sendRedirect("/");

        return false;
    }
}
