package com.qiein.jupiter;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTest {
    private String key = "qieK4cua9YHNs98mztRin";

    @Test
    public void compact() {
//        Key key = MacProvider.generateKey();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        jsonObject.put("phone", "131000000000");
        jsonObject.put("uid", 1);
        jsonObject.put("cid", 1);

        String compactJws = Jwts.builder()
                .setSubject(jsonObject.toString())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        System.out.println(compactJws);
        String subject = Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getSubject();
        System.out.println(subject);
    }

    @Test
    public void parse() {
        String j = "eyJhbGciOiJIUzUxMiJ9.1eyJzdWIiOiJ7XCJ1aWRcIjoxLFwiaWRcIjoxLFwicGhvbmVcIjpcIjEzMTAwMDAwMDAwMFwiLFwiY2lkXCI6MX0ifQ.SV-60W1_E2Kr1RbKgyFtMs3fUDFrPyMgoCRPRy3C3SoiDhAiDbrdEYF6sSQuOl86lODhnuaApq3JUDoHrct3OA";
        String subject = Jwts.parser().setSigningKey(key).parseClaimsJws(j).getBody().getSubject();
        System.out.println(subject);
    }
}
