/*
*   Caio Henrique Giacomelli - 620297
*   Matheus Augusto Thomaz   - 620297
*/


 package ast;

public class Variable {

    private Boolean isMethod = false;

    public Variable( String name, Type type ) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }
    
    
    public void genKra(PW pw)
    {
        pw.printIdent(this.type.getName() + " " + this.getName());
    }

    public Type getType() {
        return type; 
    }
    
    public void setMethod(Boolean isMethod){
        this.isMethod = isMethod;
    }
    
    public Boolean isMethod(){
        return this.isMethod;
    }

    private String name;
    private Type type;
    private String methodName;

    public void setMethodName(String name) {
        this.methodName = name;
    }

    public Object getMethodName() {
        return this.methodName;
    }
}