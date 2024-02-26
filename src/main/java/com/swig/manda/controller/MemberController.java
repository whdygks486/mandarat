package com.swig.manda.controller;

import com.swig.manda.dto.MemberDto;
import com.swig.manda.model.Member;
import com.swig.manda.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    MemberService memberService;

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("member" ,new MemberDto());
        model.addAttribute("boolean" ,true);
        return "join/joinForm";
    }

    @PostMapping("/join")
    public String joinSave(@Valid @ModelAttribute("member") MemberDto memberDto , BindingResult bindingResult ,BCryptPasswordEncoder bCryptPasswordEncoder,Model model) {




        if(bindingResult.hasErrors()){
            System.out.println("바인딩리절트"+ bindingResult);
            System.out.println("바인딩리절트 필드에러 "+bindingResult.getFieldError());
            System.out.println("바인딩리절트 올 에러"+bindingResult.getAllErrors());
           System.out.println(memberDto.getPassword());
           System.out.println(memberDto.getUsername());
           System.out.println(memberDto.getRepassword());
           model.addAttribute("boolean" ,true);
           return "join/joinForm";
       }
        String pw = memberDto.getPassword();
        String rePW = memberDto.getRepassword();

        if (!pw.equals(rePW)){
            model.addAttribute("boolean" ,false);
            System.out.println("저장안됨. = " + rePW);
            return "join/joinForm";
        }else {
            System.out.println("저장됨. = " + rePW);
            Member member= Member.createMember(memberDto,bCryptPasswordEncoder);  //DTO를 엔티티로 변환
            memberService.join(member);
            return "redirect:/member/login";
        }


   //     Member member= Member.createMember(memberDto,bCryptPasswordEncoder);  //DTO를 엔티티로 변환
 //   memberService.join(member);



    }

}