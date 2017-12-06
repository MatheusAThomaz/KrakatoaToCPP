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

import lexer.Symbol;

/**
 *
 * @author matos
 */
public class MethodDec {

    private String name;
    private final Type returnType;
    private final Symbol qualifier;
    private ParamList pList;
    private StatementList stlist;

    public StatementList getStlist() {
        return stlist;
    }

    public void setStlist(StatementList stlist) {
        this.stlist = stlist;
    }

    public MethodDec(String name, Type returnType, Symbol qualifier) {
        this.name = name;
        this.returnType = returnType;
        this.qualifier = qualifier;    
    }    

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void genKra(PW pw, boolean sameClassName)
    {
        if(this.getName().equals("run"))
            pw.printIdent("int main(");
        else{
            
            if (this.returnType instanceof KraClass){
                if (sameClassName){
                    this.setName(this.getName() + "2");
                    pw.printIdent(this.returnType.getName() + " *" + this.getName() + "(");
                }
                else
                    pw.printIdent(this.returnType.getName() + " *" + this.getName() + "(");
            }
            else{
                if (sameClassName){
                    this.setName(this.getName() + "2");
                    pw.printIdent(this.returnType.getName() + " " + this.getName() + "(");
                }            
                else
                    pw.printIdent(this.returnType.getName() + " " + this.getName() + "(");
            }
        }
        
        if(pList != null)
            pList.genKra(pw);
        pw.print(") {");
        pw.println();
        pw.add();
        
        if (stlist != null)
            stlist.genKra(pw);
        
        pw.sub();
        pw.printIdent("}");
        pw.println();
    }
    
    public void setParamList(ParamList p){
        this.pList = p;
    }
    
    public ParamList getParamList() {
        return pList;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Symbol getQualifier() {
        return qualifier;
    }
    
}
