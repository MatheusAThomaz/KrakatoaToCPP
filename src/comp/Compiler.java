
package comp;

import ast.*;
import lexer.*;
import java.io.*;
import java.util.*;

public class Compiler {

	// compile must receive an input with an character less than
	// p_input.lenght
	public Program compile(char[] input, PrintWriter outError) {
		ArrayList<CompilationError> compilationErrorList = new ArrayList<>();
		signalError = new ErrorSignaller(outError, compilationErrorList);
		symbolTable = new SymbolTable();
		lexer = new Lexer(input, signalError);
		signalError.setLexer(lexer);

		Program program = null;
		lexer.nextToken();
		program = program(compilationErrorList);
		return program;
	}

	private Program program(ArrayList<CompilationError> compilationErrorList) {
		// Program ::= KraClass { KraClass }
		ArrayList<MetaobjectCall> metaobjectCallList = new ArrayList<>();
		ArrayList<KraClass> kraClassList = new ArrayList<>();
		Program program = new Program(kraClassList, metaobjectCallList, compilationErrorList);
                Boolean flag = false;
                
		try {
			while ( lexer.token == Symbol.MOCall ) {
				metaobjectCallList.add(metaobjectCall());
			}
			kraClassList.add(classDec());
			while ( lexer.token == Symbol.CLASS ){
                            
                            this.symbolTable.removeLocalIdent();
                            
                            kraClassList.add(classDec());
                            
                            
                        }
                        
                        
                        
                        for (KraClass kr: kraClassList){
                            if (kr.getCname().equals("Program")){
                                flag = true;
                            } 
                        }
                        
                        if (!flag){
                            this.signalError.showError("Source code without a class 'Program'");
                        }
                        
			if ( lexer.token != Symbol.EOF ) {
				signalError.showError("End of file expected");
			}
		}
		catch( CompilerError e) {
                    // if there was an exception, there is a compilation signalError
                }
                catch ( RuntimeException e ) {
                    e.printStackTrace();
                }
                
		return program;
	}

	/**  parses a metaobject call as <code>{@literal @}ce(...)</code> in <br>
     * <code>
     * @ce(5, "'class' expected") <br>
     * clas Program <br>
     *     public void run() { } <br>
     * end <br>
     * </code>
     * 
	   
	 */
	@SuppressWarnings("incomplete-switch")
	private MetaobjectCall metaobjectCall() {
		String name = lexer.getMetaobjectName();
		lexer.nextToken();
		ArrayList<Object> metaobjectParamList = new ArrayList<>();
		if ( lexer.token == Symbol.LEFTPAR ) {
			// metaobject call with parameters
			lexer.nextToken();
			while ( lexer.token == Symbol.LITERALINT || lexer.token == Symbol.LITERALSTRING ||
					lexer.token == Symbol.IDENT ) {
				switch ( lexer.token ) {
				case LITERALINT:
					metaobjectParamList.add(lexer.getNumberValue());
					break;
				case LITERALSTRING:
					metaobjectParamList.add(lexer.getLiteralStringValue());
					break;
				case IDENT:
					metaobjectParamList.add(lexer.getStringValue());
                                        break;
				}
				lexer.nextToken();
				if ( lexer.token == Symbol.COMMA ) 
					lexer.nextToken();
				else
					break;
			}
			if ( lexer.token != Symbol.RIGHTPAR ) 
				signalError.showError("')' expected after metaobject call with parameters");
			else
				lexer.nextToken();
		}
		if ( name.equals("nce") ) {
			if ( metaobjectParamList.size() != 0 )
				signalError.showError("Metaobject 'nce' does not take parameters");
		}
		else if ( name.equals("ce") ) {
			if ( metaobjectParamList.size() != 3 && metaobjectParamList.size() != 4 )
				signalError.showError("Metaobject 'ce' take three or four parameters");
			if ( !( metaobjectParamList.get(0) instanceof Integer)  )
				signalError.showError("The first parameter of metaobject 'ce' should be an integer number");
			if ( !( metaobjectParamList.get(1) instanceof String) ||  !( metaobjectParamList.get(2) instanceof String) )
				signalError.showError("The second and third parameters of metaobject 'ce' should be literal strings");
			if ( metaobjectParamList.size() >= 4 && !( metaobjectParamList.get(3) instanceof String) )  
				signalError.showError("The fourth parameter of metaobject 'ce' should be a literal string");
			
		}
			
		return new MetaobjectCall(name, metaobjectParamList);
	}

