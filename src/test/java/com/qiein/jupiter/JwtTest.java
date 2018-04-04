package com.qiein.jupiter;

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
    @Value("${jwt-token}")
    private String key;

    @Test
    public void compact(){
//        Key key = MacProvider.generateKey();
        String compactJws = Jwts.builder()
                .setSubject("Joe")
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        System.out.println(compactJws);
        String subject = Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getSubject();
        System.out.println(subject);
    }

    @Test
    public  void parse(){
        String compactJws="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKb2UifQ.SjYwBMIgUdaMwtL5Y1MT_2EwDhEDwS9dybMh6NGzxtGypf1jzeZCnnwHtT8O7G541WsEmiuhjs0-retHvpdDjQ";
        Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getSubject().equals("Joe");
    }
}
