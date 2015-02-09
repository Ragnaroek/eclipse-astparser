import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import de.defmacro.ast.JavaParser;
import static de.defmacro.ast.Constants.TEST_DIR;


public class TestOverride {
	
	public static void main(String[] args) throws Exception {
		
		File file = new File(TEST_DIR, "OverrideTest.java");
		CompilationUnit unit = new JavaParser().parseCompilationUnit(file);
		unit.recordModifications();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuilder text = new StringBuilder();
		String line;
		while( (line = reader.readLine()) != null) {
			text.append(line);
			text.append('\n');
		}
		
		
		Document doc = new Document(text.toString());

		unit.accept(new ASTVisitor() {

			@Override
            public boolean visit( EnumDeclaration node ) {
                // TODO Auto-generated method stub
                return super.visit( node );
            }

            @Override
            public boolean visit( AnonymousClassDeclaration node ) {

                //node.bodyDeclarations()

                // TODO Auto-generated method stub
                return super.visit( node );
            }

            @Override
			public boolean visit(TypeDeclaration node) {
				
				if(node.isInterface()) {
					return false;
				}
				

				//TODO In Groovy-Skript umwandeln (sch�ner)
				//TODO MethodDeclaration muss @Override haben -> alle CheckForNull und Nonnull entfernen
				//TODO Reporting: Methoden-Aufrufe mit mismatching Annotation rausschreiben!!!!
				//     --> Aufwendig, transitiv super-type rausfinden und methoden suchen!!!
				
				MethodDeclaration method = node.getMethods()[0];
				method.modifiers().remove(0);
				
				 IExtendedModifier modifier = (IExtendedModifier)method.modifiers().get(0);
				
				 Annotation annon = (Annotation)modifier;
				 //method.modifiers().re
				 //annon.getTypeName().
				 
				SingleVariableDeclaration param = (SingleVariableDeclaration)method.parameters().get(0);

				param.modifiers().clear();
				/*
			    IExtendedModifier modifier = (IExtendedModifier)method.modifiers().get(0);
			    Annotation annon = (Annotation)modifier;
			    
				BodyDeclaration decl = (BodyDeclaration)node.bodyDeclarations().get(0);
				*/
				return true;
			}
			
		});
	
		TextEdit edit = unit.rewrite(doc, null);
		edit.apply(doc);
		System.out.println(doc.get());		
	}
}
