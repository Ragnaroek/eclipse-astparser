package de.defmacro.ast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class JavaParser 
{
	private static final String NL = System.getProperty("line.separator");
	private final ASTParser fParser;
	
	/**
	 * 
	 * @param level - aus {@link AST}-Klasse
	 */
	public JavaParser(final int level)
	{
		fParser = ASTParser.newParser(level);
	}
	
	/**
	 * Erstellt einen Parser mit der aktuellen Java Language Specification.
	 */
	public JavaParser()
	{
		this(AST.JLS8);
        @SuppressWarnings( "unchecked" )
        Map<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions( JavaCore.VERSION_1_8, options );
        fParser.setCompilerOptions( options );
	}
	
	/**
	 * Liefert die geparste CompilationUnit zurueck.
	 * @param file - java.io.File
	 * @return der geparste DOM-Tree
	 * @throws IOException - wenn Datei nicht vorhanden oder Fehler bei Lesen aus Datei
	 */
	public CompilationUnit parseCompilationUnit(final File file) 
	throws IOException
	{
		return this.parseCompilationUnit(readContentFromFile(file), null);
	}
	
	public CompilationUnit parseCompilationUnit(final String content) 
	throws IOException
	{
		return this.parseCompilationUnit(content, null);
	}
	
	public CompilationUnit parseCompilationUnit(final String content, final IProgressMonitor monitor) 
	throws IOException
	{
		fParser.setSource(content.toCharArray());
		fParser.setKind(ASTParser.K_COMPILATION_UNIT);
		return (CompilationUnit)fParser.createAST(monitor);
	}
	
	private String readContentFromFile(final File file)
	throws IOException
	{
		try( BufferedReader br = new BufferedReader(new FileReader(file))) {
			final StringBuilder s = new StringBuilder();
			//Zeile fuer Zeile lesen (newline anhaengen, wird von Parser benoetigt)
			//und in String konvertieren
			String line = null;
			while((line = br.readLine()) != null) {
				s.append(line);
				s.append(NL);
			}
			return s.toString();
		}
	}
}
