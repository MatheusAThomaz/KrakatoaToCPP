/*
*   Caio Henrique Giacomelli - 620297
*   Matheus Augusto Thomaz   - 620297
*/


 package ast;

public class VariableExpr extends Expr {
    
    public VariableExpr( Variable v ) {
        this.v = v;
    }

    public Variable getV() {
        return v;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print( v.getName() );
    }
    
    @Override
    public void genKra(PW pw){
        pw.print(v.getName());
    }
    
    
    @Override
    public void genKraIdented(PW pw, boolean isString){
        if(isString)
            pw.print("*" + v.getName());
        else
            pw.printIdent(v.getName());
    }
    
    public Type getType() {
        return v.getType();
    }
    
    private Variable v;

}