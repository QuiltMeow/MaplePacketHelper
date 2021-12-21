package ew.quilt.packet;

import ew.quilt.helper.PacketHelperTool;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

public class RadixConverter extends javax.swing.JFrame {

    public RadixConverter() {
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
        setIconImage(new ImageIcon(RadixConverter.class.getResource("/ew/quilt/icon/Maple.png")).getImage());
        updateNOT();
    }

    private void updateLeftConvert(JTextField left) {
        updateLeftConvert(left, true);
    }

    private void updateRightConvert(JTextField right) {
        updateRightConvert(right, true);
    }

    private void updateLeftConvert(JTextField left, boolean updateLogic) {
        boolean exception = false;
        try {
            switch (cbMode.getSelectedIndex()) {
                case 0: {
                    long data = 0;
                    switch (left.getName()) {
                        case "txtRadixHex": {
                            data = Long.parseLong(PacketHelperTool.cleanNotHexString(left.getText()), 16);
                            break;
                        }
                        case "txtRadixDec": {
                            data = Long.parseLong(PacketHelperTool.cleanNotNumberString(left.getText()), 10);
                            break;
                        }
                        case "txtRadixOct": {
                            data = Long.parseLong(PacketHelperTool.cleanNotOctString(left.getText()), 8);
                            break;
                        }
                        case "txtRadixBin": {
                            data = Long.parseLong(PacketHelperTool.cleanNotBinString(left.getText()), 2);
                            break;
                        }
                    }

                    if (tBtnRadixNegative.isSelected()) {
                        data *= -1;
                    }

                    txtRadixHex.setText(Long.toHexString(data).toUpperCase());
                    txtRadixDec.setText(String.valueOf(data));
                    txtRadixOct.setText(Long.toOctalString(data));
                    txtRadixBin.setText(Long.toBinaryString(data));
                    break;
                }
                case 1: {
                    int data = 0;
                    switch (left.getName()) {
                        case "txtRadixHex": {
                            data = Integer.parseInt(PacketHelperTool.cleanNotHexString(left.getText()), 16);
                            break;
                        }
                        case "txtRadixDec": {
                            data = Integer.parseInt(PacketHelperTool.cleanNotNumberString(left.getText()), 10);
                            break;
                        }
                        case "txtRadixOct": {
                            data = Integer.parseInt(PacketHelperTool.cleanNotOctString(left.getText()), 8);
                            break;
                        }
                        case "txtRadixBin": {
                            data = Integer.parseInt(PacketHelperTool.cleanNotBinString(left.getText()), 2);
                            break;
                        }
                    }

                    if (tBtnRadixNegative.isSelected()) {
                        data *= -1;
                    }

                    txtRadixHex.setText(Integer.toHexString(data).toUpperCase());
                    txtRadixDec.setText(String.valueOf(data));
                    txtRadixOct.setText(Integer.toOctalString(data));
                    txtRadixBin.setText(Integer.toBinaryString(data));
                    break;
                }
                case 2: {
                    short data = 0;
                    switch (left.getName()) {
                        case "txtRadixHex": {
                            data = Short.parseShort(PacketHelperTool.cleanNotHexString(left.getText()), 16);
                            break;
                        }
                        case "txtRadixDec": {
                            data = Short.parseShort(PacketHelperTool.cleanNotNumberString(left.getText()), 10);
                            break;
                        }
                        case "txtRadixOct": {
                            data = Short.parseShort(PacketHelperTool.cleanNotOctString(left.getText()), 8);
                            break;
                        }
                        case "txtRadixBin": {
                            data = Short.parseShort(PacketHelperTool.cleanNotBinString(left.getText()), 2);
                            break;
                        }
                    }

                    if (tBtnRadixNegative.isSelected()) {
                        data *= -1;
                    }

                    txtRadixHex.setText(Integer.toHexString(data & 0xFFFF).toUpperCase());
                    txtRadixDec.setText(String.valueOf(data));
                    txtRadixOct.setText(Integer.toOctalString(data & 0xFFFF));
                    txtRadixBin.setText(Integer.toBinaryString(data & 0xFFFF));
                    break;
                }
                case 3: {
                    byte data = 0;
                    switch (left.getName()) {
                        case "txtRadixHex": {
                            data = Byte.parseByte(PacketHelperTool.cleanNotHexString(left.getText()), 16);
                            break;
                        }
                        case "txtRadixDec": {
                            data = Byte.parseByte(PacketHelperTool.cleanNotNumberString(left.getText()), 10);
                            break;
                        }
                        case "txtRadixOct": {
                            data = Byte.parseByte(PacketHelperTool.cleanNotOctString(left.getText()), 8);
                            break;
                        }
                        case "txtRadixBin": {
                            data = Byte.parseByte(PacketHelperTool.cleanNotBinString(left.getText()), 2);
                            break;
                        }
                    }

                    if (tBtnRadixNegative.isSelected()) {
                        data *= -1;
                    }

                    txtRadixHex.setText(Integer.toHexString(data & 0xFF).toUpperCase());
                    txtRadixDec.setText(String.valueOf(data));
                    txtRadixOct.setText(Integer.toOctalString(data & 0xFF));
                    txtRadixBin.setText(Integer.toBinaryString(data & 0xFF));
                    break;
                }
            }
        } catch (NumberFormatException ex) {
            exception = true;
            clearLeftInput();
            labelLeftWarning.setText("左方數值已被重設");
        }
        if (!exception) {
            labelLeftWarning.setText("");
        }

        if (updateLogic) {
            updateLogicResult();
            updateNOT();
        }
    }

