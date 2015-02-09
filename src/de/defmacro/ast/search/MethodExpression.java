package de.defmacro.ast.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;


public class MethodExpression
extends ResultVisitor
implements IEvaluable<ClassDeclaration>
{
	private final String fMethodName;
	private final List<IEvaluable<Block>> fExpressions;
	private final List<ModifierKeyword> fModifiers;
	private Class<?> fReturnType;
	private List<Class<?>> fParameters;
	
	public MethodExpression()
	{
		this(null);
	}
	
	public MethodExpression(final String methodName)
	{
		this.fMethodName  = methodName;
		this.fExpressions = new ArrayList<IEvaluable<Block>>();
		this.fParameters  = Collections.emptyList();
		this.fModifiers = new ArrayList<ModifierKeyword>(1); 
	}
	
	public boolean eval(final ClassDeclaration subSearch) 
	{
		subSearch.getASTNode().accept(this);		
		return hasMatch();
	}

	@SuppressWarnings("unchecked") //node.parameters() liefert Liste von SingleVariableDeclarations, siehe API doc
	                               //node.modifiers() liefert List von Modifier zurueck
	@Override
	public boolean visit(final MethodDeclaration node) 
	{		
		if( node.getName().getIdentifier().equals(fMethodName)
				&& hasParameters(node.parameters())
				&& hasModifiers(node.modifiers())
				&& hasReturnType(node)) {
			
			for(IEvaluable<Block> expr : fExpressions) {
				if( !expr.eval(node.getBody()) ) {
					setMatch(false);
					return true;
				}
			}
		
			//gesuchte Methode gefunden, und (evtl.) Subexpression ok, nur dann ok
			setMatch(true);
			return true;
		}
		
		return true;
	}
	
	private boolean hasReturnType(final MethodDeclaration node) {
		if(fReturnType == null) { //return type matches always if not supplied
			return true;
		} else {
			return testTypeEquals(fReturnType, node.getReturnType2());
		}
	}
	
	@SuppressWarnings("unchecked") //keine Generics in ASTParser
	private boolean hasModifiers(final List modifiers) 
	{	
		for(ModifierKeyword modifierWanted : fModifiers) {
			if(!containsModifier(modifiers, modifierWanted)) {
				return false; //ein modifier nicht vorhanden
			}
		}
		
		//invariante: alle modifier die vorhanden sein sollten waren in actual liste
		return true;
	}
	
	@SuppressWarnings("unchecked") //keine Generics in ASTParser
	private boolean containsModifier(final List modifiers, final ModifierKeyword keyword)
	{
		for(int i=0;i<modifiers.size();i++) {
			Object obj = modifiers.get(i);
			if(obj instanceof Modifier) {
				Modifier modifier = (Modifier)obj;
				if(modifier.getKeyword().equals(keyword)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasParameters(final List<SingleVariableDeclaration> parameters)
	{
		if(parameters.size() != fParameters.size()) {
			return false;
		}
		
		//invariante: Laenge in Source == Laenge gewuenscht
		
		for(int i=0;i<parameters.size();i++) {
			SingleVariableDeclaration parameter = parameters.get(i);
			if(!testTypeEquals(fParameters.get(i), parameter.getType())) {
				return false;
			}
			/*
			if(parameter.getType().isSimpleType()) {				
				SimpleType simpleDeclaration = (SimpleType)parameter.getType();
				if( !testParameterName(fParameters.get(i), simpleDeclaration.getName())) {
					return false;
				}
			} else if (parameter.getType().isPrimitiveType()) {
				PrimitiveType primitive = (PrimitiveType)parameter.getType();
				if( !testPrimitiveType(fParameters.get(i), primitive.getPrimitiveTypeCode()) ) {
					return false;
				}
			} else { //unbekannter Typ
				System.out.println("warning: not simple type, feature not implemented. type is: " + parameter.getType().getClass());
				return false;
			} */
		}
		
		//invariante: Parameter in Source == gewuenschte Paramter
		
		return true;
	}
	
	private boolean testTypeEquals(final Class<?> expected, Type actual)
	{
		if(actual.isSimpleType()) {				
			SimpleType simpleDeclaration = (SimpleType)actual;
			return testNameEquals(expected, simpleDeclaration.getName());
		} else if (actual.isPrimitiveType()) {
			PrimitiveType primitive = (PrimitiveType)actual;
			return testPrimitiveTypeEquals(expected, primitive.getPrimitiveTypeCode());
		} else { //unbekannter Typ
			System.err.println("warning: not simple type, feature not implemented. type is: " + actual.getClass());
			throw new UnsupportedOperationException();
		}
	}
	
	private boolean testNameEquals(final Class<?> expected, final Name actual)
	{
		if( actual.isQualifiedName() ) { //qualifiziert angegeben => teste gegen kanonischen Namen
			return actual.getFullyQualifiedName().equals(expected.getCanonicalName());
		} else { //simple Name, ohne qualifier => teste gegen simpleName aus class
			return actual.getFullyQualifiedName().equals(expected.getSimpleName());
		}
	}
	
	private boolean testPrimitiveTypeEquals(final Class<?> expected, final Code actual)
	{
		String name = actual.toString();
		if( name.equals(PrimitiveType.BOOLEAN.toString()) ) {
			return expected.equals(Boolean.TYPE);
		} else if(name.equals(PrimitiveType.INT.toString())) {
			return expected.equals(Integer.TYPE);
		} else if(name.equals(PrimitiveType.CHAR.toString())) {
			return expected.equals(Character.TYPE);
		} else if(name.equals(PrimitiveType.SHORT.toString())) {
			return expected.equals(Short.TYPE);
		} else if(name.equals(PrimitiveType.LONG.toString())) {
			return expected.equals(Long.TYPE);
		} else if(name.equals(PrimitiveType.FLOAT.toString())) {
			return expected.equals(Float.TYPE);
		} else if(name.equals(PrimitiveType.DOUBLE.toString())) {
			return expected.equals(Double.TYPE);
		} else if(name.equals(PrimitiveType.BYTE.toString())) {
			return expected.equals(Byte.TYPE);
		} else if(name.equals(PrimitiveType.VOID.toString())) {
			return expected.equals(Void.TYPE);
		}
		System.err.println("warning: unknown primitive type: " + actual);
		throw new UnsupportedOperationException();
		//return false;
	}
	

	
	public void addExpression(final IEvaluable<Block> expression)
	{
		fExpressions.add(expression);
	}
	
	protected void setArguments(final Class<?>... classes)
	{
		if (classes == null) {
			throw new NullPointerException("classes must not be null, this is a vararg argument");
		}

		this.fParameters = Arrays.asList(classes);
	}

	public void addModifier(final ModifierKeyword... modifiers) {
		for(ModifierKeyword modifier : modifiers) {
			if(!fModifiers.contains(modifiers)) {
				fModifiers.add(modifier);
			}
		}
	}
	
	public void setReturnType(final Class<?> returnType) {
		this.fReturnType = returnType;
	}
}
