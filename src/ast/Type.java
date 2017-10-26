package ast;

abstract public class Type {

    public Type( String name ) {
        this.name = name;
    }

    public static Type booleanType = new TypeBoolean();
    public static Type intType = new TypeInt();
    public static Type stringType = new TypeString();
    public static Type voidType = new TypeVoid();
    public static Type undefinedType = new TypeUndefined();
    public static Type nullType = new TypeNull();

    public String getName() {
        return name;
    }

    abstract public String getCname();

    private String name;
 
    public boolean isCompatible(Type other) {
        
       if (this == booleanType){
           return other == booleanType;
       }
       else if ( this == intType){
           return other == intType;
       }
       else if ( this == stringType){
           return other == stringType;
       }
       else if ( this == voidType){
           return false;
       }
       else if (this instanceof TypeNull){
           return other instanceof TypeNull;
       }
       else if (this instanceof KraClass){
           if (other instanceof TypeNull){
               return true;
           }
           return this.getName().equals(other.getName()) || ((KraClass) this).isSubclasOf(other);
       }
       else {
           return false;
       }
    }
}
