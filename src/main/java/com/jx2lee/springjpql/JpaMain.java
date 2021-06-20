package com.jx2lee.springjpql;

import com.jx2lee.springjpql.domain.Member;

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

            // TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            // TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            // Query query3 = em.createQuery("select m.username, m.age from Member m", Member.class);

            List<Member> resultList = em.createQuery(
                    "select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "jx2lee")
                    .getResultList();
            // 메소드 체이닝 (method chaining 사용하지 않으면 다음과 같이 풀어서 사용) 적용하지 않는 경우
            // TypedQuery<Member> query1 = em.createQuery(
            //         "select m from Member m where m.username = :username", Member.class);
            // query1.setParameter("username", "jx2lee");
            // List<Member> resultList = query1.getResultList();
            for (Member member1 : resultList) {
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
