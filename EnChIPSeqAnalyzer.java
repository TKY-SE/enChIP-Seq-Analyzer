import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;

public class EnChIPSeqAnalyzer extends JFrame {

	private JPanel contentPane;
	private JLabel lblPosFile;
	private JLabel lblNegFile;
	private static JButton btnPosAdd;
	private static JButton btnPosRemove;
	private static JButton btnPosView;
	private static JButton btnNegAdd;
	private static JButton btnNegView;
	private static JButton btnNegRemove;
	private static JTable MainTable;

	static MyTableModel table_model;

	static TableRowSorter<TableModel> sorter;
	private static JScrollPane PosFiles_scrollPane;
	private static JList<Object> PosFile_list;
	private static JList<Object> NegFile_list;

	static DefaultListModel<Object> posfile_model;
	static DefaultListModel<Object> negfile_model;

	private JScrollPane MainTable_scrollPane;
	private JLabel lbland;
	private JScrollPane NegFiles_scrollPane;

	private JLabel lblor;

	private static JButton btnExportcsvFile;
	private JLabel lbltag;
	private JLabel lblfolen;
	private static JComboBox<Object> cb_tag;
	private static JComboBox<Object> cb_fen;
	private static JTextField tf_tag;
	private static JTextField tf_fen;
	private static JButton btn_pmn;
	private JLabel lblcount;
	private static JLabel lblcounter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					posfile_model = new DefaultListModel<Object>();
					PosFile_list = new JList<Object>(posfile_model);
					PosFile_list.setVisibleRowCount(4);

					negfile_model = new DefaultListModel<Object>();
					NegFile_list = new JList<Object>(negfile_model);
					NegFile_list.setVisibleRowCount(4);

					EnChIPSeqAnalyzer frame = new EnChIPSeqAnalyzer();
					frame.setVisible(true);



