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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(20);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            // 서브쿼리 예제
            String query1 = "select m from Member m where m.age >= (select avg(m2.age) from Member m2)";
            List<Member> resultList = em.createQuery(query1, Member.class).getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1.getId() = " + member1.getId());
                System.out.println("member1.getUsername() = " + member1.getUsername());
                System.out.println("====================");
            }
            
            tx.commit();

            } catch (Exception e) {
                tx.rollback();
            } finally {
                em.close();
                emf.close();
            }
    }
}
