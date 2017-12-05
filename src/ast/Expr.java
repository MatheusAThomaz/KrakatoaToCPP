/*
*   Caio Henrique Giacomelli - 620297
*   Matheus Augusto Thomaz   - 620297
*/


 package ast;

abstract public class Expr {
    abstract public void genC( PW pw, boolean putParenthesis );
      // new method: the type of the expression
    abstract public Type getType();

    public void genKra(PW pw) {
    }
    
    public void genKraIdented(PW pw, boolean isObj){
        
    }
}