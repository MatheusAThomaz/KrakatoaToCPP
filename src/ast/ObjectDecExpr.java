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
    
    private String className;
    private Type type;

    public String getClassName() {
        return className;
    }
    
    public ObjectDecExpr(String className)
    {
        this.className = className;
        this.type = new KraClass(className);
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
