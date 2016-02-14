package org.c.lins.auth.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.c.lins.auth.security.Tokens;
import org.c.lins.auth.security.jwt.verifier.MACVerifierExtended;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lins on 16-1-19.
 */
@Service
public class JWTToken implements Tokens {
    private byte[] sharedKey;

    @Override
    public String getIssuer() {
        return "issuer";
    }

    @Override
    public byte[] getSharedKey() {
        if (sharedKey == null) {
            sharedKey = generateSharedKey();
        }
        return sharedKey;
    }

    @Override
    public String createToken(Object userId) {
        try {
            JWTClaimsSet jwtClaims = new JWTClaimsSet();
            jwtClaims.setIssuer(getIssuer());
            jwtClaims.setSubject(userId.toString());
            jwtClaims.setIssueTime(new Date());
            jwtClaims.setNotBeforeTime(new Date());
            jwtClaims.setExpirationTime(new Date(new Date().getTime() + getExpirationDate()));
            jwtClaims.setJWTID(UUID.randomUUID().toString());

            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

            Payload payload = new Payload(jwtClaims.toJSONObject());

            JWSObject jwsObject = new JWSObject(header, payload);

            JWSSigner signer = new MACSigner(getSharedKey());
            jwsObject.sign(signer);
            return jwsObject.serialize();
        } catch (JOSEException ex) {
            return null;
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            SignedJWT signed = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifierExtended(getSharedKey(), signed.getJWTClaimsSet());
            return signed.verify(verifier);
        } catch (JOSEException | ParseException ex) {
            return false;
        }
    }
}
