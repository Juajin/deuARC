import javax.swing.JCheckBox;

public class Parsing {
	public String[] instruction;

	Parsing() {
	}

	public String[] ParsingIM(String[] asm, Node firstData, String[] iMemory) {
		String[] instructions = new String[asm.length];
		boolean dataAdress = false;
		boolean adr1 = false;
		boolean adr2 = false;
		boolean flag = true;
		boolean hlt = false;
		boolean outr = false;
		boolean ret = false;
		int tempi = 0;
		boolean sub = false;
		boolean sub2 = false;
		boolean jmr = false;
		for (int i = 0; i < asm.length; i++) {
			ret = false;
			sub = false;
			jmr = false;
			String[] tempAsm = asm[i].replaceAll(",", " ").replaceAll("\t", " ").split(" ", -1);
			if (tempAsm[0].equals("")) {
				for (int l = 0; l < tempAsm.length - 1; l++) {
					tempAsm[l] = tempAsm[l + 1];
				}
			}

			if (tempAsm[0].equals("ORG") && tempAsm[1].equals("D") && sub2) {
				i = tempi;
				tempAsm = asm[i].replaceAll(",", " ").replaceAll("\t", " ").split(" ");
			}

			if (tempAsm[0].equals("ORG") && !tempAsm[1].equals("") && flag) {

				flag = false;
			}
			if (!tempAsm[0].startsWith("HLT") && !tempAsm[0].startsWith("END")) {
				if (!dataAdress && tempAsm[0] != null && tempAsm[1] != null && tempAsm[0].equals("ORG")
						&& tempAsm[1].equals("C")) {
					instructions[i] = tempAsm[2];
				} else if (dataAdress || (tempAsm[0] != null && tempAsm[1] != null && tempAsm[0].equals("ORG")
						&& tempAsm[1].equals("D"))) {
					instructions[i] = "00000000000";
					dataAdress = true;
				} else {
					if (tempAsm[1] != null && tempAsm[1].startsWith("#") && !adr1) {
						instructions[i] = "1";
						adr1 = true;
					}
					if (tempAsm.length > 2 && tempAsm[2].startsWith("#") && !adr2) {
						instructions[i] = "1";
						adr2 = true;
					}
					if (instructions[i] == null) {
						instructions[i] = "0";
					}
					instructions[i] += tempAsm[0].equals("LD") ? "0110"
							: tempAsm[0].equals("ST") ? "0111"
									: tempAsm[0].equals("TSF") ? "1001"
											: tempAsm[0].equals("ADD") ? "0000"
													: tempAsm[0].equals("INC") ? "0001"
															: tempAsm[0].equals("DBL") ? "0010"
																	: tempAsm[0].equals("DBT") ? "0011"
																			: tempAsm[0].equals("NOT") ? "0100"
																					: tempAsm[0].equals("AND") ? "0101"
																							: tempAsm[0].equals("CAL")
																									? "1010"
																									: tempAsm[0]
																											.equals("RET")
																													? "1011000000"
																													: tempAsm[0]
																															.equals("JMP")
																																	? "1100"
																																	: tempAsm[0]
																																			.equals("JMR")
																																					? "1101"
																																					: tempAsm[0]
																																							.equals("PSH")
																																									? "1110"
																																									: tempAsm[0]
																																											.equals("POP")
																																													? "1111"
																																													: "x";
					if (tempAsm[0].equals("RET"))
						ret = true;

					if (tempAsm[0].equals("CAL")) {
						for (int j = 0; j < asm.length; j++) {
							String[] tempasm = asm[j].replaceAll(",", " ").split(" ");
							if (tempasm[0].startsWith(tempAsm[1])) {
								subProg(asm, j, instructions);
								sub = true;
								sub2 = true;
								binaryRepresent(Integer.valueOf(instructions[0]) + j - 1, 5, null);
								instructions[i] += "0"
										+ binaryRepresent(Integer.valueOf(instructions[0]) + j - 1, 5, null);
								tempi = i + 1;
								i = j;
								break;
							}
						}
					}

					else if (tempAsm[0].equals("JMR")) {
						for (Node temp = firstData; temp != null; temp = temp.getNextColumn()) {
							if (temp.getIndex().equals(tempAsm[1] + ":")) {
								String tp = Integer.toBinaryString(Integer.parseInt(temp.getAddress()));
								while (tp.length() != 4) {
									tp = "0" + tp;
								}
								instructions[i] += "00" + tp;
								jmr = true;
								break;
							}
						}
					} else if (tempAsm[0].equals("JMP") || tempAsm[0].equals("POP") || tempAsm[0].equals("PSH")) {
						for (Node temp = firstData; temp != null; temp = temp.getNextColumn()) {
							if (temp.getIndex().equals(tempAsm[1] + ":")) {
								String tp = Integer.toBinaryString(Integer.parseInt(temp.getAddress()));
								while (tp.length() != 5) {
									tp = "0" + tp;
								}
								instructions[i] += "0" + tp;
								jmr = true;
								break;
							}
						}
					}
					if (!tempAsm[0].equals("POP") && !tempAsm[0].equals("PSH") && !tempAsm[0].equals("JMR")
							&& !tempAsm[0].equals("JMP") && !tempAsm[0].equals("RET") && !tempAsm[0].equals("CAL")) {
						if (!adr1) {// bu gerekli mi yoksa her þekilde
							// registerlarý kullanmak zorunda
							// mý(deðiþken kullanma ihtimali var mý?
							if (tempAsm.length > 2 && (tempAsm[1].equals("OUTR") || tempAsm[2].equals("OUTR"))) {
								instructions[i] += "11";
								outr = true;
							} else {
								instructions[i] += tempAsm[1].equals("R0") ? "00"
										: tempAsm[1].equals("R1") ? "01" : tempAsm[1].equals("R2") ? "10" : "x";
							}
						}
					}

					if (tempAsm.length > 2 && tempAsm[2] != null && tempAsm[2].startsWith("@")) {
						int adress = 0;
						boolean data = false;
						for (int j = 0; j < asm.length; j++) {
							if (!asm[j].startsWith("HLT") && !asm[j].startsWith("END")) {
								String[] asmData = asm[j].replaceAll(",", " ").split(" ");
								if (asmData[0] != null && asmData[1] != null && asmData[0].equals("ORG")
										&& asmData[1].equals("D")) {
									if (asmData[2] != null) {
										adress = Integer.valueOf(asmData[2]) - 1;
										data = true;
									}
								}
								if (data) {
									if ((asmData[0].substring(0, asmData[0].length() - 1))
											.equals(tempAsm[2].substring(1)) && asmData[0].endsWith(":")) {
										instructions[i] += binaryRepresent(adress, 4, null);
									} else {
										adress++;
									}
								}
							}
						}
					} else if (tempAsm.length > 2 && tempAsm[2].startsWith("#")) {
						instructions[i] += binaryRepresent(Integer.valueOf(tempAsm[2].substring(1)), 4, null);
					} else if ((tempAsm.length > 3) || (tempAsm.length > 3 && !tempAsm[3].startsWith("%"))) {
						if (tempAsm[2] != null && tempAsm[2].startsWith("@")) {
							int adress = 0;
							boolean data = false;
							for (int j = 0; j < asm.length; j++) {
								if (!asm[j].startsWith("HLT") && !asm[j].startsWith("END")) {
									String[] asmData = asm[j].replaceAll(",", " ").replaceAll("\t", " ").split(" ");
									if (asmData[0] != null && asmData[1] != null && asmData[0].equals("ORG")
											&& asmData[1].equals("D")) {
										if (asmData[2] != null) {
											adress = Integer.valueOf(asmData[2]) - 1;
											data = true;
										}
									}
									if (data) {
										if ((asmData[0].substring(0, asmData[0].length() - 1))
												.equals(tempAsm[2].substring(1)) && asmData[0].endsWith(":")) {
											instructions[i] += binaryRepresent(adress, 4, null);
											break;
										} else {
											adress++;
										}
									}
								}
							}
						}
					} else if (tempAsm[2] != null && tempAsm[2].startsWith("#")) {
						instructions[i] += binaryRepresent(Integer.valueOf(tempAsm[2].substring(1)), 4, null);
					} else if (!sub && !ret) {
						if (!outr) {
							instructions[i] += tempAsm[2].equals("R0") ? "00"
									: tempAsm[2].equals("R1") ? "01"
											: tempAsm[2].equals("R2") ? "10"
													: tempAsm[2].equals("INPR") ? "1100"
															: tempAsm[2].equals("")
																	? instructions[i].substring(
																			instructions[i].length() - 2,
																			instructions[i].length()) + "00"
																	: tempAsm[2].startsWith("%")
																			? instructions[i]
																					.substring(
																							instructions[i].length()
																									- 2,
																							instructions[i].length())
																					+ "00"
																			: tempAsm.length == 2
																					? instructions[i].substring(
																							instructions[i].length()
																									- 2,
																							instructions[i].length())
																							+ "00"
																					: "x";
						} else {
							instructions[i] += tempAsm[1].equals("R0") ? "00"
									: tempAsm[1].equals("R1") ? "01" : tempAsm[1].equals("R2") ? "10" : "x";
						}
					}
					if (tempAsm.length > 3 && (tempAsm[2].startsWith("R")) && tempAsm[2].length() == 2) {
						instructions[i] += tempAsm[2].equals("R0") ? "00"
								: tempAsm[2].equals("R1") ? "01" : tempAsm[2].equals("R2") ? "10" : "x";
					}
					if (tempAsm.length >= 4 && (tempAsm[3] != null && tempAsm[3].startsWith(("R")))) {
						instructions[i] += tempAsm[3].equals("R0") ? "00"
								: tempAsm[3].equals("R1") ? "01" : tempAsm[3].equals("R2") ? "10" : "x";

					}
				}

			} else if (tempAsm[0] != null && tempAsm[0].equals("HLT")) {
				instructions[i] = "01000000000";
				hlt = true;
				break;
			}

		}

		return instructions;
	}

