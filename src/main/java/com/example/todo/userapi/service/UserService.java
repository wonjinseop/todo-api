package com.example.todo.userapi.service;

import com.example.todo.userapi.dto.request.LoginRequestDTO;
import com.example.todo.userapi.dto.request.UserSignUpRequestDTO;
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
    
    
}
