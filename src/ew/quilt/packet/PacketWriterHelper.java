package ew.quilt.packet;

import ew.quilt.helper.PacketHelperTool;
import ew.quilt.helper.component.TextLineNumber;
import ew.quilt.tool.HexTool;
import ew.quilt.tool.data.MaplePacketLittleEndianWriter;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

public class PacketWriterHelper extends javax.swing.JFrame {

    public PacketWriterHelper() {
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
        setIconImage(new ImageIcon(PacketWriterHelper.class.getResource("/ew/quilt/icon/Maple.png")).getImage());
        initAutomaticLineWarp();
    }

    private void initAutomaticLineWarp() {
        txtAreaPacketResult.setLineWrap(true);
        txtAreaPacketResult.setWrapStyleWord(true);
        spPacketResult.setColumnHeaderView(labelColumn);

        txtAreaPacketData.setLineWrap(true);
        txtAreaPacketData.setWrapStyleWord(true);

        TextLineNumber tlnPacketLog = new TextLineNumber(txtAreaPacketLog);
        spPacketLog.setRowHeaderView(tlnPacketLog);
        TextLineNumber tlnProgramOutput = new TextLineNumber(txtAreaProgramOutput);
        spProgramOutput.setRowHeaderView(tlnProgramOutput);
    }

    private MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

    private final Stack<byte[]> undoStorage = new Stack<>();
    private final Stack<RedoWriterInformation> redoStorage = new Stack<>();

    public void appendPacketResult(byte[] packet) {
        opCodeButtonControl(false);

        StringBuilder sb = new StringBuilder();
        if (txtAreaPacketResult.getDocument().getLength() > 0) {
            sb.append(" ");
        }
        sb.append(HexTool.toString(packet));
        txtAreaPacketResult.append(sb.toString());
        txtAreaPacketResult.setCaretPosition(txtAreaPacketResult.getDocument().getLength());
        updateCurrentLength();
    }

    public void appendPacketLog(int size, String prefix, String packet) {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(size).append(") ").append(prefix).append(" [").append(packet).append("]");
        String comment = txtComment.getText().trim();
        if (!comment.equals("")) {
            sb.append(" ").append(comment);
        }
        sb.append("\r\n");
        txtAreaPacketLog.append(sb.toString());
        txtAreaPacketLog.setCaretPosition(txtAreaPacketLog.getDocument().getLength());
    }

    public void appendPacketOPCode(short opCode, byte[] packet) {
        txtAreaPacketLog.append("[封包包頭] (2) mplew.writeShort(" + opCode + ") [" + HexTool.toString(packet) + "]\r\n");
        txtAreaPacketLog.setCaretPosition(txtAreaPacketLog.getDocument().getLength());
        txtAreaProgramOutput.append("mplew.writeShort(" + opCode + "); // 封包包頭\r\n");
        txtAreaProgramOutput.setCaretPosition(txtAreaProgramOutput.getDocument().getLength());
    }

    public void appendProgramOutput(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append(code);
        String comment = txtComment.getText().trim();
        if (!comment.equals("")) {
            sb.append(" // ").append(comment);
        }
        sb.append("\r\n");
        txtAreaProgramOutput.append(sb.toString());
        txtAreaProgramOutput.setCaretPosition(txtAreaProgramOutput.getDocument().getLength());
    }

    public void appendProgramPos(Point pos) {
        StringBuilder sb = new StringBuilder();
        sb.append("mplew.writePos(pos); // X = ").append(pos.x).append(" Y = ").append(pos.y);
        String comment = txtComment.getText().trim();
        if (!comment.equals("")) {
            sb.append(" ").append(comment);
        }
        sb.append("\r\n");
        txtAreaProgramOutput.append(sb.toString());
        txtAreaProgramOutput.setCaretPosition(txtAreaProgramOutput.getDocument().getLength());
    }

