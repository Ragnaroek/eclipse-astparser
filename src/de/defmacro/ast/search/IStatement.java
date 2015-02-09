package de.defmacro.ast.search;

import org.eclipse.jdt.core.dom.Block;



public interface IStatement 
extends IEvaluable<Block>
{
	//Der Typ hier existiert hauptsaechlich wegen Compiler-Warnung Varargs in Verbindung mit Generics
	//evtl fuer spaetere Erweiterung Statements nuetzlich
}
