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
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            member1.setType(MemberType.ADMIN);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            member2.setType(MemberType.ADMIN);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            member3.setType(MemberType.ADMIN);
            em.persist(member3);

            em.flush();
            em.clear();

            // Entity 직접 사용
            String entityQuery = "select m from Member m where m = :member";
            Member findMember = em.createQuery(entityQuery, Member.class)
                    .setParameter("member", member1)
                    .getSingleResult();
            System.out.println("findMember = " + findMember);

            // 외래키 직접 사용
            String foreignQuery = "select m from Member m where m.team = :team";
            List<Member> members = em.createQuery(foreignQuery, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();

            for (Member member : members) {
                System.out.println("member = " + member);
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
