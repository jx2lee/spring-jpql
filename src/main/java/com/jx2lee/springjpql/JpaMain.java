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
            for(int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member-" + i);
                member.setAge(i+1);
                em.persist(member);
            }

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m from Member m order by m.age", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result.size = " + result.size());
            for (Member member1: result) {
                System.out.println("member1 = " + member1);
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
