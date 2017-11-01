/*
*   Caio Henrique Giacomelli - 620297
*   Matheus Augusto Thomaz   - 620297
*/


 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

/**
 *
 * @author matos
 */
public class ObjectDecExpr extends Expr {
    
    private KraClass classType;
    private Type type;

    public KraClass getClassName() {
        return classType;
    }
    
    public void genKra(PW pw)
    {
        pw.print("new " + classType.getName() + "()");
    }
    
    public ObjectDecExpr(KraClass classType)
    {
        this.classType = classType;
        this.type = classType;
    }

    @Override
    public void genC(PW pw, boolean putParenthesis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Type getType() {
        return this.type;
    }
    
}