					btnPosAdd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {

							FileDialog fd = new FileDialog(frame, "Open File", FileDialog.LOAD);
							try {
								fd.setMultipleMode(true);
								fd.setVisible(true);

								File[] files = fd.getFiles();
								for (File file : files) {
									if (!(posfile_model.contains(file))) {
										posfile_model.addElement(file);
									}
								}
							} finally{
								fd.dispose();
							}
						}
					});

					btnNegAdd.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {

							FileDialog fd = new FileDialog(frame, "Open File", FileDialog.LOAD);
							try {
								fd.setMultipleMode(true);
								fd.setVisible(true);

								File[] files = fd.getFiles();
								for (File file : files) {
									if (!(negfile_model.contains(file))) {
										negfile_model.addElement(file);
									}
								}
							} finally{
								fd.dispose();
							}
						}
					});

					btnPosRemove.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (!PosFile_list.isSelectionEmpty()) {
								int index = PosFile_list.getSelectedIndex();
								posfile_model.remove(index);
							}
						}
					});

					btnNegRemove.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (!NegFile_list.isSelectionEmpty()) {
								int index = NegFile_list.getSelectedIndex();
								negfile_model.remove(index);
							}
						}
					});

					btnPosView.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							List<String> Last_List = new ArrayList<String>();
							int plsize = posfile_model.getSize();
							Boolean pflag = false;

							if (plsize == 1) {
								File file = new File(posfile_model.get(0).toString());
								Last_List = loadTableData(file);
								pflag = true;
							} else if (plsize > 0 && !PosFile_list.isSelectionEmpty()) {
								Last_List = Output_AND_List();
								pflag = true;
							} else {
								JOptionPane.showMessageDialog(null, "Please select a base data.");
							}

							if (pflag) {
								table_model = new MyTableModel();
								sorter = new TableRowSorter<TableModel>(table_model) {
									@Override
									public void toggleSortOrder(int column) {
										if (column >= 0 && column < getModelWrapper().getColumnCount()
												&& isSortable(column)) {
											List<SortKey> keys = new ArrayList<>(getSortKeys());
											if (!keys.isEmpty()) {
												SortKey sortKey = keys.get(0);
												if (sortKey.getColumn() == column
														&& sortKey.getSortOrder() == SortOrder.DESCENDING) {
													setSortKeys(null);
													return;
												}
											}
										}
										super.toggleSortOrder(column);
									}
								};

								for (String nl : Last_List) {
									String ip[] = Util.split(nl, "\t");

									Object[] ob = new Object[11];
									ob[0] = ip[0];
									ob[1] = Integer.valueOf(ip[1]);
									ob[2] = Integer.valueOf(ip[2]);
									ob[3] = Integer.valueOf(ip[3]);
									ob[4] = Integer.valueOf(ip[4]);
									ob[5] = Integer.valueOf(ip[5]);
									ob[6] = Float.valueOf(ip[6]);
									ob[7] = Float.valueOf(ip[7]);
									ob[8] = Float.valueOf(ip[8]);
									ob[9] = ip[9];
									ob[10] = ip[10];
									table_model.add(ob);
								}
								MainTable.setRowSorter(sorter);
								MainTable.setModel(table_model);
								int rowcount = MainTable.getRowCount();
								lblcounter.setText(String.valueOf(rowcount));
							}
						}

					});

					btnNegView.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							List<String> Last_List = new ArrayList<String>();
							int nlsize = negfile_model.getSize();
							Boolean nflag = false;


							if (nlsize == 1) {
								File file = new File(negfile_model.get(0).toString());
								Last_List = loadTableData(file);
								nflag = true;
							} else if (nlsize > 0 && !NegFile_list.isSelectionEmpty()) {
								Last_List = Output_OR_List();
								nflag = true;
							} else {
								JOptionPane.showMessageDialog(null, "Please select a base data.");
							}

							if (nflag) {
								table_model = new MyTableModel();
								sorter = new TableRowSorter<TableModel>(table_model) {
									@Override
									public void toggleSortOrder(int column) {
										if (column >= 0 && column < getModelWrapper().getColumnCount()
												&& isSortable(column)) {
											List<SortKey> keys = new ArrayList<>(getSortKeys());
											if (!keys.isEmpty()) {
												SortKey sortKey = keys.get(0);
												if (sortKey.getColumn() == column
														&& sortKey.getSortOrder() == SortOrder.DESCENDING) {
													setSortKeys(null);
													return;
												}
											}
										}
										super.toggleSortOrder(column);
									}
								};

								for (String nl : Last_List) {
									String ip[] = Util.split(nl, "\t");

									Object[] ob = new Object[11];
									ob[0] = ip[0];
									ob[1] = Integer.valueOf(ip[1]);
									ob[2] = Integer.valueOf(ip[2]);
									ob[3] = Integer.valueOf(ip[3]);
									ob[4] = Integer.valueOf(ip[4]);
									ob[5] = Integer.valueOf(ip[5]);
									ob[6] = Float.valueOf(ip[6]);
									ob[7] = Float.valueOf(ip[7]);
									ob[8] = Float.valueOf(ip[8]);
									ob[9] = ip[9];
									ob[10] = ip[10];
									table_model.add(ob);
								}
								MainTable.setRowSorter(sorter);
								MainTable.setModel(table_model);
								int rowcount = MainTable.getRowCount();
								lblcounter.setText(String.valueOf(rowcount));
							}
						}
					});

					btn_pmn.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							List<String> positive_List = new ArrayList<String>();
							List<String> negative_List = new ArrayList<String>();


							int plsize = posfile_model.getSize();
							int nlsize = negfile_model.getSize();

							if (plsize == 1) {
								File pfile = new File(posfile_model.get(0).toString());
								positive_List = loadTableData(pfile);
							}
							else if (plsize > 1 && !PosFile_list.isSelectionEmpty()) {
								positive_List = Output_AND_List();
							}
							else {
								JOptionPane.showMessageDialog(null, "Please select a base data.");
							}

							if (nlsize == 1) {
								File nfile = new File(negfile_model.get(0).toString());
								negative_List = loadTableData(nfile);
							} else if (nlsize > 1 && !NegFile_list.isSelectionEmpty()) {
								negative_List = Output_OR_List();
							} else {
								JOptionPane.showMessageDialog(null, "Please select a base data.");
							}

							table_model = new MyTableModel();

							try {
								for (String tl : positive_List) {
									String tp[] = Util.split(tl, "\t");
									int tpos[] = { Integer.parseInt(tp[1]), Integer.parseInt(tp[2]) };
									int flag = 0;
									for (String il : negative_List) {
										String ip[] = Util.split(il, "\t");
										int ipos[] = { Integer.parseInt(ip[1]), Integer.parseInt(ip[2]) };

										if (tp[0].equals(ip[0])) {
											if	((ipos[0] <= tpos[0] && tpos[0] <= ipos[1]) ||
													(ipos[0] <= tpos[1] && tpos[1] <= ipos[1]) ||
													(ipos[0] < tpos[0] && tpos[1] < ipos[1]) ||
													(tpos[0] <= ipos[0] && ipos[1] <= tpos[1])) {
												flag = 1;
												break;
											}
										}
									}

									if (flag == 0) {
										Object[] ob = new Object[11];
										ob[0] = tp[0];
										ob[1] = Integer.valueOf(tp[1]);
										ob[2] = Integer.valueOf(tp[2]);
										ob[3] = Integer.valueOf(tp[3]);
										ob[4] = Integer.valueOf(tp[4]);
										ob[5] = Integer.valueOf(tp[5]);
										ob[6] = Float.valueOf(tp[6]);
										ob[7] = Float.valueOf(tp[7]);
										ob[8] = Float.valueOf(tp[8]);
										ob[9] = tp[9];
										ob[10] = tp[10];
										table_model.add(ob);
									}
								}
								sorter = new TableRowSorter<TableModel>(table_model) {
									@Override
									public void toggleSortOrder(int column) {
										if (column >= 0 && column < getModelWrapper().getColumnCount()
												&& isSortable(column)) {
											List<SortKey> keys = new ArrayList<>(getSortKeys());
											if (!keys.isEmpty()) {
												SortKey sortKey = keys.get(0);
												if (sortKey.getColumn() == column
														&& sortKey.getSortOrder() == SortOrder.DESCENDING) {
													setSortKeys(null);
													return;
												}
											}
										}
										super.toggleSortOrder(column);
									}
								};

								List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>();
								try {
									int intag = Integer.parseInt(tf_tag.getText());
									float inenr = Float.parseFloat(tf_fen.getText());
									int incbt = cb_tag.getSelectedIndex();
									int incbe = cb_fen.getSelectedIndex();
									int tag = 5;
									int enr = 7;
									//  1:"=", 2:"≥", 3:"≤"
									if (intag > 0) {
										switch (incbt) {
										case 1:
											filters.add(
													RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, intag, tag));
											break;
										case 2:
											filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER,
													intag - 1, tag));
											break;
										case 3:
											filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE,
													intag + 1, tag));
											break;

										}
									}


									if (inenr > 0) {
										switch (incbe) {
										case 1:
											filters.add(
													RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, inenr, enr));
											break;
										case 2:
											filters.add(
													RowFilter.numberFilter(RowFilter.ComparisonType.AFTER,
															inenr - 0.001, enr));
											break;
										case 3:
											filters.add(
													RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE,
															inenr + 0.001, enr));
											break;

										}
									}
								} catch (NumberFormatException err) {
									err.printStackTrace();
								} catch (Exception err) {
									err.printStackTrace();
								}

								sorter.setRowFilter(RowFilter.andFilter(filters));

								MainTable.setRowSorter(sorter);
								MainTable.setModel(table_model);
								int rowcount = MainTable.getRowCount();

								lblcounter.setText(String.valueOf(rowcount));

							} catch (Exception err) {
								err.printStackTrace();
							}
						}
					});

					btnExportcsvFile.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {

							FileDialog fd = new FileDialog(frame, "Save As", FileDialog.SAVE);
							String path = null;
							try {
								fd.setFile("*.csv");
								fd.setVisible(true);
								if (fd.getFile() != null) {
									path = fd.getDirectory() + fd.getFile();
								}

							} finally {
								fd.dispose();
							}

							try {
								FileWriter filewriter = new FileWriter(path);

								ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();

								ArrayList<String> title = new ArrayList<String>();
								for (int index = 0; index < MainTable.getColumnCount(); index++) {
									String name = MainTable.getColumnName(index);
									title.add(name);
								}
								table.add(title);

								int cols = MainTable.getColumnCount();
								int rows = MainTable.getRowCount();

								for (int i = 0; i < rows; i++) {
									ArrayList<String> line = new ArrayList<String>();
									for (int j = 0; j < cols; j++) {
										line.add(MainTable.getValueAt(i, j).toString());
									}
									table.add(line);
								}

								for (int i = 0; i < table.size(); i++) {
									ArrayList<String> temp = table.get(i);
									for (int j = 0; j < temp.size(); j++) {
										filewriter.write(temp.get(j));
										if (j < temp.size() - 1)
											filewriter.write("\t");
									}
									filewriter.write("\r\n");
								}
								filewriter.close();

							} catch(NullPointerException ex) {
								ex.printStackTrace();
							} catch (FileNotFoundException ex) {
								ex.printStackTrace();
							} catch (IOException ex) {
								ex.printStackTrace();
							}


						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});


	}

	public static List<String> Output_AND_List () {

		List<List<String>> Pall_List = new ArrayList<List<String>>();
		List<String> Last_List = new ArrayList<String>();
		int plsize = posfile_model.getSize();
		int sindex = PosFile_list.getSelectedIndex();
		List<String> Selected_pList = new ArrayList<String>();
		File sfile = new File(posfile_model.get(sindex).toString());
		Selected_pList = loadTableData(sfile);

		for (int index = 0; index < plsize; index++) {
			if (index == sindex) continue;
			File file = new File(posfile_model.get(index).toString());
			List<String> list = new ArrayList<String>();
			list = loadTableData(file);
			Pall_List.add(list);
		}

		for (List<String> plist : Pall_List) {
			if (Last_List.isEmpty()) {
				for (String sl : Selected_pList) {
					String sp[] = Util.split(sl, "\t");
					int spos[] = { Integer.parseInt(sp[1]), Integer.parseInt(sp[2]) };
					for (String pl : plist) {
						String pp[] = Util.split(pl, "\t");
						int ppos[] = { Integer.parseInt(pp[1]), Integer.parseInt(pp[2]) };
						if (!sp[0].equals(pp[0])) continue;
						if (spos[0] > ppos[1] || spos[1] < ppos[0]) continue;
						if (spos[0] < ppos[0]) sp[1] = pp[1];
						if (spos[1] > ppos[1]) sp[2] = pp[2];
						int pp3 = Integer.parseInt(sp[2]) - Integer.parseInt(sp[1]) + 1;
						if (pp3 > 0) {
							sp[3] = String.valueOf(pp3);
							String and_sl = Util.listToString(sp);
							Last_List.add(and_sl);
						}
					}
				}
			} else {
				for (int i = 0; i < Last_List.size(); i++) {
					String sl = Last_List.get(i);
					String sp[] = Util.split(sl, "\t");
					int spos[] = { Integer.parseInt(sp[1]), Integer.parseInt(sp[2]) };
					for (String pl : plist) {
						String pp[] = Util.split(pl, "\t");
						int ppos[] = { Integer.parseInt(pp[1]), Integer.parseInt(pp[2]) };
						if (!sp[0].equals(pp[0])) continue;
						if (spos[0] > ppos[1] || spos[1] < ppos[0]) continue;
						if (spos[0] < ppos[0]) sp[1] = pp[1];
						if (spos[1] > ppos[1]) sp[2] = pp[2];
						int pp3 = Integer.parseInt(sp[2]) - Integer.parseInt(sp[1]) + 1;
						if (pp3 > 0) {
							sp[3] = String.valueOf(pp3);
							String and_sl = Util.listToString(sp);
							Last_List.set(i, and_sl);
						}
					}
				}
			}

		}

		return Last_List;
	}

	public static List<String> Output_OR_List () {
		List<List<String>> Nall_List = new ArrayList<List<String>>();
		List<String> Last_List = new ArrayList<String>();
		int nlsize = negfile_model.getSize();

		int sindex = NegFile_list.getSelectedIndex();
		List<String> Selected_nList = new ArrayList<String>();
		File sfile = new File(negfile_model.get(sindex).toString());
		Selected_nList = loadTableData(sfile);

		for (int index = 0; index < nlsize; index++) {
			if (index == sindex) continue;
			File file = new File(negfile_model.get(index).toString());
			List<String> list = new ArrayList<String>();
			list = loadTableData(file);
			Nall_List.add(list);
		}

		for (List<String> nlist : Nall_List) {
			if (Last_List.isEmpty()) {
				for (String sl : Selected_nList) {
					String sp[] = Util.split(sl, "\t");
					int spos[] = { Integer.parseInt(sp[1]), Integer.parseInt(sp[2]) };
					for (String nl : nlist) {
						String np[] = Util.split(nl, "\t");
						int npos[] = { Integer.parseInt(np[1]), Integer.parseInt(np[2]) };
						if (!sp[0].equals(np[0])) continue;
						if (spos[0] > npos[1] || spos[1] < npos[0]) continue;
						if (spos[0] > npos[0]) sp[1] = np[1];
						if (spos[1] < npos[1]) sp[2] = np[2];
						String and_sl = Util.listToString(sp);
						Last_List.add(and_sl);
					}
				}
			} else {
				for (int i = 0; i < Last_List.size(); i++) {
					String sl = Last_List.get(i);
					String sp[] = Util.split(sl, "\t");
					int spos[] = { Integer.parseInt(sp[1]), Integer.parseInt(sp[2]) };
					for (String nl : nlist) {
						String np[] = Util.split(nl, "\t");
						int npos[] = { Integer.parseInt(np[1]), Integer.parseInt(np[2]) };
						if (!sp[0].equals(np[0])) continue;
						if (spos[0] > npos[1] || spos[1] < npos[0]) continue;
						if (spos[0] > npos[0]) sp[1] = np[1];
						if (spos[1] < npos[1]) sp[2] = np[2];
						String and_sl = Util.listToString(sp);
						Last_List.set(i, and_sl);
					}
				}
			}
		}

		return Last_List;
	}


	/**
	 * Create the frame.
	 */
	public EnChIPSeqAnalyzer() {
		setTitle("enChIP-Seq Analyzer");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 960, 640);
		contentPane = new JPanel();
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PosFile_list.clearSelection();
				NegFile_list.clearSelection();
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 30, 0, 0, 60, 70, 80, 80, 30, 0 };
		gbl_contentPane.rowHeights = new int[] { 30, 0, 0, 25, 0, 0, 30, 30, 30, 300, 0, 30, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0,
				0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		lblPosFile = new JLabel("Positive");
		GridBagConstraints gbc_lblPosFile = new GridBagConstraints();
		gbc_lblPosFile.anchor = GridBagConstraints.SOUTH;
		gbc_lblPosFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblPosFile.gridx = 1;
		gbc_lblPosFile.gridy = 1;
		contentPane.add(lblPosFile, gbc_lblPosFile);

		PosFiles_scrollPane = new JScrollPane();
		GridBagConstraints gbc_PosFiles_scrollPane = new GridBagConstraints();
		gbc_PosFiles_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_PosFiles_scrollPane.gridheight = 3;
		gbc_PosFiles_scrollPane.gridwidth = 4;
		gbc_PosFiles_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_PosFiles_scrollPane.gridx = 2;
		gbc_PosFiles_scrollPane.gridy = 1;
		contentPane.add(PosFiles_scrollPane, gbc_PosFiles_scrollPane);
		PosFiles_scrollPane.setViewportView(PosFile_list);

		btnPosAdd = new JButton("add");
		GridBagConstraints gbc_btnPosAdd = new GridBagConstraints();
		gbc_btnPosAdd.fill = GridBagConstraints.BOTH;
		gbc_btnPosAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnPosAdd.gridx = 6;
		gbc_btnPosAdd.gridy = 1;
		contentPane.add(btnPosAdd, gbc_btnPosAdd);

		lbland = new JLabel("(AND)");
		GridBagConstraints gbc_lbland = new GridBagConstraints();
		gbc_lbland.anchor = GridBagConstraints.NORTH;
		gbc_lbland.insets = new Insets(0, 0, 5, 5);
		gbc_lbland.gridx = 1;
		gbc_lbland.gridy = 2;
		contentPane.add(lbland, gbc_lbland);

		btnPosRemove = new JButton("remove");
		GridBagConstraints gbc_btnPosRemove = new GridBagConstraints();
		gbc_btnPosRemove.fill = GridBagConstraints.BOTH;
		gbc_btnPosRemove.insets = new Insets(0, 0, 5, 5);
		gbc_btnPosRemove.gridx = 6;
		gbc_btnPosRemove.gridy = 2;
		contentPane.add(btnPosRemove, gbc_btnPosRemove);

		btnPosView = new JButton("view");
		GridBagConstraints gbc_btnPosView = new GridBagConstraints();
		gbc_btnPosView.fill = GridBagConstraints.BOTH;
		gbc_btnPosView.insets = new Insets(0, 0, 5, 5);
		gbc_btnPosView.gridx = 6;
		gbc_btnPosView.gridy = 3;
		contentPane.add(btnPosView, gbc_btnPosView);

		lblNegFile = new JLabel("Negative");
		GridBagConstraints gbc_lblNegFile = new GridBagConstraints();
		gbc_lblNegFile.anchor = GridBagConstraints.SOUTH;
		gbc_lblNegFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblNegFile.gridx = 1;
		gbc_lblNegFile.gridy = 4;
		contentPane.add(lblNegFile, gbc_lblNegFile);

		NegFiles_scrollPane = new JScrollPane();
		GridBagConstraints gbc_NegFiles_scrollPane = new GridBagConstraints();
		gbc_NegFiles_scrollPane.gridheight = 3;
		gbc_NegFiles_scrollPane.gridwidth = 4;
		gbc_NegFiles_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_NegFiles_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_NegFiles_scrollPane.gridx = 2;
		gbc_NegFiles_scrollPane.gridy = 4;
		contentPane.add(NegFiles_scrollPane, gbc_NegFiles_scrollPane);
		NegFiles_scrollPane.setViewportView(NegFile_list);

		btnNegAdd = new JButton("add");
		GridBagConstraints gbc_btnNegAdd = new GridBagConstraints();
		gbc_btnNegAdd.fill = GridBagConstraints.BOTH;
		gbc_btnNegAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnNegAdd.gridx = 6;
		gbc_btnNegAdd.gridy = 4;
		contentPane.add(btnNegAdd, gbc_btnNegAdd);

		lblor = new JLabel("(OR)");
		GridBagConstraints gbc_lblor = new GridBagConstraints();
		gbc_lblor.anchor = GridBagConstraints.NORTH;
		gbc_lblor.insets = new Insets(0, 0, 5, 5);
		gbc_lblor.gridx = 1;
		gbc_lblor.gridy = 5;
		contentPane.add(lblor, gbc_lblor);

		btnNegRemove = new JButton("remove");
		GridBagConstraints gbc_btnNegRemove = new GridBagConstraints();
		gbc_btnNegRemove.fill = GridBagConstraints.BOTH;
		gbc_btnNegRemove.insets = new Insets(0, 0, 5, 5);
		gbc_btnNegRemove.gridx = 6;
		gbc_btnNegRemove.gridy = 5;
		contentPane.add(btnNegRemove, gbc_btnNegRemove);

		btnNegView = new JButton("view");
		GridBagConstraints gbc_btnNegView = new GridBagConstraints();
		gbc_btnNegView.fill = GridBagConstraints.BOTH;
		gbc_btnNegView.insets = new Insets(0, 0, 5, 5);
		gbc_btnNegView.gridx = 6;
		gbc_btnNegView.gridy = 6;
		contentPane.add(btnNegView, gbc_btnNegView);

		lbltag = new JLabel("tag");
		lbltag.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lbltag = new GridBagConstraints();
		gbc_lbltag.anchor = GridBagConstraints.EAST;
		gbc_lbltag.gridwidth = 2;
		gbc_lbltag.insets = new Insets(0, 0, 5, 5);
		gbc_lbltag.gridx = 1;
		gbc_lbltag.gridy = 7;
		contentPane.add(lbltag, gbc_lbltag);

		cb_tag = new JComboBox<Object>();
		cb_tag.setModel(new DefaultComboBoxModel<Object>(new String[] { "", "=", "≥", "≤" }));
		GridBagConstraints gbc_cb_tag = new GridBagConstraints();
		gbc_cb_tag.fill = GridBagConstraints.BOTH;
		gbc_cb_tag.insets = new Insets(0, 0, 5, 5);
		gbc_cb_tag.gridx = 3;
		gbc_cb_tag.gridy = 7;
		contentPane.add(cb_tag, gbc_cb_tag);

		tf_tag = new JTextField();
		tf_tag.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_tag.setText("0");
		tf_tag.setInputVerifier(new IntegerInputVerifier());
		GridBagConstraints gbc_tf_tag = new GridBagConstraints();
		gbc_tf_tag.fill = GridBagConstraints.BOTH;
		gbc_tf_tag.insets = new Insets(0, 0, 5, 5);
		gbc_tf_tag.gridx = 4;
		gbc_tf_tag.gridy = 7;
		contentPane.add(tf_tag, gbc_tf_tag);
		tf_tag.setColumns(10);

		lblfolen = new JLabel("fold enrichment");
		GridBagConstraints gbc_lblfolen = new GridBagConstraints();
		gbc_lblfolen.anchor = GridBagConstraints.EAST;
		gbc_lblfolen.gridwidth = 2;
		gbc_lblfolen.insets = new Insets(0, 0, 5, 5);
		gbc_lblfolen.gridx = 1;
		gbc_lblfolen.gridy = 8;
		contentPane.add(lblfolen, gbc_lblfolen);

		cb_fen = new JComboBox<Object>();
		cb_fen.setModel(new DefaultComboBoxModel<Object>(new String[] { "", "=", "≥", "≤" }));
		GridBagConstraints gbc_cb_fen = new GridBagConstraints();
		gbc_cb_fen.fill = GridBagConstraints.BOTH;
		gbc_cb_fen.insets = new Insets(0, 0, 5, 5);
		gbc_cb_fen.gridx = 3;
		gbc_cb_fen.gridy = 8;
		contentPane.add(cb_fen, gbc_cb_fen);

		tf_fen = new JTextField();
		tf_fen.setText("0");
		tf_fen.setInputVerifier(new IntegerInputVerifier());
		tf_fen.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_tf_fen = new GridBagConstraints();
		gbc_tf_fen.fill = GridBagConstraints.BOTH;
		gbc_tf_fen.insets = new Insets(0, 0, 5, 5);
		gbc_tf_fen.gridx = 4;
		gbc_tf_fen.gridy = 8;
		contentPane.add(tf_fen, gbc_tf_fen);
		tf_fen.setColumns(10);

		btn_pmn = new JButton("Pos.-Neg.");
		GridBagConstraints gbc_btn_pmn = new GridBagConstraints();
		gbc_btn_pmn.fill = GridBagConstraints.BOTH;
		gbc_btn_pmn.insets = new Insets(0, 0, 5, 5);
		gbc_btn_pmn.gridx = 6;
		gbc_btn_pmn.gridy = 8;
		contentPane.add(btn_pmn, gbc_btn_pmn);

		MainTable_scrollPane = new JScrollPane();
		GridBagConstraints gbc_MainTable_scrollPane = new GridBagConstraints();
		gbc_MainTable_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_MainTable_scrollPane.gridwidth = 6;
		gbc_MainTable_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_MainTable_scrollPane.gridx = 1;
		gbc_MainTable_scrollPane.gridy = 9;
		contentPane.add(MainTable_scrollPane, gbc_MainTable_scrollPane);

		MainTable = new JTable();
		MainTable_scrollPane.setViewportView(MainTable);

		lblcount = new JLabel("count : ");
		GridBagConstraints gbc_lblcount = new GridBagConstraints();
		gbc_lblcount.anchor = GridBagConstraints.WEST;
		gbc_lblcount.insets = new Insets(0, 0, 5, 5);
		gbc_lblcount.gridx = 1;
		gbc_lblcount.gridy = 10;
		contentPane.add(lblcount, gbc_lblcount);

		lblcounter = new JLabel("0");
		lblcounter.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblcounter = new GridBagConstraints();
		gbc_lblcounter.anchor = GridBagConstraints.EAST;
		gbc_lblcounter.insets = new Insets(0, 0, 5, 5);
		gbc_lblcounter.gridx = 2;
		gbc_lblcounter.gridy = 10;
		contentPane.add(lblcounter, gbc_lblcounter);

		btnExportcsvFile = new JButton("Export Table");
		GridBagConstraints gbc_btnExportcsvFile = new GridBagConstraints();
		gbc_btnExportcsvFile.anchor = GridBagConstraints.WEST;
		gbc_btnExportcsvFile.fill = GridBagConstraints.VERTICAL;
		gbc_btnExportcsvFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnExportcsvFile.gridx = 6;
		gbc_btnExportcsvFile.gridy = 10;
		contentPane.add(btnExportcsvFile, gbc_btnExportcsvFile);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
			Toolkit.getDefaultToolkit().beep();
		}
	}


	public static List<String> loadTableData(File file) {
		String line = null;
		List<String> list = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null) {
				if (line.startsWith("#") || line.startsWith("\t") || line.startsWith("\""))
					continue;
				list.add(line);
			}
			list.remove(0);

			br.close();
		} catch (

				Exception err) {
			err.printStackTrace();
		}
		return list;
	}

}


