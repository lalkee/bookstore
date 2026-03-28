package com.lalke.bookstore.security;

import com.lalke.bookstore.domain.Role;
import com.lalke.bookstore.domain.User;
import com.lalke.bookstore.dto.UserDto;
import com.lalke.bookstore.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerNewUserAccount(UserDto userDto) throws RuntimeException {
        if (emailExists(userDto.getEmail())) {
            throw new RuntimeException("An account for that email already exists: " + userDto.getEmail());
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(false);

        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    public boolean validateVerificationToken(String token) {
        Optional<VerificationToken> verificationTokenOpt = verificationTokenRepository.findByToken(token);

        if (verificationTokenOpt.isEmpty()) {
            return false;
        }

        VerificationToken verificationToken = verificationTokenOpt.get();
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return false;
        }

        user.setEnabled(true);
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken);

        return true;
    }

    public void deleteUser(User user) {
        verificationTokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }
}
