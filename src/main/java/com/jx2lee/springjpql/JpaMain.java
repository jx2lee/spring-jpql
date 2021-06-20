package com.jx2lee.springjpql;

import com.jx2lee.springjpql.domain.*;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("spring-jpql");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("jx2lee");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            List<Team> resultList = em.createQuery(
                    "select t from Member m join m.team t", Team.class)
                    // "select m.team from Member m", Team.class) 대신 join 을 명시해주는 것이 운영에 좋다.
                    .getResultList();

            for (Team team1 : resultList) {
                System.out.println("member1 = " + team1);
            }

            em.createQuery("select o.address from Order o", Address.class).getResultList();
            em.createQuery("select o.id, o.orderAmount from Order o").getResultList();
            // new 연산자 이용방법
            List<MemberDTO> resultList2 = em.createQuery(
                    "select new com.jx2lee.springjpql.domain.MemberDTO(m.username, m.age) from Member m",
                    MemberDTO.class).getResultList();
            MemberDTO memberDTO = resultList2.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge = " + memberDTO.getAge());


            tx.commit();

            } catch (Exception e) {
                tx.rollback();
            } finally {
                em.close();
                emf.close();
            }
    }
}
