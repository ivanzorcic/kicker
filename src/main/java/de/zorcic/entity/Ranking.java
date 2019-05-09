package de.zorcic.entity;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class Ranking {

    EloScores eloScores;

    @PersistenceContext
    EntityManager em;

    @PostConstruct
    public void initScores() {
        eloScores = new EloScores();
        em.createNamedQuery("Game.findAllSorted", Game.class).getResultList().forEach(this.eloScores::updateRanking);
    }

    public void observeNewGame(@Observes NewGameEvent event) {
        this.eloScores.updateRanking(em.find(Game.class, event.gameId()));
    }

    public EloScores getEloScores() {
        return eloScores;
    }

}