	private KraClass classDec() {
		// Note que os m�todos desta classe n�o correspondem exatamente �s
		// regras
		// da gram�tica. Este m�todo classDec, por exemplo, implementa
		// a produ��o KraClass (veja abaixo) e partes de outras produ��es.

		/*
		 * KraClass ::= ``class'' Id [ ``extends'' Id ] "{" MemberList "}"
		 * MemberList ::= { Qualifier Member } 
		 * Member ::= InstVarDec | MethodDec
		 * InstVarDec ::= Type IdList ";" 
		 * MethodDec ::= Qualifier Type Id "("[ FormalParamDec ] ")" "{" StatementList "}" 
		 * Qualifier ::= [ "static" ]  ( "private" | "public" )
		 */
                
                Boolean flag = false;
                KraClass kr;
                
		if ( lexer.token != Symbol.CLASS ) signalError.showError("'class' expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.IDENT )
			signalError.show(ErrorSignaller.ident_expected);
		String className = lexer.getStringValue();
                this.currentClass =  new KraClass(className);
                
		symbolTable.putInGlobal(className, this.currentClass);
		lexer.nextToken();
		if ( lexer.token == Symbol.EXTENDS ) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.show(ErrorSignaller.ident_expected);
			String superclassName = lexer.getStringValue();     
               
                        if(className.equals(superclassName))
                        {
                            this.signalError.showError("Class '" + className + "' is inheriting from itself");
                        }            

                        kr = this.symbolTable.getInGlobal(superclassName);
                        
                        this.currentClass.setSuperClass(kr);
                        this.symbolTable.getInGlobal(className).setSuperClass(kr);
                        

			lexer.nextToken();
		}
		if ( lexer.token != Symbol.LEFTCURBRACKET )
			signalError.showError("{ expected", true);
		lexer.nextToken();

		while (lexer.token == Symbol.PRIVATE || lexer.token == Symbol.PUBLIC) {
			Symbol qualifier;
			switch (lexer.token) {
			case PRIVATE:
				lexer.nextToken();
				qualifier = Symbol.PRIVATE;
				break;
			case PUBLIC:
				lexer.nextToken();
				qualifier = Symbol.PUBLIC;
				break;
			default:
				signalError.showError("private, or public expected");
				qualifier = Symbol.PUBLIC;
			}
			Type t = type();
			if ( lexer.token != Symbol.IDENT )
                        {
                            
                            signalError.showError("Identifier expected");
                        }
			String name = lexer.getStringValue();
			lexer.nextToken();
			if ( lexer.token == Symbol.LEFTPAR ){
                                if (name.equals("run")) flag = true;
				methodDec(t, name, qualifier);
                        }
			else if ( qualifier != Symbol.PRIVATE )
				signalError.showError("Attempt to declare a public instance variable");
			else
				instanceVarDec(t, name);   
                
		}
                
                if (!flag && this.currentClass.getCname().equals("Program")){
                    this.signalError.showError("Method 'run' was not found in class 'Program'");
                }
                
		if ( lexer.token != Symbol.RIGHTCURBRACKET )
			signalError.showError("public/private or \"}\" expected");
		lexer.nextToken();
                
                
                return this.currentClass;
                
	}

	private void instanceVarDec(Type type, String name) {
		// InstVarDec ::= [ "static" ] "private" Type IdList ";"
                
                this.currentClass.addInstanceVariable(new InstanceVariable(name, type));
                
                 if (this.symbolTable.getInLocal(name) != null){
                    this.signalError.showError("Variable '" + name 
                                               + "' is being redefined");
                }
                    
                InstanceVariable iv = new InstanceVariable(name, type);

                this.symbolTable.putInLocal(iv.getName(), iv);
                
 
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
                        {
                            
                            signalError.showError("Identifier expected");
                        }
			String variableName = lexer.getStringValue();
                        
                        InstanceVariable ivr = new InstanceVariable(variableName, type);
                        
                        this.currentClass.addInstanceVariable(ivr);
                        this.symbolTable.getInGlobal(this.currentClass.getCname()).addInstanceVariable(ivr);
                        
                        if (this.symbolTable.getInLocal(variableName) != null){
                            this.signalError.showError("Variable '" + variableName
                                               + "' is being redefined");
                        }
                        
                        this.symbolTable.putInLocal(ivr.getName(), ivr);
                        
			lexer.nextToken();
		}
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
                