    private void updateRightConvert(JTextField right, boolean updateLogic) {
        boolean exception = false;
        try {
            switch (cbMode.getSelectedIndex()) {
                case 0: {
                    long data = 0;
                    switch (right.getName()) {
                        case "txtExpressionHex": {
                            data = Long.parseLong(PacketHelperTool.cleanNotHexString(right.getText()), 16);
                            break;
                        }
                        case "txtExpressionDec": {
                            data = Long.parseLong(PacketHelperTool.cleanNotNumberString(right.getText()), 10);
                            break;
                        }
                        case "txtExpressionOct": {
                            data = Long.parseLong(PacketHelperTool.cleanNotOctString(right.getText()), 8);
                            break;
                        }
                        case "txtExpressionBin": {
                            data = Long.parseLong(PacketHelperTool.cleanNotBinString(right.getText()), 2);
                            break;
                        }
                    }

                    if (tBtnExpressionNegative.isSelected()) {
                        data *= -1;
                    }

                    txtExpressionHex.setText(Long.toHexString(data).toUpperCase());
                    txtExpressionDec.setText(String.valueOf(data));
                    txtExpressionOct.setText(Long.toOctalString(data));
                    txtExpressionBin.setText(Long.toBinaryString(data));
                    break;
                }
                case 1: {
                    int data = 0;
                    switch (right.getName()) {
                        case "txtExpressionHex": {
                            data = Integer.parseInt(PacketHelperTool.cleanNotHexString(right.getText()), 16);
                            break;
                        }
                        case "txtExpressionDec": {
                            data = Integer.parseInt(PacketHelperTool.cleanNotNumberString(right.getText()), 10);
                            break;
                        }
                        case "txtExpressionOct": {
                            data = Integer.parseInt(PacketHelperTool.cleanNotOctString(right.getText()), 8);
                            break;
                        }
                        case "txtExpressionBin": {
                            data = Integer.parseInt(PacketHelperTool.cleanNotBinString(right.getText()), 2);
                            break;
                        }
                    }

                    if (tBtnExpressionNegative.isSelected()) {
                        data *= -1;
                    }

                    txtExpressionHex.setText(Integer.toHexString(data).toUpperCase());
                    txtExpressionDec.setText(String.valueOf(data));
                    txtExpressionOct.setText(Integer.toOctalString(data));
                    txtExpressionBin.setText(Integer.toBinaryString(data));
                    break;
                }
                case 2: {
                    short data = 0;
                    switch (right.getName()) {
                        case "txtExpressionHex": {
                            data = Short.parseShort(PacketHelperTool.cleanNotHexString(right.getText()), 16);
                            break;
                        }
                        case "txtExpressionDec": {
                            data = Short.parseShort(PacketHelperTool.cleanNotNumberString(right.getText()), 10);
                            break;
                        }
                        case "txtExpressionOct": {
                            data = Short.parseShort(PacketHelperTool.cleanNotOctString(right.getText()), 8);
                            break;
                        }
                        case "txtExpressionBin": {
                            data = Short.parseShort(PacketHelperTool.cleanNotBinString(right.getText()), 2);
                            break;
                        }
                    }

                    if (tBtnExpressionNegative.isSelected()) {
                        data *= -1;
                    }

                    txtExpressionHex.setText(Integer.toHexString(data & 0xFFFF).toUpperCase());
                    txtExpressionDec.setText(String.valueOf(data));
                    txtExpressionOct.setText(Integer.toOctalString(data & 0xFFFF));
                    txtExpressionBin.setText(Integer.toBinaryString(data & 0xFFFF));
                    break;
                }
                case 3: {
                    byte data = 0;
                    switch (right.getName()) {
                        case "txtExpressionHex": {
                            data = Byte.parseByte(PacketHelperTool.cleanNotHexString(right.getText()), 16);
                            break;
                        }
                        case "txtExpressionDec": {
                            data = Byte.parseByte(PacketHelperTool.cleanNotNumberString(right.getText()), 10);
                            break;
                        }
                        case "txtExpressionOct": {
                            data = Byte.parseByte(PacketHelperTool.cleanNotOctString(right.getText()), 8);
                            break;
                        }
                        case "txtExpressionBin": {
                            data = Byte.parseByte(PacketHelperTool.cleanNotBinString(right.getText()), 2);
                            break;
                        }
                    }

                    if (tBtnExpressionNegative.isSelected()) {
                        data *= -1;
                    }

                    txtExpressionHex.setText(Integer.toHexString(data & 0xFF).toUpperCase());
                    txtExpressionDec.setText(String.valueOf(data));
                    txtExpressionOct.setText(Integer.toOctalString(data & 0xFF));
                    txtExpressionBin.setText(Integer.toBinaryString(data & 0xFF));
                    break;
                }
            }
        } catch (NumberFormatException ex) {
            exception = true;
            clearRightInput();
            labelRightWarning.setText("右方數值已被重設");
        }
        if (!exception) {
            labelRightWarning.setText("");
        }

        if (updateLogic) {
            updateLogicResult();
        }
    }

