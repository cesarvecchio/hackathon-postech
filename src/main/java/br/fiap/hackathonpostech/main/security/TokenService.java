//package br.fiap.hackathonpostech.main.security;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TokenService {
//    @Value("${api.security.token.secret}")
//    private String secret;
//    @Value("${api.security.token.issuer}")
//    private String issuer;
//
//    public String validateToken(String token){
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(secret);
//            return JWT.require(algorithm)
//                    .withIssuer(issuer)
//                    .build()
//                    .verify(token)
//                    .getSubject();
//        } catch (JWTVerificationException exception){
//            return "";
//        }
//    }
//
//}