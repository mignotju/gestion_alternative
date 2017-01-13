/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author kamowskn
 */
public class ButtonSave extends JButton {

    public ButtonSave(JFreeChart graph) {
        super("save");
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    File selectedFile = new File(".").getCanonicalFile();
                    JFileChooser dialogue = new JFileChooser(selectedFile);
                    dialogue.setApproveButtonText("Save");
                    dialogue.setFileFilter(new FileNameExtensionFilter("Image (.jpeg)", "jpeg"));
                    dialogue.showOpenDialog(null);
                    if (dialogue.getSelectedFile() == null) { //Cas "Annuler"
                        return;
                    }
                    if (!dialogue.getSelectedFile().getCanonicalPath().endsWith(".jpeg")) {
                        dialogue.setSelectedFile(new File(dialogue.getSelectedFile().getAbsolutePath().concat(".jpeg")));
                    }
                    ChartUtilities.saveChartAsJPEG(dialogue.getSelectedFile(), graph, 800, 500);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

}