    private void updateLogicResult() {
        switch (cbMode.getSelectedIndex()) {
            case 0: {
                long left = Long.parseLong(txtRadixDec.getText(), 10);
                long right = Long.parseLong(txtExpressionDec.getText(), 10);
                long resultAND = left & right;
                long resultOR = left | right;
                long resultXOR = left ^ right;

                txtANDHex.setText(Long.toHexString(resultAND).toUpperCase());
                txtANDDec.setText(String.valueOf(resultAND));
                txtANDOct.setText(Long.toOctalString(resultAND));
                txtANDBin.setText(Long.toBinaryString(resultAND));

                txtORHex.setText(Long.toHexString(resultOR).toUpperCase());
                txtORDec.setText(String.valueOf(resultOR));
                txtOROct.setText(Long.toOctalString(resultOR));
                txtORBin.setText(Long.toBinaryString(resultOR));

                txtXORHex.setText(Long.toHexString(resultXOR).toUpperCase());
                txtXORDec.setText(String.valueOf(resultXOR));
                txtXOROct.setText(Long.toOctalString(resultXOR));
                txtXORBin.setText(Long.toBinaryString(resultXOR));
                break;
            }
            case 1: {
                int left = Integer.parseInt(txtRadixDec.getText(), 10);
                int right = Integer.parseInt(txtExpressionDec.getText(), 10);
                int resultAND = left & right;
                int resultOR = left | right;
                int resultXOR = left ^ right;

                txtANDHex.setText(Integer.toHexString(resultAND).toUpperCase());
                txtANDDec.setText(String.valueOf(resultAND));
                txtANDOct.setText(Integer.toOctalString(resultAND));
                txtANDBin.setText(Integer.toBinaryString(resultAND));

                txtORHex.setText(Integer.toHexString(resultOR).toUpperCase());
                txtORDec.setText(String.valueOf(resultOR));
                txtOROct.setText(Integer.toOctalString(resultOR));
                txtORBin.setText(Integer.toBinaryString(resultOR));

                txtXORHex.setText(Integer.toHexString(resultXOR).toUpperCase());
                txtXORDec.setText(String.valueOf(resultXOR));
                txtXOROct.setText(Integer.toOctalString(resultXOR));
                txtXORBin.setText(Integer.toBinaryString(resultXOR));
                break;
            }
            case 2: {
                short left = Short.parseShort(txtRadixDec.getText(), 10);
                short right = Short.parseShort(txtExpressionDec.getText(), 10);
                short resultAND = (short) (left & right);
                short resultOR = (short) (left | right);
                short resultXOR = (short) (left ^ right);

                txtANDHex.setText(Integer.toHexString(resultAND & 0xFFFF).toUpperCase());
                txtANDDec.setText(String.valueOf(resultAND));
                txtANDOct.setText(Integer.toOctalString(resultAND & 0xFFFF));
                txtANDBin.setText(Integer.toBinaryString(resultAND & 0xFFFF));

                txtORHex.setText(Integer.toHexString(resultOR & 0xFFFF).toUpperCase());
                txtORDec.setText(String.valueOf(resultOR));
                txtOROct.setText(Integer.toOctalString(resultOR & 0xFFFF));
                txtORBin.setText(Integer.toBinaryString(resultOR & 0xFFFF));

                txtXORHex.setText(Integer.toHexString(resultXOR & 0xFFFF).toUpperCase());
                txtXORDec.setText(String.valueOf(resultXOR));
                txtXOROct.setText(Integer.toOctalString(resultXOR & 0xFFFF));
                txtXORBin.setText(Integer.toBinaryString(resultXOR & 0xFFFF));
                break;
            }
            case 3: {
                byte left = Byte.parseByte(txtRadixDec.getText(), 10);
                byte right = Byte.parseByte(txtExpressionDec.getText(), 10);
                byte resultAND = (byte) (left & right);
                byte resultOR = (byte) (left | right);
                byte resultXOR = (byte) (left ^ right);

                txtANDHex.setText(Integer.toHexString(resultAND & 0xFF).toUpperCase());
                txtANDDec.setText(String.valueOf(resultAND));
                txtANDOct.setText(Integer.toOctalString(resultAND & 0xFF));
                txtANDBin.setText(Integer.toBinaryString(resultAND & 0xFF));

                txtORHex.setText(Integer.toHexString(resultOR & 0xFF).toUpperCase());
                txtORDec.setText(String.valueOf(resultOR));
                txtOROct.setText(Integer.toOctalString(resultOR & 0xFF));
                txtORBin.setText(Integer.toBinaryString(resultOR & 0xFF));

                txtXORHex.setText(Integer.toHexString(resultXOR & 0xFF).toUpperCase());
                txtXORDec.setText(String.valueOf(resultXOR));
                txtXOROct.setText(Integer.toOctalString(resultXOR & 0xFF));
                txtXORBin.setText(Integer.toBinaryString(resultXOR & 0xFF));
                break;
            }
        }
    }

