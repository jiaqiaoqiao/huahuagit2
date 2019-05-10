package jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QueryJWT {
    public static void main(String[] args) {
        //令牌
        String token = "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I kpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7Hg Q";

        try{
            Claims body = Jwts.parser().setSigningKey("huahuaCommunity")
                    .parseClaimsJws(token).getBody();
            System.out.println("用户的id:"+body.getId());
            System.out.println("用户角色"+body.get("roles"));
            System.out.println("用户手机号"+body.get("telphone"));
            System.out.println("用户的名称:"+body.getSubject());
            System.out.println("系统时间:"+new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            System.out.println("过期时间:"+sdf.format(body.getExpiration()));
            System.out.println("签发时间:"+sdf.format(body.getIssuedAt()));
        }catch (Exception e){
            System.out.println("签名认证失效,请重新获取");
        }


    }

}
