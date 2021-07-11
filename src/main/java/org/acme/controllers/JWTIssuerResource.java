package org.acme.controllers;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;


import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Slf4j
@Path("/jwt")
@Tag(name = "JWTIssuerResource", description = "test jwt token")
public class JWTIssuerResource {

    @Inject
    @ConfigProperty(name="mp.jwt.verify.issuer",
      defaultValue="my-issuer-name" )
    String jwtIssuer;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{firstName}/{lastName}/generate-token-for")
    public Response getJWT(@PathParam("firstName") String firstName,
                           @PathParam("lastName") String lastName) {
        String jwt = null;
        try {
            Map<String, Object> rolesMap = new HashMap<>();
            rolesMap.put("role 1", "VIP");
            rolesMap.put("role 2", "WIP");
            rolesMap.put("role 3", "visitor");
            List<String> groups = new ArrayList<>();
            groups.add("Group 1");
            jwt = generateAndSignJWT(
              firstName, lastName, groups, rolesMap);
        } catch (InvalidKeySpecException
          | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return Response.ok(jwt).build();
    }

    PrivateKey loadPrivateKey() throws IOException,
      InvalidKeySpecException, NoSuchAlgorithmException {
        // load the raw file
        byte[] keyFileBuffer = JWTIssuerResource.class
          .getResourceAsStream(
          "/META-INF/resources/openssl/privateKey.pem"
        ).readAllBytes();

        //strip marker text from the raw key text
        String cleanKey = new String(keyFileBuffer, 0,
          keyFileBuffer.length)
          .replaceAll("-----BEGIN (.*)-----", "")
          .replaceAll("-----END (.*)----", "")
          .replaceAll("\r\n", "")
          .replaceAll("\n", "")
          .trim();

        //convert text to PKCS8/RSA key
        return KeyFactory.getInstance("RSA").generatePrivate(
          new PKCS8EncodedKeySpec(Base64.getDecoder().decode(cleanKey)));
    }

    String generateAndSignJWT(String firstName, String lastName,
                              List groups, Map roles) throws
      InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("firstName", firstName);
        claimsMap.put("lastName", lastName);
        JwtClaimsBuilder claims = Jwt.claims(claimsMap)
          .subject(firstName + " " + lastName)
          .claim("roleMappings", roles)
          .claim("groups", groups)
          .issuer(jwtIssuer)
          .issuedAt(Instant.now().toEpochMilli())
          .expiresAt(Instant.now().plus(2, ChronoUnit.DAYS));
        PrivateKey privateKey = loadPrivateKey();
        return claims.jws().sign(privateKey);
    }
}
