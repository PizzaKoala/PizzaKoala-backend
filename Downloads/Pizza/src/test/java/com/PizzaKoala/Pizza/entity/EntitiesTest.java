//package com.PizzaKoala.Pizza.entity;
//
//import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
//import com.PizzaKoala.Pizza.domain.entity.Member;
//import jakarta.persistence.EntityManager;
//import jakarta.transaction.Transactional;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//
//import java.util.List;
//
//@SpringBootTest
//@Transactional
//public class EntitiesTest {
//    @Autowired
//    private MemberRepository mbRepo;
//    @Autowired
//    private EntityManager entityManager;
//
////    @Test
////    public void MemberEntityTest() {
////        Member mb = Member.builder()
////                .email("slugshu@kakao.com")
////                .password("meep")
////                .nickName("Meep")
////                .build();
////        mbRepo.save(mb);
////        entityManager.flush();
////        entityManager.clear();
////        List<Member> mb2 = mbRepo.findAll();
////        Member mb3 = mb2.get(0);
////        Assertions.assertThat(mb.getEmail()).isEqualTo(mb3.getEmail());
////        System.out.println("mb2.getCreatedAt() = " + mb3());
////
////
////    }
//
//}
