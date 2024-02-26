package com.swig.manda;

import com.swig.manda.model.Member;
import com.swig.manda.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Init {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    MemberRepository memberRepository;

    @PostConstruct
    private void initialize(){
        String oriPW = "alsdl1004!";
        String encodedPW = bCryptPasswordEncoder.encode(oriPW);

        Member member = new Member();
        member.setUsername("whdygks486");
        member.setPassword(encodedPW);
        member.setRepassword(encodedPW);
        member.setRegTime(LocalDateTime.now());
        member.setRole("ADMIN");
        memberRepository.save(member);
    }
}
