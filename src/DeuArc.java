
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JToolBar;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JList;
import java.awt.List;
import java.awt.Dimension;
import javax.swing.table.DefaultTableModel;
import javax.management.remote.SubjectDelegationPermission;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.synth.SynthScrollBarUI;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Cursor;
import java.awt.ComponentOrientation;
import javax.swing.border.EtchedBorder;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.TextField;
import java.awt.TextArea;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

public class DeuArc {

	private JFrame DEUARC;
	private JTable IMTable;
	private JTable DMTable;
	private JTable LTTable;
	private Node firstData = new Node(null, null, null);
	private String[] iMemory;
	private String[] lableTable;
	private JTable table;
	private static String pc, D = "", S1 = "", S2 = "", SP = "0000", sPC = "", sAR = "";
	private static int num = 0, click = 0, sc = 1, sumSP = 0, iAR = 0;
	private static String sIR;
	private static boolean[] dArray = new boolean[16];
	private static boolean d6 = false, d7 = false, d10 = false, d11 = false, d141 = false, d142 = false, d151 = false,
			d152 = false;
	private static int numberType = 0;
	private JTable SMTable;
	private JTextField textField;
	private boolean hlt = false;
	private boolean bin = true, dec = false, hex = false;
	private Memory instructionMemory=new Memory(32);
	private Memory dataMemory=new Memory(16);
	private Memory stackMemory=new Memory(16);
	private String[] instr = new String[32];
	private String[] data = new String[16];
	private String[] stack = new String[16];
	private boolean inputFlag = false, outputFlag = false;
	private JTextField inputText;
	private static String str3;
	static int ct = 0;
	public void binToDec() {

		for (int i = 0; i < 32; i++) {
			String s = instr[i];
			int binIR = 0;
			for (int x = 0; x < s.length(); x++) {
				if (s.charAt(x) == '1')
					binIR = (int) (binIR + Math.pow(2, s.length() - 1 - x));
			}
			IMTable.setValueAt(binIR, i, 0);
		}

		for (int i = 0; i < 16; i++) {
			String g = stack[i];
			String s = data[i];
			int binDM = 0;
			int binSM = 0;
			for (int x = 0; x < s.length(); x++) {
				if (s.charAt(x) == '1')
					binDM = (int) (binDM + Math.pow(2, s.length() - 1 - x));
			}
			DMTable.setValueAt(binDM, i, 1);
			for (int x = 0; x < g.length(); x++) {
				if (g.charAt(x) == '1')
					binSM = (int) (binSM + Math.pow(2, g.length() - 1 - x));
			}
			SMTable.setValueAt(binSM, i, 0);
		}
	}

	public void binToHex() {
		int digitNumber = 1;
		int binIR = 0;
		int digitNumberD = 1;
		int binD = 0;
		int digitNumberS = 1;
		int binS = 0;

		for (int i = 0; i < instr.length; i++) {
			String temp = "";
			if (!instr[i].equals("00000000000")) {
				String s = "0" + instr[i];
				for (int a = 0; a < s.length(); a++) {
					if (digitNumber == 1)
						binIR += Integer.parseInt(s.charAt(a) + "") * 8;
					else if (digitNumber == 2)
						binIR += Integer.parseInt(s.charAt(a) + "") * 4;
					else if (digitNumber == 3)
						binIR += Integer.parseInt(s.charAt(a) + "") * 2;
					else if (digitNumber == 4) {
						binIR += Integer.parseInt(s.charAt(a) + "") * 1;
						digitNumber = 0;
						if (binIR < 10)
							temp = temp + Integer.toString(binIR);
						else if (binIR == 10)
							temp = temp + "A";
						else if (binIR == 11)
							temp = temp + "B";
						else if (binIR == 12)
							temp = temp + "C";
						else if (binIR == 13)
							temp = temp + "D";
						else if (binIR == 14)
							temp = temp + "E";
						else if (binIR == 15)
							temp = temp + "F";
						binIR = 0;
					}
					digitNumber++;
					IMTable.setValueAt(temp, i, 0);
				}
			} else {
				IMTable.setValueAt("000", i, 0);
			}

		}

		for (int i = 0; i < data.length; i++) {
			String temp = "";
			if (!data[i].equals("0000")) {
				String s = data[i];
				for (int a = 0; a < s.length(); a++) {
					if (digitNumberD == 1)
						binD += Integer.parseInt(s.charAt(a) + "") * 8;
					else if (digitNumberD == 2)
						binD += Integer.parseInt(s.charAt(a) + "") * 4;
					else if (digitNumberD == 3)
						binD += Integer.parseInt(s.charAt(a) + "") * 2;
					else if (digitNumberD == 4) {
						binD += Integer.parseInt(s.charAt(a) + "") * 1;
						digitNumberD = 0;
						if (binD < 10)
							temp = temp + Integer.toString(binD);
						else if (binD == 10)
							temp = temp + "A";
						else if (binD == 11)
							temp = temp + "B";
						else if (binD == 12)
							temp = temp + "C";
						else if (binD == 13)
							temp = temp + "D";
						else if (binD == 14)
							temp = temp + "E";
						else if (binD == 15)
							temp = temp + "F";
						binD = 0;
					}
					digitNumberD++;
					DMTable.setValueAt(temp, i, 1);
				}
			} else {
				DMTable.setValueAt("0", i, 1);
			}
			temp = "";
			if (!stack[i].equals("0000")) {
				String s = stack[i];
				for (int a = 0; a < s.length(); a++) {
					if (digitNumberS == 1)
						binD += Integer.parseInt(s.charAt(a) + "") * 8;
					else if (digitNumberS == 2)
						binD += Integer.parseInt(s.charAt(a) + "") * 4;
					else if (digitNumberS == 3)
						binD += Integer.parseInt(s.charAt(a) + "") * 2;
					else if (digitNumberS == 4) {
						binD += Integer.parseInt(s.charAt(a) + "") * 1;
						digitNumberS = 0;
						if (binD < 10)
							temp = temp + Integer.toString(binS);
						else if (binS == 10)
							temp = temp + "A";
						else if (binS == 11)
							temp = temp + "B";
						else if (binS == 12)
							temp = temp + "C";
						else if (binS == 13)
							temp = temp + "D";
						else if (binS == 14)
							temp = temp + "E";
						else if (binS == 15)
							temp = temp + "F";
						binS = 0;
					}
					digitNumberS++;
					SMTable.setValueAt(temp, i, 0);
				}
			} else {
				SMTable.setValueAt("0", i, 0);
			}
		}
	}

	public void decToBin() {
		for (int i = 0; i < instr.length; i++) {
			String temp = Integer.toBinaryString(Integer.parseInt(instr[i]));
			while (temp.length() != 11) {
				temp = "0" + temp;
			}
			IMTable.setValueAt(temp, i, 0);
		}
		for (int i = 0; i < data.length; i++) {
			String temp = Integer.toBinaryString(Integer.parseInt(data[i]));
			while (temp.length() != 5) {
				temp = "0" + temp;
			}
			DMTable.setValueAt(temp, i, 1);
		}
		for (int i = 0; i < stack.length; i++) {
			String temp = Integer.toBinaryString(Integer.parseInt(stack[i]));
			while (temp.length() != 5) {
				temp = "0" + temp;
			}
			SMTable.setValueAt(temp, i, 0);
		}

	}

