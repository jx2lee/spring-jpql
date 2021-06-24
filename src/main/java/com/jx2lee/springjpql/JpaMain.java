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

            // fetch join: alias 는 주지 않는것이 관례
            // 가끔 유용할 때도 있다..
            String notAliasQuery = "select distinct t from Team t join fetch t.members as m";
            List<Team> teams = em.createQuery(notAliasQuery, Team.class)
                    .getResultList();

            for (Team team : teams) {
                System.out.println("team.getName() = " + team.getName() + ", " + team);
                for (Member member : team.getMembers()) {
                    System.out.println("==> member = " + member.getUsername() + ", " + member);
                }
            }

            em.clear();

            String batchQuery = "select t from Team t";
            List<Team> batchTeams = em.createQuery(batchQuery, Team.class)
                    .setMaxResults(2)
                    .setFirstResult(0)
                    .getResultList();

            for (Team team : batchTeams) {
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
