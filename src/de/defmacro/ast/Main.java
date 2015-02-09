package de.defmacro.ast;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Main-Klasse fuer Toolkit. Zeigt nur einen Dialog an
 * wenn auf die .jar File doppelt geklickt wird.
 * @author Michael Bohn
 *
 */
public class Main {
	
	public static final String VERSION = "@version@";
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(null, "ASTParser Version "+VERSION+"\n(c) defmacro.de, eclipse.org");
			}
		});
	}
}
