package app.workers;

import app.beans.Personne;
import app.exceptions.MyDBException;
import app.helpers.SystemLib;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DbWorkerTest {

    static DbWorker dbWrk;
    static String DB_NAME = "223_personne_1table";

    public DbWorkerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        dbWrk = new DbWorker();
    }

    @Test
    public void a0_testerConnecterMYSQL() throws MyDBException {
        System.out.println(SystemLib.getTestCurrentMethod());
        dbWrk.connecterBdMySQL(DB_NAME);
    }

    @Test
    public void b_lirePersonnes() throws MyDBException {
        System.out.println(SystemLib.getTestCurrentMethod());
        List<Personne> pers = dbWrk.lirePersonnes();
        boolean ok = pers != null && !pers.isEmpty();
        if (ok) {
            System.out.println("Nombre de personnes: " + pers.size());
        }
        assertTrue(ok);
    }

    @Test
    public void c_testerPrecedentMYSQL() {
        System.out.println(SystemLib.getTestCurrentMethod());
        Personne pers;
        boolean ok = false;
        try {
            pers = dbWrk.precedentPersonne();
            ok = pers != null;
            if (ok) {
                System.out.println("Nom: " + pers.getNom() + "  Prénom: " + pers.getPrenom());
            }
        } catch (MyDBException ex) {
            System.out.println("Une exception est survenue : " + ex.toString());
            ok = false;
        }

        assertTrue(ok);
    }

    @Test
    public void d_suivantPrecedentMYSQL() {
        System.out.println(SystemLib.getTestCurrentMethod());
        Personne pers;
        boolean ok = false;
        try {
            pers = dbWrk.suivantPersonne();
            ok = pers != null;
            if (ok) {
                System.out.println("Nom: " + pers.getNom() + "  Prénom: " + pers.getPrenom());
            }
        } catch (MyDBException ex) {
            System.out.println("Une exception est survenue : " + ex.toString());
            ok = false;
        }

        assertTrue(ok);
    }

    @Test
    public void e_testerDeconnecterMYSQL() throws MyDBException {
        System.out.println(SystemLib.getTestCurrentMethod());
        dbWrk.deconnecter();
    }
}
