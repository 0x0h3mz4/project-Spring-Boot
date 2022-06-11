package com.gsnotes.dao;

import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface INiveauDao extends JpaRepository<Niveau, Long> {
    @Query(value="SELECT * FROM niveau WHERE titre=?;", nativeQuery=true)
    public Niveau getByTitre(String titre);
}