		lexer.nextToken();
                
	}

	private void methodDec(Type type, String name, Symbol qualifier) {
		/*
		 * MethodDec ::= Qualifier Return Id "("[ FormalParamDec ] ")" "{"
		 *                StatementList "}"
		 */
                
                ParamList pList;
                
                MethodDec mred;
                
                this.currentMethod = new MethodDec(name, type, qualifier);
                
                Variable varMethodCurrent = new Variable (name, type);
                
                
                if (this.symbolTable.getInLocal(varMethodCurrent.getName()) != null){
                    this.signalError.showError("Method '" + this.currentMethod.getName() 
                                               + "' is being redefined");
                }
                
                if (name.equals("run") && qualifier == Symbol.PRIVATE){
                    this.signalError.showError("Method '" + this.currentMethod.getName() 
                                               + "' of class '" 
                                               + this.currentClass.getName()
                                               + "' cannot be private");
                }
                
                if (this.currentClass.getSuperClass() != null){
                    mred = this.currentClass.searchSuperMethod(name);
                    
                    if (mred != null && mred.getReturnType() != type){
                        this.signalError.showError("Method '" + name + "' of subclass '" + this.currentClass.getCname() + "' has a signature different from method inherited from superclass '" + this.currentClass.getSuperClass().getCname() + "'");
                    }
                }
                varMethodCurrent.setMethod(true);
                
                this.symbolTable.putInLocal(varMethodCurrent.getName(), varMethodCurrent);
               
                
                
		lexer.nextToken();
		if ( lexer.token != Symbol.RIGHTPAR ){
                    pList = formalParamDec();
                    this.currentMethod.setParamList(pList);
                    
                    if (name.equals("run") && pList.getSize() > 0 && this.currentClass.getCname().equals("Program")){
                        this.signalError.showError(" Method 'run' of class 'Program' cannot take parameters");
                    }
                    
                    if (this.currentClass.getSuperClass() != null){
                        mred = this.currentClass.searchSuperMethod(name);
                        
                        if (mred != null){
                            for (int i = 0; i < pList.getSize(); i++){
                                if (mred.getParamList() == null || mred.getParamList().getArray().get(i) == null ||
                                    mred.getParamList().getArray().get(i).getType() != pList.getArray().get(i).getType())
                                    this.signalError.showError("Method '" + name +"' is being redefined in subclass '" 
                                                                + this.currentClass.getCname() 
                                                                + "' with a signature different from the method of superclass '" 
                                                                + this.currentClass.getSuperClass().getCname() + "'");
                            }
                        }
                    }
                    
                    
                    
                }
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
                
                this.currentClass.addMethod(this.currentMethod);
                
                //this.symbolTable.getInGlobal(this.currentClass.getCname()).addMethod(this.currentMethod);
                
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTCURBRACKET ) signalError.showError("{ expected");
                
		lexer.nextToken();
                
		this.currentMethod.setStlist(statementList());
                
		if ( lexer.token != Symbol.RIGHTCURBRACKET ) signalError.showError("} expected");

		lexer.nextToken();

                this.currentMethod = null;
                
	}

private ArrayList<VariableExpr> localDec() {
		// LocalDec ::= Type IdList ";"
                
		Type type = type();
                
                ArrayList<VariableExpr> vexprList = new ArrayList();
                
		if ( lexer.token != Symbol.IDENT ) 
                {
                    
                    signalError.showError("Identifier expected");
                }
                
                Variable v = new Variable(lexer.getStringValue(), type);
                VariableExpr ve = new VariableExpr(v);
                v.setMethodName(this.currentMethod.getName());
                
                vexprList.add(new VariableExpr(v));
                
                if (this.symbolTable.getInLocal(v.getName()) != null 
                    && !this.symbolTable.getInLocal(v.getName()).isMethod()
                    && !(this.symbolTable.getInLocal(v.getName()) instanceof InstanceVariable)){
                    
                    if(this.currentMethod.getName().equals(this.symbolTable.getInLocal(v.getName()).getMethodName()))
                        this.signalError.showError("Variable '"
                                               + v.getName()
                                               + "' is being redeclared");
                }
                
                this.symbolTable.putInLocal(v.getName(), v);
                
		lexer.nextToken();
                
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			v = new Variable(lexer.getStringValue(), type);
                        vexprList.add(new VariableExpr(v));
                        this.symbolTable.putInLocal(v.getName(), v);
			lexer.nextToken();
                        
                }
                
                if (lexer.token != Symbol.SEMICOLON) 
                    this.signalError.showError("Missing ';'", true);
                
                return vexprList;
	}

	private ParamList formalParamDec() {
		// FormalParamDec ::= ParamDec { "," ParamDec }
                ParamList pList = new ParamList();
		pList.addElement(paramDec());
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			pList.addElement(paramDec());
		}
                
                return pList;
	}

	private Parameter paramDec() {
		// ParamDec ::= Type Id

		Type t = type();
		if ( lexer.token != Symbol.IDENT ) signalError.showError("Identifier expected");
                
                Parameter p = new Parameter(lexer.getStringValue(), t);
                VariableExpr ve = new VariableExpr(p);
                p.setMethodName(this.currentMethod.getName());
                this.symbolTable.putInLocal(p.getName(), p);
		lexer.nextToken();
                
                return p;
	}

	private Type type() {
		// Type ::= BasicType | Id
		Type result;

		switch (lexer.token) {
		case VOID:
			result = Type.voidType;
			break;
		case INT:
			result = Type.intType;
			break;
		case BOOLEAN:
			result = Type.booleanType;
			break;
                case NULL:
                        result = Type.nullType;
                        break;
		case STRING:
			result = Type.stringType;
			break;
		case IDENT:
			// # corrija: fa�a uma busca na TS para buscar a classe
			// IDENT deve ser uma classe.
                        
                        KraClass kra = this.symbolTable.getInGlobal(lexer.getStringValue());
                    
			result = kra;
			break;
		default:
			signalError.showError("Type expected");
			result = Type.undefinedType;
		}
		lexer.nextToken();
		return result;
	}

	private CompositeStatement compositeStatement() {
                
                StatementList stl;
            
		lexer.nextToken();
		stl = statementList();
		if ( lexer.token != Symbol.RIGHTCURBRACKET )
			signalError.showError("} expected");
		else
			lexer.nextToken();
                
                return new CompositeStatement(stl);
	}

	private StatementList statementList() {
                
                StatementList stl = new StatementList();
                Statement st = null;
                Boolean hasReturn = false;
		// CompStatement ::= "{" { Statement } "}"
		Symbol tk;
		// statements always begin with an identifier, if, read, write, ...
                
		while ((tk = lexer.token) != Symbol.RIGHTCURBRACKET
				&& tk != Symbol.ELSE){
                        
			st = statement();
                        
                        stl.addElement(st);
                }

                for(Statement stat : stl.getStmtList())
                {
                    if(stat instanceof IfStatement || stat instanceof ReturnStatement)
                        hasReturn = true;
                }
                
                if ( !(this.currentMethod.getReturnType() instanceof TypeVoid) 
                         && !hasReturn){
                            this.signalError.showError("Missing 'return' statement in method '"
                                                       + this.currentMethod.getName() + "'", true);
                    }
                return stl;
	}

	private Statement statement() {
		/*
		 * Statement ::= Assignment ``;'' | IfStat |WhileStat | MessageSend
		 *                ``;'' | ReturnStat ``;'' | ReadStat ``;'' | WriteStat ``;'' |
		 *               ``break'' ``;'' | ``;'' | CompStatement | LocalDec
		 */
                
		switch (lexer.token) {
		case THIS:
		case IDENT:
                    
		case SUPER:
		case INT:
		case BOOLEAN:
		case STRING:

			return assignExprLocalDec();

		case ASSERT:
			return assertStatement();
		case RETURN:
			return returnStatement();
		case READ:
			return readStatement();
		case WRITE:
			return writeStatement();
		case WRITELN:
			return writelnStatement();
		case IF:
			return ifStatement();
		case BREAK:
			return breakStatement();
		case WHILE:
			return whileStatement();
		case SEMICOLON:
			return nullStatement();
		case LEFTCURBRACKET:
			return compositeStatement();
		default:
			signalError.showError("Statement expected");
		}
                
                return null;
	}

	private Statement assertStatement() {
		lexer.nextToken();
		int lineNumber = lexer.getLineNumber();
		Expr e = expr();
		if ( e.getType() != Type.booleanType )
			signalError.showError("boolean expression expected");
		if ( lexer.token != Symbol.COMMA ) {
			this.signalError.showError("',' expected after the expression of the 'assert' statement");
		}
		lexer.nextToken();
		if ( lexer.token != Symbol.LITERALSTRING ) {
			this.signalError.showError("A literal string expected after the ',' of the 'assert' statement");
		}
		String message = lexer.getLiteralStringValue();
		lexer.nextToken();
		if ( lexer.token == Symbol.SEMICOLON )
			lexer.nextToken();
		
		return new StatementAssert(e, lineNumber, message);
	}

	/*
	 * retorne true se 'name' � uma classe declarada anteriormente. � necess�rio
	 * fazer uma busca na tabela de s�mbolos para isto.
	 */
	private boolean isType(String name) {
		return this.symbolTable.getInGlobal(name) != null;
	}
        
	/*
	 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ] | LocalDec
	 */
