package org.maox.wampire.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.maox.arq.gui.component.GUIButton;
import org.maox.arq.gui.component.GUILabel;
import org.maox.arq.gui.component.GUIPanel;
import org.maox.arq.gui.component.GUIStatusBar;
import org.maox.arq.gui.component.GUITextField;
import org.maox.arq.gui.menu.GUIMenuBar;
import org.maox.arq.gui.menu.GUIMenuItem;
import org.maox.arq.gui.menu.GUIMenuOption;
import org.maox.arq.gui.view.GUIFrame;
import org.maox.wampire.Wampire;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Marco visual del emulador
 *
 * @author Alex Orgaz
 *
 */
@SuppressWarnings("serial")
public class AppFrame extends GUIFrame {

	/* Log */
	final private static Logger logger = LoggerFactory.getLogger(AppFrame.class);
	/* Barra de estado */
	private GUIStatusBar statusBar;

	/* Logica del emulador (Funcionaría como controlador) */
	private Wampire wampire = null;
	private GUIPanel pnlContent;
	private GUILabel lblStartAddress;
	private GUITextField txtStartAddress;
	private GUIButton btnScan;
	private GUILabel lblRootAddress;
	private GUITextField txtRootAddress;

	/**
	 * Inialización del Marco Visual
	 *
	 * @param emulator
	 */
	public AppFrame(Wampire wampire) {
		super();
		this.wampire = wampire;
		setTitle("Wampire");
		setMinimumSize(new Dimension(800, 300));
		initLookAndFeel("Nimbus");
	}

	/**
	 * Manejador de los eventos de la ventana
	 *
	 * @author Alex
	 */
	public void actionPerformed(ActionEvent e) {
		setCursor(new Cursor(Cursor.WAIT_CURSOR));

		Object source = e.getSource();

		// Tratamiento de los eventos de menu
		if (source instanceof GUIMenuItem) {
			if (e.getActionCommand().equals("Open")) {
				logger.trace("Evento: {}", e.getActionCommand());
			}
		}

		// Eventos de botones
		if (source == getBtnScan()) {
			scan();
		}

		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Cierra la aplicación con error de Excepcion
	 *
	 * @param e Excepcion de cierre
	 */
	@Override
	protected void exit() {
		// Desvincular el marco
		this.dispose();
		// Salida
		wampire.exit();
	}

	/**
	 * Botón de comienzo de scaneo
	 *
	 * @return
	 */
	private GUIButton getBtnScan() {
		if (btnScan == null) {
			btnScan = new GUIButton("Scan");
			btnScan.setBounds(338, 162, 66, 23);
			btnScan.addActionListener(this);
			btnScan.setType(GUIButton.ACCEPT_BUTTON);
		}
		return btnScan;
	}

	private GUILabel getLblRootAddress() {
		if (lblRootAddress == null) {
			lblRootAddress = new GUILabel("Address");
			lblRootAddress.setBounds(84, 43, 83, 14);
			lblRootAddress.setText("Root Address");
		}
		return lblRootAddress;
	}

	private GUILabel getLblStartAddress() {
		if (lblStartAddress == null) {
			lblStartAddress = new GUILabel("Address");
			lblStartAddress.setBounds(84, 11, 83, 14);
			lblStartAddress.setText("Start Address");
		}
		return lblStartAddress;
	}

	/**
	 * Panel principal
	 *
	 * @return
	 */
	private GUIPanel getPnlContent() {
		if (pnlContent == null) {
			pnlContent = new GUIPanel();
			pnlContent.setLayout(null);
			pnlContent.add(getLblStartAddress());
			pnlContent.add(getTxtStartAddress());
			pnlContent.add(getLblRootAddress());
			pnlContent.add(getTxtRootAddress());
			pnlContent.add(getBtnScan());
		}
		return pnlContent;
	}

	private GUITextField getTxtRootAddress() {
		if (txtRootAddress == null) {
			txtRootAddress = new GUITextField();
			txtRootAddress.setRequired(false);
			txtRootAddress.setColumns(10);
			txtRootAddress.setBounds(177, 39, 410, 22);
		}
		return txtRootAddress;
	}

	private GUITextField getTxtStartAddress() {
		if (txtStartAddress == null) {
			txtStartAddress = new GUITextField();
			txtStartAddress.setBounds(177, 7, 410, 22);
			txtStartAddress.setRequired(true);
			txtStartAddress.setColumns(10);
		}
		return txtStartAddress;
	}

	/**
	 * Inicialización de la parte gráfica del escritorio
	 */
	@Override
	protected void initComponents() {
		// Estructura Layout
		getContentPane().setLayout(new BorderLayout());

		// Se añade la barra de estatus en la parte de abajo
		statusBar = new GUIStatusBar();
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		getContentPane().add(getPnlContent(), BorderLayout.CENTER);
	}

	@Override
	protected void initMenu() {
		menu = new GUIMenuBar();

		GUIMenuOption menuOption = new GUIMenuOption("File");
		menuOption.setMnemonic(KeyEvent.VK_F);
		menu.add(menuOption);

		GUIMenuItem menuItem = new GUIMenuItem("Open", KeyEvent.VK_O);
		menuItem.setActionCommand("Open");
		menuOption.add(menuItem);
		menu.addItem(menuItem);
	}

	/**
	 * Inicia el scaneo de la web introducida
	 */
	private void scan() {
		wampire.startScan((String) getTxtStartAddress().getData(), (String) getTxtRootAddress().getData());
	}
}
