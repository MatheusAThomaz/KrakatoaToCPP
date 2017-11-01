package ast;

public class LiteralString extends Expr {
    
    public LiteralString( String literalString ) { 
        this.literalString = literalString;
    }
    
    public void genC( PW pw, boolean putParenthesis ) {
        pw.print(literalString);
    }
    
    public Type getType() {
        return Type.stringType;
    }
    
    @Override
    public void genKra(PW pw){
        char aspas = 34;
        pw.print(aspas + this.literalString + aspas);
    }
    
    @Override
    public void genKraIdented(PW pw){
        char aspas = 34;
        pw.printIdent(aspas + this.literalString + aspas);
    }
    
    private String literalString;
}
