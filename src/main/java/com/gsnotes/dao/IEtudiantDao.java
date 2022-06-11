package com.gsnotes.dao;

import com.gsnotes.bo.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IEtudiantDao extends JpaRepository<Etudiant, Long> {
    @Query(value="SELECT * FROM etudiant WHERE cne=?;", nativeQuery=true)
    public Etudiant getByCne(String cne);
}
