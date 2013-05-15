package com.applifit.bilifit.server.formulaires.DAO;

import java.util.List;

import com.applifit.bilifit.server.formulaires.Parametre;

public interface ParametreDao {

	Parametre getParametreById(Long id);

	Parametre ajouterParametre(Parametre param);

	List<Parametre> getParametres();

	List<Parametre> getParametresByElement(Long element);

}
