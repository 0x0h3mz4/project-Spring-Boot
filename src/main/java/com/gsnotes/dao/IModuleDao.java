package com.gsnotes.dao;

import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IModuleDao extends JpaRepository<Module, Long> {
    @Query(value="SELECT COUNT(*) FROM element el, module md WHERE el.idModule = md.idModule AND el.idModule = ?;",
            nativeQuery = true)
    public int getElementCount(Long idModule);

    @Query(value="SELECT * FROM module WHERE titre=?;", nativeQuery=true)
    public Module getByTitre(String titre);
}