    private void updateNOT() {
        switch (cbMode.getSelectedIndex()) {
            case 0: {
                long left = Long.parseLong(txtRadixDec.getText(), 10);
                long resultNOT = ~left;

                txtNOTHex.setText(Long.toHexString(resultNOT).toUpperCase());
                txtNOTDec.setText(String.valueOf(resultNOT));
                txtNOTOct.setText(Long.toOctalString(resultNOT));
                txtNOTBin.setText(Long.toBinaryString(resultNOT));
                break;
            }
            case 1: {
                int left = Integer.parseInt(txtRadixDec.getText(), 10);
                int resultNOT = ~left;

                txtNOTHex.setText(Integer.toHexString(resultNOT).toUpperCase());
                txtNOTDec.setText(String.valueOf(resultNOT));
                txtNOTOct.setText(Integer.toOctalString(resultNOT));
                txtNOTBin.setText(Integer.toBinaryString(resultNOT));
                break;
            }
            case 2: {
                short left = Short.parseShort(txtRadixDec.getText(), 10);
                short resultNOT = (short) ~left;

                txtNOTHex.setText(Integer.toHexString(resultNOT & 0xFFFF).toUpperCase());
                txtNOTDec.setText(String.valueOf(resultNOT));
                txtNOTOct.setText(Integer.toOctalString(resultNOT & 0xFFFF));
                txtNOTBin.setText(Integer.toBinaryString(resultNOT & 0xFFFF));
                break;
            }
            case 3: {
                byte left = Byte.parseByte(txtRadixDec.getText(), 10);
                byte resultNOT = (byte) (~left);

                txtNOTHex.setText(Integer.toHexString(resultNOT & 0xFF).toUpperCase());
                txtNOTDec.setText(String.valueOf(resultNOT));
                txtNOTOct.setText(Integer.toOctalString(resultNOT & 0xFF));
                txtNOTBin.setText(Integer.toBinaryString(resultNOT & 0xFF));
                break;
            }
        }
    }

    private void clearLeftInput() {
        JTextField[] leftInputArea = new JTextField[]{txtRadixHex, txtRadixDec, txtRadixOct, txtRadixBin};
        for (JTextField input : leftInputArea) {
            input.setText("0");
        }
    }

