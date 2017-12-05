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

import java.util.ArrayList;

/**
 *
 * @author matos
 */
public class ReadStatement extends Statement{
    
    private ArrayList<String> nameList;
    
    public ReadStatement(){
        nameList = new ArrayList();
    }
    
    public void addNameList(String name){
        nameList.add(name);
    }

    @Override
    public void genC(PW pw) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void genKra(PW pw) {
        pw.printIdent("cin >> ");
        
        pw.print(nameList.get(0));
        for(int i = 1; i < this.nameList.size(); i++)
        {
            pw.print(" >> ");
            pw.print(nameList.get(i));
        }
        pw.println(";");
    }
    
}