class IntegerInputVerifier extends InputVerifier {
	@Override public boolean verify(JComponent c) {
		boolean verified = false;
		if (c instanceof JTextComponent) {
			JTextComponent textField = (JTextComponent) c;
			try {
				Integer.parseInt(textField.getText());
				verified = true;
			} catch (NumberFormatException ex) {
				UIManager.getLookAndFeel().provideErrorFeedback(c);
				// Toolkit.getDefaultToolkit().beep();
			}
		}
		return verified;
	}
}

class MyTableModel extends DefaultTableModel {

	ColumnData[] coldata;

	boolean[] canEdit = new boolean[] {
			false, false, false, false, false,
			false, false, false, false, false,
			false
	};

	MyTableModel() {

		coldata = new ColumnData[11];
		coldata[0] = new ColumnData("chr", String.class); // 0
		coldata[1] = new ColumnData("start", Integer.class); // 1
		coldata[2] = new ColumnData("end", Integer.class); // 2
		coldata[3] = new ColumnData("length", Integer.class); // 3
		coldata[4] = new ColumnData("summit", Integer.class);// 4
		coldata[5] = new ColumnData("tags", Integer.class); // 5
		coldata[6] = new ColumnData("pvalue", Float.class); // 6
		coldata[7] = new ColumnData("fold_enrichment", Float.class); // 7
		coldata[8] = new ColumnData("FDR", Float.class); // 8
		coldata[9] = new ColumnData("gene1", String.class); // 9
		coldata[10] = new ColumnData("gene2", String.class); // 10

		for (ColumnData title : coldata) {
			addColumn(title.getTitle());
		}
	}

