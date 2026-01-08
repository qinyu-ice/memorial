package org.qiaice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.qiaice.entity.Result;
import org.qiaice.expcetion.CustomException;
import org.qiaice.tool.JsonUtil;
import org.qiaice.tool.TokenUtil;
import org.springframework.web.servlet.HandlerInterceptor;

public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler
    ) {
        if (request.getHeader("X-Server-Key") != null) return true;
        String header = request.getHeader("Authorization");
        System.out.println("header:" + header);
        if (header == null || !header.startsWith("Bearer ")) {
            JsonUtil.write(response, Result.no("未携带 token 或 token 格式有误..."));
            return false;
        }
        try {
            TokenUtil.parseToken(header.substring("Bearer ".length()));
        } catch (CustomException e) {
            JsonUtil.write(response, Result.no(e.getMessage()));
            return false;
        }
        return true;
    }
}
