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
            member.setUsername("test");
            member.setAge(10);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String query1 = "select m from Member m, Team t where m.username = t.name";
            em.createQuery(query1, Member.class).getResultList();

            // on - 대상 필터링 (teamA 만 조회)
            String query2 = "select m from Member m left join m.team t on t.name = 'teamA'";
            em.createQuery(query2, Member.class).getResultList();

            // on - 연관관계가 없는 엔티티 외부 조인
            String query3 = "select m from Member m left join Team t on t.name = m.username";
            em.createQuery(query3, Member.class).getResultList();


            tx.commit();

            } catch (Exception e) {
                tx.rollback();
            } finally {
                em.close();
                emf.close();
            }
    }
}
