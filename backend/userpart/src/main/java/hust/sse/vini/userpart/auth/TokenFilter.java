package hust.sse.vini.userpart.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TokenFilter extends OncePerRequestFilter {

    private final static String tokenSavedHeader = "Authorization";

    String[] excludedPaths={"/login","/user/create"};
    boolean excludeFlag;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //自定义非过滤文件名数组
        excludeFlag=false;
        String undecidedUrl=request.getRequestURI();
        for(String excludedPath:excludedPaths){
            if(undecidedUrl.equals(excludedPath)){
                excludeFlag = true;
                break;
            }
        }
        //如果不是预留的非过滤路径就让filter过滤
        if(!excludeFlag){
            String token=request.getHeader(tokenSavedHeader);
            if(null!=token&&verifyToken(token)){
                token=token.replaceFirst("Bearer ", "");
                String userIdStr = JWT.decode(token).getAudience().get(0);
                filterChain.doFilter(request, response);
            }else {
                response.setStatus(401);
                PrintWriter writer = response.getWriter();
                writer.write("{\"errMsg\":\"Not authorized!\"}");
                writer.close();
            }
        }else {
            filterChain.doFilter(request, response);
        }

    }


    boolean verifyToken(String token){
        if(!token.startsWith("Bearer ")){
            return false;
        }
        token=token.replaceFirst("Bearer ", "");
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("vini_sns")).build();
        try {
            jwtVerifier.verify(token);
            return true;
        }catch(JWTVerificationException e){
            return false;
        }
    }
}
