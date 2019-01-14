package com.ferhat.generateToken.service;

import com.ferhat.generateToken.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class GenerateTokenService {

    @Value("${tokenSecretKey}")
    private String secretKey;

    @Value("${audMeetHub}")
    private String aud;

    @Value("${issMeetHub}")
    private String iss;

    @Value("${subMeetHub}")
    private String sub;


    public String create(UserInfo userInfo) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key singingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        return addPayloadToToken(signatureAlgorithm, singingKey, userInfo);
    }

    public Claims decodeToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token).getBody();
        /*
        List<String> tokenInfoList = new ArrayList<>();
        tokenInfoList.add(claims.getSubject());
        tokenInfoList.add(claims.getAudience());
        tokenInfoList.add(claims.);
        return tokenInfoList;
        */
        return claims;
    }

    private String addPayloadToToken(SignatureAlgorithm signatureAlgorithm, Key singingKey, UserInfo userInfo) {
        userInfo.setAvatar("https:/gravatar.com/avatar/abc123");
        long expireDate = 1577826000000L;
        Date date = new Date(expireDate);

        Claims subClaims = new DefaultClaims();
        subClaims.put("user", userInfo);

        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("context", subClaims)
                .setAudience(aud)
                .setIssuer(iss)
                .setSubject(sub)
                .claim("room", "test50")
                .setExpiration(date)
                .signWith(signatureAlgorithm, singingKey);


        return builder.compact();
    }
}
