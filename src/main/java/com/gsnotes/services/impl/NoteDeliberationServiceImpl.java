package com.gsnotes.services.impl;

import com.gsnotes.bo.*;
import com.gsnotes.bo.Module;
import com.gsnotes.dao.*;
import com.gsnotes.services.INoteDeliberationService;
import com.gsnotes.utils.ExcelImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class NoteDeliberationServiceImpl implements INoteDeliberationService {
    @Autowired
    private IModuleDao moduleDao;
    @Autowired
    private INiveauDao niveauDao;
    @Autowired
    private IElementDao elementDao;
    @Autowired
    private IFiliereDao filiereDao;
    @Autowired
    private IInscriptionAnnuelleDao inscriptionAnnuelleDao;
    @Autowired
    private IInsciptionModuleDao inscriptionModuleDao;
    @Autowired
    private IInsciptionMatiereDao inscriptionMatiereDao;
    @Autowired
    private IEtudiantDao etudiantDao;

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), Paths.get("uploads").resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Impossible de stocker le fichier. Erreur: " + e.getMessage());
        }
    }

    public void processExcelFile()
    {
        String filename="C:\\Users\\habsh\\Downloads\\Test.xlsx";
        ArrayList<ArrayList<Object>> data = ExcelImporter.importExcel(filename);

        String annee = (String) data.get(0).get(1);
        String dateDeliberation = (String) data.get(0).get(3);
        String classe = (String) data.get(1).get(1);
        System.out.println(annee);
        System.out.println(dateDeliberation);
        System.out.println(classe);
        Niveau niveau = niveauDao.getByTitre(classe);
        //nomModules=[module1,module2..]
        ArrayList<String> nomModules = new ArrayList<String>();
        //modules=[[],[]]
        ArrayList<ArrayList<String>> modules = new ArrayList<ArrayList<String>>();
        for (int i = 4; i < data.get(3).size() - 2; i++) {
            modules.add(new ArrayList<String>());

            String nomModule = (String) data.get(3).get(i);

            nomModules.add(nomModule.trim());
            System.out.println(nomModules);
        }

        Map<String, Integer> nombreElements = new HashMap<String, Integer>();
        List<Module> modulesdB = moduleDao.findAll();
        System.out.println(modulesdB);
        for (Iterator<Module> it = modulesdB.iterator(); it.hasNext(); ) {
            Module moduledB = it.next();
            System.out.println(moduledB.getTitre());
            nombreElements.put( moduledB.getTitre(),
                    moduleDao.getElementCount(moduledB.getIdModule())
            );
        }
        //nomElements=[module1=nbEle,module2=nbEle..]
        for (int i = 0, k = 0; i < modules.size(); i++, k += 2) {
            for (int j = 0; j < nombreElements.get(nomModules.get(i)); j++) {
                modules.get(i).add((String) data.get(4).get(k++));
            }
        }
        System.out.println("nada "+modules);

        for (int i = 0; i < modules.size(); i++) {
            System.out.print(nomModules.get(i) + ": ");
            for (int j = 0; j < modules.get(i).size(); j++) {
                System.out.print(modules.get(i).get(j) + " ");
            }
            System.out.println();
        }

        for (int i = 5; i < data.size(); i++) {
            String id = (String) data.get(i).get(0);
            String cne = (String) data.get(i).get(1);
            String nom = (String) data.get(i).get(2);
            String prenom = (String) data.get(i).get(3);

            System.out.print(id +" "+cne+" "+nom+" "+prenom+" :");

            InscriptionAnnuelle ia = new InscriptionAnnuelle();

            ArrayList<InscriptionMatiere> ies = new ArrayList<InscriptionMatiere>();
            ArrayList<InscriptionModule> ims = new ArrayList<InscriptionModule>();

            int t = 4; // current Column

            for (int j = 0; j < modules.size(); j++) {
                for (int k = 0; k < modules.get(j).size(); k++) {
                    double noteElement = (double) data.get(i).get(t);

                    System.out.println("element name : " + (String) data.get(4).get(t - 4));
                    Element elem = elementDao.getByTitre((String) data.get(4).get(t - 4));
                    InscriptionMatiere ie = new InscriptionMatiere();
                    ie.setCoefficient(elem.getCurrentCoefficient());
                    ie.setNoteSN(noteElement);
                    ie.setNoteSR(noteElement);
                    ie.setNoteFinale(noteElement);
                    ie.setPlusInfos("info");
                    ie.setValidation(noteElement >= 12 ? "V" : "NV");
                    ie.setMatiere(elem);
                    ie.setInscriptionAnnuelle(ia);

                    ies.add(ie);

                    System.out.print(noteElement + " ");

                    t++;
                }
                double moyenne = (double) data.get(i).get(t++);

                String validation = (String) data.get(i).get(t++);

                InscriptionModule im = new InscriptionModule();
                im.setNoteSN(moyenne);
                im.setNoteSR(moyenne);
                Module module = moduleDao.getByTitre(nomModules.get(j));
                im.setModule(module);
                im.setNoteFinale(moyenne);
                im.setPlusInfos("info");
                im.setValidation(validation);
                im.setInscriptionAnnuelle(ia);

                ims.add(im);

                System.out.print("la moyenne:" + moyenne + "/" + validation );
            }

            double moyenneGlobale = (double) data.get(i).get(t++);
            double rangGlobale = (double) data.get(i).get(t++);

            ia.setAnnee(Integer.valueOf(annee.split("/")[1]));
            ia.setEtat(0);
            ia.setPlusInfos("info");
            ia.setMention("Mention");
            ia.setRang((int) rangGlobale);
            ia.setNiveau(niveau);
            ia.setType("type");
            ia.setValidation("validation");

            inscriptionAnnuelleDao.save(ia);
            for (InscriptionModule im : ims) {
                inscriptionModuleDao.save(im);
            }
            for (InscriptionMatiere ie : ies) {
                inscriptionMatiereDao.save(ie);
            }

            System.out.println();
        }
    }

}
