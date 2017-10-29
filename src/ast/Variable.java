package ast;

public class Variable {

    private Boolean isMethod = false;

    public Variable( String name, Type type ) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }

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
}