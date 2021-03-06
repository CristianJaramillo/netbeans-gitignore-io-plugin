/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2013 Sun Microsystems, Inc.
 */
package com.junichi11.netbeans.gitignoreio.ui;

import com.junichi11.netbeans.gitignoreio.options.GitignoreioOptions;
import java.awt.Dialog;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;

/**
 *
 * @author junichi11
 */
public class GitignoreListPanel extends JPanel {

    private static final long serialVersionUID = -5481811228115109055L;
    private static final String GITIGNORE_API = "http://gitignore.io/api/"; // NOI18N
    private static final String GITIGNORE_API_LIST = GITIGNORE_API + "list"; // NOI18N
    private static final String UTF8 = "UTF-8"; // NOI18N
    private static final GitignoreListPanel INSTANCE = new GitignoreListPanel();
    private boolean isConnectedNetwork = true;

    /**
     * Creates new form GitignoreListPanel
     */
    public GitignoreListPanel() {
        initComponents();

        init();
    }

    public static GitignoreListPanel getDefault() {
        // retry
        if (!INSTANCE.isConnectedNetwork) {
            INSTANCE.init();
        }
        return INSTANCE;
    }

    private void init() {
        String gitignoreList = getAvailableGitignoreList();
        if (gitignoreList != null) {
            String[] gitignores = splitGitignores(gitignoreList);
            Arrays.sort(gitignores);
            DefaultListModel model = new DefaultListModel();
            for (int i = 0; i < gitignores.length; i++) {
                model.add(i, gitignores[i].trim());
            }
            availableList.setModel(model);
        }
        normalRadioButton.setSelected(true);
    }

    public String getGitignoreContent() throws MalformedURLException, IOException {
        String gitignores = getGitignores();
        String address = GITIGNORE_API + gitignores;
        URL url = new URL(address);
        URLConnection connection = url.openConnection();
        return getContent(connection, UTF8);
    }

    public String getGitignores() {
        return gitignoresTextField.getText().trim();
    }

    public void setGitignores(String ignores) {
        gitignoresTextField.setText(ignores);
    }

    public boolean isNormal() {
        return normalRadioButton.isSelected();
    }

    public boolean isOverwrite() {
        return overwriteRadioButton.isSelected();
    }

    public boolean isPostscript() {
        return postscriptRadioButton.isSelected();
    }

    public void setEnabledOverwrite(boolean isEnabled) {
        overwriteRadioButton.setEnabled(isEnabled);
    }

    public void setEnabledPostscript(boolean isEnabled) {
        postscriptRadioButton.setEnabled(isEnabled);
    }

    @NbBundle.Messages({
        "GitignoreListPanel.dialog.title=gitignore.io available list",
        "GitignoreListPanel.network.error=You have to connect to the internet."
    })
    public DialogDescriptor showDialog() throws IOException {
        if (!isConnectedNetwork) {
            throw new IOException(Bundle.GitignoreListPanel_network_error());
        }
        DialogDescriptor descriptor = new DialogDescriptor(this, Bundle.GitignoreListPanel_dialog_title());
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.pack();
        dialog.setVisible(true);
        return descriptor;
    }

    private String getAvailableGitignoreList() {
        String list = null;
        try {
            URL url = new URL(GITIGNORE_API_LIST);
            URLConnection openConnection = url.openConnection();
            list = getContent(openConnection, UTF8);
            isConnectedNetwork = true;
        } catch (MalformedURLException ex) {
            isConnectedNetwork = false;
        } catch (IOException ex) {
            isConnectedNetwork = false;
        }
        return list;
    }