    public void appendProgramByteArray(byte[] packet) {
        StringBuilder sb = new StringBuilder();
        sb.append("mplew.write(packet); // [").append(HexTool.toString(packet)).append("]");
        String comment = txtComment.getText().trim();
        if (!comment.equals("")) {
            sb.append(" ").append(comment);
        }
        sb.append("\r\n");
        txtAreaProgramOutput.append(sb.toString());
        txtAreaProgramOutput.setCaretPosition(txtAreaProgramOutput.getDocument().getLength());
    }

    private void updateCurrentLength() {
        txtPacketLength.setText(String.valueOf(mplew.getLength()));
        btnClear.setEnabled(mplew.getLength() > 0);
        updateCurrentCursor();
    }

    private void updateCurrentCursor() {
        updateCurrentCursor(-1);
    }

    private void updateCurrentCursor(int forcePosition) {
        int position = forcePosition == -1 ? txtAreaPacketResult.getCaretPosition() : forcePosition;
        txtCursorPosition.setText(String.valueOf(position));
        int index = position / 3;
        txtCursorIndex.setText(String.valueOf(index));
    }

    private void clearState() {
        redoStorage.clear();
        undoStorage.clear();

        btnRedo.setEnabled(false);
        btnUndo.setEnabled(false);
    }

    private void toDefaultValue() {
        handleAutomaticInputClear(true);

        cbOPCodeDecHex.setSelectedIndex(1);
        chkAutomaticClean.setSelected(false);
    }

    private void clearPacketData() {
        txtAreaPacketLog.setText("");
        txtAreaProgramOutput.setText("");

        txtPacketLength.setText("0");

        txtAreaPacketResult.setText("");
        txtCursorPosition.setText("0");
        txtCursorIndex.setText("0");

        txtAreaPacketResult.setCaretPosition(0);
        mplew = new MaplePacketLittleEndianWriter();
    }

    private void clearButtonControl() {
        opCodeButtonControl(true);

        btnUndo.setEnabled(false);
        btnRedo.setEnabled(false);
        btnClear.setEnabled(false);
    }

    private void opCodeButtonControl(boolean enable) {
        btnWriteOPCode.setEnabled(enable);
        cbOPCodeDecHex.setEnabled(enable);
    }

    private void updateUndoStack(byte[] currentPacket) {
        if (!redoStorage.empty()) {
            btnRedo.setEnabled(false);
            redoStorage.clear();
        }
        undoStorage.push(currentPacket);
        btnUndo.setEnabled(true);
        handleAutomaticInputClear(false);
    }

    private void handleAutomaticInputClear(boolean force) {
        if (chkAutomaticClean.isSelected() || force) {
            txtAreaPacketData.setText("");
            txtComment.setText("");
            txtX.setText("");
            txtY.setText("");
            txtZeroByte.setText("");
        }
    }

    private class RedoWriterInformation {

        public byte[] packet;
        public String data, programData;

