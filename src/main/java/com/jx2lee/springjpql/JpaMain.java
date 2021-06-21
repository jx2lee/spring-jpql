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
            member.setUsername("관리자");
            member.setAge(20);
            member.setType(MemberType.ADMIN);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String query = "select " +
                                    "case when m.age <= 10 then '학생요금' " +
                                         "when m.age >= 60 then '경로요금' " +
                                         "else '일반요금' " +
                                    "end " +
                           "from Member m";
            String query1 = "select coalesce(m.username, '이름 없는 사람') from Member m";
            String query2 = "select nullif(m.username, '관리자') from Member m";

            List<String> resultList = em.createQuery(query, String.class).getResultList();
            List<String> resultList1 = em.createQuery(query1, String.class).getResultList();
            List<String> resultList2 = em.createQuery(query2, String.class).getResultList();

            for (String s : resultList) {
                System.out.println("s = " + s);
            }

            for (String s : resultList1) {
                System.out.println("s = " + s);
            }

            for (String s : resultList2) {
                System.out.println("s = " + s);
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
