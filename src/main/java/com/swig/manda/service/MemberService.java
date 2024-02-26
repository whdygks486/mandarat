package com.swig.manda.service;

import com.swig.manda.dto.MemberDto;
import com.swig.manda.model.Member;
import com.swig.manda.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MemberService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;

public void join(Member member){

  //  newmember.setRegTime(LocalDateTime.now());
    //JPA 가 @TimeStamCreation 있으면 자동생성.

    memberRepository.save(member);


}

    public Boolean duplicateUsername(String username){

    return memberRepository.existsByUsername(username);

    }
    public boolean userEmailCheck(String email, String username) {
        System.out.println("유저이메일체크 메소드 호출함");
        Member member = memberRepository.findByUsername(username);
        if(member!=null && member.getUsername().equals(username)) {
            return true;
        }
        else {
            return false;
        }
    }

}
