package com.sunyard.cloud.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 描述: jwt 工具类
 *
 */
public class JwtUtil {


	private static String ISSUER = "sunyard";

	/**
	 * 由字符串生成加密key
	 *
	 * @return
	 */
	public static SecretKey generalKey(String keyStr) {
		byte[] encodedKey = Base64.getEncoder().encode(keyStr.substring(8).getBytes());
		return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
	}

	/**
	 * 创建jwt
	 * 
	 * @param subject 私有信息
	 * @param timeOut 超时时间（分钟）
	 * @param keyStr  密钥字符串
	 * @return
	 * @throws Exception
	 */
	public static String createJWT(String keyStr, long timeOut,String clientIp, String userNo, String organNo, String subject)
			throws Exception {
		// 生成JWT的时间
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		Map<String, Object> claims = new HashMap<>();
		claims.put("clientIp", clientIp);
		claims.put("userNo", userNo);
		claims.put("organNo", organNo);

		JwtBuilder builder = Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
				.setClaims(claims) // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
				.setId(UUID.randomUUID().toString()) // 设置jti(JWTID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
				.setIssuedAt(now) // iat: jwt的签发时间
				.setIssuer(ISSUER) // issuer：jwt签发人
//              .setSubject(subject)        // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
				.signWith(SignatureAlgorithm.HS256, generalKey(keyStr)); // 设置签名使用的签名算法和签名使用的秘钥

		if (subject != null && subject.length() > 0) {
			builder.setSubject(subject);
		}

		if (timeOut >= 0) {
			long expMillis = nowMillis + timeOut * 1000 * 60;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		return builder.compact();
	}


	/**
	 * 校验token
	 * 
	 * @param jwt
	 * @param keyStr
	 * @return
	 */
	public static JwtCheckResult checkToken(String jwt, String keyStr) {
		try {
			Claims claims = Jwts.parser() // 得到DefaultJwtParser
					.setSigningKey(generalKey(keyStr)) // 设置签名的秘钥
					.parseClaimsJws(jwt).getBody(); // 设置需要解析的jwt
			return new JwtCheckResult(true, claims, null);
		} catch (ExpiredJwtException e) {
			// TODO: handle exception
			String message = "";
			if (e.getMessage().contains("Allowed clock skew")) {
				message = "token 超时！";
			} else {
				message = "token 认证失败_1！";
			}
			return new JwtCheckResult(false, null, message);
		} catch (Exception e) {
			// TODO: handle exception
			return new JwtCheckResult(false, null, "token 认证失败_2！");
		}
	}
}
