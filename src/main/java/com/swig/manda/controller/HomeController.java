package com.swig.manda.controller;

import com.swig.manda.model.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String str = "";
        String idx2="";
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            idx2 = String.valueOf((charSet.length * Math.random()));
           // System.out.println("idx 2 이다  "+ idx2);
       //     System.out.println("캐릭터셋 랭쓰 "+ charSet.length);
          //  System.out.println("메스점렌덤 "+ Math.random());
            System.out.println("인덱스 이다 ::: "+ idx);
            str += charSet[idx];

            System.out.println("str 테스트이다 처음 :: " + str);
        }
     //   System.out.println("인덱스 마지막 이다 ::: "+ idx);
        System.out.println("str 테스트이다 마지막 :: " + str);
        model.addAttribute("member", new Member());


        return "index";





    }
}