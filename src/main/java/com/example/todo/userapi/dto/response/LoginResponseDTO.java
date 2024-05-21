package com.example.todo.userapi.dto.response;

import com.example.todo.userapi.entity.Role;
import com.example.todo.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 로그인 성공 후 클라이언트에게 전송할 데이터 객체
public class LoginResponseDTO {
    
    private String email;
    private String userName;
    private Role role;
    
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDate joinDate;
    
    private String token;
    
    public LoginResponseDTO(User user, String token) {
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.role = user.getRole();
        this.joinDate = LocalDate.from(user.getJoinDate());
        this.token = token;
    }
    
}
