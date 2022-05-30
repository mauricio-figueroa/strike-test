package service.auth;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

@Service
public class JWTTokenService implements Clock, TokenService {
  private static final String DOT = ".";
  private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

  private final String issuer;
  private final int expirationSec;
  private final int clockSkewSec;
  private final String secretKey;

  private HashSet<String> CURRENT_TOKENS = new HashSet();

  private JWTTokenService(
      @Value("${jwt.issuer:octoperf}") final String issuer,
      @Value("${jwt.expiration-sec:86400}") final int expirationSec,
      @Value("${jwt.clock-skew-sec:60000}") final int clockSkewSec,
      @Value("${jwt.secret:secret:jwt-secret}") final String secret) {
    super();
    this.issuer = issuer;
    this.expirationSec = expirationSec;
    this.clockSkewSec = clockSkewSec;
    this.secretKey = TextCodec.BASE64.encode(secret);
  }

  @Override
  public String permanent(final Map<String, String> attributes) {
    return newToken(attributes, 0);
  }

  @Override
  public String expiring(final Map<String, String> attributes) {
    return newToken(attributes, expirationSec);
  }

  private String newToken(final Map<String, String> attributes, final int expiresInSec) {
    final LocalDateTime now = LocalDateTime.now();
    ZoneId defaultZoneId = ZoneId.systemDefault();
    final Date date = Date.from(now.atZone(defaultZoneId).toInstant());

    final Claims claims = Jwts.claims().setIssuer(issuer).setIssuedAt(date);

    if (expiresInSec > 0) {
      final LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expiresInSec);
      Date expiresAtDate = Date.from(expiresAt.atZone(defaultZoneId).toInstant());

      claims.setExpiration(expiresAtDate);
    }
    claims.putAll(attributes);
    final String tokenResponse =
        Jwts.builder()
            .setClaims(claims)
            .signWith(HS256, secretKey)
            .compressWith(COMPRESSION_CODEC)
            .compact();

    CURRENT_TOKENS.add(tokenResponse);

    return tokenResponse;
  }

  @Override
  public Map<String, String> verify(final String token) {

    if(!CURRENT_TOKENS.contains(token)){
      return ImmutableMap.of();
    }

    final JwtParser parser =
        Jwts.parser()
            .requireIssuer(issuer)
            .setClock(this)
            .setAllowedClockSkewSeconds(clockSkewSec)
            .setSigningKey(secretKey);
    return parseClaims(() -> parser.parseClaimsJws(token).getBody());
  }

  @Override
  public Map<String, String> untrusted(final String token) {
    final JwtParser parser =
        Jwts.parser().requireIssuer(issuer).setClock(this).setAllowedClockSkewSeconds(clockSkewSec);

    // See: https://github.com/jwtk/jjwt/issues/135
    final String withoutSignature = substringBeforeLast(token, DOT) + DOT;
    return parseClaims(() -> parser.parseClaimsJwt(withoutSignature).getBody());
  }

  private static Map<String, String> parseClaims(final Supplier<Claims> toClaims) {
    try {
      final Claims claims = toClaims.get();
      final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
      for (final Map.Entry<String, Object> e : claims.entrySet()) {
        builder.put(e.getKey(), String.valueOf(e.getValue()));
      }
      return builder.build();
    } catch (final IllegalArgumentException | JwtException e) {
      return ImmutableMap.of();
    }
  }

  private  void logout(final String token) {
    CURRENT_TOKENS.remove(token);
  }

  @Override
  public Date now() {
    final LocalDateTime expiresAt = LocalDateTime.now();

    return Date.from(expiresAt.atZone(ZoneId.systemDefault()).toInstant());
  }
}
