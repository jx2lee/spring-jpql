package com.jx2lee.springjpql;

import com.jx2lee.springjpql.domain.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("spring-jpql");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("test");
            em.persist(team);

            Member member = new Member();
            member.setUsername("관리자A");
            member.setAge(20);
            member.setTeam(team);
            member.setType(MemberType.ADMIN);
            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("관리자B");
            member2.setAge(30);
            member2.setTeam(team);
            member2.setType(MemberType.ADMIN);
            em.persist(member2);

            em.flush();
            em.clear();

            // 명시적 조인 예시
            String sizeQuery = "select m.username from Team t join t.members m";

            Collection result = em.createQuery(sizeQuery, String.class).getResultList();
            System.out.println("result = " + result);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
