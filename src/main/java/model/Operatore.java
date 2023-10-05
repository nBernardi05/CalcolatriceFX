package model;

/**
 *
 * @author Bernardi Nicola
 */
public enum Operatore {
    ADD('+', (byte)1, 'L'),
    SUB('-', (byte)1, 'L'),
    MULT('*', (byte)2, 'L'),
    DIV('/', (byte)2, 'L'),
    POW('^', (byte)3, 'R');
    private char simbolo;
    private byte priority;
    private char associativity;

    private Operatore(char simbolo, byte priority, char scorr) {
        this.simbolo = simbolo;
        this.priority = priority;
        this.associativity = scorr;
    }

    public byte getPriority() {
        return priority;
    }

    public char getAssociativity() {
        return associativity;
    }    

    public char getSimbolo() {
        return simbolo;
    }
    
    public static Operatore getOperatore(char c){
        for(Operatore op: Operatore.values()){
            if(op.simbolo==c)
                return op;
        }
        return null;
    }
    
//    @Override
//    public String toString(){
//        return Character.toString(simbolo);
//    }
//    
    
}
