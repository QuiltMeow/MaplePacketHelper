package ew.quilt.packet;

import ew.quilt.exception.PacketStringWrongSizeException;
import ew.quilt.helper.PacketHelperTool;
import ew.quilt.helper.component.TextLineNumber;
import ew.quilt.tool.HexTool;
import ew.quilt.tool.data.ByteArrayByteStream;
import ew.quilt.tool.data.LittleEndianAccessor;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

public class PacketReaderHelper extends javax.swing.JFrame {

    public PacketReaderHelper() {
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
        setIconImage(new ImageIcon(PacketReaderHelper.class.getResource("/ew/quilt/icon/Maple.png")).getImage());
        initExtraComponent();
    }

    private void initExtraComponent() {
        txtAreaPacketField.setLineWrap(true);
        txtAreaPacketField.setWrapStyleWord(true);
        spPacketField.setColumnHeaderView(labelColumn);

        TextLineNumber tlnPacketOutput = new TextLineNumber(txtAreaPacketOutput);
        spPacketOutput.setRowHeaderView(tlnPacketOutput);
        TextLineNumber tlnProgramOutput = new TextLineNumber(txtAreaProgramOutput);
        spProgramOutput.setRowHeaderView(tlnProgramOutput);

        txtAreaPacketField.requestFocus();
    }

    private LittleEndianAccessor slea = null;

    private boolean analysis = false;
    private int currentNoName = 0;

    private final Stack<String> packetName = new Stack<>();
    private final Stack<UndoReaderInformation> undoStorage = new Stack<>();
    private final Stack<RedoReaderInformation> redoStorage = new Stack<>();

    private void updateCurrentPacket() {
        updateCurrentPacket(-1);
    }

    private void updateCurrentPacket(int forcePosition) {
        int position = forcePosition == -1 ? txtAreaPacketField.getCaretPosition() : forcePosition;
        txtCursorPosition.setText(String.valueOf(position));

        if (analysis) {
            int index = position / 3;
            txtCursorIndex.setText(String.valueOf(index));

            try {
                byte byteRead = slea.getByte(index);
                txtByte.setText(rbDec.isSelected() ? String.valueOf(byteRead) : Integer.toHexString(byteRead & 0xFF).toUpperCase());
            } catch (Exception ex) {
                txtByte.setText("無法辨識");
            }

            try {
                short shortRead = slea.getShort(index);
                txtShort.setText(rbDec.isSelected() ? String.valueOf(shortRead) : Integer.toHexString(shortRead & 0xFFFF).toUpperCase());
            } catch (Exception ex) {
                txtShort.setText("無法辨識");
            }

            try {
                int intRead = slea.getInt(index);
                txtInteger.setText(rbDec.isSelected() ? String.valueOf(intRead) : Integer.toHexString(intRead).toUpperCase());
            } catch (Exception ex) {
                txtInteger.setText("無法辨識");
            }

            try {
                long longRead = slea.getLong(index);
                txtLong.setText(rbDec.isSelected() ? String.valueOf(longRead) : Long.toHexString(longRead).toUpperCase());
            } catch (Exception ex) {
                txtLong.setText("無法辨識");
            }

            int StringLength = checkStringLength();
            if (StringLength != -1) {
                try {
                    String StringRead = slea.getAsciiString(index, StringLength);
                    txtAsciiString.setText(PacketHelperTool.handleEscapeString(StringRead));
                } catch (Exception ex) {
                    txtAsciiString.setText("無法辨識");
                }
            } else {
                txtAsciiString.setText("輸入長度錯誤");
            }

            try {
                String mapleAsciiStringRead = slea.getMapleAsciiString(index);
                txtMapleAsciiString.setText(PacketHelperTool.handleEscapeString(mapleAsciiStringRead));
            } catch (Exception ex) {
                txtMapleAsciiString.setText("無法辨識");
            }

            try {
                Point posRead = slea.getPos(index);
                txtPositionX.setText(rbDec.isSelected() ? String.valueOf(posRead.x) : Integer.toHexString(posRead.x & 0xFFFF).toUpperCase());
                txtPositionY.setText(rbDec.isSelected() ? String.valueOf(posRead.y) : Integer.toHexString(posRead.y & 0xFFFF).toUpperCase());
            } catch (Exception ex) {
                txtPositionX.setText("無法辨識");
                txtPositionY.setText("無法辨識");
            }
        }
    }

    private void updateCurrentIndex() {
        txtCurrent.setText(String.valueOf(slea.getPosition()));
        updateCurrentCursor();
    }

    private void updateCurrentCursor() {
        txtAreaPacketField.requestFocus();
        int docLength = txtAreaPacketField.getDocument().getLength();
        int packet = (int) slea.getPosition() * 3;
        txtAreaPacketField.setCaretPosition(packet > docLength ? docLength : packet);
        updateCurrentPacket();
    }

    private int checkStringLength() {
        int ret = -1;
        try {
            ret = Integer.parseInt(txtStringLength.getText());
        } catch (NumberFormatException ex) {
        }
        if (ret < 0 || ret > slea.getlength()) {
            ret = -1;
        }
        return ret;
    }

    private boolean checkPacketName() {
        if (txtPacketName.getText().trim().equals("未命名")) {
            JOptionPane.showMessageDialog(this, "封包名稱包含不允許字元");
            return false;
        }
        return true;
    }

