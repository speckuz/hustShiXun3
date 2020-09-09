package hust.sse.vini.userpart.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class TokenUtils {
    public static String generateToken(Integer userId){
        Date date = new Date();
        return "Bearer "+JWT.create().withExpiresAt(new Date(date.getTime()+300*1000L)).withAudience(String.valueOf(userId)).sign(Algorithm.HMAC256("vini_sns"));
    }
}
