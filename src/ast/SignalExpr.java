/*
*   Caio Henrique Giacomelli - 620297
*   Matheus Augusto Thomaz   - 620297
*/


 package ast;

import lexer.*;

public class SignalExpr extends Expr {

    public SignalExpr( Symbol oper, Expr expr ) {
       this.oper = oper;
       this.expr = expr;
    }
    
    public void genKra(PW pw)
    {
        pw.print(oper == Symbol.PLUS ? "+" : "-");
        expr.genKra(pw);
    }

    @Override
	public void genC( PW pw, boolean putParenthesis ) {
       if ( putParenthesis )
          pw.print("(");
       pw.print( oper == Symbol.PLUS ? "+" : "-" );
       expr.genC(pw, true);
       if ( putParenthesis )
          pw.print(")");
    }

    @Override
    public Type getType() {
       return expr.getType();
    }

    private Expr expr;
    private Symbol oper;
}