	public void add(Object[] ob) {
		addRow(ob);
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return coldata[col].getCellClass();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return canEdit[col];
	}
}

class ColumnData {

	String title;

	Class<?> cl;

	public ColumnData(String title, Class<?> cl){
		this.title = title;
		this.cl = cl;
	}

	public String getTitle(){
		return title;
	}

	public Class<?> getCellClass(){
		return this.cl;
	}

}

class Util {

	public static String[] split(String line, String str) {
		List<String> al = splitList(line, str);

		if(al == null)	return null;

		return (String[])al.toArray(new String[al.size()]);
	}

	public static List<String> splitList(String line, String str){

		if(line == null)
			return null;

		if(str == null || str.equals(""))
			return Arrays.asList(new String[]{line.trim()});

		List<String> al = new ArrayList<String>();

		int i = 0;
		int j = 0;

		if(line.indexOf(str) == -1)
			return Arrays.asList(new String[]{line.trim()});

		while((i = line.indexOf(str, j)) != -1){
			al.add(line.substring(j,i).trim());
			j = i + str.length();
		}

		al.add(line.substring(j, line.length()).trim());

		return al;
	}

	public static String listToString(String ... datas) {

		if(datas == null)	return null;

		String str = "";

		for(String data : datas){
			str += (str.equals("") ? "" : "\t") + data;
		}

		return str;
	}
}
