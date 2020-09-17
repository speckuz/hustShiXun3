package hust.sse.vini.userpart.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

public class TokenFilter extends OncePerRequestFilter {

    private final static String tokenSavedHeader = "Authorization";

    String[] excludedPaths={"/login","/user/create","/user/exists","/user/interestOptions", "/link",
            "/group/create", "/group/search", "/group/add", "/group/permission", "/group/refuse", "/group/delete", "/group/escape", "/group/remove"};
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
                HeaderHttpServletRequestWrapper requestWrapper=new HeaderHttpServletRequestWrapper(request);
                requestWrapper.addHeader("Vini-User-Id",userIdStr);
                filterChain.doFilter(requestWrapper, response);
            }else {
                response.setStatus(401);
                PrintWriter writer = response.getWriter();
                writer.write("{\"statusCode\":\"401\",\"errMsg\":\"Not authorized!\"}");
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

class HeaderHttpServletRequestWrapper extends HttpServletRequestWrapper{

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    private final HashMap<String,String> modifiedHeaders=new HashMap<>();
    private final ArrayList<String> headerNames=new ArrayList<>();

    public HeaderHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        headerNames.addAll(Collections.list(super.getHeaderNames()));
    }

    public void addHeader(String key,String value){
        this.modifiedHeaders.put(key,value);
        this.headerNames.add(key);
    }

    @Override
    public String getHeader(String name) {
        if(this.modifiedHeaders.containsKey(name)){
            return this.modifiedHeaders.get(name);
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(headerNames);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if(this.modifiedHeaders.containsKey(name)){
            return Collections.enumeration(Arrays.asList(modifiedHeaders.get(name)));
        }
        return super.getHeaders(name);
    }
}
