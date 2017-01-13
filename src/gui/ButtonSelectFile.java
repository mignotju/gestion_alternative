/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author kamowskn
 */
public class ButtonSelectFile extends JButton{
    
    private File fichierExcel;
    private MainPanel affichage;
    
    public ButtonSelectFile(MainPanel pnl){
        super("Select Excel File");
        affichage = pnl;
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File selectedFile = null;
                try {
                    // obtention d'un objet File qui désigne le répertoire courant.
                    selectedFile = new File(".").getCanonicalFile();
                } catch (IOException ex) {
                }

                // création de la boîte de dialogue dans ce répertoire courant
                // (ou dans "home" s'il y a eu une erreur d'entrée/sortie, auquel
                // cas repertoireCourant vaut null)
                JFileChooser dialogue = new JFileChooser(selectedFile);
                dialogue.setFileFilter(new FileNameExtensionFilter("Tableur Excel","xlsx", "xsl"));
                // affichage
                dialogue.showOpenDialog(null);

                // récupération du fichier sélectionné
                fichierExcel = dialogue.getSelectedFile();
                pnl.repaint();
            }
        });   
    }
    
    public File getFile(){
        return fichierExcel;
    }
            
            
}
