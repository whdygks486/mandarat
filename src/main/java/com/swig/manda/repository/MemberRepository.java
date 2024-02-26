package com.swig.manda.repository;

import com.swig.manda.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;


@Transactional
public interface MemberRepository extends JpaRepository<Member,Long> {

   Member findByUsername(String username);

    Boolean existsByUsername(String username);
    // Member findUserByUserId(String username);
    @Transactional
    @Query("select m.password from Member m where m.username = :username ")
     String findByPw(@Param("username") String username );
@Transactional
@Modifying//셀렉트 후에 업데이트 쿼리를 날려주는거를 스프링이 알고있어야하기 때문에 @Modifying 넣어야 한다.
    @Query("UPDATE Member m set m.password = :password, m.repassword = :password where m.id = :id")
    void updateUserPassword(@Param("password") String pw,@Param("id") Long id );

 //   public Member findByUsername(String oauthUsername);
}