	public void decToHex() {

		for (int i = 0; i < instr.length; i++) {
			String temp = Integer.toHexString(Integer.parseInt(instr[i]));
			IMTable.setValueAt(temp, i, 0);
		}
		for (int i = 0; i < data.length; i++) {
			String temp = Integer.toHexString(Integer.parseInt(data[i]));
			DMTable.setValueAt(temp, i, 1);
		}
		for (int i = 0; i < stack.length; i++) {
			String temp = Integer.toHexString(Integer.parseInt(stack[i]));
			SMTable.setValueAt(temp, i, 0);
		}
	}

	public void hexToDec() {
		for (int i = 0; i < instr.length; i++) {
			int temp = Integer.parseInt(instr[i], 16);
			IMTable.setValueAt(temp, i, 0);
		}
		for (int i = 0; i < data.length; i++) {
			int temp = Integer.parseInt(data[i], 16);
			DMTable.setValueAt(temp, i, 1);
		}
		for (int i = 0; i < stack.length; i++) {
			int temp = Integer.parseInt(stack[i], 16);
			SMTable.setValueAt(temp, i, 0);
		}
	}

	public void hexToBin() {
		for (int i = 0; i < instr.length; i++) {
			String temp = Integer.toBinaryString(Integer.parseInt(instr[i], 16));
			while (temp.length() != 11) {
				temp = "0" + temp;
			}
			IMTable.setValueAt(temp, i, 0);
		}
		for (int i = 0; i < data.length; i++) {
			String temp = Integer.toBinaryString(Integer.parseInt(data[i], 16));
			while (temp.length() != 5) {
				temp = "0" + temp;
			}
			DMTable.setValueAt(temp, i, 1);
		}
		for (int i = 0; i < stack.length; i++) {
			String temp = Integer.toBinaryString(Integer.parseInt(stack[i], 16));
			while (temp.length() != 5) {
				temp = "0" + temp;
			}
			SMTable.setValueAt(temp, i, 0);
		}
	}

	/**
	 * Launch the application.
	 */
	public static String[] fileRead(File file) {
		String[] code = null;
		int count = 0;
		try {
			InputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				count++;
			}
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			code = new String[count];
			int i = 0;
			while ((line = br.readLine()) != null) {
				code[i] = line.replaceAll("\t", " ").replaceFirst("\t", " ").replaceFirst("^\\s+", " ").replaceAll("\\s+", " ");
				i++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return code;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeuArc window = new DeuArc();
					window.DEUARC.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DeuArc() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		DEUARC = new JFrame();
		DEUARC.getContentPane().setForeground(Color.WHITE);
		DEUARC.setVisible(true);
		DEUARC.getContentPane().setBackground(new Color(0, 191, 255));
		DEUARC.setBounds(100, 100, 1127, 715);
		DEUARC.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DEUARC.getContentPane().setLayout(null);

		IMTable = new JTable();
		IMTable.setBorder(UIManager.getBorder("Button.border"));
		IMTable.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		IMTable.setModel(new DefaultTableModel(
				new Object[][] { { "00000000000" }, { "00000000000" }, { "00000000000" }, { "00000000000" },
						{ "00000000000" }, { "00000000000" }, { "00000000000" }, { "00000000000" }, { "00000000000" },
						{ "00000000000" }, { "00000000000" }, { "00000000000" }, { "00000000000" }, { "00000000000" },
						{ "00000000000" }, { "00000000000" }, { "00000000000" }, { "00000000000" }, { "00000000000" },
						{ "00000000000" }, { "00000000000" }, { "00000000000" }, { "00000000000" }, { "00000000000" },
						{ "00000000000" }, { "00000000000" }, { "00000000000" }, { "00000000000" }, { "00000000000" },
						{ "00000000000" }, { "00000000000" }, { "00000000000" }, },
				new String[] { "instruction memory" }));
		IMTable.getColumnModel().getColumn(0).setPreferredWidth(163);
		IMTable.setBounds(26, 60, 245, 512);
		DEUARC.getContentPane().add(IMTable);

		JCheckBox chckbxOverflow = new JCheckBox("Overflow");
		chckbxOverflow.setBounds(224, 605, 97, 23);
		DEUARC.getContentPane().add(chckbxOverflow);

		JCheckBox chckbxInpuFlag = new JCheckBox("Input Flag");
		chckbxInpuFlag.setBounds(26, 605, 97, 23);
		DEUARC.getContentPane().add(chckbxInpuFlag);

		JCheckBox chckbxOutputFlag = new JCheckBox("Output Flag");
		chckbxOutputFlag.setBounds(125, 605, 97, 23);
		DEUARC.getContentPane().add(chckbxOutputFlag);

		JLabel lblDataMemory = new JLabel("Data Memory");
		lblDataMemory.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, Color.CYAN, Color.LIGHT_GRAY));
		lblDataMemory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblDataMemory.setBounds(377, 32, 109, 19);
		DEUARC.getContentPane().add(lblDataMemory);

		JLabel lblInstructionMemory = new JLabel("Instruction Memory");
		lblInstructionMemory.setBackground(Color.LIGHT_GRAY);
		lblInstructionMemory.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblInstructionMemory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblInstructionMemory.setBounds(28, 33, 157, 16);
		DEUARC.getContentPane().add(lblInstructionMemory);

		DMTable = new JTable();
		DMTable.setModel(new DefaultTableModel(new Object[][] { { "0", "0000" }, { "1", "0000" }, { "2", "0000" },
				{ "3", "0000" }, { "4", "0000" }, { "5", "0000" }, { "6", "0000" }, { "7", "0000" }, { "8", "0000" },
				{ "9", "0000" }, { "10", "0000" }, { "11", "0000" }, { "12", "0000" }, { "13", "0000" },
				{ "14", "0000" }, { "15", "0000" }, }, new String[] { "", "Data" }));
		DMTable.getColumnModel().getColumn(0).setPreferredWidth(42);
		DMTable.getColumnModel().getColumn(1).setPreferredWidth(179);
		DMTable.setBounds(330, 65, 208, 256);
		DEUARC.getContentPane().add(DMTable);

		JLabel lblMicrooperations = new JLabel("Microoperations");
		lblMicrooperations.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblMicrooperations.setBounds(635, 319, 121, 28);
		DEUARC.getContentPane().add(lblMicrooperations);

		LTTable = new JTable();
		LTTable.setModel(new DefaultTableModel(
				new Object[][] { { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null },
						{ null, null, null }, { null, null, null }, { null, null, null }, { null, null, null },
						{ null, null, null }, { null, null, null }, { null, null, null }, { null, null, null },
						{ null, null, null }, { null, null, null }, { null, null, null }, },
				new String[] { " ", "New column", "New column" }));
		LTTable.setBounds(330, 354, 208, 240);
		DEUARC.getContentPane().add(LTTable);

