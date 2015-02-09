import groovy.io.FileType

import org.eclipse.jdt.core.dom.ASTNode
import org.eclipse.jdt.core.dom.ASTVisitor
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.EnumDeclaration
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jface.text.Document
import org.eclipse.text.edits.TextEdit

import de.defmacro.ast.JavaParser

sourceDir = new File('/Users/mb/projekte/hunter-clone/src/main/java/')
//sourceDir = new File('/Users/mb/tmp/fixTest/')

/*
 * Schreibt die Aenderungen in eine Kopie der Datei.
 * Kann zum Testen benutzt werden.
 */
copy = false;

CompilationUnit.metaClass.eachType = {Closure f ->
    delegate.accept(new ASTVisitor() {
                public boolean visit( AnonymousClassDeclaration node ) {
                    f node
                    true
                }

                public boolean visit(TypeDeclaration node) {
                    f node
                    true
                }
            })
}

sourceDir.eachFileRecurse(FileType.FILES) {
    if(it.name.endsWith('.java')) {
        fixJavaFile it
    }
}
println "Done Fixing"

def fixJavaFile(File file) {
    println "Fixing $file.name"
    String encoding = 'UTF-8'
    String fileContent = file.getText encoding

    CompilationUnit unit = new JavaParser().parseCompilationUnit(fileContent);
    assert unit.problems == []
    unit.recordModifications()

    unit.eachType {
        if(it.nodeType == ASTNode.ANONYMOUS_CLASS_DECLARATION) {
            it.bodyDeclarations().each {
                if(it.nodeType == ASTNode.METHOD_DECLARATION) {
                    fixMethod it
                }
            }
        } else if(it.nodeType == ASTNode.TYPE_DECLARATION) {
            if(it.isInterface()) {
                fixInterface it
            } else {
                fixClass it
            }
        }
    }

    Document doc = new Document(fileContent)
    TextEdit edit = unit.rewrite doc, null
    edit.apply doc

    File target
    if(copy) {
        target = new File(file.getAbsolutePath() + '.copy')
    } else {
        target = file
    }
    target.setText doc.get(), encoding
}

def fixInterface(TypeDeclaration node) {
    node.getMethods().each {
        it.parameters().each { param ->
            def finalModifier = param.modifiers().find {
                it.isModifier() && it.isFinal()
            }
            if(finalModifier) {
                param.modifiers().remove finalModifier
            }
        }
    }
}

def fixClass(TypeDeclaration node) {
    node.getMethods().each { fixMethod it }
}

def fixMethod(MethodDeclaration m) {
    if(hasOverride(m)) {
        /**
         * Fuer den Return Wert ist erlaubt Nonnull zu definieren und eine evtl.
         * vorhanden CheckForNull Deklaration zu verstärken.
         */
        removeRedundantModifiers m.modifiers(), true

        m.parameters().each { removeRedundantModifiers it.modifiers(), false }
    }
}

def removeRedundantModifiers(def modifiers, boolean allowNonNull) {
    def toBeRemoved = modifiers.findAll {
        if(it.isAnnotation()) {
            if(allowNonNull) {
                ['CheckForNull', 'Nullable'].contains (it.typeName.fullyQualifiedName)
            } else {
                ['Nonnull', 'CheckForNull', 'Nullable'].contains (it.typeName.fullyQualifiedName)
            }
        }
    }
    modifiers.removeAll toBeRemoved
}

def hasOverride(MethodDeclaration method) {
    method.modifiers().findResult {
        it.isAnnotation() && it.typeName.fullyQualifiedName ==  Override.class.simpleName ? it : null
    }
}
