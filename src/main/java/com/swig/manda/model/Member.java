package com.swig.manda.model;

import com.swig.manda.dto.MemberDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Member {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String repassword;
    private String role; //ROLE_USER, ROLE_ADMIN
     private String provider;
    private String providerId;
    @CreationTimestamp
    private LocalDateTime regTime;  //회원 가입시간
    @CreationTimestamp
    private LocalDateTime loginTime; //회원 로그인시간


    public Member(String username, String password, String repassword, String role, LocalDateTime regTime, LocalDateTime loginTime) {
        this.username = username;
        this.password = password;
        this.repassword = repassword;
        this.role = role;
        this.regTime = regTime;
        this.loginTime = loginTime;
    }

    public static Member createMember(MemberDto dto, BCryptPasswordEncoder bCryptPasswordEncoder) {
        Member member = new Member();
        member.setUsername(dto.getUsername());
        String encodedPW = bCryptPasswordEncoder.encode(dto.getPassword());
        member.setPassword(encodedPW);
        member.setRepassword(encodedPW);
        member.setRole("USER");
        return member;
    }
}
