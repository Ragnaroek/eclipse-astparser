import static de.defmacro.ast.Constants.TEST_DIR;

import java.io.File;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import de.defmacro.ast.JavaParser;



/**
 * TODO Diese Klasse gehört zum clojure transform paket.
 * Hier erstmal nur zum Testen bis gradle build ok.
 */
public class TestFuncImportModification {
	@Test public void test() throws Exception {
		JavaParser parser = new JavaParser();
		CompilationUnit unit = parser.parseCompilationUnit(new File("/Users/mb/pprojects/source-transform-clojure/testfiles/ImmutableCollection.java"));
		System.out.println(unit);
	}
}
