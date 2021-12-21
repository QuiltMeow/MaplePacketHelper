package ew.quilt.packet;

import javax.swing.ImageIcon;

public class PacketToolBox extends javax.swing.JFrame {

    public PacketToolBox() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            System.err.println("程式皮膚載入失敗 ...");
        }

        initComponents();
        setIconImage(new ImageIcon(PacketToolBox.class.getResource("/ew/quilt/icon/Maple.png")).getImage());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelPacketToolBox = new javax.swing.JLabel();
        btnReaderHelper = new javax.swing.JButton();
        btnWriterHelper = new javax.swing.JButton();
        btnConverter = new javax.swing.JButton();
        btnStringAnalyzer = new javax.swing.JButton();
        btnRadixConverter = new javax.swing.JButton();
        labelCopyright = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("棉被家族 - 封包工具箱");
        setResizable(false);

        labelPacketToolBox.setFont(new java.awt.Font("微軟正黑體", 0, 14)); // NOI18N
        labelPacketToolBox.setText("封包工具箱");

        btnReaderHelper.setText("輔助解包工具");
        btnReaderHelper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReaderHelperActionPerformed(evt);
            }
        });

        btnWriterHelper.setText("輔助發包工具");
        btnWriterHelper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWriterHelperActionPerformed(evt);
            }
        });

        btnConverter.setText("封包轉換工具");
        btnConverter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConverterActionPerformed(evt);
            }
        });

        btnStringAnalyzer.setText("字串分析工具");
        btnStringAnalyzer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStringAnalyzerActionPerformed(evt);
            }
        });

        btnRadixConverter.setText("進位轉換工具");
        btnRadixConverter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRadixConverterActionPerformed(evt);
            }
        });

        labelCopyright.setText("© 2016 Quilt All Rights Reserved");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelPacketToolBox)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnConverter)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnStringAnalyzer))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnReaderHelper)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnWriterHelper)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRadixConverter, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelCopyright, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelPacketToolBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReaderHelper)
                    .addComponent(btnWriterHelper))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConverter)
                    .addComponent(btnStringAnalyzer))
                .addGap(18, 18, 18)
                .addComponent(btnRadixConverter)
                .addGap(18, 18, 18)
                .addComponent(labelCopyright)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReaderHelperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReaderHelperActionPerformed
        // TODO add your handling code here:
        new PacketReaderHelper().setVisible(true);
    }//GEN-LAST:event_btnReaderHelperActionPerformed

    private void btnWriterHelperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWriterHelperActionPerformed
        // TODO add your handling code here:
        new PacketWriterHelper().setVisible(true);
    }//GEN-LAST:event_btnWriterHelperActionPerformed

    private void btnConverterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConverterActionPerformed
        // TODO add your handling code here:
        new PacketConverter().setVisible(true);
    }//GEN-LAST:event_btnConverterActionPerformed

    private void btnStringAnalyzerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStringAnalyzerActionPerformed
        // TODO add your handling code here:
        new PacketStringAnalyzer().setVisible(true);
    }//GEN-LAST:event_btnStringAnalyzerActionPerformed

    private void btnRadixConverterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRadixConverterActionPerformed
        // TODO add your handling code here:
        new RadixConverter().setVisible(true);
    }//GEN-LAST:event_btnRadixConverterActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            System.err.println("程式皮膚載入失敗 ...");
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PacketToolBox().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConverter;
    private javax.swing.JButton btnRadixConverter;
    private javax.swing.JButton btnReaderHelper;
    private javax.swing.JButton btnStringAnalyzer;
    private javax.swing.JButton btnWriterHelper;
    private javax.swing.JLabel labelCopyright;
    private javax.swing.JLabel labelPacketToolBox;
    // End of variables declaration//GEN-END:variables
}
