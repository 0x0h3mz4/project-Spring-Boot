package com.gsnotes.dao;

import com.gsnotes.bo.InscriptionAnnuelle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInscriptionAnnuelleDao extends JpaRepository<InscriptionAnnuelle, Long> {
}