    private void updateSamePacketName() {
        Set<String> packetNameCheck = new HashSet<>(packetName);
        labelWarning.setText(packetNameCheck.size() < packetName.size() ? "<偵測到相同名稱封包>" : "");
    }

    private String getPacketName() {
        String ret = txtPacketName.getText().trim();
        if (ret.equals("")) {
            return "未命名";
        }
        if (packetName.contains(ret)) {
            labelWarning.setText("<偵測到相同名稱封包>");
        }
        packetName.push(ret);
        return ret;
    }

    private void updateUndoStack(int currentSize) {
        if (!redoStorage.empty()) {
            btnRedo.setEnabled(false);
            redoStorage.clear();
        }
        String currentPacketName = null;
        String packetNameText = txtPacketName.getText().trim();
        if (!packetNameText.equals("")) {
            currentPacketName = packetNameText;
        }
        undoStorage.push(new UndoReaderInformation(currentSize, currentPacketName));
        btnUndo.setEnabled(true);
    }

    public void appendPacketOutput(int size, String prefix, String packet) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getPacketName()).append("] (").append(size).append(") ").append(prefix).append(" : ").append(packet);
        String comment = txtComment.getText().trim();
        if (!comment.equals("")) {
            sb.append(" ").append(comment);
        }
        sb.append("\r\n");
        txtAreaPacketOutput.append(sb.toString());
        txtAreaPacketOutput.setCaretPosition(txtAreaPacketOutput.getDocument().getLength());
    }

    public void appendProgramOutput(String type, String code) {
        String name = txtPacketName.getText().trim();
        if (name.equals("")) {
            name = "unk_" + (++currentNoName);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(" ").append(name).append(" = ").append(code);
        String comment = txtComment.getText().trim();
        if (!comment.equals("")) {
            sb.append(" // ").append(comment);
        }
        sb.append("\r\n");
        txtAreaProgramOutput.append(sb.toString());
        txtAreaProgramOutput.setCaretPosition(txtAreaProgramOutput.getDocument().getLength());
    }

    public void appendProgramSkip(int length) {
        StringBuilder sb = new StringBuilder();
        sb.append("slea.skip(").append(length).append(");");
        String comment = txtComment.getText().trim();
        if (!comment.equals("")) {
            sb.append(" // ").append(comment);
        }
        txtAreaProgramOutput.append(sb.toString());
        txtAreaProgramOutput.setCaretPosition(txtAreaProgramOutput.getDocument().getLength());
    }

    public void appendOPCode(String packet) {
        txtAreaPacketOutput.append("[封包包頭] (2) slea.readShort() : " + packet + "\r\n");
        txtAreaPacketOutput.setCaretPosition(txtAreaPacketOutput.getDocument().getLength());
    }

    private void buttonControl(boolean enable) {
        btnAnalysis.setEnabled(!enable);
        chkContainOPCode.setEnabled(!enable);

        btnClear.setEnabled(enable);
        btnReadByte.setEnabled(enable);
        btnReadShort.setEnabled(enable);
        btnReadInt.setEnabled(enable);
        btnReadLong.setEnabled(enable);
        btnReadAsciiString.setEnabled(enable);
        btnReadMapleAsciiString.setEnabled(enable);
        btnReadPos.setEnabled(enable);
        btnRead.setEnabled(enable);
        btnCurrentCursor.setEnabled(enable);

        txtStringLength.setEnabled(enable);
        txtReadLength.setEnabled(enable);
        txtPacketName.setEnabled(enable);
        txtComment.setEnabled(enable);

        chkSkip.setEnabled(enable);
    }

    private void clearState() {
        redoStorage.clear();
        undoStorage.clear();

        packetName.clear();
        currentNoName = 0;

        btnRedo.setEnabled(false);
        btnUndo.setEnabled(false);

        labelWarning.setText("");
    }

    private void toDefaultValue() {
        txtByte.setText("0");
        txtShort.setText("0");
        txtInteger.setText("0");
        txtLong.setText("0");
        txtAsciiString.setText("");
        txtMapleAsciiString.setText("");
        txtPositionX.setText("0");
        txtPositionY.setText("0");
        txtPacketName.setText("");
        txtStringLength.setText("1");
        txtReadLength.setText("1");
        txtComment.setText("");

        rbDec.setSelected(true);
    }

    private void clearPacketData() {
        txtAreaPacketOutput.setText("");
        txtAreaProgramOutput.setText("");

        txtAll.setText("0");
        txtCurrent.setText("0");

        txtAreaPacketField.setText("");
        txtCursorPosition.setText("0");
        txtCursorIndex.setText("0");

        txtAreaPacketField.setCaretPosition(0);
        slea = null;
    }

    private static class UndoReaderInformation {

        public int size;
        public String packetName;

        public UndoReaderInformation(int size, String packetName) {
            this.size = size;
            this.packetName = packetName;
        }
    }

    private static class RedoReaderInformation {

        public int size;
        public String data, programData, packetName;

        public RedoReaderInformation(int size, String data, String programData, String packetName) {
            this.size = size;
            this.data = data;
            this.programData = programData;
            this.packetName = packetName;
        }
    }

    public byte[] getPacket() { // Export
        return slea.getPacket();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgDecHex = new javax.swing.ButtonGroup();
        labelColumn = new javax.swing.JLabel();
        labelPacketReaderHelper = new javax.swing.JLabel();
        labelPacketTip = new javax.swing.JLabel();
        spPacketField = new javax.swing.JScrollPane();
        txtAreaPacketField = new javax.swing.JTextArea();
        btnAnalysis = new javax.swing.JButton();
        labelPacketAnalysis = new javax.swing.JLabel();
        btnReadByte = new javax.swing.JButton();
        btnReadShort = new javax.swing.JButton();
        btnReadInt = new javax.swing.JButton();
        btnReadLong = new javax.swing.JButton();
        btnReadAsciiString = new javax.swing.JButton();
        btnReadMapleAsciiString = new javax.swing.JButton();
        btnReadPos = new javax.swing.JButton();
        btnRead = new javax.swing.JButton();
        txtReadLength = new javax.swing.JTextField();
        chkSkip = new javax.swing.JCheckBox();
        labelCurrentStatus = new javax.swing.JLabel();
        labelByte = new javax.swing.JLabel();
        txtByte = new javax.swing.JTextField();
        labelShort = new javax.swing.JLabel();
        txtShort = new javax.swing.JTextField();
        labelInteger = new javax.swing.JLabel();
        txtInteger = new javax.swing.JTextField();
        labelLong = new javax.swing.JLabel();
        txtLong = new javax.swing.JTextField();
        labelMapleAsciiString = new javax.swing.JLabel();
        txtMapleAsciiString = new javax.swing.JTextField();
        labelPosition = new javax.swing.JLabel();
        txtPositionX = new javax.swing.JTextField();
        btnUndo = new javax.swing.JButton();
        chkContainOPCode = new javax.swing.JCheckBox();
        labelPacketName = new javax.swing.JLabel();
        labelPacketOutput = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        labelAsciiString = new javax.swing.JLabel();
        txtAsciiString = new javax.swing.JTextField();
        btnCurrentCursor = new javax.swing.JButton();
        labelPositionX = new javax.swing.JLabel();
        labelPositionY = new javax.swing.JLabel();
        txtPositionY = new javax.swing.JTextField();
        rbDec = new javax.swing.JRadioButton();
        rbHex = new javax.swing.JRadioButton();
        btnRedo = new javax.swing.JButton();
        labelCursortPosition = new javax.swing.JLabel();
        labelCurrent = new javax.swing.JLabel();
        txtCurrent = new javax.swing.JTextField();
        labelAll = new javax.swing.JLabel();
        txtAll = new javax.swing.JTextField();
        txtCursorPosition = new javax.swing.JTextField();
        txtPacketName = new javax.swing.JTextField();
        txtStringLength = new javax.swing.JTextField();
        labelCursorIndex = new javax.swing.JLabel();
        txtCursorIndex = new javax.swing.JTextField();
        tabPanePacketOutput = new javax.swing.JTabbedPane();
        panelPacketOutput = new javax.swing.JPanel();
        spPacketOutput = new javax.swing.JScrollPane();
        txtAreaPacketOutput = new javax.swing.JTextArea();
        panelProgramOutput = new javax.swing.JPanel();
        spProgramOutput = new javax.swing.JScrollPane();
        txtAreaProgramOutput = new javax.swing.JTextArea();
        txtComment = new javax.swing.JTextField();
        labelComment = new javax.swing.JLabel();
        btnTryString = new javax.swing.JButton();
        labelWarning = new javax.swing.JLabel();

        labelColumn.setFont(new java.awt.Font("細明體", 0, 13)); // NOI18N
        labelColumn.setText(" 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("棉被家族 - 輔助解包工具");
        setResizable(false);

        labelPacketReaderHelper.setFont(new java.awt.Font("微軟正黑體", 0, 14)); // NOI18N
        labelPacketReaderHelper.setText("輔註解包工具");

        labelPacketTip.setText("請餵食封包");

        txtAreaPacketField.setColumns(20);
        txtAreaPacketField.setFont(new java.awt.Font("細明體", 0, 13)); // NOI18N
        txtAreaPacketField.setRows(5);
        txtAreaPacketField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAreaPacketFieldMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtAreaPacketFieldMouseReleased(evt);
            }
        });
        txtAreaPacketField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAreaPacketFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAreaPacketFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaPacketFieldKeyTyped(evt);
            }
        });
        spPacketField.setViewportView(txtAreaPacketField);

        btnAnalysis.setText("分析");
        btnAnalysis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalysisActionPerformed(evt);
            }
        });

        labelPacketAnalysis.setText("封包解析");

        btnReadByte.setText("readByte()");
        btnReadByte.setEnabled(false);
        btnReadByte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadByteActionPerformed(evt);
            }
        });

        btnReadShort.setText("readShort()");
        btnReadShort.setEnabled(false);
        btnReadShort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadShortActionPerformed(evt);
            }
        });

        btnReadInt.setText("readInt()");
        btnReadInt.setEnabled(false);
        btnReadInt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadIntActionPerformed(evt);
            }
        });

        btnReadLong.setText("readLong()");
        btnReadLong.setEnabled(false);
        btnReadLong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadLongActionPerformed(evt);
            }
        });

        btnReadAsciiString.setText("readAsciiString()");
        btnReadAsciiString.setEnabled(false);
        btnReadAsciiString.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadAsciiStringActionPerformed(evt);
            }
        });

        btnReadMapleAsciiString.setText("readMapleAsciiString()");
        btnReadMapleAsciiString.setEnabled(false);
        btnReadMapleAsciiString.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadMapleAsciiStringActionPerformed(evt);
            }
        });

        btnReadPos.setText("readPos()");
        btnReadPos.setEnabled(false);
        btnReadPos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadPosActionPerformed(evt);
            }
        });

        btnRead.setText("read()");
        btnRead.setEnabled(false);
        btnRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadActionPerformed(evt);
            }
        });

        txtReadLength.setText("1");
        txtReadLength.setEnabled(false);
        txtReadLength.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtReadLengthKeyTyped(evt);
            }
        });

        chkSkip.setText("skip");
        chkSkip.setEnabled(false);

        labelCurrentStatus.setText("目前狀態");

        labelByte.setText("Byte");

        txtByte.setText("0");
        txtByte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtByteKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtByteKeyTyped(evt);
            }
        });

        labelShort.setText("Short");

        txtShort.setText("0");
        txtShort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtShortKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtShortKeyTyped(evt);
            }
        });

        labelInteger.setText("Integer");

        txtInteger.setText("0");
        txtInteger.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIntegerKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIntegerKeyTyped(evt);
            }
        });

        labelLong.setText("Long");

        txtLong.setText("0");
        txtLong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLongKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtLongKeyTyped(evt);
            }
        });

        labelMapleAsciiString.setText("MapleAsciiString");

        txtMapleAsciiString.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMapleAsciiStringKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMapleAsciiStringKeyTyped(evt);
            }
        });

        labelPosition.setText("Position");

        txtPositionX.setText("0");
        txtPositionX.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPositionXKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPositionXKeyTyped(evt);
            }
        });

        btnUndo.setText("復原");
        btnUndo.setEnabled(false);
        btnUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUndoActionPerformed(evt);
            }
        });

        chkContainOPCode.setText("包含包頭");

        labelPacketName.setText("封包命名");

        labelPacketOutput.setText("封包輸出");

        btnClear.setText("清除");
        btnClear.setEnabled(false);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        labelAsciiString.setText("AsciiString");

        txtAsciiString.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAsciiStringKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAsciiStringKeyTyped(evt);
            }
        });

        btnCurrentCursor.setText("移動到目前註標");
        btnCurrentCursor.setEnabled(false);
        btnCurrentCursor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCurrentCursorActionPerformed(evt);
            }
        });

        labelPositionX.setText("X");

        labelPositionY.setText("Y");

        txtPositionY.setText("0");
        txtPositionY.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPositionYKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPositionYKeyTyped(evt);
            }
        });

        bgDecHex.add(rbDec);
        rbDec.setSelected(true);
        rbDec.setText("10 進位");
        rbDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbDecActionPerformed(evt);
            }
        });

        bgDecHex.add(rbHex);
        rbHex.setText("16 進位");
        rbHex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbHexActionPerformed(evt);
            }
        });

        btnRedo.setText("重做");
        btnRedo.setEnabled(false);
        btnRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedoActionPerformed(evt);
            }
        });

        labelCursortPosition.setText("游標位置");

        labelCurrent.setText("目前");

        txtCurrent.setText("0");
        txtCurrent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCurrentKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCurrentKeyTyped(evt);
            }
        });

        labelAll.setText("全部");

        txtAll.setText("0");
        txtAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAllKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAllKeyTyped(evt);
            }
        });

        txtCursorPosition.setText("0");
        txtCursorPosition.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCursorPositionKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCursorPositionKeyTyped(evt);
            }
        });

        txtPacketName.setEnabled(false);

        txtStringLength.setText("1");
        txtStringLength.setEnabled(false);
        txtStringLength.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStringLengthKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStringLengthKeyTyped(evt);
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

        txtAreaPacketOutput.setColumns(20);
        txtAreaPacketOutput.setRows(5);
        txtAreaPacketOutput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAreaPacketOutputKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAreaPacketOutputKeyTyped(evt);
            }
        });
        spPacketOutput.setViewportView(txtAreaPacketOutput);

        javax.swing.GroupLayout panelPacketOutputLayout = new javax.swing.GroupLayout(panelPacketOutput);
        panelPacketOutput.setLayout(panelPacketOutputLayout);
        panelPacketOutputLayout.setHorizontalGroup(
            panelPacketOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spPacketOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
        );
        panelPacketOutputLayout.setVerticalGroup(
            panelPacketOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spPacketOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
        );

        tabPanePacketOutput.addTab("分析紀錄", panelPacketOutput);

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
            .addComponent(spProgramOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
        );
        panelProgramOutputLayout.setVerticalGroup(
            panelProgramOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spProgramOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
        );

        tabPanePacketOutput.addTab("程式碼輸出", panelProgramOutput);

        txtComment.setEnabled(false);

        labelComment.setText("註解");

        btnTryString.setText("字串分析");
        btnTryString.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTryStringActionPerformed(evt);
            }
        });

        labelWarning.setForeground(java.awt.Color.blue);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spPacketField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelPacketTip)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnTryString)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUndo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRedo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkContainOPCode))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelPacketAnalysis)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnReadLong)
                                            .addComponent(btnReadMapleAsciiString)
                                            .addComponent(btnReadShort)
                                            .addComponent(btnReadInt))
                                        .addGap(312, 312, 312)
                                        .addComponent(tabPanePacketOutput))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(labelCurrent)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                                .addComponent(btnCurrentCursor)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelAll)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtAll, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelCursortPosition)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCursorPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelCursorIndex)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtCursorIndex, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(labelComment)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtComment, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnReadByte)
                                            .addComponent(btnReadPos)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnRead)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtReadLength, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(chkSkip))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnReadAsciiString)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtStringLength, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(labelByte)
                                                            .addComponent(labelShort))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(txtByte, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(txtShort, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(labelMapleAsciiString)
                                                            .addComponent(labelLong)
                                                            .addComponent(labelInteger)
                                                            .addComponent(labelPosition)
                                                            .addComponent(labelPacketName)
                                                            .addComponent(labelAsciiString))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(txtLong)
                                                            .addComponent(txtMapleAsciiString)
                                                            .addComponent(txtAsciiString)
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addComponent(labelPositionX)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtPositionX, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                                                                .addComponent(labelPositionY)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtPositionY, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addComponent(txtInteger)
                                                            .addComponent(txtPacketName)))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(rbDec)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(rbHex))))
                                            .addComponent(labelCurrentStatus))
                                        .addGap(18, 18, 18)
                                        .addComponent(labelPacketOutput)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(labelWarning))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelPacketReaderHelper)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAnalysis, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelPacketReaderHelper)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPacketTip)
                    .addComponent(chkContainOPCode)
                    .addComponent(btnUndo)
                    .addComponent(btnRedo)
                    .addComponent(btnTryString))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spPacketField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnalysis)
                    .addComponent(btnClear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPacketAnalysis)
                    .addComponent(labelCurrentStatus)
                    .addComponent(labelPacketOutput)
                    .addComponent(labelWarning))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReadByte)
                            .addComponent(labelByte)
                            .addComponent(txtByte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnReadShort)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelShort)
                                .addComponent(txtShort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnReadInt)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelInteger)
                                .addComponent(txtInteger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReadLong)
                            .addComponent(labelLong)
                            .addComponent(txtLong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReadAsciiString)
                            .addComponent(labelAsciiString)
                            .addComponent(txtAsciiString, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStringLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReadMapleAsciiString)
                            .addComponent(labelMapleAsciiString)
                            .addComponent(txtMapleAsciiString, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReadPos)
                            .addComponent(labelPosition)
                            .addComponent(txtPositionX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelPositionX)
                            .addComponent(txtPositionY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelPositionY))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRead)
                            .addComponent(txtReadLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkSkip)
                            .addComponent(rbDec)
                            .addComponent(rbHex))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelPacketName)
                            .addComponent(btnCurrentCursor)
                            .addComponent(txtPacketName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(tabPanePacketOutput))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCursortPosition)
                    .addComponent(labelCurrent)
                    .addComponent(txtCurrent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelAll)
                    .addComponent(txtAll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCursorPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCursorIndex)
                    .addComponent(txtCursorIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtComment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelComment))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnalysisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalysisActionPerformed
        // TODO add your handling code here:
        String packet = txtAreaPacketField.getText();
        try {
            txtAreaPacketField.setText(PacketHelperTool.fixPacketInput(packet));
        } catch (PacketStringWrongSizeException ex) {
            JOptionPane.showMessageDialog(this, "封包資料長度錯誤 請重新輸入");
            return;
        }
        if (txtAreaPacketField.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "請輸入封包資料");
            return;
        }

        slea = new LittleEndianAccessor(new ByteArrayByteStream(HexTool.getByteArrayFromHexString(txtAreaPacketField.getText())));
        if (chkContainOPCode.isSelected()) {
            if (slea.available() < 2) {
                slea = null;
                JOptionPane.showMessageDialog(this, "封包資料不完全 請重新輸入");
                return;
            }
            analysis = true;
            byte[] byteArray = slea.getBytes((int) slea.getPosition(), 2);
            short read = slea.readShort();
            appendOPCode(read + " [" + HexTool.toString(byteArray) + "]");
            updateCurrentIndex();
        } else {
            analysis = true;
            updateCurrentIndex();
        }
        labelPacketTip.setText("分析進行中");
        txtAll.setText(String.valueOf(slea.getlength()));
        buttonControl(analysis);
    }//GEN-LAST:event_btnAnalysisActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearState();
        toDefaultValue();
        clearPacketData();

        analysis = false;
        buttonControl(analysis);
        labelPacketTip.setText("請餵食封包");
        txtAreaPacketField.requestFocus();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnReadByteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadByteActionPerformed
        // TODO add your handling code here:
        if (!checkPacketName()) {
            return;
        }
        byte read;
        try {
            read = slea.readByte();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Code : 38 (到達檔案結尾)");
            return;
        }
        appendPacketOutput(1, "slea.readByte() 位元組", read + " [" + HexTool.toString(read) + "]");
        appendProgramOutput("byte", "slea.readByte();");
        updateCurrentIndex();
        updateUndoStack(1);
    }//GEN-LAST:event_btnReadByteActionPerformed

    private void btnReadShortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadShortActionPerformed
        // TODO add your handling code here:
        if (!checkPacketName()) {
            return;
        }
        byte[] byteArray;
        short read;
        try {
            byteArray = slea.getBytes((int) slea.getPosition(), 2);
            read = slea.readShort();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Code : 38 (到達檔案結尾)");
            return;
        }
        appendPacketOutput(2, "slea.readShort() 短整數", read + " [" + HexTool.toString(byteArray) + "]");
        appendProgramOutput("short", "slea.readShort();");
        updateCurrentIndex();
        updateUndoStack(2);
    }//GEN-LAST:event_btnReadShortActionPerformed

    private void btnReadIntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadIntActionPerformed
        // TODO add your handling code here:
        if (!checkPacketName()) {
            return;
        }
        byte[] byteArray;
        int read;
        try {
            byteArray = slea.getBytes((int) slea.getPosition(), 4);
            read = slea.readInt();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Code : 38 (到達檔案結尾)");
            return;
        }
        appendPacketOutput(4, "slea.readInt() 整數", read + " [" + HexTool.toString(byteArray) + "]");
        appendProgramOutput("int", "slea.readInt();");
        updateCurrentIndex();
        updateUndoStack(4);
    }//GEN-LAST:event_btnReadIntActionPerformed

    private void btnReadLongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadLongActionPerformed
        // TODO add your handling code here:
        if (!checkPacketName()) {
            return;
        }
        byte[] byteArray;
        long read;
        try {
            byteArray = slea.getBytes((int) slea.getPosition(), 8);
            read = slea.readLong();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Code : 38 (到達檔案結尾)");
            return;
        }
        appendPacketOutput(8, "slea.readLong() 長整數", read + " [" + HexTool.toString(byteArray) + "]");
        appendProgramOutput("long", "slea.readLong();");
        updateCurrentIndex();
        updateUndoStack(8);
    }//GEN-LAST:event_btnReadLongActionPerformed

    private void btnReadAsciiStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadAsciiStringActionPerformed
        // TODO add your handling code here:
        int length;
        try {
            length = Integer.parseInt(txtStringLength.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "請輸入正確字串長度格式");
            return;
        }
        if (length <= 0) {
            JOptionPane.showMessageDialog(this, "請輸入正確字串長度");
            return;
        }

        if (!checkPacketName()) {
            return;
        }
        byte[] byteArray;
        String read;
        try {
            byteArray = slea.getBytes((int) slea.getPosition(), length);
            read = slea.readAsciiString(length);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Code : 38 (到達檔案結尾)");
            return;
        }
        appendPacketOutput(length, "slea.readAsciiString(" + length + ") 字串", PacketHelperTool.handleEscapeString(read) + " [" + HexTool.toString(byteArray) + "]");
        appendProgramOutput("String", "slea.readAsciiString(" + length + ");");
        updateCurrentIndex();
        updateUndoStack(length);
    }//GEN-LAST:event_btnReadAsciiStringActionPerformed

    private void btnReadMapleAsciiStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadMapleAsciiStringActionPerformed
        // TODO add your handling code here:
        if (!checkPacketName()) {
            return;
        }
        byte[] StringLength, StringData;
        int byte1, byte2, length;
        String read;
        try {
            StringLength = slea.getBytes((int) slea.getPosition(), 2);
            byte1 = StringLength[0];
            byte2 = StringLength[1];
            length = (byte2 << 8) + byte1;
            StringData = slea.getBytes((int) slea.getPosition() + 2, length);
            read = slea.readMapleAsciiString();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Code : 38 (到達檔案結尾)");
            return;
        }
        appendPacketOutput(length + 2, "slea.readMapleAsciiString() 楓之谷字串", PacketHelperTool.handleEscapeString(read) + " [" + HexTool.toString(StringLength) + "] [" + HexTool.toString(StringData) + "]");
        appendProgramOutput("String", "slea.readMapleAsciiString();");
        updateCurrentIndex();
        updateUndoStack(length + 2);
    }//GEN-LAST:event_btnReadMapleAsciiStringActionPerformed

    private void btnReadPosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadPosActionPerformed
        // TODO add your handling code here:
        if (!checkPacketName()) {
            return;
        }
        byte[] bytePosX, bytePosY;
        Point pos;
        try {
            bytePosX = slea.getBytes((int) slea.getPosition(), 2);
            bytePosY = slea.getBytes((int) slea.getPosition() + 2, 2);
            pos = slea.readPos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Code : 38 (到達檔案結尾)");
            return;
        }
        appendPacketOutput(4, "slea.readPos() 座標", "X : " + pos.x + " Y : " + pos.y + " [" + HexTool.toString(bytePosX) + "] [" + HexTool.toString(bytePosY) + "]");
        appendProgramOutput("Point", "slea.readPos();");
        updateCurrentIndex();
        updateUndoStack(4);
    }//GEN-LAST:event_btnReadPosActionPerformed

    private void btnReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadActionPerformed
        // TODO add your handling code here:
        int length;
        try {
            length = Integer.parseInt(txtReadLength.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "請輸入正確位元組長度格式");
            return;
        }
        if (length <= 0) {
            JOptionPane.showMessageDialog(this, "請輸入正確位元組長度");
            return;
        }

        if (!checkPacketName()) {
            return;
        }
        byte[] read;
        try {
            read = slea.read(length);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Code : 38 (到達檔案結尾)");
            return;
        }
        if (chkSkip.isSelected()) {
            appendPacketOutput(length, "slea.skip(" + length + ") 位元組陣列", "[跳過] [" + HexTool.toString(read) + "]");
            appendProgramSkip(length);
        } else {
            appendPacketOutput(length, "slea.read(" + length + ") 位元組陣列", "[" + HexTool.toString(read) + "]");
            appendProgramOutput("byte[]", "slea.read(" + length + ");");
        }
        updateCurrentIndex();
        updateUndoStack(length);
    }//GEN-LAST:event_btnReadActionPerformed

    private void btnUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUndoActionPerformed
        // TODO add your handling code here:
        String toRemove, toRemoveProgram;
        try {
            toRemove = PacketHelperTool.removeLastLine(txtAreaPacketOutput);
            toRemoveProgram = PacketHelperTool.removeLastLine(txtAreaProgramOutput);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
            return;
        }

        UndoReaderInformation info = undoStorage.pop();
        slea.unRead(info.size);
        updateCurrentIndex();

        if (info.packetName != null) {
            packetName.pop();
            updateSamePacketName();
        } else {
            --currentNoName;
        }

        redoStorage.push(new RedoReaderInformation(info.size, toRemove, toRemoveProgram, info.packetName));
        btnRedo.setEnabled(true);
        if (undoStorage.empty()) {
            btnUndo.setEnabled(false);
        }
    }//GEN-LAST:event_btnUndoActionPerformed

    private void btnCurrentCursorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCurrentCursorActionPerformed
        // TODO add your handling code here:
        updateCurrentCursor();
    }//GEN-LAST:event_btnCurrentCursorActionPerformed

    private void txtAreaPacketFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaPacketFieldKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        if (analysis) {
            evt.consume();
        } else {
            PacketHelperTool.hexArrayOnly(evt);
        }
    }//GEN-LAST:event_txtAreaPacketFieldKeyTyped

    private void txtByteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByteKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtByteKeyTyped

    private void txtShortKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtShortKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtShortKeyTyped

    private void txtIntegerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIntegerKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtIntegerKeyTyped

    private void txtLongKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLongKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtLongKeyTyped

    private void txtAsciiStringKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAsciiStringKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAsciiStringKeyTyped

    private void txtMapleAsciiStringKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMapleAsciiStringKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtMapleAsciiStringKeyTyped

    private void txtPositionXKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPositionXKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtPositionXKeyTyped

    private void txtPositionYKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPositionYKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtPositionYKeyTyped

    private void txtAreaPacketOutputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaPacketOutputKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaPacketOutputKeyTyped

    private void txtReadLengthKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReadLengthKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.numberOnly(evt);
    }//GEN-LAST:event_txtReadLengthKeyTyped

    private void rbDecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbDecActionPerformed
        // TODO add your handling code here:
        updateCurrentPacket();
    }//GEN-LAST:event_rbDecActionPerformed

    private void rbHexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbHexActionPerformed
        // TODO add your handling code here:
        updateCurrentPacket();
    }//GEN-LAST:event_rbHexActionPerformed

    private void txtAreaPacketFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAreaPacketFieldMouseClicked
        // TODO add your handling code here:
        txtStringLength.setText("1");
        updateCurrentPacket();
    }//GEN-LAST:event_txtAreaPacketFieldMouseClicked

    private void btnRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedoActionPerformed
        // TODO add your handling code here:
        RedoReaderInformation info = redoStorage.pop();
        slea.read(info.size);
        updateCurrentIndex();

        PacketHelperTool.appendTextArea(txtAreaPacketOutput, info.data);
        PacketHelperTool.appendTextArea(txtAreaProgramOutput, info.programData);

        if (info.packetName != null) {
            packetName.push(info.packetName);
            updateSamePacketName();
        } else {
            ++currentNoName;
        }

        undoStorage.push(new UndoReaderInformation(info.size, info.packetName));
        btnUndo.setEnabled(true);
        if (redoStorage.empty()) {
            btnRedo.setEnabled(false);
        }
    }//GEN-LAST:event_btnRedoActionPerformed

    private void txtCurrentKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCurrentKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtCurrentKeyTyped

    private void txtAllKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAllKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAllKeyTyped

    private void txtCursorPositionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCursorPositionKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtCursorPositionKeyTyped

    private void txtStringLengthKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStringLengthKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.numberOnly(evt);
    }//GEN-LAST:event_txtStringLengthKeyTyped

    private void txtStringLengthKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStringLengthKeyReleased
        // TODO add your handling code here:
        updateCurrentPacket();
    }//GEN-LAST:event_txtStringLengthKeyReleased

    private void txtAreaPacketFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaPacketFieldKeyReleased
        // TODO add your handling code here:
        int index = txtAreaPacketField.getCaretPosition();
        txtCursorPosition.setText(String.valueOf(index));
        if (analysis) {
            txtCursorIndex.setText(String.valueOf(index / 3));
        }
    }//GEN-LAST:event_txtAreaPacketFieldKeyReleased

    private void txtAreaProgramOutputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaProgramOutputKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaProgramOutputKeyTyped

    private void btnTryStringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTryStringActionPerformed
        // TODO add your handling code here:
        if (slea == null || slea.getPosition() >= slea.getlength()) {
            new PacketStringAnalyzer().setVisible(true);
        } else {
            new PacketStringAnalyzer(slea.subByteArray((int) slea.getPosition())).setVisible(true);
        }
    }//GEN-LAST:event_btnTryStringActionPerformed

    private void txtAreaPacketFieldMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAreaPacketFieldMouseReleased
        // TODO add your handling code here:
        if (txtAreaPacketField.getSelectedText() != null) {
            int start = txtAreaPacketField.getSelectionStart();
            int endIndex = txtAreaPacketField.getSelectionEnd() / 3;

            txtStringLength.setText(String.valueOf(endIndex - start / 3 + 1));
            updateCurrentPacket(start);
        }
    }//GEN-LAST:event_txtAreaPacketFieldMouseReleased

    private void txtAreaPacketOutputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaPacketOutputKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAreaPacketOutputKeyPressed

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

    private void txtAreaPacketFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAreaPacketFieldKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        if (analysis) {
            evt.consume();
        } else if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAreaPacketFieldKeyPressed

    private void txtCurrentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCurrentKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtCurrentKeyPressed

    private void txtAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAllKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAllKeyPressed

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

    private void txtByteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtByteKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtByteKeyPressed

    private void txtShortKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtShortKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtShortKeyPressed

    private void txtIntegerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIntegerKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtIntegerKeyPressed

    private void txtLongKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLongKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtLongKeyPressed

    private void txtAsciiStringKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAsciiStringKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtAsciiStringKeyPressed

    private void txtMapleAsciiStringKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMapleAsciiStringKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtMapleAsciiStringKeyPressed

    private void txtPositionXKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPositionXKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtPositionXKeyPressed

    private void txtPositionYKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPositionYKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtPositionYKeyPressed

    private void txtCursorIndexKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCursorIndexKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtCursorIndexKeyTyped

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
                new PacketReaderHelper().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgDecHex;
    private javax.swing.JButton btnAnalysis;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnCurrentCursor;
    private javax.swing.JButton btnRead;
    private javax.swing.JButton btnReadAsciiString;
    private javax.swing.JButton btnReadByte;
    private javax.swing.JButton btnReadInt;
    private javax.swing.JButton btnReadLong;
    private javax.swing.JButton btnReadMapleAsciiString;
    private javax.swing.JButton btnReadPos;
    private javax.swing.JButton btnReadShort;
    private javax.swing.JButton btnRedo;
    private javax.swing.JButton btnTryString;
    private javax.swing.JButton btnUndo;
    private javax.swing.JCheckBox chkContainOPCode;
    private javax.swing.JCheckBox chkSkip;
    private javax.swing.JLabel labelAll;
    private javax.swing.JLabel labelAsciiString;
    private javax.swing.JLabel labelByte;
    private javax.swing.JLabel labelColumn;
    private javax.swing.JLabel labelComment;
    private javax.swing.JLabel labelCurrent;
    private javax.swing.JLabel labelCurrentStatus;
    private javax.swing.JLabel labelCursorIndex;
    private javax.swing.JLabel labelCursortPosition;
    private javax.swing.JLabel labelInteger;
    private javax.swing.JLabel labelLong;
    private javax.swing.JLabel labelMapleAsciiString;
    private javax.swing.JLabel labelPacketAnalysis;
    private javax.swing.JLabel labelPacketName;
    private javax.swing.JLabel labelPacketOutput;
    private javax.swing.JLabel labelPacketReaderHelper;
    private javax.swing.JLabel labelPacketTip;
    private javax.swing.JLabel labelPosition;
    private javax.swing.JLabel labelPositionX;
    private javax.swing.JLabel labelPositionY;
    private javax.swing.JLabel labelShort;
    private javax.swing.JLabel labelWarning;
    private javax.swing.JPanel panelPacketOutput;
    private javax.swing.JPanel panelProgramOutput;
    private javax.swing.JRadioButton rbDec;
    private javax.swing.JRadioButton rbHex;
    private javax.swing.JScrollPane spPacketField;
    private javax.swing.JScrollPane spPacketOutput;
    private javax.swing.JScrollPane spProgramOutput;
    private javax.swing.JTabbedPane tabPanePacketOutput;
    private javax.swing.JTextField txtAll;
    private javax.swing.JTextArea txtAreaPacketField;
    private javax.swing.JTextArea txtAreaPacketOutput;
    private javax.swing.JTextArea txtAreaProgramOutput;
    private javax.swing.JTextField txtAsciiString;
    private javax.swing.JTextField txtByte;
    private javax.swing.JTextField txtComment;
    private javax.swing.JTextField txtCurrent;
    private javax.swing.JTextField txtCursorIndex;
    private javax.swing.JTextField txtCursorPosition;
    private javax.swing.JTextField txtInteger;
    private javax.swing.JTextField txtLong;
    private javax.swing.JTextField txtMapleAsciiString;
    private javax.swing.JTextField txtPacketName;
    private javax.swing.JTextField txtPositionX;
    private javax.swing.JTextField txtPositionY;
    private javax.swing.JTextField txtReadLength;
    private javax.swing.JTextField txtShort;
    private javax.swing.JTextField txtStringLength;
    // End of variables declaration//GEN-END:variables
}