    private void clearRightInput() {
        JTextField[] rightInputArea = new JTextField[]{txtExpressionHex, txtExpressionDec, txtExpressionOct, txtExpressionBin};
        for (JTextField input : rightInputArea) {
            input.setText("0");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTitle = new javax.swing.JLabel();
        labelMode = new javax.swing.JLabel();
        cbMode = new javax.swing.JComboBox<>();
        labelRadix = new javax.swing.JLabel();
        labelRadixHex = new javax.swing.JLabel();
        txtRadixBin = new javax.swing.JTextField();
        labelRadixDec = new javax.swing.JLabel();
        txtRadixHex = new javax.swing.JTextField();
        labelRadixOct = new javax.swing.JLabel();
        txtRadixDec = new javax.swing.JTextField();
        labelRadixBin = new javax.swing.JLabel();
        txtRadixOct = new javax.swing.JTextField();
        txtExpressionHex = new javax.swing.JTextField();
        txtExpressionDec = new javax.swing.JTextField();
        txtExpressionOct = new javax.swing.JTextField();
        txtExpressionBin = new javax.swing.JTextField();
        labelExpression = new javax.swing.JLabel();
        labelLogic = new javax.swing.JLabel();
        txtANDHex = new javax.swing.JTextField();
        labelLogicOct = new javax.swing.JLabel();
        txtANDDec = new javax.swing.JTextField();
        labelLogicBin = new javax.swing.JLabel();
        txtANDOct = new javax.swing.JTextField();
        labelLogicHex = new javax.swing.JLabel();
        txtANDBin = new javax.swing.JTextField();
        labelLogicDec = new javax.swing.JLabel();
        labelOR = new javax.swing.JLabel();
        txtOROct = new javax.swing.JTextField();
        txtORBin = new javax.swing.JTextField();
        txtORHex = new javax.swing.JTextField();
        txtORDec = new javax.swing.JTextField();
        tBtnRadixNegative = new javax.swing.JToggleButton();
        labelXOR = new javax.swing.JLabel();
        txtXOROct = new javax.swing.JTextField();
        txtXORBin = new javax.swing.JTextField();
        txtXORHex = new javax.swing.JTextField();
        txtXORDec = new javax.swing.JTextField();
        labelNOT = new javax.swing.JLabel();
        txtNOTOct = new javax.swing.JTextField();
        txtNOTBin = new javax.swing.JTextField();
        txtNOTHex = new javax.swing.JTextField();
        txtNOTDec = new javax.swing.JTextField();
        tBtnExpressionNegative = new javax.swing.JToggleButton();
        btnClear = new javax.swing.JButton();
        labelLeftWarning = new javax.swing.JLabel();
        labelRightWarning = new javax.swing.JLabel();
        labelAND = new javax.swing.JLabel();
        btnCalc = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("棉被家族 - 進位 / 邏輯計算工具");
        setResizable(false);

        labelTitle.setFont(new java.awt.Font("微軟正黑體", 0, 14)); // NOI18N
        labelTitle.setText("進位轉換 / 邏輯計算器");

        labelMode.setText("模式");

        cbMode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "QWORD", "DWORD", "WORD", "BYTE" }));
        cbMode.setSelectedIndex(1);
        cbMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbModeActionPerformed(evt);
            }
        });

        labelRadix.setText("數值轉換");

        labelRadixHex.setText("HEX");

        txtRadixBin.setText("0");
        txtRadixBin.setName("txtRadixBin"); // NOI18N
        txtRadixBin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRadixBinKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRadixBinKeyTyped(evt);
            }
        });

        labelRadixDec.setText("DEC");

        txtRadixHex.setText("0");
        txtRadixHex.setName("txtRadixHex"); // NOI18N
        txtRadixHex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRadixHexKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRadixHexKeyTyped(evt);
            }
        });

        labelRadixOct.setText("OCT");

        txtRadixDec.setText("0");
        txtRadixDec.setName("txtRadixDec"); // NOI18N
        txtRadixDec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRadixDecKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRadixDecKeyTyped(evt);
            }
        });

        labelRadixBin.setText("BIN");

        txtRadixOct.setText("0");
        txtRadixOct.setName("txtRadixOct"); // NOI18N
        txtRadixOct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRadixOctKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRadixOctKeyTyped(evt);
            }
        });

        txtExpressionHex.setText("0");
        txtExpressionHex.setName("txtExpressionHex"); // NOI18N
        txtExpressionHex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtExpressionHexKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtExpressionHexKeyTyped(evt);
            }
        });

        txtExpressionDec.setText("0");
        txtExpressionDec.setName("txtExpressionDec"); // NOI18N
        txtExpressionDec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtExpressionDecKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtExpressionDecKeyTyped(evt);
            }
        });

        txtExpressionOct.setText("0");
        txtExpressionOct.setName("txtExpressionOct"); // NOI18N
        txtExpressionOct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtExpressionOctKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtExpressionOctKeyTyped(evt);
            }
        });

        txtExpressionBin.setText("0");
        txtExpressionBin.setName("txtExpressionBin"); // NOI18N
        txtExpressionBin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtExpressionBinKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtExpressionBinKeyTyped(evt);
            }
        });

        labelExpression.setText("運算元");

        labelLogic.setText("邏輯");

        txtANDHex.setText("0");
        txtANDHex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtANDHexKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtANDHexKeyTyped(evt);
            }
        });

        labelLogicOct.setText("OCT");

        txtANDDec.setText("0");
        txtANDDec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtANDDecKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtANDDecKeyTyped(evt);
            }
        });

        labelLogicBin.setText("BIN");

        txtANDOct.setText("0");
        txtANDOct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtANDOctKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtANDOctKeyTyped(evt);
            }
        });

        labelLogicHex.setText("HEX");

        txtANDBin.setText("0");
        txtANDBin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtANDBinKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtANDBinKeyTyped(evt);
            }
        });

        labelLogicDec.setText("DEC");

        labelOR.setText("OR");

        txtOROct.setText("0");
        txtOROct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOROctKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtOROctKeyTyped(evt);
            }
        });

        txtORBin.setText("0");
        txtORBin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtORBinKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtORBinKeyTyped(evt);
            }
        });

        txtORHex.setText("0");
        txtORHex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtORHexKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtORHexKeyTyped(evt);
            }
        });

        txtORDec.setText("0");
        txtORDec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtORDecKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtORDecKeyTyped(evt);
            }
        });

        tBtnRadixNegative.setText("數值負號");
        tBtnRadixNegative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBtnRadixNegativeActionPerformed(evt);
            }
        });

        labelXOR.setText("XOR");

        txtXOROct.setText("0");
        txtXOROct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtXOROctKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtXOROctKeyTyped(evt);
            }
        });

        txtXORBin.setText("0");
        txtXORBin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtXORBinKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtXORBinKeyTyped(evt);
            }
        });

        txtXORHex.setText("0");
        txtXORHex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtXORHexKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtXORHexKeyTyped(evt);
            }
        });

        txtXORDec.setText("0");
        txtXORDec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtXORDecKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtXORDecKeyTyped(evt);
            }
        });

        labelNOT.setText("NOT");

        txtNOTOct.setText("0");
        txtNOTOct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNOTOctKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNOTOctKeyTyped(evt);
            }
        });

        txtNOTBin.setText("0");
        txtNOTBin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNOTBinKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNOTBinKeyTyped(evt);
            }
        });

        txtNOTHex.setText("0");
        txtNOTHex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNOTHexKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNOTHexKeyTyped(evt);
            }
        });

        txtNOTDec.setText("0");
        txtNOTDec.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNOTDecKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNOTDecKeyTyped(evt);
            }
        });

        tBtnExpressionNegative.setText("運算元負號");
        tBtnExpressionNegative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBtnExpressionNegativeActionPerformed(evt);
            }
        });

        btnClear.setText("清除");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        labelLeftWarning.setForeground(java.awt.Color.red);

        labelRightWarning.setForeground(java.awt.Color.red);

        labelAND.setText("AND");

        btnCalc.setText("小算盤");
        btnCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelTitle)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(labelMode)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbMode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(tBtnRadixNegative)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tBtnExpressionNegative)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnClear))
                            .addComponent(labelRadix)
                            .addComponent(labelLogic)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(labelRadixHex)
                                            .addComponent(labelRadixDec)
                                            .addComponent(labelRadixOct)
                                            .addComponent(labelRadixBin))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(168, 168, 168)
                                                .addComponent(labelExpression)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(labelLeftWarning)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnCalc))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtRadixDec, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtRadixBin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtRadixOct, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtRadixHex, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtExpressionHex)
                                                    .addComponent(txtExpressionDec)
                                                    .addComponent(txtExpressionOct)
                                                    .addComponent(txtExpressionBin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(labelRightWarning))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(labelLogicDec)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(txtANDDec, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                    .addComponent(labelLogicOct)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(txtANDOct, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                    .addComponent(labelLogicBin)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(txtANDBin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(labelLogicHex)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(labelAND)
                                                    .addComponent(txtANDHex, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtORHex, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(labelOR)
                                            .addComponent(txtORDec, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtORBin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtOROct, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtXORHex, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(labelXOR)
                                            .addComponent(txtXORDec, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtXORBin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtXOROct, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(labelNOT)
                                            .addComponent(txtNOTDec, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNOTBin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNOTOct, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtNOTHex, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelXOR)
                            .addComponent(labelNOT))
                        .addGap(144, 144, 144))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelMode)
                            .addComponent(cbMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tBtnRadixNegative)
                            .addComponent(tBtnExpressionNegative)
                            .addComponent(btnClear))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelRadix)
                            .addComponent(labelExpression)
                            .addComponent(labelLeftWarning)
                            .addComponent(btnCalc))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelRightWarning)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(labelRadixHex)
                                .addComponent(txtRadixHex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtExpressionHex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelRadixDec)
                            .addComponent(txtRadixDec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtExpressionDec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelRadixOct)
                            .addComponent(txtRadixOct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtExpressionOct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelRadixBin)
                            .addComponent(txtRadixBin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtExpressionBin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelLogic)
                            .addComponent(labelOR)
                            .addComponent(labelAND))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtORDec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelLogicDec)
                                    .addComponent(txtANDDec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtXORDec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNOTDec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtOROct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelLogicOct)
                                    .addComponent(txtANDOct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtXOROct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNOTOct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtORBin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelLogicBin)
                                    .addComponent(txtANDBin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtXORBin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNOTBin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtORHex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelLogicHex)
                                    .addComponent(txtANDHex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtXORHex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNOTHex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(108, 108, 108)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tBtnRadixNegativeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBtnRadixNegativeActionPerformed
        // TODO add your handling code here:
        updateLeftConvert(txtRadixDec);
    }//GEN-LAST:event_tBtnRadixNegativeActionPerformed

    private void txtRadixHexKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRadixHexKeyReleased
        // TODO add your handling code here:
        updateLeftConvert(txtRadixHex);
    }//GEN-LAST:event_txtRadixHexKeyReleased

    private void txtRadixDecKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRadixDecKeyReleased
        // TODO add your handling code here:
        updateLeftConvert(txtRadixDec);
    }//GEN-LAST:event_txtRadixDecKeyReleased

    private void txtRadixOctKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRadixOctKeyReleased
        // TODO add your handling code here:
        updateLeftConvert(txtRadixOct);
    }//GEN-LAST:event_txtRadixOctKeyReleased

    private void txtRadixBinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRadixBinKeyReleased
        // TODO add your handling code here:
        updateLeftConvert(txtRadixBin);
    }//GEN-LAST:event_txtRadixBinKeyReleased

    private void txtRadixHexKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRadixHexKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.hexOnly(evt);
    }//GEN-LAST:event_txtRadixHexKeyTyped

    private void txtRadixDecKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRadixDecKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.numberOnly(evt);
    }//GEN-LAST:event_txtRadixDecKeyTyped

    private void txtRadixOctKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRadixOctKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.octOnly(evt);
    }//GEN-LAST:event_txtRadixOctKeyTyped

    private void txtRadixBinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRadixBinKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.binOnly(evt);
    }//GEN-LAST:event_txtRadixBinKeyTyped

    private void txtExpressionHexKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpressionHexKeyReleased
        // TODO add your handling code here:
        updateRightConvert(txtExpressionHex);
    }//GEN-LAST:event_txtExpressionHexKeyReleased

    private void txtExpressionDecKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpressionDecKeyReleased
        // TODO add your handling code here:
        updateRightConvert(txtExpressionDec);
    }//GEN-LAST:event_txtExpressionDecKeyReleased

    private void txtExpressionOctKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpressionOctKeyReleased
        // TODO add your handling code here:
        updateRightConvert(txtExpressionOct);
    }//GEN-LAST:event_txtExpressionOctKeyReleased

    private void txtExpressionBinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpressionBinKeyReleased
        // TODO add your handling code here:
        updateRightConvert(txtExpressionBin);
    }//GEN-LAST:event_txtExpressionBinKeyReleased

    private void txtExpressionHexKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpressionHexKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.hexOnly(evt);
    }//GEN-LAST:event_txtExpressionHexKeyTyped

    private void txtExpressionDecKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpressionDecKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.numberOnly(evt);
    }//GEN-LAST:event_txtExpressionDecKeyTyped

    private void txtExpressionOctKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpressionOctKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.octOnly(evt);
    }//GEN-LAST:event_txtExpressionOctKeyTyped

    private void txtExpressionBinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpressionBinKeyTyped
        // TODO add your handling code here:
        PacketHelperTool.binOnly(evt);
    }//GEN-LAST:event_txtExpressionBinKeyTyped

    private void txtANDHexKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtANDHexKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtANDHexKeyTyped

    private void txtANDDecKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtANDDecKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtANDDecKeyTyped

    private void txtANDOctKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtANDOctKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtANDOctKeyTyped

    private void txtANDBinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtANDBinKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtANDBinKeyTyped

    private void txtORHexKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtORHexKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtORHexKeyTyped

    private void txtORDecKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtORDecKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtORDecKeyTyped

    private void txtOROctKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOROctKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtOROctKeyTyped

    private void txtORBinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtORBinKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtORBinKeyTyped

    private void txtXORHexKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtXORHexKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtXORHexKeyTyped

    private void txtXORDecKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtXORDecKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtXORDecKeyTyped

    private void txtXOROctKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtXOROctKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtXOROctKeyTyped

    private void txtXORBinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtXORBinKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtXORBinKeyTyped

    private void txtNOTHexKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNOTHexKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtNOTHexKeyTyped

    private void txtNOTDecKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNOTDecKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtNOTDecKeyTyped

    private void txtNOTOctKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNOTOctKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtNOTOctKeyTyped

    private void txtNOTBinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNOTBinKeyTyped
        // TODO add your handling code here:
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtNOTBinKeyTyped

    private void cbModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbModeActionPerformed
        // TODO add your handling code here:
        updateLeftConvert(txtRadixDec, false);
        updateRightConvert(txtExpressionDec, false);
        updateLogicResult();
        updateNOT();
    }//GEN-LAST:event_cbModeActionPerformed

    private void tBtnExpressionNegativeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBtnExpressionNegativeActionPerformed
        // TODO add your handling code here:
        updateRightConvert(txtExpressionDec);
    }//GEN-LAST:event_tBtnExpressionNegativeActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        JTextField[] inputArea = new JTextField[]{txtRadixHex, txtRadixDec, txtRadixOct, txtRadixBin, txtExpressionHex, txtExpressionDec, txtExpressionOct, txtExpressionBin};
        for (JTextField input : inputArea) {
            input.setText("0");
        }
        updateLogicResult();
        updateNOT();
    }//GEN-LAST:event_btnClearActionPerformed

    private void txtANDHexKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtANDHexKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtANDHexKeyPressed

    private void txtANDDecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtANDDecKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtANDDecKeyPressed

    private void txtANDOctKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtANDOctKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtANDOctKeyPressed

    private void txtANDBinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtANDBinKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtANDBinKeyPressed

    private void txtORHexKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtORHexKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtORHexKeyPressed

    private void txtORDecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtORDecKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtORDecKeyPressed

    private void txtOROctKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOROctKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtOROctKeyPressed

    private void txtORBinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtORBinKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtORBinKeyPressed

    private void txtXORHexKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtXORHexKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtXORHexKeyPressed

    private void txtXORDecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtXORDecKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtXORDecKeyPressed

    private void txtXOROctKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtXOROctKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtXOROctKeyPressed

    private void txtXORBinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtXORBinKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtXORBinKeyPressed

    private void txtNOTHexKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNOTHexKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtNOTHexKeyPressed

    private void txtNOTDecKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNOTDecKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtNOTDecKeyPressed

    private void txtNOTOctKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNOTOctKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtNOTOctKeyPressed

    private void txtNOTBinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNOTBinKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && (evt.getKeyCode() == KeyEvent.VK_C || evt.getKeyCode() == KeyEvent.VK_A)) {
            return;
        }
        if (evt.getKeyCode() >= KeyEvent.VK_LEFT && evt.getKeyCode() <= KeyEvent.VK_DOWN) {
            return;
        }
        evt.consume();
    }//GEN-LAST:event_txtNOTBinKeyPressed

    private void btnCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcActionPerformed
        // TODO add your handling code here:
        try {
            Runtime.getRuntime().exec("cmd.exe /c calc");
        } catch (IOException ex) {
        }
    }//GEN-LAST:event_btnCalcActionPerformed

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
                new RadixConverter().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalc;
    private javax.swing.JButton btnClear;
    private javax.swing.JComboBox<String> cbMode;
    private javax.swing.JLabel labelAND;
    private javax.swing.JLabel labelExpression;
    private javax.swing.JLabel labelLeftWarning;
    private javax.swing.JLabel labelLogic;
    private javax.swing.JLabel labelLogicBin;
    private javax.swing.JLabel labelLogicDec;
    private javax.swing.JLabel labelLogicHex;
    private javax.swing.JLabel labelLogicOct;
    private javax.swing.JLabel labelMode;
    private javax.swing.JLabel labelNOT;
    private javax.swing.JLabel labelOR;
    private javax.swing.JLabel labelRadix;
    private javax.swing.JLabel labelRadixBin;
    private javax.swing.JLabel labelRadixDec;
    private javax.swing.JLabel labelRadixHex;
    private javax.swing.JLabel labelRadixOct;
    private javax.swing.JLabel labelRightWarning;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JLabel labelXOR;
    private javax.swing.JToggleButton tBtnExpressionNegative;
    private javax.swing.JToggleButton tBtnRadixNegative;
    private javax.swing.JTextField txtANDBin;
    private javax.swing.JTextField txtANDDec;
    private javax.swing.JTextField txtANDHex;
    private javax.swing.JTextField txtANDOct;
    private javax.swing.JTextField txtExpressionBin;
    private javax.swing.JTextField txtExpressionDec;
    private javax.swing.JTextField txtExpressionHex;
    private javax.swing.JTextField txtExpressionOct;
    private javax.swing.JTextField txtNOTBin;
    private javax.swing.JTextField txtNOTDec;
    private javax.swing.JTextField txtNOTHex;
    private javax.swing.JTextField txtNOTOct;
    private javax.swing.JTextField txtORBin;
    private javax.swing.JTextField txtORDec;
    private javax.swing.JTextField txtORHex;
    private javax.swing.JTextField txtOROct;
    private javax.swing.JTextField txtRadixBin;
    private javax.swing.JTextField txtRadixDec;
    private javax.swing.JTextField txtRadixHex;
    private javax.swing.JTextField txtRadixOct;
    private javax.swing.JTextField txtXORBin;
    private javax.swing.JTextField txtXORDec;
    private javax.swing.JTextField txtXORHex;
    private javax.swing.JTextField txtXOROct;
    // End of variables declaration//GEN-END:variables
}
