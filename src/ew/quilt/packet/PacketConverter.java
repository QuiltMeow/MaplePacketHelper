package ew.quilt.packet;

import ew.quilt.helper.PacketHelperTool;
import java.awt.Point;
import java.awt.event.KeyEvent;
import ew.quilt.tool.HexTool;
import ew.quilt.tool.data.ByteArrayByteStream;
import ew.quilt.tool.data.LittleEndianAccessor;
import ew.quilt.tool.data.MaplePacketLittleEndianWriter;
import javax.swing.ImageIcon;

public class PacketConverter extends javax.swing.JFrame {

    public PacketConverter() {
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
        setIconImage(new ImageIcon(PacketConverter.class.getResource("/ew/quilt/icon/Maple.png")).getImage());
        initAutomaticLineWarp();
    }

    private void initAutomaticLineWarp() {
        txtAreaConvertInput.setLineWrap(true);
        txtAreaConvertInput.setWrapStyleWord(true);

        txtAreaRestoreInput.setLineWrap(true);
        txtAreaRestoreInput.setWrapStyleWord(true);
        txtAreaRestoreOutput.setLineWrap(true);
        txtAreaRestoreOutput.setWrapStyleWord(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPacketConverter = new javax.swing.JTabbedPane();
        panelConvert = new javax.swing.JPanel();
        labelConvertInput = new javax.swing.JLabel();
        spConvertInput = new javax.swing.JScrollPane();
        txtAreaConvertInput = new javax.swing.JTextArea();
        btnRead = new javax.swing.JButton();
        btnWrite = new javax.swing.JButton();
        labelLength = new javax.swing.JLabel();
        txtLength = new javax.swing.JTextField();
        labelReadLoop = new javax.swing.JLabel();
        txtReadLoop = new javax.swing.JTextField();
        cbType = new javax.swing.JComboBox<>();
        labelConvertOutput = new javax.swing.JLabel();
        txtY = new javax.swing.JTextField();
        labelY = new javax.swing.JLabel();
        txtX = new javax.swing.JTextField();
        labelX = new javax.swing.JLabel();
        spConvertOutput = new javax.swing.JScrollPane();
        txtAreaConvertOutput = new javax.swing.JTextArea();
        panelRestore = new javax.swing.JPanel();
        labelRestoreInput = new javax.swing.JLabel();
        spRestoreInput = new javax.swing.JScrollPane();
        txtAreaRestoreInput = new javax.swing.JTextArea();
        btnRestore = new javax.swing.JButton();
        labelRestoreOutput = new javax.swing.JLabel();
        labelRestoreWarning = new javax.swing.JLabel();
        spRestoreOutput = new javax.swing.JScrollPane();
        txtAreaRestoreOutput = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("棉被家族 - 封包轉換工具");
        setResizable(false);

        labelConvertInput.setText("輸入資料");

        txtAreaConvertInput.setColumns(20);
        txtAreaConvertInput.setRows(5);
        spConvertInput.setViewportView(txtAreaConvertInput);

        btnRead.setText("封包讀取");
        btnRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadActionPerformed(evt);
            }
        });

        btnWrite.setText("封包寫入");
        btnWrite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWriteActionPerformed(evt);
            }
        });

        labelLength.setText("字串長度");

        txtLength.setText("1");
        txtLength.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtLengthKeyTyped(evt);
            }
        });

        labelReadLoop.setText("重複讀取");

        txtReadLoop.setText("1");
        txtReadLoop.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtReadLoopKeyTyped(evt);
            }
        });

        cbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "位元組", "短整數", "整數", "長整數", "字串", "楓之谷字串", "座標", "取得長度" }));

        labelConvertOutput.setText("輸出資料");

        labelY.setText("Y");

        labelX.setText("X");

        txtAreaConvertOutput.setColumns(20);
        txtAreaConvertOutput.setRows(5);
        txtAreaConvertOutput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAreaConvertOutputKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaConvertOutputKeyTyped(evt);
            }
        });
        spConvertOutput.setViewportView(txtAreaConvertOutput);

        javax.swing.GroupLayout panelConvertLayout = new javax.swing.GroupLayout(panelConvert);
        panelConvert.setLayout(panelConvertLayout);
        panelConvertLayout.setHorizontalGroup(
            panelConvertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConvertLayout.createSequentialGroup()
                .addGroup(panelConvertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelConvertLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelConvertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelConvertLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(spConvertInput))
                            .addGroup(panelConvertLayout.createSequentialGroup()
                                .addComponent(btnRead)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnWrite)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelLength)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLength, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelReadLoop)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtReadLoop, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                                .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelConvertLayout.createSequentialGroup()
                                .addComponent(labelConvertOutput)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelConvertLayout.createSequentialGroup()
                                .addComponent(labelConvertInput)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelX)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtX, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelY)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtY, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelConvertLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(spConvertOutput)))
                .addContainerGap())
        );
        panelConvertLayout.setVerticalGroup(
            panelConvertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConvertLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelConvertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelConvertInput)
                    .addComponent(txtY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelY)
                    .addComponent(txtX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelX))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spConvertInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelConvertLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRead)
                    .addComponent(btnWrite)
                    .addComponent(labelLength)
                    .addComponent(txtLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelReadLoop)
                    .addComponent(txtReadLoop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelConvertOutput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spConvertOutput)
                .addContainerGap())
        );

        tabPacketConverter.addTab("封包轉換", panelConvert);

        labelRestoreInput.setText("輸入資料");

        txtAreaRestoreInput.setColumns(20);
        txtAreaRestoreInput.setRows(5);
        spRestoreInput.setViewportView(txtAreaRestoreInput);

        btnRestore.setText("還原封包結構");
        btnRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestoreActionPerformed(evt);
            }
        });

        labelRestoreOutput.setText("輸出資料");

        labelRestoreWarning.setForeground(java.awt.Color.blue);

        txtAreaRestoreOutput.setColumns(20);
        txtAreaRestoreOutput.setRows(5);
        txtAreaRestoreOutput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAreaRestoreOutputKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaRestoreOutputKeyTyped(evt);
            }
        });
        spRestoreOutput.setViewportView(txtAreaRestoreOutput);

        javax.swing.GroupLayout panelRestoreLayout = new javax.swing.GroupLayout(panelRestore);
        panelRestore.setLayout(panelRestoreLayout);
        panelRestoreLayout.setHorizontalGroup(
            panelRestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRestoreLayout.createSequentialGroup()
                .addGroup(panelRestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRestoreLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelRestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRestore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelRestoreLayout.createSequentialGroup()
                                .addComponent(labelRestoreInput)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelRestoreLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(spRestoreInput, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE))
                            .addGroup(panelRestoreLayout.createSequentialGroup()
                                .addComponent(labelRestoreOutput)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labelRestoreWarning))))
                    .addGroup(panelRestoreLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(spRestoreOutput)))
                .addContainerGap())
        );
        panelRestoreLayout.setVerticalGroup(
            panelRestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRestoreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelRestoreInput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spRestoreInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRestore)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRestoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRestoreOutput)
                    .addComponent(labelRestoreWarning))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spRestoreOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPacketConverter.addTab("結構還原", panelRestore);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPacketConverter)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPacketConverter)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadActionPerformed
        // TODO add your handling code here:
        LittleEndianAccessor slea;
        try {
            slea = new LittleEndianAccessor(new ByteArrayByteStream(HexTool.getByteArrayFromHexString(txtAreaConvertInput.getText())));
        } catch (Exception ex) {
            txtAreaConvertOutput.setText("請檢查輸入資料是否符合格式 ...");
            return;
        }

        int loop;
        try {
            loop = Integer.parseInt(txtReadLoop.getText());
        } catch (NumberFormatException ex) {
            txtAreaConvertOutput.setText("重複次數輸入錯誤 ...");
            return;
        }
        if (loop <= 0) {
            txtAreaConvertOutput.setText("重複次數不得低於 1 ...");
            return;
        }

        txtAreaConvertOutput.setLineWrap(false);
        txtAreaConvertOutput.setWrapStyleWord(false);

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= loop; ++i) {
            sb.append("第 ").append(i).append(" 次讀取 : ");
            try {
                switch (cbType.getSelectedIndex()) {
                    case 0: {
                        byte read = slea.readByte();
                        sb.append(read).append(" [").append(HexTool.toString(read)).append("]");
                        break;
                    }
                    case 1: {
                        byte[] byteArray = slea.getBytes((int) slea.getPosition(), 2);
                        sb.append(slea.readShort()).append(" [").append(HexTool.toString(byteArray)).append("]");
                        break;
                    }
                    case 2: {
                        byte[] byteArray = slea.getBytes((int) slea.getPosition(), 4);
                        sb.append(slea.readInt()).append(" [").append(HexTool.toString(byteArray)).append("]");
                        break;
                    }
                    case 3: {
                        byte[] byteArray = slea.getBytes((int) slea.getPosition(), 8);
                        sb.append(slea.readLong()).append(" [").append(HexTool.toString(byteArray)).append("]");
                        break;
                    }
                    case 4: {
                        int length;
                        try {
                            length = Integer.parseInt(txtLength.getText());
                        } catch (NumberFormatException ex) {
                            txtAreaConvertOutput.setText("字串長度輸入錯誤 ...");
                            return;
                        }
                        if (length <= 0) {
                            txtAreaConvertOutput.setText("字串長度不得低於 1 ...");
                            return;
                        }

                        byte[] byteArray = slea.getBytes((int) slea.getPosition(), length);
                        sb.append(PacketHelperTool.handleEscapeString(slea.readAsciiString(length))).append(" [").append(HexTool.toString(byteArray)).append("]");
                        break;
                    }
                    case 5: {
                        byte[] lengthByteArray = slea.getBytes((int) slea.getPosition(), 2);
                        int byte1, byte2, length;
                        byte1 = lengthByteArray[0];
                        byte2 = lengthByteArray[1];
                        length = (byte2 << 8) + byte1;

                        byte[] byteArray = slea.getBytes((int) slea.getPosition() + 2, length);
                        sb.append(PacketHelperTool.handleEscapeString(slea.readMapleAsciiString())).append(" [").append(HexTool.toString(lengthByteArray)).append("] [").append(HexTool.toString(byteArray)).append("]");
                        break;
                    }
                    case 6: {
                        byte[] bytePosX, bytePosY;
                        bytePosX = slea.getBytes((int) slea.getPosition(), 2);
                        bytePosY = slea.getBytes((int) slea.getPosition() + 2, 2);

                        Point pos = slea.readPos();
                        sb.append("X = ").append(pos.x).append(" Y = ").append(pos.y).append(" [").append(HexTool.toString(bytePosX)).append("] [").append(HexTool.toString(bytePosY)).append("]");
                        break;
                    }
                    case 7: {
                        txtAreaConvertOutput.setText("封包長度 : " + slea.getlength());
                        return;
                    }
                }
            } catch (Exception ex) {
                sb.append("無法辨識");
                break;
            }
            sb.append("\r\n");
        }
        txtAreaConvertOutput.setText(sb.toString());
    }//GEN-LAST:event_btnReadActionPerformed

    private void btnWriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWriteActionPerformed
        // TODO add your handling code here:
        txtAreaConvertOutput.setLineWrap(true);
        txtAreaConvertOutput.setWrapStyleWord(true);

        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        try {
            switch (cbType.getSelectedIndex()) {
                case 0: {
                    mplew.writeByte(Byte.parseByte(txtAreaConvertInput.getText()));
                    break;
                }
                case 1: {
                    mplew.writeShort(Short.parseShort(txtAreaConvertInput.getText()));
                    break;
                }
                case 2: {
                    mplew.writeInt(Integer.parseInt(txtAreaConvertInput.getText()));
                    break;
                }
                case 3: {
                    mplew.writeLong(Long.parseLong(txtAreaConvertInput.getText()));
                    break;
                }
                case 4: {
                    mplew.writeAsciiString(txtAreaConvertInput.getText());
                    break;
                }
                case 5: {
                    mplew.writeMapleAsciiString(txtAreaConvertInput.getText());
                    break;
                }
                case 6: {
                    short x, y;
                    try {
                        x = Short.parseShort(txtX.getText());
                        y = Short.parseShort(txtY.getText());
                    } catch (NumberFormatException ex) {
                        txtAreaConvertOutput.setText("座標資訊輸入錯誤 ...");
                        return;
                    }
                    mplew.writePos(new Point(x, y));
                    break;
                }
                case 7: {
                    txtAreaConvertOutput.setText("取得長度不支援進行寫入 ...");
                    return;
                }
            }
        } catch (NumberFormatException ex) {
            txtAreaConvertOutput.setText("無法辨識輸入資訊 請檢查輸入型別是否正確 ...");
            return;
        }
        txtAreaConvertOutput.setText(HexTool.toString(mplew.getPacket()));
    }//GEN-LAST:event_btnWriteActionPerformed

    private void txtLengthKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLengthKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.numberOnly(evt);
    }//GEN-LAST:event_txtLengthKeyTyped

    private void txtReadLoopKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReadLoopKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.numberOnly(evt);
    }//GEN-LAST:event_txtReadLoopKeyTyped

    private void btnRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestoreActionPerformed
        // TODO add your handling code here:
        labelRestoreWarning.setText("");
        String packet = PacketHelperTool.fixPacketStream(txtAreaRestoreInput.getText());
        if (packet.length() <= 0) {
            txtAreaRestoreOutput.setText("請輸入有效封包資料 ...");
            return;
        }
        if (packet.length() % 2 == 1) {
            labelRestoreWarning.setText("長度錯誤 已自動補 0 修正");
        }
        packet = PacketHelperTool.fixPacketInputAutomaticFix(txtAreaRestoreInput.getText());
        txtAreaRestoreOutput.setText(packet);
    }//GEN-LAST:event_btnRestoreActionPerformed

    private void txtAreaConvertOutputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaConvertOutputKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaConvertOutputKeyPressed

    private void txtAreaConvertOutputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaConvertOutputKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaConvertOutputKeyTyped

    private void txtAreaRestoreOutputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaRestoreOutputKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaRestoreOutputKeyPressed

    private void txtAreaRestoreOutputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaRestoreOutputKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaRestoreOutputKeyTyped

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
                new PacketConverter().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRead;
    private javax.swing.JButton btnRestore;
    private javax.swing.JButton btnWrite;
    private javax.swing.JComboBox<String> cbType;
    private javax.swing.JLabel labelConvertInput;
    private javax.swing.JLabel labelConvertOutput;
    private javax.swing.JLabel labelLength;
    private javax.swing.JLabel labelReadLoop;
    private javax.swing.JLabel labelRestoreInput;
    private javax.swing.JLabel labelRestoreOutput;
    private javax.swing.JLabel labelRestoreWarning;
    private javax.swing.JLabel labelX;
    private javax.swing.JLabel labelY;
    private javax.swing.JPanel panelConvert;
    private javax.swing.JPanel panelRestore;
    private javax.swing.JScrollPane spConvertInput;
    private javax.swing.JScrollPane spConvertOutput;
    private javax.swing.JScrollPane spRestoreInput;
    private javax.swing.JScrollPane spRestoreOutput;
    private javax.swing.JTabbedPane tabPacketConverter;
    private javax.swing.JTextArea txtAreaConvertInput;
    private javax.swing.JTextArea txtAreaConvertOutput;
    private javax.swing.JTextArea txtAreaRestoreInput;
    private javax.swing.JTextArea txtAreaRestoreOutput;
    private javax.swing.JTextField txtLength;
    private javax.swing.JTextField txtReadLoop;
    private javax.swing.JTextField txtX;
    private javax.swing.JTextField txtY;
    // End of variables declaration//GEN-END:variables
}
