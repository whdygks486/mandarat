package com.swig.manda.service;

import com.swig.manda.dto.MailDto;
import com.swig.manda.model.Member;
import com.swig.manda.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
@Service  @Transactional
public class SendMailService {
    @Autowired
    private PasswordEncoder passwordEncoder;
@Autowired
    MemberRepository memberRepository;
@Autowired
    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "whdygks4@gmail.com123";



    public MailDto createMailAndChangePassword(String email, String username){
        System.out.println("유저네임 크리에이트"+username);
        System.out.println("유저이메일 크리에잍"+email);
     //   Member member = memberRepository.findByUsername(username);
        String str = getTempPassword();
        MailDto dto = new MailDto();
        dto.setAddress(email);
        dto.setTitle(username+"님의 만다라트 임시 비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. 만다라트 임시 비밀번호 안내 관련 이메일 입니다." + "[" + username + "]" +"님의 임시 비밀번호는 "
                + str + " 입니다.");
        updatePassword(str,username);
        return dto;
    }


    public void updatePassword(String str,String username){
        String pw = passwordEncoder.encode(str);
        Long id = memberRepository.findByUsername(username).getId();
        System.out.println("아이디야"+id);
        System.out.println("유저네임이야"+username);
        memberRepository.updateUserPassword(pw,id);
    }


    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
    public void mailSend(MailDto mailDto){
        System.out.println("메일센드 디티오"+mailDto);
        System.out.println("유저이메일체크 메소드 호출함");
        System.out.println("이멜 전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(SendMailService.FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());
        System.out.println("메세지 심플메일메시니"+message);
        mailSender.send(message);
    }
}
