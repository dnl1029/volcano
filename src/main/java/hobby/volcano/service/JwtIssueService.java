package hobby.volcano.service;

import hobby.volcano.common.CustomEnum;
import hobby.volcano.common.RestApiException;
import hobby.volcano.dto.UserIdRequestDto;
import hobby.volcano.entity.Member;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

import static hobby.volcano.common.CommonErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtIssueService {

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    private final MemberService memberService;

    // jwt 토큰 생성 메서드
    public String createJwt(UserIdRequestDto userIdRequestDto) {
        Date now = new Date();
        Optional<Member> member = memberService.getMember(userIdRequestDto);

        if (member.isEmpty()) {
            throw new RestApiException(USER_NOT_FOUND);
        } else {
            String jwt = Jwts.builder()
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setClaims(createClaims(userIdRequestDto))
                    .setIssuedAt(now)
                    .setExpiration(createExpiredDate()) // 발급날짜 계산
                    .signWith(SignatureAlgorithm.HS256, createSignature())
                    .compact();
            log.info("createJwt. 로그인 성공. userID : {}, jwt : {}",userIdRequestDto.getUserId(),jwt);
            return jwt;
        }
    }

    // Claims 생성 메서드. payload 정보 세팅
    private Map<String, Object> createClaims(UserIdRequestDto userIdRequestDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CustomEnum.USER_ID.getContent(), userIdRequestDto.getUserId());
        return claims;
    }

    // jwt 서명(Signature) 발급해주는 메서드
    private Key createSignature() {
        // Base64 인코딩을 적용
        byte[] apiKeySecretBytes = java.util.Base64.getEncoder().encode(jwtSecretKey.getBytes());
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    // jwt 복호화 메서드
    public Jws<Claims> getClaims(String jwt) {
        try {
            byte[] apiKeySecretBytes = java.util.Base64.getEncoder().encode(jwtSecretKey.getBytes());
            return Jwts.parser().setSigningKey(apiKeySecretBytes).parseClaimsJws(jwt);
        } catch (SignatureException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    // jwt 만료날짜 메서드
    private Date createExpiredDate() {
        Calendar now = Calendar.getInstance();
        // 만료날짜 1일
        now.add(Calendar.DATE, 1);
        // jwt 만료테스트시
//        now.add(Calendar.SECOND, 10);
        return now.getTime();
    }

    // jwt 유효성 체크 메서드
    public boolean tokenValidCheck(String jwt) {
        try {
            Jws<Claims> claims = getClaims(jwt);
//            log.info("expireTime : {}", claims.getBody().getExpiration());
//            log.info("userId : {}", claims.getBody().get(CustomEnum.USER_ID.getContent()));
            return true;
        }
        catch (ExpiredJwtException e) {
            throw new RestApiException(JWT_EXPIRATION);
        }
        catch (JwtException e) {
            throw new RestApiException(JWT_INVALID);
        }
        catch (NullPointerException e) {
            throw new RestApiException(JWT_INVALID);
        }
    }
}
