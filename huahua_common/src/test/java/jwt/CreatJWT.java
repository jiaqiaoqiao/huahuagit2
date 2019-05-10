package jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreatJWT {
    //生成token(jwt)
    public static void main(String[] args) {
        //Jwts.builder()创建
        // .setId("123456789")设置id
        JwtBuilder huahuaCommunity = Jwts.builder().setId("123456789")
                .setSubject("1208v admin")//使用者
                .setExpiration(new Date(new Date().getTime()+60000))
                .setIssuedAt(new Date())//jwt签发时间
                .claim("roles","admin,teacher")
                .claim("telphone","1850034106")
                .signWith(SignatureAlgorithm.HS256, "huahuaCommunity");//加密的算法ES256,加密的签名是huahuaCommunity
        System.out.println(huahuaCommunity.compact());
        //通过加盐的规则 huahuaCommunity.compact()获取token令牌
    }
}
