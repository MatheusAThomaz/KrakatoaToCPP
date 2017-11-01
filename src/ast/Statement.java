/*
*   Caio Henrique Giacomelli - 620297
*   Matheus Augusto Thomaz   - 620297
*/


 package ast;

abstract public class Statement {

	abstract public void genC(PW pw);
        
        abstract public void genKra(PW pw);

}
