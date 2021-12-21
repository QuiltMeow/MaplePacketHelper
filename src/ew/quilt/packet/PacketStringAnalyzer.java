package ew.quilt.packet;

import ew.quilt.exception.PacketStringWrongSizeException;
import ew.quilt.helper.PacketHelperTool;
import ew.quilt.tool.HexTool;
import ew.quilt.tool.data.ByteArrayByteStream;
import ew.quilt.tool.data.LittleEndianAccessor;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class PacketStringAnalyzer extends javax.swing.JFrame {

    public PacketStringAnalyzer() {
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
        setIconImage(new ImageIcon(PacketStringAnalyzer.class.getResource("/ew/quilt/icon/Maple.png")).getImage());
        txtAreaPacket.setLineWrap(true);
        txtAreaPacket.setWrapStyleWord(true);
        txtAreaPacket.requestFocus();
    }

    public PacketStringAnalyzer(byte[] packet) {
        this();
        if (packet.length > 0) {
            slea = new LittleEndianAccessor(new ByteArrayByteStream(packet));
            txtAreaPacket.setText(HexTool.toString(packet));
        }
        updateOutput();
    }

    public PacketStringAnalyzer(String packet) {
        this();
        try {
            packet = PacketHelperTool.fixPacketInput(packet);
        } catch (PacketStringWrongSizeException ex) {
            updateOutput();
            return;
        }

        if (!packet.equals("")) {
            txtAreaPacket.setText(packet);
            slea = new LittleEndianAccessor(new ByteArrayByteStream(HexTool.getByteArrayFromHexString(txtAreaPacket.getText())));
        }
        updateOutput();
    }

    private void updateOutput() {
        if (slea == null) {
            txtAreaOutput.setText("沒有封包資料或封包資料錯誤 ...");
            return;
        }

        int offset;
        try {
            offset = Integer.parseInt(txtOffset.getText());
        } catch (NumberFormatException ex) {
            txtAreaOutput.setText("請輸入正確偏移數值 ...");
            return;
        }
        if (offset < 0 || offset >= slea.getlength()) {
            txtAreaOutput.setText("輸入偏移錯誤 ...");
            return;
        }

        int limit;
        try {
            limit = Integer.parseInt(txtLimit.getText());
        } catch (NumberFormatException ex) {
            txtAreaOutput.setText("請輸入正確上限數值 ...");
            return;
        }
        if (limit <= 0) {
            txtAreaOutput.setText("輸入上限錯誤 ...");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= limit; ++i) {
            sb.append("字串長度 ").append(i).append(" : ");
            try {
                byte[] byteArray = slea.getBytes(offset, i);
                String read = slea.getAsciiString(offset, i);
                sb.append("[").append(PacketHelperTool.handleEscapeString(read)).append("] [").append(HexTool.toString(byteArray)).append("]");
            } catch (Exception ex) {
                sb.append("無法辨識");
            }
            sb.append("\r\n");
        }
        txtAreaOutput.setText(sb.toString());
    }

    private LittleEndianAccessor slea = null;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelPacketString = new javax.swing.JLabel();
        labelPacket = new javax.swing.JLabel();
        spPacket = new javax.swing.JScrollPane();
        txtAreaPacket = new javax.swing.JTextArea();
        txtOffset = new javax.swing.JTextField();
        labelOffset = new javax.swing.JLabel();
        labelOutput = new javax.swing.JLabel();
        txtLimit = new javax.swing.JTextField();
        labelLimit = new javax.swing.JLabel();
        spOutput = new javax.swing.JScrollPane();
        txtAreaOutput = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("封包字串分析工具");
        setResizable(false);

        labelPacketString.setFont(new java.awt.Font("微軟正黑體", 0, 14)); // NOI18N
        labelPacketString.setText("封包字串分析");

        labelPacket.setText("封包");

        txtAreaPacket.setColumns(20);
        txtAreaPacket.setRows(5);
        txtAreaPacket.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAreaPacketKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaPacketKeyTyped(evt);
            }
        });
        spPacket.setViewportView(txtAreaPacket);

        txtOffset.setText("0");
        txtOffset.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOffsetKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOffsetKeyTyped(evt);
            }
        });

        labelOffset.setText("偏移");

        labelOutput.setText("輸出");

        txtLimit.setText("20");
        txtLimit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLimitKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtLimitKeyTyped(evt);
            }
        });

        labelLimit.setText("上限");

        txtAreaOutput.setColumns(20);
        txtAreaOutput.setRows(5);
        txtAreaOutput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAreaOutputKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaOutputKeyTyped(evt);
            }
        });
        spOutput.setViewportView(txtAreaOutput);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelPacketString)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(labelPacket)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelOffset)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtOffset, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spPacket, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelOutput)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelLimit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(spOutput)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelPacketString)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPacket)
                    .addComponent(txtOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelOffset))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spPacket, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelOutput)
                    .addComponent(txtLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelLimit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAreaPacketKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaPacketKeyReleased
        // TODO add your handling code here:
        String packet = PacketHelperTool.fixPacketStream(txtAreaPacket.getText());
        String fixPacket = PacketHelperTool.insertPeriodically(packet, " ", 2);
        if (packet.equals("") || packet.length() % 2 == 1) {
            slea = null;
        } else {
            slea = new LittleEndianAccessor(new ByteArrayByteStream(HexTool.getByteArrayFromHexString(fixPacket)));
        }
        txtAreaPacket.setText(fixPacket);
        updateOutput();
    }//GEN-LAST:event_txtAreaPacketKeyReleased

    private void txtOffsetKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOffsetKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.numberOnly(evt);
    }//GEN-LAST:event_txtOffsetKeyTyped

    private void txtLimitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLimitKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.numberOnly(evt);
    }//GEN-LAST:event_txtLimitKeyTyped

    private void txtOffsetKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOffsetKeyReleased
        // TODO add your handling code here:
        updateOutput();
    }//GEN-LAST:event_txtOffsetKeyReleased

    private void txtAreaPacketKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaPacketKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.hexArrayOnly(evt);
    }//GEN-LAST:event_txtAreaPacketKeyTyped

    private void txtLimitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLimitKeyReleased
        // TODO add your handling code here:
        updateOutput();
    }//GEN-LAST:event_txtLimitKeyReleased

    private void txtAreaOutputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaOutputKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaOutputKeyPressed

    private void txtAreaOutputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaOutputKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaOutputKeyTyped

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
                new PacketStringAnalyzer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelLimit;
    private javax.swing.JLabel labelOffset;
    private javax.swing.JLabel labelOutput;
    private javax.swing.JLabel labelPacket;
    private javax.swing.JLabel labelPacketString;
    private javax.swing.JScrollPane spOutput;
    private javax.swing.JScrollPane spPacket;
    private javax.swing.JTextArea txtAreaOutput;
    private javax.swing.JTextArea txtAreaPacket;
    private javax.swing.JTextField txtLimit;
    private javax.swing.JTextField txtOffset;
    // End of variables declaration//GEN-END:variables
}
