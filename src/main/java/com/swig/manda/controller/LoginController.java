package com.swig.manda.controller;

import com.swig.manda.dto.MailDto;
import com.swig.manda.dto.MemberDto;
import com.swig.manda.dto.PWupdateDto;
import com.swig.manda.model.Member;
import com.swig.manda.repository.MemberRepository;
import com.swig.manda.service.MemberService;
import com.swig.manda.service.SendMailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Controller
@Transactional
@RequestMapping("/member")
public class LoginController {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    MemberService memberService;
    @Autowired
    SendMailService sendMailService;

    @GetMapping("/login")
    public String loginForm() {
        return "login/loginForm";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "login/loginSuccess";
    }

    @GetMapping("/explanation")
    public String explanation() {

        return "login/explanation";
    }

    @GetMapping("/duplicate")
    public @ResponseBody Boolean duplicate(@RequestParam("username") String username) {
        System.out.println("username있는지 확인 = " + username);
        Boolean isDupplicate = memberService.duplicateUsername(username);

        System.out.println("isDupplicate 인지 확인 = " + isDupplicate);
        //AJAX 이메일 중복확인 요청처리
        //   Map<String, Boolean> map = new HashMap<String, Boolean>();

        //   Boolean check = memberService.duplicateEmail(email);
        //  map.put("check", check);

        return isDupplicate;

    }

    @GetMapping("/auth/login")
    public String failurvalid(@RequestParam("error") String error, @RequestParam("exception") String exception, Model model) {

        // model.addAttribute("error",error);
        model.addAttribute("exception", exception);
        model.addAttribute("boolean", true);
        return "login/loginForm";
    }

    @GetMapping("/pwCheck")
    public String pwCheck(Model model) {

        model.addAttribute("dto", new MailDto());
        return "login/pwCheck";
    }

    @GetMapping("/check/findPw")
    public @ResponseBody Map<String, Boolean> pw_find(String email, String username) {
        Map<String, Boolean> json = new HashMap<>();
        boolean pwFindCheck = memberService.userEmailCheck(email, username);

        System.out.println(pwFindCheck);
        json.put("check", pwFindCheck);
        return json;
    }

    //등록된 이메일로 임시비밀번호를 발송하고 발송된 임시비밀번호로 사용자의 pw를 변경하는 컨트롤러
    @PostMapping("/check/findPw/sendEmail")
    public String sendEmail(String email, String username) {
        System.out.println("포스트 실행됨");
        MailDto dto = sendMailService.createMailAndChangePassword(email, username);
        sendMailService.mailSend(dto);
        return "redirect:/member/pwUpdate";
    }

    @GetMapping("/pwUpdate")
    public String pwUpdateForm(Model model) {
        model.addAttribute("member", new PWupdateDto());

        return "login/pwupdate";
    }

