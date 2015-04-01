package de.defmacro.ast;

import static de.defmacro.ast.Constants.*;
import static org.junit.Assert.*;

import java.io.File;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import static org.hamcrest.core.Is.*;

public class TestJavaParser 
{
	
	
	/**
	 * Testet ob das Parsen mit dem ASTParser gelingt. Der Test
	 * ist vorhanden um nach dem Update der ASTParser Libraries
	 * die Funktionalitaet zu testen.
	 * @throws Exception
	 */
	@Test public void shouldParseVerySimpleCompilationUnit() throws Exception {
		JavaParser parser = new JavaParser();
		CompilationUnit unit = parser.parseCompilationUnit(new File(TEST_DIR, "JavaClass.java"));
		assertThat(unit.getProblems().length, is(0));
	}
	
	@Test public void shouldParseJava8CompilationUnit() throws Exception {
		JavaParser parser = new JavaParser();
		CompilationUnit unit = parser.parseCompilationUnit(new File(TEST_DIR, "Java8Class.java"));
		assertThat(unit.getProblems().length, is(0));
	}
}
