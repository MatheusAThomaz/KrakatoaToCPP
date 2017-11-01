package ast;

import java.util.*;

public class ParamList {

    public ParamList() {
       paramList = new ArrayList<Variable>();
    }
    
    public void genKra(PW pw)
    {
        paramList.get(0).genKra(pw);
        for(int i = 1; i < this.paramList.size(); i++)
        {
            pw.print(", ");
            paramList.get(i).genKra(pw);
        }
    }

    public void addElement(Variable v) {
       paramList.add(v);
    }
    
    public ArrayList<Variable> getArray(){
        return paramList;
    }

    public Iterator<Variable> elements() {
        return paramList.iterator();
    }

    public int getSize() {
        return paramList.size();
    }

    private ArrayList<Variable> paramList;

}
