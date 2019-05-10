package com.huahua.article.filter;

import huahua.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("----------进入拦截器---------");
        //获取请求的头中的数据
        String header = request.getHeader("Authorization");//Authorization 参数名称
        if (StringUtils.isEmpty(header)){
            throw new RuntimeException("权限不足");
        }

        if (!header.startsWith("Bearer ")){
            throw new RuntimeException("权限不足");
        }

        String token = header.substring(7);
        //token解析后的明文数据
        Claims claims=null;
        try {
             claims = jwtUtil.parseJWT(token);
        }catch (Exception e){
            e.printStackTrace();
        }

        //效验claims 不能为空
        if (null ==claims){
            throw new RuntimeException("权限不足");
        }
        //用户或者管理员的信息 放入session中
        if (StringUtils.equals("admin", (String)(claims.get("roles")))){
            request.setAttribute("admin_claims",claims);
        }
        if (StringUtils.equals("user", (String)(claims.get("roles")))){
            request.setAttribute("user_claims",claims);
        }

        return true;

    }
}