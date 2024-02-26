package com.swig.manda.config.oauth;

import com.swig.manda.config.auth.PrincipalDetails;
import com.swig.manda.model.Member;
import com.swig.manda.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//OAuth 구글 심사요청 기간이 6주 소요되어 기한내에 개발 불가능


@Service
@Transactional
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
@Autowired
    private MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest 유저리퀘스트  = " + userRequest.getAccessToken() +
                "겟클라이언트아이디"+
                userRequest.getClientRegistration().getClientId() +
                "겟클라이언트이름"+
                userRequest.getClientRegistration().getClientName() +
                "겟레지스트레션아이디"+
                userRequest.getClientRegistration().getRegistrationId()
        );

        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientId(); //google
        String providerId = oauth2User.getAttribute("sub");
        String username = provider+"_"+providerId;
        String password = bCryptPasswordEncoder.encode("getinthere");
        String repassword = password;
        String role ="USER";
        Member userEntity =  memberRepository.findByUsername(username);
        System.out.println("userEntity 구글 널이지확인= " + userEntity);
        if(userEntity ==null){
                userEntity =  Member.builder()
                        .username(username)
                        .password(password)
                        .repassword(repassword)
                        .role(role)
                        .provider(provider)
                        .providerId(providerId)
                        .build();
                memberRepository.save(userEntity);
            System.out.println("당신은 구글");
        }else {
            System.out.println("구글로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어 있습니다.");
        }

        return new PrincipalDetails(userEntity,oauth2User.getAttributes());

    }
}