	public static void subProg(String asm[], int i, String[] instructions) {
		boolean dataAdress = false;
		boolean adr1 = false;
		boolean adr2 = false;
		boolean flag = true;
		boolean hlt = false;
		boolean not = false;

		String[] tempAsm = asm[i].replaceAll(",", " ").replace(":", "").split(" ", -1);
		if (tempAsm[1].equals("ORG") && !tempAsm[2].equals("") && flag) {
			// pc = tempAsm[3];
			flag = false;
		}
		if (!tempAsm[1].startsWith("HLT") && !tempAsm[1].startsWith("END")) {
			if (!dataAdress && tempAsm[1] != null && tempAsm[2] != null && tempAsm[1].equals("ORG")
					&& tempAsm[2].equals("C")) {
				instructions[i] = tempAsm[3];
			} else if (dataAdress || (tempAsm[1] != null && tempAsm[2] != null && tempAsm[1].equals("ORG")
					&& tempAsm[2].equals("D"))) {
				instructions[i] = "00000000000";
				dataAdress = true;
			} else {
				if (tempAsm[2] != null && tempAsm[2].startsWith("#") && !adr1) {
					instructions[i] = "1";
					adr1 = true;
				}
				if (tempAsm.length > 3 && tempAsm[3].startsWith("#") && !adr2) {
					instructions[i] = "1";
					adr2 = true;
				}
				if (instructions[i] == null) {
					instructions[i] = "0";
				}
				instructions[i] += tempAsm[1].equals("LD") ? "0110"
						: tempAsm[1].equals("ST") ? "0111"
								: tempAsm[1].equals("TSF") ? "1001"
										: tempAsm[1].equals("ADD") ? "0000"
												: tempAsm[1].equals("INC") ? "0001" : tempAsm[1].equals("DBL") ? "0010"
														: tempAsm[1].equals("DBT") ? "0011"
																: tempAsm[1].equals("NOT") ? "0100"
																		: tempAsm[1].equals("AND") ? "0101"
																				: tempAsm[1].equals("CAL") ? "1010"
																						: tempAsm[1].equals("RET")
																								? "1011"
																								: tempAsm[1]
																										.equals("JMP")
																												? "1100"
																												: tempAsm[1]
																														.equals("JMR")
																																? "1101"
																																: tempAsm[1]
																																		.equals("PSH")
																																				? "1110"
																																				: tempAsm[1]
																																						.equals("POP")
																																								? "1111"
																																								: "x";
				if (tempAsm[1].equals("POP") || tempAsm[1].equals("PSH") || tempAsm[1].equals("JMP")
						|| tempAsm[1].equals("RET") || tempAsm[1].equals("CAL")) {

				}
				if (tempAsm[1].equals("NOT")) {
					not = true;
				}
				if (!tempAsm[1].equals("POP") && !tempAsm[1].equals("PSH") && !tempAsm[1].equals("JMR")
						&& !tempAsm[1].equals("JMP") && !tempAsm[1].equals("RET") && !tempAsm[1].equals("CAL")) {
					if (!adr1) {// bu gerekli mi yoksa her þekilde
						// registerlarý kullanmak zorunda
						// mý(deðiþken kullanma ihtimali var mý?
						instructions[i] += tempAsm[2].equals("R0") ? "00"
								: tempAsm[2].equals("R1") ? "01" : tempAsm[2].equals("R2") ? "10" : "x";
					}
				}

				if (tempAsm.length > 4 && (tempAsm[4] == null || tempAsm[4].startsWith("%"))) {
					if (tempAsm[3] != null && tempAsm[3].startsWith("@")) {
						int adress = 0;
						boolean data = false;
						for (int j = 0; j < asm.length; j++) {
							if (!asm[j].startsWith("HLT") && !asm[j].startsWith("END")) {
								String[] asmData = asm[j].replaceAll(",", " ").split(" ");
								if (asmData[0].equals("")) {
									for (int l = 0; l < asmData.length - 1; l++) {
										asmData[l] = asmData[l + 1];
									}
								}
								if (asmData[0] != null && asmData[1] != null && asmData[0].equals("ORG")
										&& asmData[1].equals("D")) {
									if (asmData[2] != null) {
										adress = Integer.valueOf(asmData[2]) - 1;
										data = true;
									}
								}
								if (data) {
									if ((asmData[0].substring(0, asmData[0].length() - 1))
											.equals(tempAsm[3].substring(1)) && asmData[0].endsWith(":")) {
										instructions[i] += binaryRepresent(adress, 4, null);
									} else {
										adress++;
									}
								}
							}
						}
					} else if (tempAsm[3] != null && tempAsm[3].startsWith("#")) {
						instructions[i] += binaryRepresent(Integer.valueOf(tempAsm[3].substring(1)), 4, null);
					}
				} else if (!(tempAsm.length > 4) || (!tempAsm[4].startsWith("%"))) {
					if (tempAsm.length > 3 && tempAsm[3].startsWith("@")) {
						int adress = 0;
						boolean data = false;
						for (int j = 0; j < asm.length; j++) {
							if (!asm[j].startsWith("HLT") && !asm[j].startsWith("END")) {
								String[] asmData = asm[j].replaceAll(",", " ").split(" ");
								if (asmData[0] != null && asmData[1] != null && asmData[0].equals("ORG")
										&& asmData[1].equals("D")) {
									if (asmData[2] != null) {
										adress = Integer.valueOf(asmData[2]) - 1;
										data = true;
									}
								}
								if (data) {
									// String a =
									// asmData[0].substring(0,asmData[0].length()-1);
									if ((asmData[0].substring(0, asmData[0].length() - 1))
											.equals(tempAsm[3].substring(1)) && asmData[0].endsWith(":")) {
										instructions[i] += binaryRepresent(adress, 4, null);
										break;
									} else {
										adress++;
									}
								}
							}
						}
					} else if (tempAsm.length > 3 && tempAsm[3].startsWith("#")) {
						instructions[i] += binaryRepresent(Integer.valueOf(tempAsm[3].substring(1)), 4, null);
					} else {
						if (not) {
							instructions[i] += instructions[i].substring(instructions[i].length() - 2,
									instructions[i].length()) + "00";
							not = false;
						}
						if (tempAsm.length <= 2) {
							instructions[i] += instructions[i].substring(instructions[i].length() - 2,
									instructions[i].length()) + "00";

						} else {
							instructions[i] += tempAsm[3].equals("R0") ? "00"
									: tempAsm[3].equals("R1") ? "01"
											: tempAsm[3].equals("R2") ? "10"
													: tempAsm[3].startsWith("%")
															? instructions[i].substring(instructions[i].length() - 2,
																	instructions[i].length()) + "00"
															: tempAsm[3].equals("")
																	? instructions[i].substring(
																			instructions[i].length() - 2,
																			instructions[i].length()) + "00"
																	: "x";
						}
					}
					if (tempAsm.length > 4 && (tempAsm[4].startsWith("R"))) {
						instructions[i] += tempAsm[4].equals("R0") ? "00"
								: tempAsm[4].equals("R1") ? "01" : tempAsm[4].equals("R2") ? "10" : "x";
					}
					if (tempAsm.length > 4 && (tempAsm[5] != null && tempAsm[5].startsWith(("R")))) {
						instructions[i] += tempAsm[4].equals("R0") ? "00"
								: tempAsm[4].equals("R1") ? "01" : tempAsm[4].equals("R2") ? "10" : "x";

					}
				}
				if (tempAsm.length == 2) {
					instructions[i] += instructions[i].substring(instructions[i].length() - 2, instructions[i].length())
							+ "00";
				}

			}
		} else if (tempAsm[1] != null && tempAsm[1].equals("HLT")) {
			instructions[i] = "01000000000";
			hlt = true;
		}

	}

	public static String[] getIMemory(String[] instructions) {

		return instructions;
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

}
