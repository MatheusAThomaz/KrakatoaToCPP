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
    public void genKraIdented(PW pw){
        pw.printIdent(v.getName());
    }
    
    public Type getType() {
        return v.getType();
    }
    
    private Variable v;

}