    private String getContent(URLConnection connection, String charset) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedInputStream inuptStream = new BufferedInputStream(connection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inuptStream, charset)); // NOI18N
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n"); // NOI18N
        }

        return sb.toString();
    }

    private String[] splitGitignores(String gitignores) {
        return gitignores.split(","); // NOI18N
    }

    private GitignoreioOptions getOptions() {
        return GitignoreioOptions.getInstance();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        writeOptionButtonGroup = new javax.swing.ButtonGroup();
        availableListLabel = new javax.swing.JLabel();
        availableListScrollPane = new javax.swing.JScrollPane();
        availableList = new javax.swing.JList();
        gitignoresTextField = new javax.swing.JTextField();
        saveAsDefaultButton = new javax.swing.JButton();
        loadDefaultButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        normalRadioButton = new javax.swing.JRadioButton();
        overwriteRadioButton = new javax.swing.JRadioButton();
        postscriptRadioButton = new javax.swing.JRadioButton();

        org.openide.awt.Mnemonics.setLocalizedText(availableListLabel, org.openide.util.NbBundle.getMessage(GitignoreListPanel.class, "GitignoreListPanel.availableListLabel.text")); // NOI18N

        availableList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        availableList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                availableListMouseReleased(evt);
            }
        });
        availableList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                availableListValueChanged(evt);
            }
        });
        availableListScrollPane.setViewportView(availableList);

        gitignoresTextField.setText(org.openide.util.NbBundle.getMessage(GitignoreListPanel.class, "GitignoreListPanel.gitignoresTextField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(saveAsDefaultButton, org.openide.util.NbBundle.getMessage(GitignoreListPanel.class, "GitignoreListPanel.saveAsDefaultButton.text")); // NOI18N
        saveAsDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsDefaultButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(loadDefaultButton, org.openide.util.NbBundle.getMessage(GitignoreListPanel.class, "GitignoreListPanel.loadDefaultButton.text")); // NOI18N
        loadDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDefaultButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(resetButton, org.openide.util.NbBundle.getMessage(GitignoreListPanel.class, "GitignoreListPanel.resetButton.text")); // NOI18N
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        writeOptionButtonGroup.add(normalRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(normalRadioButton, org.openide.util.NbBundle.getMessage(GitignoreListPanel.class, "GitignoreListPanel.normalRadioButton.text")); // NOI18N

        writeOptionButtonGroup.add(overwriteRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(overwriteRadioButton, org.openide.util.NbBundle.getMessage(GitignoreListPanel.class, "GitignoreListPanel.overwriteRadioButton.text")); // NOI18N

        writeOptionButtonGroup.add(postscriptRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(postscriptRadioButton, org.openide.util.NbBundle.getMessage(GitignoreListPanel.class, "GitignoreListPanel.postscriptRadioButton.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(availableListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(availableListLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(resetButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loadDefaultButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveAsDefaultButton))
                    .addComponent(gitignoresTextField)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(normalRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(overwriteRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(postscriptRadioButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(availableListLabel)
                    .addComponent(saveAsDefaultButton)
                    .addComponent(loadDefaultButton)
                    .addComponent(resetButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(availableListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gitignoresTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(normalRadioButton)
                    .addComponent(overwriteRadioButton)
                    .addComponent(postscriptRadioButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void availableListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_availableListValueChanged
//        Object[] gitignores = availableList.getSelectedValues();
//        StringBuilder sb = new StringBuilder();
//        for (Object gitignore : gitignores) {
//            sb.append((String) gitignore).append(","); // NOI18N
//        }
//        int length = sb.length();
//        if (length > 0) {
//            sb.deleteCharAt(length - 1);
//        }
//        gitignoresTextField.setText(sb.toString());
    }//GEN-LAST:event_availableListValueChanged

    private void loadDefaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadDefaultButtonActionPerformed
        gitignoresTextField.setText(getOptions().getDefaultGitignores());
    }//GEN-LAST:event_loadDefaultButtonActionPerformed

    private void saveAsDefaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsDefaultButtonActionPerformed
        getOptions().setDefaultGitignores(getGitignores());
    }//GEN-LAST:event_saveAsDefaultButtonActionPerformed

    private void availableListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_availableListMouseReleased
        JList list = (JList) evt.getSource();
        String selectedValue = (String) list.getSelectedValue();
        String gitignores = getGitignores();
        String[] items = gitignores.split(","); // NOI18N
        if (Arrays.asList(items).contains(selectedValue)) {
            return;
        }

        // add value
        if (gitignores.isEmpty()) {
            setGitignores(selectedValue);
        } else {
            setGitignores(gitignores + "," + selectedValue); // NOI18N
        }
    }//GEN-LAST:event_availableListMouseReleased

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        setGitignores(""); // NOI18N
    }//GEN-LAST:event_resetButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList availableList;
    private javax.swing.JLabel availableListLabel;
    private javax.swing.JScrollPane availableListScrollPane;
    private javax.swing.JTextField gitignoresTextField;
    private javax.swing.JButton loadDefaultButton;
    private javax.swing.JRadioButton normalRadioButton;
    private javax.swing.JRadioButton overwriteRadioButton;
    private javax.swing.JRadioButton postscriptRadioButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton saveAsDefaultButton;
    private javax.swing.ButtonGroup writeOptionButtonGroup;
    // End of variables declaration//GEN-END:variables
}
