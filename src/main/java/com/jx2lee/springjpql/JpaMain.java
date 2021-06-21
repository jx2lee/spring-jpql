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
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("관리자A");
            member.setAge(20);
            member.setType(MemberType.ADMIN);
            member.setTeam(team);

            Member member2 = new Member();
            member2.setUsername("관리자B");
            member2.setAge(30);
            member2.setType(MemberType.ADMIN);

            em.persist(member);
            em.persist(member2);

            em.flush();
            em.clear();

            String concatQuery = "select concat('a', 'b') From Member m";
            String substringQuery = "select substring(m.username, 2, 3) From Member m";
            String locateQuery = "select locate('de', 'abcdefgh') From Member m";
            String sizeQuery = "select size(t.members) From Team t";
            String groupConcatQuery = "select function('group_concat',m.username) From Member m";

            List<String> resultList1 = em.createQuery(concatQuery, String.class).getResultList();
            List<String> resultList2 = em.createQuery(substringQuery, String.class).getResultList();
            List<Integer> resultList3 = em.createQuery(locateQuery, Integer.class).getResultList();
            List<Integer> resultList4 = em.createQuery(sizeQuery, Integer.class).getResultList();
            List<String> resultList5 = em.createQuery(groupConcatQuery, String.class).getResultList();

            for (String s : resultList1) {
                System.out.println("s = " + s);
            }
            for (String s : resultList2) {
                System.out.println("s = " + s);
            }
            for (Integer integer : resultList3) {
                System.out.println("integer = " + integer);
            }
            for (Integer integer : resultList4) {
                System.out.println("integer = " + integer);
            }
            for (String s : resultList5) {
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
