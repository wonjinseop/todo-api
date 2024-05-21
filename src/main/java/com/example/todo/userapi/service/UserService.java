package com.example.todo.userapi.service;

import com.example.todo.auth.TokenProvider;
import com.example.todo.userapi.dto.request.LoginRequestDTO;
import com.example.todo.userapi.dto.request.UserSignUpRequestDTO;
import com.example.todo.userapi.dto.response.LoginResponseDTO;
import com.example.todo.userapi.dto.response.UserSignUpResponseDTO;
import com.example.todo.userapi.entity.User;
import com.example.todo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    
    public boolean isDuplicate(String email) {
//        int count = userRepository.findByEmail(email);
//        return count == 0;
        if (userRepository.existsByEmail(email)) {
            log.warn("이메일이 중복되었습니다. - {}", email);
            return true;
        } else return false;
    }
    
    public UserSignUpResponseDTO create(final UserSignUpRequestDTO dto) throws Exception {
        String email = dto.getEmail();
        
        if (isDuplicate(email)) {
            throw new RuntimeException("중복된 이메일 입니다.");
        }
        
        // 패스워드 인코딩
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        // dto를 User Entity로 변환해서 저장.
        User saved = userRepository.save(dto.toEntity());
        log.info("회원 가입 정상 수행 됨! - saved user - {}", saved);
        
        return new UserSignUpResponseDTO(saved);
    }
    
    public LoginResponseDTO authenticate(LoginRequestDTO dto) throws Exception {
        
        // 이메일을 통해 회원 정보 조회
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디 입니다."));
        
        // 패스워드 검증
        String rawPassword = dto.getPassword();
        String encodedPassword = user.getPassword();
        
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
        
        log.info("{}님 로그인 성공!", user.getUserName());
        
        // 로그인 성공 후에 클라이언트에게 뭘 리턴해 줄 것인가?
        // -> JWT를 클라이언트에 발급해 주어야 한다! -> 로그인 유지를 위해!
        String token = tokenProvider.createToken(user);
        
        return new LoginResponseDTO(user, token);
    }
    
}