private Statement assignExprLocalDec() {
                
                ArrayList<VariableExpr> vExprList = new ArrayList();
                Expr leftSide = null;
                Expr rightSide = null;
		if ( lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN
				|| lexer.token == Symbol.STRING ||
				// token � uma classe declarada textualmente antes desta
				// instru��o
				(lexer.token == Symbol.IDENT && isType(lexer.getStringValue()) 
                                    && this.symbolTable.getInLocal(lexer.getStringValue()) == null) ) {
			/*
			 * uma declara��o de vari�vel. 'lexer.token' � o tipo da vari�vel
			 * 
			 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ] | LocalDec 
			 * LocalDec ::= Type IdList ``;''
			 */
                        
			vExprList = localDec();
                        return new AssignExprStatement(vExprList, rightSide, true);
		}
		else {
			/*
			 * AssignExprLocalDec ::= Expression [ ``$=$'' Expression ]
			 */
                        
			leftSide = expr();
    
                        if(leftSide instanceof ObjectCallExpr)
                        {
                            
                            MethodDec md;
                            md = ((ObjectCallExpr) leftSide).getMethod();                            
                            
                            if( md != null)
                            {
                                if(!(md.getReturnType() instanceof TypeVoid))
                                {
                                    this.signalError.showError("Message send returns a value that is not used");
                                }
                            }
                        }
                        
			if ( lexer.token == Symbol.ASSIGN ) {
                                
				lexer.nextToken();
				rightSide = expr();
                                
                                //System.out.println( instanceof TypeVoid);
                                
                                if(rightSide instanceof ObjectCallExpr)
                                {
                                    Type type;
                                    type = ((ObjectCallExpr) rightSide).getMethod().getReturnType();  
                                    
                                    if(type instanceof TypeVoid)
                                    {
                                        this.signalError.showError("Expression expected in the right-hand side of assignment");
                                    }
                                }
                                if (rightSide instanceof ThisExpr){
                                    ThisExpr t = (ThisExpr) rightSide;
                                    if (t.getVar() == null && t.getMethod() == null && !(this.currentClass.isCompatible(leftSide.getType()))){
                                        this.signalError.showError("Wrong type in the right-hand side of the expression");
                                    }
                                }

                                else if (!(rightSide.getType().isCompatible(leftSide.getType()))){
                                    if(!((leftSide.getType() instanceof KraClass) && (rightSide.getType() instanceof TypeNull)))
                                    {
                                        this.signalError.showError("Wrong type in the right-hand side of the expression");                                        
                                    }
                                }
                                
                                
				if ( lexer.token != Symbol.SEMICOLON )
					signalError.showError("';' expected", true);
				else
					lexer.nextToken();
			}
                        return new AssignExprStatement(leftSide, rightSide, false);
		}
	}

	private ExprList realParameters() {
		ExprList anExprList = null;

		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		if ( startExpr(lexer.token) ) anExprList = exprList();
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		return anExprList;
	}

	private WhileStatement whileStatement() {
                
                Boolean firstWhile = false;
                
                if(!this.currentWhileStatement)
                {
                    firstWhile = true;
                    this.currentWhileStatement = true;
                }
                
                
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		Expr e = expr();
                if ( e.getType() != Type.booleanType){
                    signalError.showError("boolean expression expected");
                }
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		Statement s = statement();
                
                if(firstWhile)
                    this.currentWhileStatement = false;
                return new WhileStatement(e, s);
	}

	private IfStatement ifStatement() {

                Statement st;
                Statement elsest = null;
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
                Expr e = expr();
                if ( e.getType() != Type.booleanType){
                    signalError.showError("boolean expression expected");
                }
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		st = statement();
		if ( lexer.token == Symbol.ELSE ) {
			lexer.nextToken();
			elsest = statement();
		}
                
                CompositeStatement compStmt;
                
                if(st instanceof CompositeStatement)
                {
                    compStmt = (CompositeStatement) st;
                    
                    int len = compStmt.getSt().getStmtList().size();
                    Statement lastStmt = null;
                    
                    if(len > 0)
                        lastStmt = compStmt.getSt().getStmtList().get(len-1);
                    
                    if ( !(this.currentMethod.getReturnType() instanceof TypeVoid) 
                         && !(lastStmt instanceof ReturnStatement)){
                            this.signalError.showError("Missing 'return' statement in method '"
                                                       + this.currentMethod.getName() + "'");
                    }
                }
                               
                return new IfStatement(e, st, elsest);
	}

	private ReturnStatement returnStatement() {

		lexer.nextToken();
		Expr e = expr();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
                
                if ( this.currentMethod.getReturnType() == Type.voidType){
                    this.signalError.showError("This method cannot return a value", true);
                }
                
                if (! e.getType().isCompatible(this.currentMethod.getReturnType())){
                    this.signalError.showError("Method return type is not compatible with the expression", true);
                }
                
                
                
                return new ReturnStatement (e);
	}

	private ReadStatement readStatement() {
            
                ReadStatement rs = new ReadStatement();
            
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		while (true) {
			if ( lexer.token == Symbol.THIS ) {
				lexer.nextToken();
				if ( lexer.token != Symbol.DOT ) signalError.showError(". expected");
				lexer.nextToken();
			}
			if ( lexer.token != Symbol.IDENT )
				signalError.show(ErrorSignaller.ident_expected);

			String name = lexer.getStringValue();
                        
                        Type type = this.symbolTable.getInLocal(name).getType();
                        
                        if(type == null)
                            this.signalError.showError("Variable '" + name + "' was not declared");
                        else if(type instanceof TypeBoolean)
                            this.signalError.showError("Command 'read' does not accept 'boolean' variables");
                        
                        rs.addNameList(name);
                        
			lexer.nextToken();
			if ( lexer.token == Symbol.COMMA )
				lexer.nextToken();
			else
				break;
		}

		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
                
                return rs;
	}

	private WriteStatement writeStatement() {

                ExprList ex = new ExprList();
                int i = 0;
                
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError(" '(' expected");
		lexer.nextToken();
		ex = exprList();
                
                while(i < ex.getExprList().size())
                {
                    
                    if(ex.getExprList().get(i).getType() instanceof TypeBoolean)
                    {
                        this.signalError.showError("Command 'write' does not accept 'boolean' variables");
                    }
                    else if (ex.getExprList().get(i).getType() instanceof KraClass){
                        
                        if(ex.getExprList().get(i) instanceof ThisExpr)
                        {
                            ThisExpr te = (ThisExpr ) ex.getExprList().get(i);
                            
                            if(te.getMethod() == null && te.getVar() == null)
                                this.signalError.showError("Command 'write' does not accept objects");
                        }
                        else
                            this.signalError.showError("Command 'write' does not accept objects");
                    }
                    
                    i++;
                }
                
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
                
                return new WriteStatement(ex);
	}

	private WritelnStatement writelnStatement() {

                ExprList ex;
                int i = 0;
                
		lexer.nextToken();
		if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
		lexer.nextToken();
		ex = exprList();
       
                while(i < ex.getExprList().size())
                {
                    if(ex.getExprList().get(i).getType() instanceof TypeBoolean)
                    {
                        this.signalError.showError("Command 'write' does not accept 'boolean' variables");
                    }
                    else if (ex.getExprList().get(i).getType() instanceof KraClass){
                        this.signalError.showError("Command 'write' does not accept objects");
                    }
                    
                    i++;
                }
                
		if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
                
                return new WritelnStatement(ex);
	}

            private BreakStatement breakStatement() {
                
                if(!this.currentWhileStatement)
                    this.signalError.showError("Break statement found outside a 'while' statement");
                
		lexer.nextToken();
		if ( lexer.token != Symbol.SEMICOLON )
			signalError.show(ErrorSignaller.semicolon_expected);
		lexer.nextToken();
                
                return new BreakStatement();
	}

	private NullStatement nullStatement() {
		lexer.nextToken();
                
                return new NullStatement();
	}

	private ExprList exprList() {
            
                // ExpressionList ::= Expression { "," Expression }

		ExprList anExprList = new ExprList();
		anExprList.addElement(expr());
		while (lexer.token == Symbol.COMMA) {
			lexer.nextToken();
			anExprList.addElement(expr());
		}
		return anExprList;
	}

	private Expr expr() {
                
		Expr left = simpleExpr();
		Symbol op = lexer.token;
		if ( op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE
				|| op == Symbol.LT || op == Symbol.GE || op == Symbol.GT ) {
			lexer.nextToken();
			Expr right = simpleExpr();                        
                        if ((!(right.getType().isCompatible(left.getType())) && !(left.getType().isCompatible(right.getType())) && (op == Symbol.NEQ || op == Symbol.EQ))){
                            
                            if(!((left.getType() instanceof TypeNull) || (right.getType() instanceof TypeNull)))
                            {
                                this.signalError.showError("Incompatible types cannot be compared with '!=' because the result will always be 'false'");
                            }
                        }
                        
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr simpleExpr() {
		Symbol op;

		Expr left = term();
		while ((op = lexer.token) == Symbol.MINUS || op == Symbol.PLUS
				|| op == Symbol.OR) {
			lexer.nextToken();
                        
                        if(left.getType() instanceof TypeBoolean)
                        {
                            if(op == Symbol.MINUS)
                                this.signalError.showError("type boolean does not support operation '-'");
                            
                            if(op == Symbol.PLUS)
                                this.signalError.showError("type boolean does not support operation '+'");
                        }
			Expr right = term();
                        
                        if(left.getType() instanceof TypeInt)
                        {
                            if(!(right.getType() instanceof TypeInt))
                            {
                                this.signalError.showError("Operator '" + op.name() + "' of 'int' expects a 'int' value");
                            }
                        }
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr term() {
		Symbol op;

		Expr left = signalFactor();
		while ((op = lexer.token) == Symbol.DIV || op == Symbol.MULT
				|| op == Symbol.AND) {
                    
                        if(left.getType() instanceof TypeBoolean)
                        {
                            if(op == Symbol.DIV)
                                this.signalError.showError("type boolean does not support operation '/'");
                            
                            if(op == Symbol.MULT)
                                this.signalError.showError("type boolean does not support operation '*'");
                        }
                                            
			lexer.nextToken();
			Expr right = signalFactor();
			left = new CompositeExpr(left, op, right);
		}
		return left;
	}

	private Expr signalFactor() {
                
		Symbol op;
		if ( (op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS ) {
			lexer.nextToken();
			return new SignalExpr(op, factor());
		}
		else
			return factor();
	}

	/*
	 * Factor ::= BasicValue | "(" Expression ")" | "!" Factor | "null" |
	 *      ObjectCreation | PrimaryExpr
	 * 
	 * BasicValue ::= IntValue | BooleanValue | StringValue 
	 * BooleanValue ::=  "true" | "false" 
	 * ObjectCreation ::= "new" Id "(" ")" 
	 * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
	 *                 Id  |
	 *                 Id "." Id | 
	 *                 Id "." Id "(" [ ExpressionList ] ")" |
	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
	 *                 "this" | 
	 *                 "this" "." Id | 
	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
	 */
	private Expr factor() {
		Expr anExpr;
		ExprList exprList;
		String messageName, id;
              
                
		switch (lexer.token) {
		// IntValue
		case LITERALINT:
			return literalInt();
			// BooleanValue
		case FALSE:
			lexer.nextToken();
			return LiteralBoolean.False;
			// BooleanValue
		case TRUE:
			lexer.nextToken();
			return LiteralBoolean.True;
			// StringValue
		case LITERALSTRING:
			String literalString = lexer.getLiteralStringValue();
			lexer.nextToken();
			return new LiteralString(literalString);
			// "(" Expression ")" |
		case LEFTPAR:
			lexer.nextToken();
			anExpr = expr();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
			lexer.nextToken();
			return new ParenthesisExpr(anExpr);

			// "null"
		case NULL:
			lexer.nextToken();
			return new NullExpr();
			// "!" Factor
		case NOT:
			lexer.nextToken();
			anExpr = expr();
			return new UnaryExpr(anExpr, Symbol.NOT);
			// ObjectCreation ::= "new" Id "(" ")"
		case NEW:
                                                
			lexer.nextToken();
                        
                        
			if ( lexer.token != Symbol.IDENT )
                        {

                            signalError.showError("Identifier expected");
                        }

			String className = lexer.getStringValue();
                        
                        KraClass kclass = this.symbolTable.getInGlobal(className);
                        
                        if(kclass == null)
                        {
                            signalError.showError("Class " + className + " does not exist");
                        }
			/*
			 * // encontre a classe className in symbol table KraClass 
			 *      aClass = symbolTable.getInGlobal(className); 
			 *      if ( aClass == null ) ...
			 */

			lexer.nextToken();
			if ( lexer.token != Symbol.LEFTPAR ) signalError.showError("( expected");
			lexer.nextToken();
			if ( lexer.token != Symbol.RIGHTPAR ) signalError.showError(") expected");
			lexer.nextToken();
			/*
			 * return an object representing the creation of an object
			 */
			return new ObjectDecExpr(kclass);
			/*
          	 * PrimaryExpr ::= "super" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 Id  |
          	 *                 Id "." Id | 
          	 *                 Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 "this" | 
          	 *                 "this" "." Id | 
          	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
			 */
		case SUPER:
			// "super" "." Id "(" [ ExpressionList ] ")"
                        MethodDec mred2;
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				signalError.showError("'.' expected");
			}
			else
				lexer.nextToken();
			if ( lexer.token != Symbol.IDENT )
				signalError.showError("Identifier expected");
			messageName = lexer.getStringValue();
			/*
			 * para fazer as confer�ncias sem�nticas, procure por 'messageName'
			 * na superclasse/superclasse da superclasse etc
			 */
                        
                        if (this.currentClass.getSuperClass() == null){
                            signalError.showError("'super' used in class 'Program' that does not have a superclass");
                        }
                        
                        if (this.currentClass.searchSuperMethod(messageName) == null){
                            signalError.showError("Method does not exist in superclass");
                        }
                        
                        else if (this.currentClass.searchSuperMethod(messageName).getQualifier() == Symbol.PRIVATE){
                            signalError.showError("Method '" + messageName + "' was not found in the public interface of '" + this.currentClass.getCname() + "' or its superclasses");
                        }
                        
                        mred2 = this.currentClass.searchSuperMethod(messageName);
                        
			lexer.nextToken();
			exprList = realParameters();
    
                        return new SuperExpr(mred2, this.currentClass, exprList);
		case IDENT:
			/*
          	 * PrimaryExpr ::=  
          	 *                 Id  |
          	 *                 Id "." Id | 
          	 *                 Id "." Id "(" [ ExpressionList ] ")" |
          	 *                 Id "." Id "." Id "(" [ ExpressionList ] ")" |
			 */
                        
			String firstId = lexer.getStringValue();
                        
			lexer.nextToken();
                        
			if ( lexer.token != Symbol.DOT ) {
				// Id
				// retorne um objeto da ASA que representa um identificador
                                
                                Variable ident = this.symbolTable.getInLocal(firstId);
                                
                                
                                
                                
                                VariableExpr ve = new VariableExpr(ident);
                                if(ident == null)
                                {
                                    signalError.showError("Identifier " + firstId + " was not declared.");
                                }
                                
                                if ((this.symbolTable.getInLocal(firstId).getClass() == InstanceVariable.class)){
                                    signalError.showError("Identifier '"
                                                          + firstId
                                                          + "' was not found");
                                }
                                
				return ve;
			}
			else { // Id "."
				lexer.nextToken(); // coma o "."
				if ( lexer.token != Symbol.IDENT ) {
					signalError.showError("Identifier expected");
				}
				else {
					// Id "." Id
					lexer.nextToken();
					id = lexer.getStringValue();
					if ( lexer.token == Symbol.DOT ) {
						this.signalError.showError("Static operation current not supported");
					}
					else if ( lexer.token == Symbol.LEFTPAR ) {
						// Id "." Id "(" [ ExpressionList ] ")"
                                                
                                                Variable var = this.symbolTable.getInLocal(firstId);
                                                VariableExpr ve = new VariableExpr(var);
                                                
                                                if(var == null)
                                                {
                                                    this.signalError.showError("Identifier '" + firstId + "' was not declared");
                                                }
                                                Type varType = var.getType();
                                                
                                                if(!(varType instanceof KraClass))
                                                {
                                                    this.signalError.showError("Attempt to call a method on a variable of a basic type.");
                                                }
                                                
                                                
                                                KraClass varClass  = this.symbolTable.getInGlobal(varType.getName());
                                                
                                                MethodDec amethod = varClass.searchPublicMethod(id);
                                                
                                                
                                                if (amethod == null){
                                         
                                                    amethod = varClass.searchSuperMethod(id);
                                                }
                                                
                                                if (amethod == null){
                                                    this.signalError.showError("Method '" + id 
                                                                               + "' is not a public method of '" 
                                                                               + varClass.getName() 
                                                                               + "' which is the type of '"
                                                                               + firstId + "'");
                                                }
                                                
                                                if (! varClass.getCname().equals(this.currentClass.getCname()) && amethod.getQualifier() == Symbol.PRIVATE){
                                                    this.signalError.showError("Method '" + amethod.getName() + "' was not found in the public interface of '" + varClass.getCname() + "' or its superclasses");
                                                }
                                                
                                                  
						exprList = this.realParameters();
                                                
                                                ObjectCallExpr obj = new ObjectCallExpr(var, amethod, exprList);
                                                
                                                return obj;
						/*
						 * para fazer as confer�ncias sem�nticas, procure por
						 * m�todo 'ident' na classe de 'firstId'
						 */
					}
					else {
                                                Variable var = this.symbolTable.getInLocal(firstId);
                                                Variable var2 = this.symbolTable.getInLocal(id);
                                                
                                                if(var == null)
                                                {
                                                    this.signalError.showError("Identifier '" + firstId + "' was not declared");
                                                }
                          
                                                if(var2 == null)
                                                {
                                                    this.signalError.showError("Identifier '" + id + "' was not declared");
                                                }
                                            
						return new ObjectCallExpr(var, var2, this.currentClass.getCname());
					}
				}
			}
			
		case THIS:
			/*
			 * Este 'case THIS:' trata os seguintes casos: 
          	 * PrimaryExpr ::= 
          	 *                 "this" | 
          	 *                 "this" "." Id | 
          	 *                 "this" "." Id "(" [ ExpressionList ] ")"  | 
          	 *                 "this" "." Id "." Id "(" [ ExpressionList ] ")"
			 */
			lexer.nextToken();
			if ( lexer.token != Symbol.DOT ) {
				// only 'this'
				// retorne um objeto da ASA que representa 'this'
				// confira se n�o estamos em um m�todo est�tico
				return new ThisExpr(this.currentClass);
			}
			else {
				lexer.nextToken();
				if ( lexer.token != Symbol.IDENT )
                                {

                                    signalError.showError("Identifier expected");
                                }
				id = lexer.getStringValue();
				lexer.nextToken();
				// j� analisou "this" "." Id
                                Variable var = this.symbolTable.getInLocal(id);
                                VariableExpr varExpr = new VariableExpr(var);
                                MethodDec mred = null;
                                
                                
				if ( lexer.token == Symbol.LEFTPAR ) {
					// "this" "." Id "(" [ ExpressionList ] ")"
					/*
					 * Confira se a classe corrente possui um m�todo cujo nome �
					 * 'ident' e que pode tomar os par�metros de ExpressionList
					 */
                                        
                                        if(this.currentClass.searchPublicMethod(id) == null
                                           && this.currentClass.searchSuperMethod(id) == null)
                                        {
                                            this.signalError.showError("Method '" + id + "' was not found in class '" + this.currentClass.getCname() + "' or its superclasses");
                                        }
                           
					exprList = this.realParameters();
                                        
                                        if (this.currentClass.searchPublicMethod(id) != null) mred = this.currentClass.searchPublicMethod(id);
                                        else mred = this.currentClass.searchSuperMethod(id);
                                        
                                        if (exprList != null){
                                            for (int i = 0; i < exprList.getSize(); i++){
                                                
                                                KraClass kr;
                                                
                                                if (mred.getParamList() == null || mred.getParamList().getArray().get(i) == null ||
                                                    ! exprList.getExprList().get(i).getType().isCompatible(mred.getParamList().getArray().get(i).getType()))
                                                {
                                                    this.signalError.showError("Type error: the type of the real parameter is not subclass of the type of the formal parameter");
                                                }
                                            }
                                            
                                        }
                                        
                                        return new ThisExpr(mred, this.currentClass, exprList);
				}
				else if ( lexer.token == Symbol.DOT ) {
					// "this" "." Id "." Id "(" [ ExpressionList ] ")"
					lexer.nextToken();
					if ( lexer.token != Symbol.IDENT )
                                        {
                                            
                                            signalError.showError("Identifier expected");
                                        }
					lexer.nextToken();
					exprList = this.realParameters();
                                        
                                        return new ThisExpr(mred,this.currentClass, varExpr, exprList);
				}
				else {
					// retorne o objeto da ASA que representa "this" "." Id
					/*
					 * confira se a classe corrente realmente possui uma
					 * vari�vel de inst�ncia 'ident'
					 */
                                        
					return new ThisExpr(varExpr, this.currentClass);
				}
			}
			
		default:
			this.signalError.showError("Expression expected");
		}
                return new NullExpr();
	}

	private LiteralInt literalInt() {

		LiteralInt e = null;

		// the number value is stored in lexer.getToken().value as an object of
		// Integer.
		// Method intValue returns that value as an value of type int.
		int value = lexer.getNumberValue();
		lexer.nextToken();
		return new LiteralInt(value);
	}

	private static boolean startExpr(Symbol token) {

		return token == Symbol.FALSE || token == Symbol.TRUE
				|| token == Symbol.NOT || token == Symbol.THIS
				|| token == Symbol.LITERALINT || token == Symbol.SUPER
				|| token == Symbol.LEFTPAR || token == Symbol.NULL
				|| token == Symbol.IDENT || token == Symbol.LITERALSTRING;

	}

	private SymbolTable		symbolTable;
	private Lexer			lexer;
	private ErrorSignaller	signalError;
        private MethodDec currentMethod;
        private KraClass currentClass;
        private boolean currentWhileStatement = false;

}
