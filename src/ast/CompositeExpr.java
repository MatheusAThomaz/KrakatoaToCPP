/*
*   Caio Henrique Giacomelli - 620297
*   Matheus Augusto Thomaz   - 620297
*/


 package ast;

import lexer.*;
import java.util.HashMap;


public class CompositeExpr extends Expr {

    public CompositeExpr( Expr pleft, Symbol poper, Expr pright ) {
        left = pleft;
        oper = poper;
        right = pright;
    }
    
    @Override
	public void genC( PW pw, boolean putParenthesis ) {
        if ( putParenthesis )
          pw.print("(");
        left.genC(pw, true);
        String strSymbol = arrayOper.get(oper);
        if ( strSymbol == null ) {
        	pw.println("internal error in CompositeExpr::genC");
        }
        else
            pw.print(" " + strSymbol + " ");
        right.genC(pw, true);
        if ( putParenthesis )
          pw.print(")");
    }
        
        
    @Override
    public void genKra(PW pw){
       
        if (left.getType().getName().equals("string") && !right.getType().getName().equals("null"))
            left.genKraIdented(pw, true);
        else
            left.genKra(pw);
        
        String strSymbol = arrayOper.get(oper);
        if (strSymbol != null){
            pw.print(" " + strSymbol + " ");
        }
        if (right.getType().getName().equals("string") && !left.getType().getName().equals("null"))
            right.genKraIdented(pw, true);
        else
            right.genKra(pw);
        
    }
    
    @Override
    public void genKraIdented(PW pw,boolean isObj){
        left.genKra(pw);
        
        String strSymbol = arrayOper.get(oper);
        if (strSymbol != null){
            pw.print(" " + strSymbol + " ");
        }
        
        right.genKra(pw);
        
    }

    
    
    
    
    
    
    
    
    
    
    
    @Override
	public Type getType() {
          // left and right must be the same type
       if ( oper == Symbol.EQ || oper == Symbol.NEQ || oper == Symbol.LE || oper == Symbol.LT ||
            oper == Symbol.GE || oper == Symbol.GT )
            return Type.booleanType;
       else if ( oper == Symbol.AND || oper == Symbol.OR )
            return Type.booleanType;
       else if ( oper == Symbol.DOT)
       {
           return right.getType();
       }
       else
            return Type.intType;
    }

    private Expr left, right;
    private Symbol oper;
    private static HashMap<Symbol, String> arrayOper;
    static {
        arrayOper = new HashMap<Symbol, String>();
        arrayOper.put(Symbol.PLUS, "+");
        arrayOper.put(Symbol.MINUS, "-");
        arrayOper.put(Symbol.MULT, "*");
        arrayOper.put(Symbol.DIV, "/");
        arrayOper.put(Symbol.LT, "<");
        arrayOper.put(Symbol.LE, "<=");
        arrayOper.put(Symbol.GT, ">");
        arrayOper.put(Symbol.GE, ">=");
        arrayOper.put(Symbol.NEQ, "!=");
        arrayOper.put(Symbol.EQ, "==");
        arrayOper.put(Symbol.ASSIGN, "=");
        arrayOper.put(Symbol.AND, "&&");
        arrayOper.put(Symbol.OR, "||");
    }
}
