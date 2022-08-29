package app.workers;

import app.beans.Personne;
import app.exceptions.MyDBException;
import app.helpers.SystemLib;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DbWorker implements DbWorkerItf {

    private Connection dbConnexion;
    private List<Personne> listePersonnes;
    private int index = 0;

    /**
     * Constructeur du worker
     */
    public DbWorker() {
        
    }

    @Override
    public void connecterBdMySQL(String nomDB) throws MyDBException {
        final String url_local = "jdbc:mysql://localhost:3306/" + nomDB;
        final String user = "root";
        final String password = "emf123";

        System.out.println("url:" + url_local);
        try {
            dbConnexion = DriverManager.getConnection(url_local, user, password);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void connecterBdHSQLDB(String nomDB) throws MyDBException {
        final String url = "jdbc:hsqldb:file:" + nomDB + ";shutdown=true";
        final String user = "SA";
        final String password = "";
        System.out.println("url:" + url);
        try {
            dbConnexion = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void connecterBdAccess(String nomDB) throws MyDBException {
        final String url = "jdbc:ucanaccess://" + nomDB;
        System.out.println("url=" + url);
        try {
            dbConnexion = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    @Override
    public void deconnecter() throws MyDBException {
        try {
            if (dbConnexion != null) {
                dbConnexion.close();
            }
        } catch (SQLException ex) {
            throw new MyDBException(SystemLib.getFullMethodName(), ex.getMessage());
        }
    }

    public List<Personne> lirePersonnes() throws MyDBException {
        listePersonnes = new ArrayList<>();
       
        //Récupération des personnes
        try{
            Statement requete = dbConnexion.createStatement();
        
            ResultSet rs = requete.executeQuery("select * from t_personne");
            
            //Pour chaque résultats
            while(rs.next()){
                listePersonnes.add(new Personne(rs.getString("Nom"), rs.getString("Prenom"))); //Ajout de la personne avec le nom et prénom 
            }
        } catch (SQLException e){
            
        }
        return listePersonnes;
    }

    @Override
    public Personne precedentPersonne() throws MyDBException {
        
        //Récupérer la liste des personnes
        List<Personne> liste = this.lirePersonnes();
        
        //Mise à jour de l'index (plus petit que 0 on retourne au max, sinon on désincrémente)
        index = index - 1 < 0 ? liste.size() - 1 : index - 1;
        
        //Retour de la personne
        return liste.get(index);

    }

    @Override
    public Personne suivantPersonne() throws MyDBException {
        
        //On prend la liste des personnes
        List<Personne> liste = this.lirePersonnes();
        
        //Mise à jour de l'index (Si on dépasse le max, on retourne à 0, sinon on incérmente)
        index = index + 1 > liste.size() - 1 ? 0 : index + 1;
        
        //Retour de la personne
        return liste.get(index);

    }

}