		JLabel lblLabelTable = new JLabel("Label Table");
		lblLabelTable.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblLabelTable.setBounds(377, 324, 109, 19);
		DEUARC.getContentPane().add(lblLabelTable);

		JLabel lblStepbystep = new JLabel("Assembly Code");
		lblStepbystep.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblStepbystep.setBounds(635, 32, 109, 19);
		DEUARC.getContentPane().add(lblStepbystep);

		TextArea MOTable = new TextArea();
		MOTable.setForeground(new Color(250, 250, 210));
		MOTable.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		MOTable.setBackground(new Color(0, 139, 139));
		MOTable.setBounds(576, 354, 255, 240);
		DEUARC.getContentPane().add(MOTable);

		JLabel lblRegistersAr = new JLabel("Registers : ");
		lblRegistersAr.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRegistersAr.setBounds(918, 52, 121, 19);
		DEUARC.getContentPane().add(lblRegistersAr);

		JButton btnNext = new JButton("Begin Debug");
		btnNext.setEnabled(false);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnNext.setText("Next");

				if (!hlt) {
					click++;
					if (click == 1) {
						MOTable.append("T0 : IR <- IM[PC] \n");
						if (sPC.length() < 5) {
							num = 0;
							num = Integer.parseInt(sPC);
							String str = Integer.toString(num);
							String sPC = "";
							sPC = sPC + str;
							table.setValueAt(sPC, 3, 1);
							sIR = (String) IMTable.getValueAt(num, 0);
							table.setValueAt(sIR, 1, 1);
						} else {
							num = 0;
							for (int x = 0; x < sPC.length(); x++) {
								if (sPC.charAt(x) == '1')
									num = (int) (num + Math.pow(2, sPC.length() - 1 - x));
							}
							String str = Integer.toString(num);
							String sPC = "";
							sPC = sPC + str;
							table.setValueAt(sPC, 3, 1);
							sIR = (String) IMTable.getValueAt(num, 0);
							table.setValueAt(sIR, 1, 1);
						}
						str3 = (String) IMTable.getValueAt(num, 0);
						table.setValueAt(str3, 1, 1);

					}

					else if (click == 2) {
						MOTable.append("T1 : PC <- PC + 1 \n");
						num++;
						String str = Integer.toString(num);
						sPC = "";
						sPC = sPC + str;
						table.setValueAt(sPC, 3, 1);

					} else if (click == 3) {
						MOTable.append(
								"T2 : D0..D15 <- IR[9..6],\n    Q <- IR[10]=1, S2 <- IR[1..0],\n    S1 <- IR[3..2],  D <- IR[5..4]\n");
						if (sIR.startsWith("0"))
							table.setValueAt("0", 9, 1);
						// System.out.println(sIR.substring(10, 11).length());

						for (int i = 0; i < 2 - sIR.substring(5, 6).length(); i++) {
							D = D + "0";
						}
						D = D + sIR.substring(5, 6);

						for (int i = 0; i < 2 - sIR.substring(7, 8).length(); i++) {
							S1 = S1 + "0";
						}
						S1 = S1 + sIR.substring(7, 8);

						for (int i = 0; i < 2 - sIR.substring(9).length(); i++) {
							S2 = S2 + "0";
						}
						S2 = S2 + sIR.substring(9);
						String bag1 = "" + sIR.charAt(1);
						int temp1 = Integer.parseInt(bag1) * 2 * 2 * 2;

						String bag2 = "" + sIR.charAt(2);
						int temp2 = Integer.parseInt(bag2) * 2 * 2;

						String bag3 = "" + sIR.charAt(3);
						int temp3 = Integer.parseInt(bag3) * 2;

						String bag4 = "" + sIR.charAt(4);
						int temp4 = Integer.parseInt(bag4);

						int result = temp1 + temp2 + temp3 + temp4;

						dArray[result] = true;
					} else if (click == 4) {

						if (dArray[0]) {
							MOTable.append("D0T3 : D <- S1 + S2 , SC <- 0 \n");
							textField.setText("D0 is enable");
							arithmeticLogic("D0T3", click, table, str3.substring(5, 7), str3.substring(7, 9),
									str3.substring(9, 11), chckbxOutputFlag);

							dArray[0] = false;
							sc = 0;
						} else if (dArray[1]) {
							dArray[1] = false;
							MOTable.append("D1T3 : D <- S1 + 1 , SC <- 0\n");
							arithmeticLogic("D1T3", click, table, str3.substring(7, 9), "0", "0", chckbxOutputFlag);
							sc = 0;
						} else if (dArray[2]) {
							dArray[2] = false;
							MOTable.append("D2T3 : D <- S1 + S1 , SC <- 0 \n");
							arithmeticLogic("D2T3", click, table, str3.substring(7, 9), "0", "0", chckbxOutputFlag);
							sc = 0;
						} else if (dArray[3]) {
							dArray[3] = false;
							MOTable.append("D3T3 : D <- S1>> , SC <- 0\n");
							arithmeticLogic("D3T3", click, table, str3.substring(7, 9), "0", "0", chckbxOutputFlag);
							sc = 0;
						} else if (dArray[4]) {
							dArray[4] = false;
							MOTable.append("D4T3 : D <- S1'1s + 1 , SC <- 0\n");
							arithmeticLogic("D4T3", click, table, str3.substring(7, 9), "0", "0", chckbxOutputFlag);
							sc = 0;
						} else if (dArray[5]) {
							dArray[5] = false;
							MOTable.append("D5T3 : DR <- S1^S2 , SC <- 0\n");
							arithmeticLogic("D5T3", click, table, str3.substring(5, 7), str3.substring(7, 9),
									str3.substring(9, 11), chckbxOutputFlag);

							sc = 0;
						} else if (dArray[6] && table.getValueAt(9, 1).equals("1")) {

							MOTable.append("D6QT3 : D <- S1S2 , SC <- 0\n");
							DataTransfer(dArray, click);
							sc = 0;
						} else if (dArray[6] && table.getValueAt(9, 1).equals("0")) {
							MOTable.append("D6Q'T3 : AR <- S1S2  \n");
							DataTransfer(dArray, click);
							d6 = true;

						} else if (dArray[7] && table.getValueAt(9, 1).equals("1")) {
							MOTable.append("D7QT3 : S2 <- D , SC <- 0\n");
							DataTransfer(dArray, click);
						} else if (dArray[7] && table.getValueAt(9, 1).equals("0")) {
							MOTable.append("D7Q'T3 : AR <- S1S2 , SC <- 0\n");
							DataTransfer(dArray, click);
							d7 = true;
						} else if (dArray[8]) {
							hlt = true;
						} else if (dArray[9]) {
							MOTable.append("D9T3 : D <- S1 , SC <- 0\n");
							DataTransfer(dArray, click);
						} else if (dArray[10]) {
							dArray[10] = false;
							MOTable.append("D10T3 : SM[SP] <- PC\n");
							d10 = true;
							// pc sPC,ir sIR
							// SP nin hesaplanmasý *******
							for (int x = 0; x < SP.length(); x++) {
								if (SP.charAt(x) == '1')
									sumSP = (int) (sumSP + Math.pow(2, SP.length() - 1 - x));
							}
							// ******************

							SMTable.setValueAt(sPC, sumSP, 0);

						} else if (dArray[11]) {
							dArray[11] = false;

							MOTable.append("D11T3 : SP <- SP - 1 , SC <- 0\n");
							d11 = true;

							if (sumSP != 0) {
								sumSP--;
								table.setValueAt(sumSP, 2, 1);

							} else
								textField.setText("Stack hasn't any element to delete !!");

						} else if (dArray[12] && table.getValueAt(9, 1).equals("0")) {
							dArray[12] = false;
							MOTable.append("D12Q'T3 : PC <- IR[4..0] , SC <- 0\n");
							sPC = "";
							for (int i = 6; i < 11; i++) {
								char a = sIR.charAt(i);
								sPC = sPC + a;
							}
							table.setValueAt(sPC, 3, 1);
							sc = 0;
						} else if (dArray[12] && table.getValueAt(9, 1).equals("1")) {
							dArray[12] = false;
							MOTable.append("D12QT3 :IF V=1 THEN PC <- IR[4..0] , SC <- 0\n");
							if (chckbxInpuFlag.isSelected()) {
								sPC = "";
								for (int i = 6; i < 11; i++) {
									char a = sIR.charAt(i);
									sPC = sPC + a;
								}
								table.setValueAt(sPC, 3, 1);
								sc = 0;
							} else {
								sc = 0;
							}
						} else if (dArray[13]) {
							dArray[13] = false;
							MOTable.append("D13T3 : PC <- PC + IR[3..0] , SC <- 0\n");

							int iPC = 0, iIR = 0;
							for (int x = 0; x < sPC.length(); x++) {
								if (sPC.charAt(x) == '1')
									iPC = (int) (iPC + Math.pow(2, sPC.length() - 1 - x));
							}
							for (int x = 0; x < sIR.length(); x++) {
								if (sIR.charAt(x) == '1')
									iIR = (int) (iIR + Math.pow(2, sIR.length() - 1 - x));
							}

							int ppc = iIR + iPC;

							sPC = Integer.toBinaryString(ppc);
							table.setValueAt(sPC, 3, 1);

						} else if (dArray[14]) {
							dArray[14] = false;
							MOTable.append("D14T3 : AR <- IR[3..0] , SC <- 0\n");
							d141 = true;
							sAR = "";
							for (int i = 7; i < 11; i++) {
								char a = sIR.charAt(i);
								sAR = sAR + a;
							}
							table.setValueAt(sAR, 0, 1);
						} else if (dArray[15]) {
							dArray[15] = false;
							MOTable.append("D15T3 : AR <- IR[3..0] , SC <- 0\n");
							d151 = true;
							sAR = "";
							for (int i = 7; i < 11; i++) {
								char a = sIR.charAt(i);
								sAR = sAR + a;
							}
							table.setValueAt(sAR, 0, 1);
						}

					} else if (click == 5) {
						DataTransfer(dArray, click);
						if (d6) {
							d6 = false;
							MOTable.append("D6Q'T4 : D <- DM[AR]  , SC <- 0\n");
							DataTransfer(dArray, click);
							sc = 0;
						} else if (d7) {
							d7 = false;
							MOTable.append("D7Q'T4 : DM[AR] <- D , SC <- 0\n");
							DataTransfer(dArray, click);
						} else if (d10) {
							d10 = false;
							MOTable.append("D10T4 : PC <- IR[4..0] , SP <- SP + 1 , SC <- 0\n");
							sPC = "";
							for (int i = 6; i < 11; i++) {
								char a = sIR.charAt(i);
								sPC = sPC + a;
							}
							table.setValueAt(sPC, 3, 1);

							sumSP++;

							table.setValueAt(sumSP, 2, 1);
							sc = 0;
						} else if (d11) {
							d11 = false;
							MOTable.append("D11T4 : PC <- SM[SP]  , SC <- 0\n");
							sPC = "";
							sPC = (String) SMTable.getValueAt(sumSP, 0);
							table.setValueAt(sPC, 3, 1);
							sc = 0;
						} else if (d141) {
							MOTable.append("D14T4 : SM[SP] <- DM[AR]  , SC <- 0\n");
							d141 = false;
							d142 = true;

							for (int x = 0; x < sAR.length(); x++) {
								if (sAR.charAt(x) == '1')
									iAR = (int) (iAR + Math.pow(2, sAR.length() - 1 - x));
							}

							String dm = (String) DMTable.getValueAt(iAR - 1, 0);

							SMTable.setValueAt(dm, sumSP, 0);
						} else if (d151) {
							MOTable.append("D15T4 : SP <- SP - 1  , SC <- 0\n");
							d151 = false;
							if (sumSP != 0) {
								d152 = true;
								sumSP--;
								table.setValueAt(sumSP, 2, 1);
							} else {
								textField.setText("Stack hasn't any element to delete !!");
								sc = 0;
							}

						} else if (sc == 0) {
							// sc sýfýrlanmýssa
							click = 0;
						}
					} else if (click == 6) {
						if (d142) {
							d142 = false;
							MOTable.append("D14T5 : SP <- SP + 1  , SC <- 0\n");
							sumSP++;
							table.setValueAt(sumSP, 2, 1);
							sc = 0;
						} else if (d152) {
							d152 = false;
							MOTable.append("D15T5 : DM[AR] <- SM[SP]  , SC <- 0\n");
							String s = (String) SMTable.getValueAt(sumSP, 0);
							DMTable.setValueAt(s, iAR, 1);
							sc = 0;
						}

					}

					if (sc == 0) {
						click = 0;
						sc++;
					}

				}
			}
		});
		btnNext.setBounds(596, 605, 108, 23);
		DEUARC.getContentPane().add(btnNext);

		JButton btnFnsh = new JButton("Run");
		btnFnsh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnNext.setEnabled(false);
				btnFnsh.setEnabled(false);
				
				while (!hlt) {
						MOTable.append("T0 : IR <- IM[PC] \n");
						if (sPC.length() < 5) {
							num = 0;
							num = Integer.parseInt(sPC);
							String str = Integer.toString(num);
							String sPC = "";
							sPC = sPC + str;
							table.setValueAt(sPC, 3, 1);
							sIR = (String) IMTable.getValueAt(num, 0);
							table.setValueAt(sIR, 1, 1);
						} else {
							num = 0;
							for (int x = 0; x < sPC.length(); x++) {
								if (sPC.charAt(x) == '1')
									num = (int) (num + Math.pow(2, sPC.length() - 1 - x));
							}
							String str = Integer.toString(num);
							String sPC = "";
							sPC = sPC + str;
							table.setValueAt(sPC, 3, 1);
							sIR = (String) IMTable.getValueAt(num, 0);
							table.setValueAt(sIR, 1, 1);
						}
						str3 = (String) IMTable.getValueAt(num, 0);
						table.setValueAt(str3, 1, 1);
						MOTable.append("T1 : PC <- PC + 1 \n");
						num++;
						String str = Integer.toString(num);
						sPC = "";
						sPC = sPC + str;
						table.setValueAt(sPC, 3, 1);					
						MOTable.append(
								"T2 : D0..D15 <- IR[9..6],\n    Q <- IR[10]=1, S2 <- IR[1..0],\n    S1 <- IR[3..2],  D <- IR[5..4]\n");
						if (sIR.startsWith("0"))
							table.setValueAt("0", 9, 1);
						// System.out.println(sIR.substring(10, 11).length());

						for (int i = 0; i < 2 - sIR.substring(5, 6).length(); i++) {
							D = D + "0";
						}
						D = D + sIR.substring(5, 6);

						for (int i = 0; i < 2 - sIR.substring(7, 8).length(); i++) {
							S1 = S1 + "0";
						}
						S1 = S1 + sIR.substring(7, 8);

						for (int i = 0; i < 2 - sIR.substring(9).length(); i++) {
							S2 = S2 + "0";
						}
						S2 = S2 + sIR.substring(9);
						String bag1 = "" + sIR.charAt(1);
						int temp1 = Integer.parseInt(bag1) * 2 * 2 * 2;

						String bag2 = "" + sIR.charAt(2);
						int temp2 = Integer.parseInt(bag2) * 2 * 2;

						String bag3 = "" + sIR.charAt(3);
						int temp3 = Integer.parseInt(bag3) * 2;

						String bag4 = "" + sIR.charAt(4);
						int temp4 = Integer.parseInt(bag4);

						int result = temp1 + temp2 + temp3 + temp4;

						dArray[result] = true;
					

						if (dArray[0]) {
							MOTable.append("D0T3 : D <- S1 + S2 , SC <- 0 \n");
							textField.setText("D0 is enable");
							arithmeticLogic("D0T3", click, table, str3.substring(5, 7), str3.substring(7, 9),
									str3.substring(9, 11), chckbxOutputFlag);

							dArray[0] = false;
						} else if (dArray[1]) {
							dArray[1] = false;
							MOTable.append("D1T3 : D <- S1 + 1 , SC <- 0\n");
							arithmeticLogic("D1T3", click, table, str3.substring(7, 9), "0", "0", chckbxOutputFlag);
						} else if (dArray[2]) {
							dArray[2] = false;
							MOTable.append("D2T3 : D <- S1 + S1 , SC <- 0 \n");
							arithmeticLogic("D2T3", click, table, str3.substring(7, 9), "0", "0", chckbxOutputFlag);
						} else if (dArray[3]) {
							dArray[3] = false;
							MOTable.append("D3T3 : D <- S1>> , SC <- 0\n");
							arithmeticLogic("D3T3", click, table, str3.substring(7, 9), "0", "0", chckbxOutputFlag);
						} else if (dArray[4]) {
							dArray[4] = false;
							MOTable.append("D4T3 : D <- S1'1s + 1 , SC <- 0\n");
							arithmeticLogic("D4T3", click, table, str3.substring(7, 9), "0", "0", chckbxOutputFlag);
						} else if (dArray[5]) {
							dArray[5] = false;
							MOTable.append("D5T3 : DR <- S1^S2 , SC <- 0\n");
							arithmeticLogic("D5T3", click, table, str3.substring(5, 7), str3.substring(7, 9),
									str3.substring(9, 11), chckbxOutputFlag);
						} else if (dArray[6] && table.getValueAt(9, 1).equals("1")) {

							MOTable.append("D6QT3 : D <- S1S2 , SC <- 0\n");
							DataTransfer(dArray, click);
						} else if (dArray[6] && table.getValueAt(9, 1).equals("0")) {
							MOTable.append("D6Q'T3 : AR <- S1S2  \n");
							DataTransfer(dArray, click);
							d6 = true;
						} else if (dArray[7] && table.getValueAt(9, 1).equals("1")) {
							MOTable.append("D7QT3 : S2 <- D , SC <- 0\n");
							DataTransfer(dArray, click);
						} else if (dArray[7] && table.getValueAt(9, 1).equals("0")) {
							MOTable.append("D7Q'T3 : AR <- S1S2 , SC <- 0\n");
							DataTransfer(dArray, click);
							d7 = true;
						} else if (dArray[8]) {
							hlt = true;
						} else if (dArray[9]) {
							MOTable.append("D9T3 : D <- S1 , SC <- 0\n");
							DataTransfer(dArray, click);
						} else if (dArray[10]) {
							dArray[10] = false;
							MOTable.append("D10T3 : SM[SP] <- PC\n");
							d10 = true;
							// pc sPC,ir sIR
							// SP nin hesaplanmasý *******
							for (int x = 0; x < SP.length(); x++) {
								if (SP.charAt(x) == '1')
									sumSP = (int) (sumSP + Math.pow(2, SP.length() - 1 - x));
							}
							// ******************

							SMTable.setValueAt(sPC, sumSP, 0);

						} else if (dArray[11]) {
							dArray[11] = false;

							MOTable.append("D11T3 : SP <- SP - 1 , SC <- 0\n");
							d11 = true;

							if (sumSP != 0) {
								sumSP--;
								table.setValueAt(sumSP, 2, 1);

							} else
								textField.setText("Stack hasn't any element to delete !!");

						} else if (dArray[12] && table.getValueAt(9, 1).equals("0")) {
							dArray[12] = false;
							MOTable.append("D12Q'T3 : PC <- IR[4..0] , SC <- 0\n");
							sPC = "";
							for (int i = 6; i < 11; i++) {
								char a = sIR.charAt(i);
								sPC = sPC + a;
							}
							table.setValueAt(sPC, 3, 1);
						} else if (dArray[12] && table.getValueAt(9, 1).equals("1")) {
							dArray[12] = false;
							MOTable.append("D12QT3 :IF V=1 THEN PC <- IR[4..0] , SC <- 0\n");
							if (chckbxInpuFlag.isSelected()) {
								sPC = "";
								for (int i = 6; i < 11; i++) {
									char a = sIR.charAt(i);
									sPC = sPC + a;
								}
								table.setValueAt(sPC, 3, 1);
							} 
						} else if (dArray[13]) {
							dArray[13] = false;
							MOTable.append("D13T3 : PC <- PC + IR[3..0] , SC <- 0\n");

							int iPC = 0, iIR = 0;
							for (int x = 0; x < sPC.length(); x++) {
								if (sPC.charAt(x) == '1')
									iPC = (int) (iPC + Math.pow(2, sPC.length() - 1 - x));
							}
							for (int x = 0; x < sIR.length(); x++) {
								if (sIR.charAt(x) == '1')
									iIR = (int) (iIR + Math.pow(2, sIR.length() - 1 - x));
							}

							int ppc = iIR + iPC;
							sPC = Integer.toBinaryString(ppc);
							table.setValueAt(sPC, 3, 1);

						} else if (dArray[14]) {
							dArray[14] = false;
							MOTable.append("D14T3 : AR <- IR[3..0] , SC <- 0\n");
							d141 = true;
							sAR = "";
							for (int i = 7; i < 11; i++) {
								char a = sIR.charAt(i);
								sAR = sAR + a;
							}
							table.setValueAt(sAR, 0, 1);
						} else if (dArray[15]) {
							dArray[15] = false;
							MOTable.append("D15T3 : AR <- IR[3..0] , SC <- 0\n");
							d151 = true;
							sAR = "";
							for (int i = 7; i < 11; i++) {
								char a = sIR.charAt(i);
								sAR = sAR + a;
							}
							table.setValueAt(sAR, 0, 1);
						}
						DataTransfer(dArray, click);
						if (d6) {
							d6 = false;
							MOTable.append("D6Q'T4 : D <- DM[AR]  , SC <- 0\n");
							DataTransfer(dArray, click);
						} else if (d7) {
							d7 = false;
							MOTable.append("D7Q'T4 : DM[AR] <- D , SC <- 0\n");
							DataTransfer(dArray, click);
						} else if (d10) {
							d10 = false;
							MOTable.append("D10T4 : PC <- IR[4..0] , SP <- SP + 1 , SC <- 0\n");
							sPC = "";
							for (int i = 6; i < 11; i++) {
								char a = sIR.charAt(i);
								sPC = sPC + a;
							}
							table.setValueAt(sPC, 3, 1);
							sumSP++;
							table.setValueAt(sumSP, 2, 1);
						} else if (d11) {
							d11 = false;
							MOTable.append("D11T4 : PC <- SM[SP]  , SC <- 0\n");
							sPC = "";
							sPC = (String) SMTable.getValueAt(sumSP, 0);
							table.setValueAt(sPC, 3, 1);
						} else if (d141) {
							MOTable.append("D14T4 : SM[SP] <- DM[AR]  , SC <- 0\n");
							d141 = false;
							d142 = true;

							for (int x = 0; x < sAR.length(); x++) {
								if (sAR.charAt(x) == '1')
									iAR = (int) (iAR + Math.pow(2, sAR.length() - 1 - x));
							}

							String dm = (String) DMTable.getValueAt(iAR - 1, 0);

							SMTable.setValueAt(dm, sumSP, 0);
						} else if (d151) {
							MOTable.append("D15T4 : SP <- SP - 1  , SC <- 0\n");
							d151 = false;
							if (sumSP != 0) {
								d152 = true;
								sumSP--;
								table.setValueAt(sumSP, 2, 1);
							} else {
								textField.setText("Stack hasn't any element to delete !!");
								
							}

						}					
						if (d142) {
							d142 = false;
							MOTable.append("D14T5 : SP <- SP + 1  , SC <- 0\n");
							sumSP++;
							table.setValueAt(sumSP, 2, 1);
							
						} else if (d152) {
							d152 = false;
							MOTable.append("D15T5 : DM[AR] <- SM[SP]  , SC <- 0\n");
							String s = (String) SMTable.getValueAt(sumSP, 0);
							DMTable.setValueAt(s, iAR, 1);
							
						}

				}
			}
		});
		btnFnsh.setEnabled(false);
		btnFnsh.setBounds(714, 605, 101, 23);
		DEUARC.getContentPane().add(btnFnsh);

		TextArea asmText = new TextArea();
		asmText.setBackground(new Color(0, 0, 128));
		asmText.setForeground(new Color(250, 250, 210));
		asmText.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		asmText.setBounds(576, 55, 272, 252);
		DEUARC.getContentPane().add(asmText);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] { { "AR", "0000" }, { "IR", "00000000000" },
				{ "SP", "0000" }, { "PC", "00000" }, { "INPR", "0000" }, { "OUTR", "0000" }, { "R0", "0000" },
				{ "R1", "0000" }, { "R2", "0000" }, { "Q", "0" }, }, new String[] { "New column", "New column" }));
		table.getColumnModel().getColumn(0).setPreferredWidth(88);
		table.getColumnModel().getColumn(0).setMinWidth(34);
		table.getColumnModel().getColumn(1).setPreferredWidth(126);
		table.setBounds(875, 82, 208, 160);
		DEUARC.getContentPane().add(table);

		JLabel lblStackMemory = new JLabel("Stack Memory");
		lblStackMemory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblStackMemory.setBounds(918, 319, 121, 28);
		DEUARC.getContentPane().add(lblStackMemory);

		SMTable = new JTable();
		SMTable.setModel(new DefaultTableModel(new Object[][] { { "00000" }, { "00000" }, { "00000" }, { "00000" },
				{ "00000" }, { "00000" }, { "00000" }, { "00000" }, { "00000" }, { "00000" }, { "00000" }, { "00000" },
				{ "00000" }, { "00000" }, { "00000" }, { "00000" }, }, new String[] { "New column" }));
		SMTable.getColumnModel().getColumn(0).setPreferredWidth(155);
		SMTable.setBounds(875, 372, 208, 256);
		DEUARC.getContentPane().add(SMTable);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setBackground(new Color(0, 191, 255));
		textField.setBounds(777, 11, 306, 20);
		DEUARC.getContentPane().add(textField);
		textField.setColumns(10);

		inputText = new JTextField();
		inputText.setText("-");
		inputText.setBounds(875, 253, 86, 20);
		DEUARC.getContentPane().add(inputText);
		inputText.setColumns(10);

		JButton btnEnterInput = new JButton("Enter Input");
		btnEnterInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (inputText.getText().equals("-")) {
					textField.setText("Please enter a input !!");
				} else {
					if (inputText.getText().charAt(0) == '-') {
						if (inputText.getText().substring(1).length() == 4) {
							String a = inputText.getText().substring(1);
							int val = 0;
							int ret = 0;
							for (int x = 0; x < a.length(); x++) {
								if (a.charAt(x) != '1' && a.charAt(x) != '0') {
									textField.setText("Entered wrong input !!");
									break;
								}
								ret++;
							}
							if (ret == 4) {
								table.getModel().setValueAt(a, 4, 1);
								inputFlag = true;
								chckbxInpuFlag.setSelected(true);
							}
						} else {
							textField.setText("Entered wrong input !!");
						}

					} else {
						if (inputText.getText().length() == 4) {
							if (inputText.getText().length() == 4) {
								String a = inputText.getText();
								int val = 0;
								int ret = 0;
								for (int x = 0; x < a.length(); x++) {
									if (a.charAt(x) != '1' || a.charAt(x) != '0') {
										textField.setText("Entered wrong input !!");
										inputText.setText("-");
										break;
									}
									ret++;
								}
								if (ret == 4) {
									table.getModel().setValueAt(a, 4, 1);
									inputFlag = true;
									chckbxInpuFlag.setSelected(true);
								}
							} else {
								textField.setText("Entered wrong input !!");
							}
						} else {
							textField.setText("Entered wrong input !!");
						}

					}
					inputText.setText("-");
				}
			}
		});
		btnEnterInput.setBounds(971, 252, 112, 23);
		DEUARC.getContentPane().add(btnEnterInput);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setToolTipText("");
		DEUARC.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mnýtmStartGenerate = new JMenuItem("Start Generate");
		mnýtmStartGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		mnFile.add(mnýtmStartGenerate);

		JMenu mnType = new JMenu("Number Types");
		menuBar.add(mnType);

		JMenuItem mnýtmHexadecimal = new JMenuItem("Hexadecimal");
		mnýtmHexadecimal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dec) {
					decToHex();
				} else if (bin) {
					binToHex();
				}
				hex = true;
				bin = false;
				dec = false;
			}
		});
		mnType.add(mnýtmHexadecimal);

		JMenuItem mnýtmDecimal = new JMenuItem("Decimal");
		mnýtmDecimal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (bin) {
					binToDec();
				} else if (hex) {
					hexToDec();
				}
				hex = false;
				bin = false;
				dec = true;
			}
		});
		mnType.add(mnýtmDecimal);

		JMenuItem mnýtmBinary = new JMenuItem("Binary");
		mnýtmBinary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (dec) {
					decToBin();
				} else if (hex) {
					hexToBin();
				}
				hex = false;
				bin = true;
				dec = false;

			}
		});
		mnType.add(mnýtmBinary);

		JMenuItem mnýtmOpenAssamblyCode = new JMenuItem("Open Assambly Code");
		mnýtmOpenAssamblyCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("asm or basm", "asm", "basm");
				fileChooser.setFileFilter(filter);
				fileChooser.showOpenDialog(null);
				File selectedFile = fileChooser.getSelectedFile();
				File file = new File(selectedFile.getAbsolutePath());
				String[] asm = fileRead(file);
				String asmTemp = "";
				for (int i = 0; i < asm.length; i++) {
					asmText.append(asm[i] + "\n");
					btnNext.setEnabled(true);
					btnFnsh.setEnabled(true);
				}
				for (int i = 0; i < instr.length; i++) {
					instr[i] = "";
				}
				for (int i = 0; i < stack.length; i++) {
					stack[i] = "";
					data[i] = "";
				}
				lableTable = dataMemory(asm, firstData);
				Parsing prs=new Parsing();
				iMemory=prs.ParsingIM(asm, firstData,iMemory);

				pc = iMemory[0];
				sPC = iMemory[0];
				table.getModel().setValueAt(sPC, 3, 1);
				table.getModel().setValueAt(pc, 3, 1);

				for (Node temp = firstData; temp != null; temp = temp.getNextColumn()) {
					LTTable.getModel().setValueAt(temp.getIndex(), ct, 0);
					LTTable.getModel().setValueAt(temp.getAddress(), ct, 1);
					LTTable.getModel().setValueAt(temp.getContent(), ct++, 2);
				}
				for (int i = 0; i < lableTable.length - 1; i++) {
					DMTable.getModel().setValueAt(lableTable[i], i, 1);
					data[i] = (String) DMTable.getModel().getValueAt(i, 1);
				}
				int ct2 = Integer.parseInt(iMemory[0]);
				for (int i = 1; i < iMemory.length; i++) {
					if (iMemory[i].equals("")) {
						IMTable.getModel().setValueAt("00000000000", ct2, 0);
					}
					IMTable.getModel().setValueAt(iMemory[i], ct2, 0);
					instr[i] = (String) IMTable.getModel().getValueAt(ct2, 0);

					ct2++;
				}

			}
		});
		mnFile.add(mnýtmOpenAssamblyCode);
		mnýtmStartGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * DEUARC.getContentPane().setBackground(Color.RED);
				 * DEUARC.getContentPane().setForeground(Color.RED);;
				 */
			}
		});
		mnýtmStartGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DEUARC.getContentPane().setBackground(Color.BLUE);
				DEUARC.getContentPane().setForeground(Color.BLUE);
			}
		});
		mnýtmStartGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DEUARC.getContentPane().setBackground(Color.green);
				DEUARC.getContentPane().setForeground(Color.green);

			}
		});
		mnýtmStartGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DEUARC.getContentPane().setBackground(Color.PINK);
				DEUARC.getContentPane().setForeground(Color.PINK);

			}
		});
		mnýtmStartGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DEUARC.getContentPane().setBackground(Color.ORANGE);
				DEUARC.getContentPane().setForeground(Color.ORANGE);
			}
		});
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	

	public static String binaryRepresent(int adress, int length, JCheckBox chckbxOutputFlag) {
		String binary = Integer.toString(adress, 2);
		if (binary.length() == length)
			return binary;
		else if (binary.length() < length) {
			int gap = length - binary.length();
			String missing = "0";
			for (int i = 1; i < gap; i++) {
				missing += "0";
			}
			binary = missing + binary;
			return binary;
		} else if (binary.length() > length && length == 4) {
			binary = binary.substring(binary.length() - 4, binary.length());
			chckbxOutputFlag.isSelected();
			return binary;
		} else {
			return "x";
		}
	}

	public static String negativeBinary(int D, int length, JCheckBox chckbxOutputFlag) {
		String binary = Integer.toBinaryString(D);
		if (binary.length() > 4) {
			binary = binary.substring(binary.length() - 4, binary.length());
		} else if (binary.length() < 4) {
			String missing = "1";
			for (int i = 0; i < 4 - binary.length(); i++) {
				missing += "1";
			}
			binary = missing + binary;
		}
		return binary;
	}

	public static void arithmeticLogic(String d, int click, JTable table, String registerD, String S1, String S2,
			JCheckBox chckbxOutputFlag) {
		int D = 0;
		int row = 0;
		int S1value = 0;
		int S2value = 0;
		int S1rowvalue = 0;
		int S2rowvalue = 0;
		row = registerD.equals("00") ? 6 : registerD.equals("01") ? 7 : registerD.equals("10") ? 8 : 6;
		System.out.println(row);
		S1value = S1.equals("00") ? 6 : S1.equals("01") ? 7 : S1.equals("10") ? 8 : S1.equals("0") ? row : 6;
		S2value = S2.equals("00") ? 6 : S2.equals("01") ? 7 : S2.equals("10") ? 8 : S2.equals("0") ? row : 6;
		S1rowvalue = Integer.parseInt((String.valueOf(table.getValueAt(S1value, 1))), 2);
		S2rowvalue = Integer.parseInt((String.valueOf(table.getValueAt(S2value, 1))), 2);

		if (d.equals("D0T3")) {
			// S2rowvalue = S2.equals("01") ? 6 : S2.equals("10") ? 7 :
			// S2.equals("11") ? 8 : S2.equals("0") ? 0 : 0;
			D = S1rowvalue + S2rowvalue;
			// table.setValueAt(binaryRepresent(D, 2,chckbxOutputFlag), 10, 1);
			if (D >= 0)
				table.setValueAt(binaryRepresent(D, 4, chckbxOutputFlag), row, 1);
			else {
				table.setValueAt(negativeBinary(D, 4, chckbxOutputFlag), row, 1);
			}
		} else if (d.equals("D1T3")) {
			D = S1rowvalue + 1;
			// table.setValueAt(binaryRepresent(D, 2,chckbxOutputFlag), 10,1);
			if (D >= 0)
				table.setValueAt(binaryRepresent(D, 4, chckbxOutputFlag), row, 1);
			else {
				table.setValueAt(negativeBinary(D, 4, chckbxOutputFlag), row, 1);
			}
		} else if (d.equals("D2T3")) {
			D = S1rowvalue + S1rowvalue;
			if (D >= 0)
				table.setValueAt(binaryRepresent(D, 4, chckbxOutputFlag), row, 1);
			else {
				table.setValueAt(negativeBinary(D, 4, chckbxOutputFlag), row, 1);
			}
		} else if (d.equals("D3T3")) {
			D = S1rowvalue / 2;
			if (D >= 0)
				table.setValueAt(binaryRepresent(D, 4, chckbxOutputFlag), row, 1);
			else {
				table.setValueAt(negativeBinary(D, 4, chckbxOutputFlag), row, 1);
			}
		} else if (d.equals("D4T3")) {
			
			String temp = "";			
			temp=(String.valueOf(table.getValueAt(S1value, 1))).charAt(0)=='0'?"1":"0";
			temp+=(String.valueOf(table.getValueAt(S1value, 1))).charAt(1)=='0'?"1":"0";
			temp+=(String.valueOf(table.getValueAt(S1value, 1))).charAt(2)=='0'?"1":"0";
			temp+=(String.valueOf(table.getValueAt(S1value, 1))).charAt(3)=='0'?"1":"0";
			// table.setValueAt(binaryRepresent(D, 2,chckbxOutputFlag), 6, 1);
			/*if (D >= 0)
				table.setValueAt(binaryRepresent(D, 4, chckbxOutputFlag), row, 1);
			else {
				table.setValueAt(negativeBinary(D, 4, chckbxOutputFlag), row, 1);
			}*/
			table.setValueAt(temp, row, 1);

		} else if (d.equals("D5T3")) {
			String right = "0";
			String left = "0";
			if (String.valueOf(table.getValueAt(S1value, 1)).charAt(0) == '0'
					|| String.valueOf(table.getValueAt(S2value, 1)).charAt(0) == '0') {
				right = "0";
			} else {
				right = "1";
			}
			if (String.valueOf(table.getValueAt(S1value, 1)).charAt(1) == '0'
					|| String.valueOf(table.getValueAt(S2value, 1)).charAt(1) == '0') {
				left = "0";
			} else {
				left = "1";
			}
			String Ds1 = right + left;
			if (String.valueOf(table.getValueAt(S1value, 1)).charAt(2) == '0'
					|| String.valueOf(table.getValueAt(S2value, 1)).charAt(2) == '0') {
				left = "0";
			} else {
				left = "1";
			}
			if (String.valueOf(table.getValueAt(S1value, 1)).charAt(3) == '0'
					|| String.valueOf(table.getValueAt(S2value, 1)).charAt(3) == '0') {
				left = "0";
			} else {
				left = "1";
			}
			String Ds = right + left;
			String Dson = Ds1 + Ds;
			// table.setValueAt(Ds, 10, 1);
			table.setValueAt(Dson, row, 1);
		}
	}

	public void DataTransfer(boolean dArray[], int click) {
		sc = 0;
		int input = 0;
		int output = 0;
		String s1, s2, d;
		s1 = sIR.substring(7, 9);
		s2 = sIR.substring(9, 11);
		d = sIR.substring(5, 7);
		int destinationReg = d.equals("00") ? 6 : d.equals("01") ? 7 : d.equals("10") ? 8 : 100;

		int S2Reg = s2.equals("00") ? 6 : s2.equals("01") ? 7 : s2.equals("10") ? 8 : 100;
		int S1Reg=s1.equals("00") ? 6 : s1.equals("01") ? 7 : s1.equals("10") ? 8 : 100;
		if (inputFlag && s1.equals("11")) {
			input = 4;
			S2Reg = d.equals("00") ? 6 : d.equals("01") ? 7 : d.equals("10") ? 8 : d.equals("11") ? 5 : 0;
		} else if (outputFlag && d.equals("11")) {
			output = 5;
		}
		int q = Integer.parseInt(String.valueOf(table.getValueAt(9, 1)));
		String addressRegister = (String) table.getValueAt(0, 1);
		if (dArray[6] && click == 4 && q == 1 && sc == 0) {
			table.setValueAt(S1 + S2, destinationReg, 1);
			sc = 0;
		} else if (dArray[6] && click == 4 && q == 0 && sc == 0) {
			addressRegister=null;
			addressRegister = s1 + s2;
			table.getModel().setValueAt(null, 0, 1);
			table.getModel().setValueAt(addressRegister, 0, 1);
			sc++;
		} else if (dArray[6] && click == 5 && q == 0) {
			table.setValueAt((String) DMTable.getValueAt(Integer.parseInt(addressRegister, 2), 1), destinationReg, 1);
			dArray[6] = false;
			sc = 0;
		} else if (dArray[7] && click == 4 && q == 1 && sc == 0) {
			dArray[7] = false;
			table.setValueAt(table.getValueAt(destinationReg, 1), S2Reg, 1);
			sc = 0;
		} else if (dArray[7] && click == 4 && q == 0 && sc == 0) {
			addressRegister = s1 + s2;
			table.getModel().setValueAt(addressRegister, 0, 1);
			sc++;
		} else if (dArray[7] && click == 5 && q == 0) {
			dArray[7] = false;
			DMTable.setValueAt(table.getModel().getValueAt(destinationReg, 1), Integer.parseInt(addressRegister, 2), 1);
			for (Node temp = firstData; temp != null; temp = temp.getNextColumn()) {
				if(temp.getAddress()!=null&&temp.getAddress().equals(Integer.toString(Integer.parseInt(addressRegister, 2)))){
					temp.setContent((String)table.getModel().getValueAt(S1Reg, 1));
				}
			}
			sc = 0;
		} else if (dArray[9] && click == 4) {
			if (s1.equals("11")) {
				table.setValueAt(table.getValueAt(input, 1), S2Reg, 1);
			} else if (d.equals("11")) {
				table.setValueAt(S1 + S2, output, 1);
			}
			dArray[9] = false;
			sc = 0;
		}

	}

	public static String[] dataMemory(String[] asm, Node firstData) {
		Node temp = firstData;
		String[] data = new String[16];
		boolean dataPart = false;
		int adress = 0;
		for (int i = 0; i < asm.length; i++) {
			String[] tempAsm = asm[i].replaceAll(",", " ").split(" ");
			if (!tempAsm[0].startsWith("HLT") && !tempAsm[0].startsWith("END")) {
				if (tempAsm[0].equals("ORG") && tempAsm[1].equals("D")) {

					adress = Integer.valueOf(tempAsm[2]);
					for (int k = 0; k < 16; k++) {
						if (dataPart) {
							data[k] = "0000";
						}
						if (k == adress) {
							i++;
							tempAsm = asm[i].replaceAll(",", " ").split("\\s+");
						}
						if (tempAsm[0].startsWith("END")) {
							data[k] = "0000";
							dataPart = true;
						} else {
							if (k < adress) {
								data[k] = "0000";
							} else if (k == adress) {

								temp.add(tempAsm[0], Integer.toString(k), tempAsm[2]);
								data[k] = binaryRepresent(Integer.valueOf(tempAsm[2]), 4, null);
								adress++;
								temp = temp.getNextColumn();
							}
						}

					}
				}
			}
		}

		return data;
	}
}
