package com.jian.community.infrastructure.bcrypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.jian.community.application.util.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@AllArgsConstructor
public class BCryptPasswordEncoder implements PasswordEncoder {

    private static final int BCRYPT_COST = 4; // 보안 강도

    @Override
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, rawPassword.toCharArray());
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword).verified;
    }
}