    @PostMapping("/pwUpdate")
    public String pwUpdate(@Valid @ModelAttribute("member") PWupdateDto pwUpdateDto, BindingResult bindingResult, BCryptPasswordEncoder bCryptPasswordEncoder, Model model) {
        Member member = memberRepository.findByUsername(pwUpdateDto.getUsername());
        String memberEncodedPW = memberRepository.findByPw(pwUpdateDto.getUsername());
        if (bindingResult.hasErrors()) {
            if (pwUpdateDto.getNewPassword().equals(pwUpdateDto.getNewRepassword())) {
                model.addAttribute("boolean", true);
            } else {
                model.addAttribute("boolean", false);
            }
            if (member == null || !bCryptPasswordEncoder.matches(pwUpdateDto.getPassword(), memberEncodedPW)) {
                bindingResult.addError(new ObjectError("pwUpdateDto", "아이디 또는 비밀번호가 정확하지 않습니다"));
            }
            System.out.println("바인딩 리절트 리턴이다.");
            return "login/pwupdate";
        } else if (member == null || !bCryptPasswordEncoder.matches(pwUpdateDto.getPassword(), memberEncodedPW)) {
            if (pwUpdateDto.getNewPassword().equals(pwUpdateDto.getNewRepassword())) {
                model.addAttribute("boolean", true);
            } else {
                model.addAttribute("boolean", false);
            }
            bindingResult.addError(new ObjectError("pwUpdateDto", "아이디 또는 비밀번호가 정확하지 않습니다"));
            return "login/pwupdate";
        }

        //    System.out.println("인코딩된 비번" + memberEncodedPW);
        //    if (member == null || !bCryptPasswordEncoder.matches(pwUpdateDto.getPassword(), memberEncodedPW)) {
        //        bindingResult.addError(new ObjectError("pwUpdateDto", "아이디 또는 비밀번호가 정확하지 않습니다"));
        //       return "login/pwupdate";
        //     }
        //      if(bCryptPasswordEncoder.matches(pwUpdateDto.getPassword(),memberEncodedPW) == false){
        ///           bindingResult.addError(new FieldError("pwUpdateDto","password","비밀번호가 정확하지 않습니다"));
        //         return "login/pwupdate";
        //      }
        //     String memberUsername = member.getUsername();

        //   Boolean result = memberService.duplicateUsername(pwUpdateDto.getUsername());
  /*      if(result == false ) {
            bindingResult.addError(new FieldError("pwUpdateDto","username","아이디 또는 비밀번호가 정확하지 않습니다"));
            return "login/pwupdate";
        }
        if(bCryptPasswordEncoder.matches(pwUpdateDto.getPassword(),memberEncodedPW) == false){
            bindingResult.addError(new FieldError("pwUpdateDto","password","아이디 또는 비밀번호가 정확하지 않습니다"));
            return "login/pwupdate";
        }*/


//        if(member.g ==null){
// /              return "login/pwupdate";
        //          }

        //    if (pwUpdateDto.getUsername().equals(memberUsername)) {
        //        if (bCryptPasswordEncoder.matches(pwUpdateDto.getPassword(), memberEncodedPW)) {

        //     }

        //}

//모든 검증이 성공하면 최종적으로 아래에서 업데이트하는 내용 추가하면 된다.
        //비밀번호 불일치 내용은 모든내용에 넣어준다. 나중에 메소드만들어서 한줄로 하자.
        System.out.println("마지막 리턴실행된것이다.");
        if (pwUpdateDto.getNewPassword().equals(pwUpdateDto.getNewRepassword())) {
            model.addAttribute("boolean", true);
        } else {

            model.addAttribute("boolean", false);
            return "login/pwupdate";
        }
        String newPW = pwUpdateDto.getNewPassword();
        System.out.println("뉴 패스워드 "+newPW);
        System.out.println("뉴리 패스워드 "+pwUpdateDto.getNewRepassword());
        String encodedNewPW = bCryptPasswordEncoder.encode(newPW);
        System.out.println("인코디드 패스워드 " +encodedNewPW);
       Member updatemember = memberRepository.findByUsername(pwUpdateDto.getUsername());

        updatemember.setPassword(encodedNewPW);
        updatemember.setRepassword(encodedNewPW);
        memberRepository.save(updatemember);
        return "redirect:/member/login";
    }
//OAuth Test
  /*  @GetMapping("/test/oauth")
    public @ResponseBody String testOAuth(Authentication authentication) {

        OAuth2User oAuth2User= (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication어덴티케이션 = " + oAuth2User.getAttributes());

        return "test/oauth";'
        \

    }
    @GetMapping("/test/oauth2")
    public @ResponseBody String testOAuth2(@AuthenticationPrincipal OAuth2User oAuth2User) {

        System.out.println("authentication어덴티케이션 = " + oAuth2User.getAttributes());
        System.out.println("authentication어덴티케이션 = " + oAuth2User.getAttribute("provider"));
        System.out.println("authentication어덴티케이션 = " + oAuth2User.getAttributes());
        System.out.println("authentication어덴티케이션 = " + oAuth2User.getAttribute(oAuth2User.getName()));
        System.out.println("authentication어덴티케이션 = " + oAuth2User.getName());

        return "test/oauth2";
    }
    @GetMapping("/test/oauth3")
    public @ResponseBody String testOAuth3(Authentication authentication) {
                   PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication어덴티케이션 = " + principalDetails.getAuthorities());
        System.out.println("authentication어덴티케이션 = " + principalDetails.getPassword());
        System.out.println("authentication어덴티케이션 = " + principalDetails.getUsername());
        System.out.println("authentication어덴티케이션 = " + principalDetails.getName());

        return "test/oauth3";
    }
    //@GetMapping("/test/oauth4")
    public @ResponseBody String testOAuth4(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        PrincipalDetails prin = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println("authentication어덴티케이션 = " + principalDetails.getAuthorities());
        System.out.println("authentication어덴티케이션 = " + principalDetails.getPassword());
        System.out.println("authentication어덴티케이션 = " + principalDetails.getUsername());
        System.out.println("authentication어덴티케이션 = " + principalDetails.getName());
        System.out.println("authentication어덴티케이션 = " + principalDetails.getAuthorities());

        System.out.println("authentication어덴티케이션 = " + prin.getPassword()


        return "test/oauth4";
    }
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        
        System.out.println("principalDetails.getMember() = " + principalDetails.getMember());



        return "member user";
    }*/
}
