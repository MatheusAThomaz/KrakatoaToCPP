package ast;

public class LiteralInt extends Expr {
    
    public LiteralInt( int value ) { 
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    public void genC( PW pw, boolean putParenthesis ) {
        pw.printIdent("" + value);
    }
    
    public void genKra(PW pw){
        pw.print("" + this.value);
    }
    
    public void genKraIdented(PW pw){
        pw.printIdent("" + this.value);
    }
    
    public Type getType() {
        return Type.intType;
    }
    
    private int value;
}