        public RedoWriterInformation(byte[] packet, String data, String programData) {
            this.packet = packet;
            this.data = data;
            this.programData = programData;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelColumn = new javax.swing.JLabel();
        labelPacketWriterHelper = new javax.swing.JLabel();
        labelPacketResult = new javax.swing.JLabel();
        spPacketResult = new javax.swing.JScrollPane();
        txtAreaPacketResult = new javax.swing.JTextArea();
        btnRedo = new javax.swing.JButton();
        btnUndo = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        labelPacketWrite = new javax.swing.JLabel();
        btnWriteByte = new javax.swing.JButton();
        btnWriteShort = new javax.swing.JButton();
        btnWriteInt = new javax.swing.JButton();
        btnWriteLong = new javax.swing.JButton();
        btnWriteAsciiString = new javax.swing.JButton();
        btnWriteMapleAsciiString = new javax.swing.JButton();
        btnWritePos = new javax.swing.JButton();
        btnWrite = new javax.swing.JButton();
        labelPacketData = new javax.swing.JLabel();
        labelDataWrite = new javax.swing.JLabel();
        spPacketData = new javax.swing.JScrollPane();
        txtAreaPacketData = new javax.swing.JTextArea();
        labelComment = new javax.swing.JLabel();
        txtComment = new javax.swing.JTextField();
        labelPacketOutput = new javax.swing.JLabel();
        labelPacketLength = new javax.swing.JLabel();
        txtPacketLength = new javax.swing.JTextField();
        labelCursorPosition = new javax.swing.JLabel();
        txtCursorPosition = new javax.swing.JTextField();
        labelCursorIndex = new javax.swing.JLabel();
        txtCursorIndex = new javax.swing.JTextField();
        tabPanePacketOutput = new javax.swing.JTabbedPane();
        panelPacketLog = new javax.swing.JPanel();
        spPacketLog = new javax.swing.JScrollPane();
        txtAreaPacketLog = new javax.swing.JTextArea();
        panelProgramOutput = new javax.swing.JPanel();
        spProgramOutput = new javax.swing.JScrollPane();
        txtAreaProgramOutput = new javax.swing.JTextArea();
        labelX = new javax.swing.JLabel();
        txtX = new javax.swing.JTextField();
        txtY = new javax.swing.JTextField();
        lavelY = new javax.swing.JLabel();
        btnWriteZeroByte = new javax.swing.JButton();
        txtZeroByte = new javax.swing.JTextField();
        btnWriteOPCode = new javax.swing.JButton();
        chkAutomaticClean = new javax.swing.JCheckBox();
        cbOPCodeDecHex = new javax.swing.JComboBox<>();

        labelColumn.setFont(new java.awt.Font("細明體", 0, 13)); // NOI18N
        labelColumn.setText(" 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("棉被家族 - 輔助發包工具");
        setResizable(false);

        labelPacketWriterHelper.setFont(new java.awt.Font("微軟正黑體", 0, 14)); // NOI18N
        labelPacketWriterHelper.setText("輔助發包工具");

        labelPacketResult.setText("發包結果");

        txtAreaPacketResult.setColumns(20);
        txtAreaPacketResult.setFont(new java.awt.Font("細明體", 0, 13)); // NOI18N
        txtAreaPacketResult.setRows(5);
        txtAreaPacketResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAreaPacketResultMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtAreaPacketResultMouseReleased(evt);
            }
        });
        txtAreaPacketResult.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAreaPacketResultKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAreaPacketResultKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaPacketResultKeyTyped(evt);
            }
        });
        spPacketResult.setViewportView(txtAreaPacketResult);

        btnRedo.setText("重做");
        btnRedo.setEnabled(false);
        btnRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedoActionPerformed(evt);
            }
        });

        btnUndo.setText("復原");
        btnUndo.setEnabled(false);
        btnUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUndoActionPerformed(evt);
            }
        });

        btnClear.setText("清除");
        btnClear.setEnabled(false);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        labelPacketWrite.setText("封包寫入");

        btnWriteByte.setText("writeByte(byte)");
        btnWriteByte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWriteByteActionPerformed(evt);
            }
        });

        btnWriteShort.setText("writeShort(short)");
        btnWriteShort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWriteShortActionPerformed(evt);
            }
        });

        btnWriteInt.setText("writeInt(int)");
        btnWriteInt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWriteIntActionPerformed(evt);
            }
        });

        btnWriteLong.setText("writeLong(long)");
        btnWriteLong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWriteLongActionPerformed(evt);
            }
        });

        btnWriteAsciiString.setText("writeAsciiString(String)");
        btnWriteAsciiString.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWriteAsciiStringActionPerformed(evt);
            }
        });

        btnWriteMapleAsciiString.setText("writeMapleAsciiString(String)");
        btnWriteMapleAsciiString.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWriteMapleAsciiStringActionPerformed(evt);
            }
        });

        btnWritePos.setText("writePos(Point)");
        btnWritePos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWritePosActionPerformed(evt);
            }
        });

        btnWrite.setText("write(byte[])");
        btnWrite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWriteActionPerformed(evt);
            }
        });

        labelPacketData.setText("封包資料");

        labelDataWrite.setText("資料寫入");

        txtAreaPacketData.setColumns(20);
        txtAreaPacketData.setRows(5);
        spPacketData.setViewportView(txtAreaPacketData);

        labelComment.setText("註解");

        labelPacketOutput.setText("封包輸出");

        labelPacketLength.setText("大小");

        txtPacketLength.setText("0");
        txtPacketLength.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPacketLengthKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPacketLengthKeyTyped(evt);
            }
        });

        labelCursorPosition.setText("游標位置");

        txtCursorPosition.setText("0");
        txtCursorPosition.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCursorPositionKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCursorPositionKeyTyped(evt);
            }
        });

        labelCursorIndex.setText("游標註標");

        txtCursorIndex.setText("0");
        txtCursorIndex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCursorIndexKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCursorIndexKeyTyped(evt);
            }
        });

        txtAreaPacketLog.setColumns(20);
        txtAreaPacketLog.setRows(5);
        txtAreaPacketLog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAreaPacketLogKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaPacketLogKeyTyped(evt);
            }
        });
        spPacketLog.setViewportView(txtAreaPacketLog);

        javax.swing.GroupLayout panelPacketLogLayout = new javax.swing.GroupLayout(panelPacketLog);
        panelPacketLog.setLayout(panelPacketLogLayout);
        panelPacketLogLayout.setHorizontalGroup(
            panelPacketLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spPacketLog, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
        );
        panelPacketLogLayout.setVerticalGroup(
            panelPacketLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spPacketLog, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );

        tabPanePacketOutput.addTab("封包紀錄", panelPacketLog);

        txtAreaProgramOutput.setColumns(20);
        txtAreaProgramOutput.setRows(5);
        txtAreaProgramOutput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAreaProgramOutputKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaProgramOutputKeyTyped(evt);
            }
        });
        spProgramOutput.setViewportView(txtAreaProgramOutput);

        javax.swing.GroupLayout panelProgramOutputLayout = new javax.swing.GroupLayout(panelProgramOutput);
        panelProgramOutput.setLayout(panelProgramOutputLayout);
        panelProgramOutputLayout.setHorizontalGroup(
            panelProgramOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spProgramOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
        );
        panelProgramOutputLayout.setVerticalGroup(
            panelProgramOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spProgramOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );

        tabPanePacketOutput.addTab("程式碼輸出", panelProgramOutput);

        labelX.setText("X");

        txtX.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtXKeyTyped(evt);
            }
        });

        txtY.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtYKeyTyped(evt);
            }
        });

        lavelY.setText("Y");

        btnWriteZeroByte.setText("writeZeroByte()");
        btnWriteZeroByte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWriteZeroByteActionPerformed(evt);
            }
        });

        txtZeroByte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtZeroByteKeyTyped(evt);
            }
        });

        btnWriteOPCode.setText("寫入包頭");
        btnWriteOPCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWriteOPCodeActionPerformed(evt);
            }
        });

        chkAutomaticClean.setText("自動清除");

        cbOPCodeDecHex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10 進位", "16 進位" }));
        cbOPCodeDecHex.setSelectedIndex(1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spPacketResult, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelPacketWriterHelper)
                            .addComponent(labelPacketWrite))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelPacketResult)
                                .addGap(18, 18, 18)
                                .addComponent(btnWriteOPCode)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbOPCodeDecHex, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnUndo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRedo))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnWriteInt)
                                    .addComponent(btnWriteLong)
                                    .addComponent(btnWritePos)
                                    .addComponent(btnWrite)
                                    .addComponent(btnWriteAsciiString)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(btnWriteShort)
                                                .addGap(8, 8, 8))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(btnWriteMapleAsciiString)
                                                .addComponent(btnWriteByte)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(labelComment)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtComment, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(labelX)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(txtX, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(74, 74, 74)
                                                        .addComponent(lavelY)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(txtY, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(labelDataWrite)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(chkAutomaticClean))
                                                    .addComponent(spPacketData)))
                                            .addComponent(labelPacketData))))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(labelPacketOutput)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(tabPanePacketOutput)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelPacketLength)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPacketLength, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelCursorPosition)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCursorPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelCursorIndex)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCursorIndex, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtZeroByte, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnWriteZeroByte)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelPacketWriterHelper)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPacketResult)
                    .addComponent(btnRedo)
                    .addComponent(btnUndo)
                    .addComponent(btnWriteOPCode)
                    .addComponent(cbOPCodeDecHex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spPacketResult, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPacketWrite)
                    .addComponent(labelPacketData)
                    .addComponent(labelPacketOutput))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnWriteByte)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnWriteShort)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnWriteInt)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnWriteLong)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnWriteAsciiString)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnWriteMapleAsciiString))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labelDataWrite)
                                    .addComponent(chkAutomaticClean))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spPacketData)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnWritePos)
                            .addComponent(labelComment)
                            .addComponent(txtComment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnWrite)
                            .addComponent(labelX)
                            .addComponent(txtX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lavelY)))
                    .addComponent(tabPanePacketOutput))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPacketLength)
                    .addComponent(txtPacketLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCursorPosition)
                    .addComponent(txtCursorPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCursorIndex)
                    .addComponent(txtCursorIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnWriteZeroByte)
                    .addComponent(txtZeroByte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAreaPacketResultKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaPacketResultKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaPacketResultKeyTyped

    private void txtXKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtXKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.numberWithNegative(evt);
    }//GEN-LAST:event_txtXKeyTyped

    private void txtYKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtYKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.numberWithNegative(evt);
    }//GEN-LAST:event_txtYKeyTyped

    private void txtPacketLengthKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPacketLengthKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtPacketLengthKeyTyped

    private void txtCursorPositionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCursorPositionKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtCursorPositionKeyTyped

    private void txtCursorIndexKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCursorIndexKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtCursorIndexKeyTyped

    private void txtZeroByteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtZeroByteKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.numberOnly(evt);
    }//GEN-LAST:event_txtZeroByteKeyTyped

    private void txtAreaPacketLogKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaPacketLogKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaPacketLogKeyTyped

    private void txtAreaProgramOutputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaProgramOutputKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaProgramOutputKeyTyped

    private void txtAreaPacketResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAreaPacketResultMouseClicked
        // TODO add your handling code here:
        updateCurrentCursor();
    }//GEN-LAST:event_txtAreaPacketResultMouseClicked

    private void btnUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUndoActionPerformed
        // TODO add your handling code here:
        String toRemove, toRemoveProgram;
        try {
            toRemove = PacketHelperTool.removeLastLine(txtAreaPacketLog);
            toRemoveProgram = PacketHelperTool.removeLastLine(txtAreaProgramOutput);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
            return;
        }

        byte[] last = undoStorage.pop();

        if (undoStorage.empty()) {
            mplew = new MaplePacketLittleEndianWriter();
            txtAreaPacketResult.setText("");

            opCodeButtonControl(true);
            btnUndo.setEnabled(false);
        } else {
            byte[] packet = mplew.subByteArray(mplew.getLength() - last.length);
            mplew = new MaplePacketLittleEndianWriter();
            mplew.write(packet);

            txtAreaPacketResult.setText(HexTool.toString(mplew.getPacket()));
            txtAreaPacketResult.setCaretPosition(txtAreaPacketResult.getDocument().getLength());
        }
        updateCurrentLength();

        redoStorage.push(new RedoWriterInformation(last, toRemove, toRemoveProgram));
        btnRedo.setEnabled(true);
    }//GEN-LAST:event_btnUndoActionPerformed

    private void btnRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedoActionPerformed
        // TODO add your handling code here:
        RedoWriterInformation info = redoStorage.pop();
        mplew.write(info.packet);

        appendPacketResult(info.packet);
        PacketHelperTool.appendTextArea(txtAreaPacketLog, info.data);
        PacketHelperTool.appendTextArea(txtAreaProgramOutput, info.programData);

        undoStorage.push(info.packet);
        btnUndo.setEnabled(true);
        if (redoStorage.empty()) {
            btnRedo.setEnabled(false);
        }
    }//GEN-LAST:event_btnRedoActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearState();
        toDefaultValue();
        clearPacketData();
        clearButtonControl();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnWriteByteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWriteByteActionPerformed
        // TODO add your handling code here:
        byte write;
        try {
            write = Byte.parseByte(txtAreaPacketData.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "請檢查輸入數值是否正確 ...");
            return;
        }

        MaplePacketLittleEndianWriter byteWrite = new MaplePacketLittleEndianWriter();
        byteWrite.writeByte(write);
        byte[] packet = byteWrite.getPacket();
        mplew.writeByte(write);

        appendPacketResult(packet);
        appendPacketLog(1, "mplew.writeByte(" + write + ") 位元組", HexTool.toString(packet));
        appendProgramOutput("mplew.writeByte(" + write + ");");
        updateUndoStack(packet);
    }//GEN-LAST:event_btnWriteByteActionPerformed

    private void btnWriteShortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWriteShortActionPerformed
        // TODO add your handling code here:
        short write;
        try {
            write = Short.parseShort(txtAreaPacketData.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "請檢查輸入數值是否正確 ...");
            return;
        }

        MaplePacketLittleEndianWriter byteWrite = new MaplePacketLittleEndianWriter();
        byteWrite.writeShort(write);
        byte[] packet = byteWrite.getPacket();
        mplew.writeShort(write);

        appendPacketResult(packet);
        appendPacketLog(2, "mplew.writeShort(" + write + ") 短整數", HexTool.toString(packet));
        appendProgramOutput("mplew.writeShort(" + write + ");");
        updateUndoStack(packet);
    }//GEN-LAST:event_btnWriteShortActionPerformed

    private void btnWriteIntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWriteIntActionPerformed
        // TODO add your handling code here:
        int write;
        try {
            write = Integer.parseInt(txtAreaPacketData.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "請檢查輸入數值是否正確 ...");
            return;
        }

        MaplePacketLittleEndianWriter byteWrite = new MaplePacketLittleEndianWriter();
        byteWrite.writeInt(write);
        byte[] packet = byteWrite.getPacket();
        mplew.writeInt(write);

        appendPacketResult(packet);
        appendPacketLog(4, "mplew.writeInt(" + write + ") 整數", HexTool.toString(packet));
        appendProgramOutput("mplew.writeInt(" + write + ");");
        updateUndoStack(packet);
    }//GEN-LAST:event_btnWriteIntActionPerformed

    private void btnWriteLongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWriteLongActionPerformed
        // TODO add your handling code here:
        long write;
        try {
            write = Long.parseLong(txtAreaPacketData.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "請檢查輸入數值是否正確 ...");
            return;
        }

        MaplePacketLittleEndianWriter byteWrite = new MaplePacketLittleEndianWriter();
        byteWrite.writeLong(write);
        byte[] packet = byteWrite.getPacket();
        mplew.writeLong(write);

        appendPacketResult(packet);
        appendPacketLog(8, "mplew.writeLong(" + write + ") 長整數", HexTool.toString(packet));
        appendProgramOutput("mplew.writeLong(" + write + ");");
        updateUndoStack(packet);
    }//GEN-LAST:event_btnWriteLongActionPerformed

    private void btnWriteAsciiStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWriteAsciiStringActionPerformed
        // TODO add your handling code here:
        String write = txtAreaPacketData.getText();
        if (write.length() <= 0) {
            JOptionPane.showMessageDialog(this, "請輸入字串資料 ...");
            return;
        }

        MaplePacketLittleEndianWriter byteWrite = new MaplePacketLittleEndianWriter();
        byteWrite.writeAsciiString(write);
        byte[] packet = byteWrite.getPacket();
        mplew.writeAsciiString(write);

        appendPacketResult(packet);
        appendPacketLog(write.length(), "mplew.writeAsciiString(\"" + PacketHelperTool.handleEscapeString(write) + "\") 字串", HexTool.toString(packet));
        appendProgramOutput("mplew.writeAsciiString(\"" + PacketHelperTool.handleEscapeString(write) + "\");");
        updateUndoStack(packet);
    }//GEN-LAST:event_btnWriteAsciiStringActionPerformed

    private void btnWriteMapleAsciiStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWriteMapleAsciiStringActionPerformed
        // TODO add your handling code here:
        String write = txtAreaPacketData.getText();
        if (write.length() <= 0) {
            JOptionPane.showMessageDialog(this, "請輸入字串資料 ...");
            return;
        }

        MaplePacketLittleEndianWriter byteWrite = new MaplePacketLittleEndianWriter();
        byteWrite.writeMapleAsciiString(write);
        byte[] packet = byteWrite.getPacket();
        mplew.writeMapleAsciiString(write);

        appendPacketResult(packet);
        appendPacketLog(write.length() + 2, "mplew.writeMapleAsciiString(\"" + PacketHelperTool.handleEscapeString(write) + "\") 楓之谷字串", HexTool.toString(packet));
        appendProgramOutput("mplew.writeMapleAsciiString(\"" + PacketHelperTool.handleEscapeString(write) + "\");");
        updateUndoStack(packet);
    }//GEN-LAST:event_btnWriteMapleAsciiStringActionPerformed

    private void btnWritePosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWritePosActionPerformed
        // TODO add your handling code here:
        short x, y;
        try {
            x = Short.parseShort(txtX.getText());
            y = Short.parseShort(txtY.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "請檢查輸入座標是否正確 ...");
            return;
        }
        Point pos = new Point(x, y);

        MaplePacketLittleEndianWriter byteWrite = new MaplePacketLittleEndianWriter();
        byteWrite.writePos(pos);
        byte[] packet = byteWrite.getPacket();
        mplew.writePos(pos);

        appendPacketResult(packet);
        appendPacketLog(4, "mplew.writePos(pos) 座標 X : " + x + " Y : " + y, HexTool.toString(packet));
        appendProgramPos(pos);
        updateUndoStack(packet);
    }//GEN-LAST:event_btnWritePosActionPerformed

    private void btnWriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWriteActionPerformed
        // TODO add your handling code here:
        int length = PacketHelperTool.fixPacketStream(txtAreaPacketData.getText()).length();
        if (length <= 0 || length % 2 == 1) {
            JOptionPane.showMessageDialog(this, "位元組陣列長度錯誤 ...");
            return;
        }

        byte[] packet;
        try {
            packet = HexTool.getByteArrayFromHexString(txtAreaPacketData.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "位元組陣列資料輸入錯誤 ...");
            return;
        }

        mplew.write(packet);

        appendPacketResult(packet);
        appendPacketLog(packet.length, "mplew.write(packet) 位元組陣列", HexTool.toString(packet));
        appendProgramByteArray(packet);
        updateUndoStack(packet);
    }//GEN-LAST:event_btnWriteActionPerformed

    private void btnWriteZeroByteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWriteZeroByteActionPerformed
        // TODO add your handling code here:
        int length;
        try {
            length = Integer.parseInt(txtZeroByte.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "長度輸入錯誤 ...");
            return;
        }

        MaplePacketLittleEndianWriter byteWrite = new MaplePacketLittleEndianWriter();
        byteWrite.writeZeroByte(length);
        byte[] packet = byteWrite.getPacket();
        mplew.writeZeroByte(length);

        appendPacketResult(packet);
        appendPacketLog(length, "mplew.writeZeroByte(" + length + ") 0 位元組資料", HexTool.toString(packet));
        appendProgramOutput("mplew.writeZeroByte(" + length + ");");
        updateUndoStack(packet);
    }//GEN-LAST:event_btnWriteZeroByteActionPerformed

    private void btnWriteOPCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWriteOPCodeActionPerformed
        // TODO add your handling code here:
        short opCode;
        try {
            opCode = Short.parseShort(txtAreaPacketData.getText(), cbOPCodeDecHex.getSelectedIndex() == 1 ? 16 : 10);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "請檢查輸入數值是否正確 ...");
            return;
        }

        MaplePacketLittleEndianWriter byteWrite = new MaplePacketLittleEndianWriter();
        byteWrite.writeShort(opCode);
        byte[] packet = byteWrite.getPacket();
        mplew.writeShort(opCode);

        appendPacketResult(packet);
        appendPacketOPCode(opCode, packet);
        updateUndoStack(packet);
    }//GEN-LAST:event_btnWriteOPCodeActionPerformed

    private void txtAreaPacketResultMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAreaPacketResultMouseReleased
        // TODO add your handling code here:
        if (txtAreaPacketResult.getSelectedText() != null) {
            updateCurrentCursor(txtAreaPacketResult.getSelectionStart());
        }
    }//GEN-LAST:event_txtAreaPacketResultMouseReleased

    private void txtAreaPacketLogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaPacketLogKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaPacketLogKeyPressed

    private void txtAreaProgramOutputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaProgramOutputKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaProgramOutputKeyPressed

    private void txtAreaPacketResultKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaPacketResultKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaPacketResultKeyPressed

    private void txtAreaPacketResultKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaPacketResultKeyReleased
        // TODO add your handling code here:
        updateCurrentCursor();
    }//GEN-LAST:event_txtAreaPacketResultKeyReleased

    private void txtPacketLengthKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPacketLengthKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtPacketLengthKeyPressed

    private void txtCursorPositionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCursorPositionKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtCursorPositionKeyPressed

    private void txtCursorIndexKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCursorIndexKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtCursorIndexKeyPressed

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
                new PacketWriterHelper().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnRedo;
    private javax.swing.JButton btnUndo;
    private javax.swing.JButton btnWrite;
    private javax.swing.JButton btnWriteAsciiString;
    private javax.swing.JButton btnWriteByte;
    private javax.swing.JButton btnWriteInt;
    private javax.swing.JButton btnWriteLong;
    private javax.swing.JButton btnWriteMapleAsciiString;
    private javax.swing.JButton btnWriteOPCode;
    private javax.swing.JButton btnWritePos;
    private javax.swing.JButton btnWriteShort;
    private javax.swing.JButton btnWriteZeroByte;
    private javax.swing.JComboBox<String> cbOPCodeDecHex;
    private javax.swing.JCheckBox chkAutomaticClean;
    private javax.swing.JLabel labelColumn;
    private javax.swing.JLabel labelComment;
    private javax.swing.JLabel labelCursorIndex;
    private javax.swing.JLabel labelCursorPosition;
    private javax.swing.JLabel labelDataWrite;
    private javax.swing.JLabel labelPacketData;
    private javax.swing.JLabel labelPacketLength;
    private javax.swing.JLabel labelPacketOutput;
    private javax.swing.JLabel labelPacketResult;
    private javax.swing.JLabel labelPacketWrite;
    private javax.swing.JLabel labelPacketWriterHelper;
    private javax.swing.JLabel labelX;
    private javax.swing.JLabel lavelY;
    private javax.swing.JPanel panelPacketLog;
    private javax.swing.JPanel panelProgramOutput;
    private javax.swing.JScrollPane spPacketData;
    private javax.swing.JScrollPane spPacketLog;
    private javax.swing.JScrollPane spPacketResult;
    private javax.swing.JScrollPane spProgramOutput;
    private javax.swing.JTabbedPane tabPanePacketOutput;
    private javax.swing.JTextArea txtAreaPacketData;
    private javax.swing.JTextArea txtAreaPacketLog;
    private javax.swing.JTextArea txtAreaPacketResult;
    private javax.swing.JTextArea txtAreaProgramOutput;
    private javax.swing.JTextField txtComment;
    private javax.swing.JTextField txtCursorIndex;
    private javax.swing.JTextField txtCursorPosition;
    private javax.swing.JTextField txtPacketLength;
    private javax.swing.JTextField txtX;
    private javax.swing.JTextField txtY;
    private javax.swing.JTextField txtZeroByte;
    // End of variables declaration//GEN-END:variables
}
