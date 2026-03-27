package studylearn.demo.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import studylearn.demo.dto.request.AuthenticationRequest;
import studylearn.demo.dto.request.IntrospectRequest;
import studylearn.demo.dto.response.AuthenticationResponse;
import studylearn.demo.dto.response.IntrospectResponse;
import studylearn.demo.entity.Student;
import studylearn.demo.exception.AppException;
import studylearn.demo.exception.ErrorCode;
import studylearn.demo.repository.StudentRepository;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    StudentRepository studentRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token=request.getToken();
        JWSVerifier verifier=new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT= SignedJWT.parse(token);
        Date expiryTime= signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }
    public AuthenticationResponse authenticate (AuthenticationRequest request){
        var student=studentRepository.findByUserName(request.getUserName()).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder(10);
        boolean authenticated= passwordEncoder.matches(request.getPassWord(),student.getPassWord());
        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var token = generateToken(student);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    private String generateToken(Student student){
        JWSHeader header =new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet= new JWTClaimsSet.Builder()
                .subject(student.getUserName())
                .issuer("devteria")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope",buildScope(student))
                .build();
        Payload payload =new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject= new JWSObject(header,payload);
        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }catch(JOSEException e){
            log.error("Cannot create token",e);
            throw new RuntimeException(e);
        }
    }
    private String buildScope(Student student) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(student.getRoles())) {
            student.getRoles().forEach(role -> {
                // 1. Thêm Role (Ví dụ: ROLE_ADMIN)
                stringJoiner.add("ROLE_"+role.getName());
                // 2. Thêm Permissions của Role đó
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                        log.info("Đã thêm Permission: {} cho Role: {}", permission.getName(), role.getName());
                    });
                } else {
                    log.warn("Role {} này không có permission nào trong DB!", role.getName());
                }
            });
        } else {
            log.error("User {} không có bất kỳ Role nào!", student.getUserName());
        }

        String scope = stringJoiner.toString();
        log.info("===> CHUỖI SCOPE CUỐI CÙNG: [{}]", scope);
        return scope;
    }
}
