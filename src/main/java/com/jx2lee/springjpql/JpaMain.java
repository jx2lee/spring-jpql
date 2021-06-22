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

            // fetch join
            String fetchQuery = "select m from Member m join fetch m.team";
            List<Member> result = em.createQuery(fetchQuery, Member.class)
                    .getResultList();

            for (Member member : result) {
                System.out.println("member.getUsername() = " + member.getUsername() + ", " + member.getTeam().getName());
                // 회원1 -> 팀A(SQL)
                // 회원2 -> 팀A(1차캐시)
                // 회원3 -> 팀B(SQL)
                // -> N + 1 문제: fetch 로 해결 가능
            }

            // fetch join - 컬렉션 패치조인 (일대다 관계)
            // 뻥튀기 조심 -> distinct 로 우회가능
            String fetchOneToManyQuery = "select distinct t from Team t join fetch t.members";
            List<Team> teams = em.createQuery(fetchOneToManyQuery, Team.class)
                    .getResultList();

            for (Team team : teams) {
                System.out.println("team.getName() = " + team.getName() + ", " + team);
                for (Member member : team.getMembers()) {
                    System.out.println("==> member = " + member.getUsername() + ", " + member);
                }
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
