package model;

/**
 * 
 * @author Bernardi Nicola
 */
public class Frazione{
    private int num;
    private int den;
    private boolean positivo;
    
    public Frazione(int num, int den) throws ArithmeticException{
        if(den==0){
            String msg = "Il denominatore della frazione non può essere 0";
            throw new ArithmeticException("Il denominatore della frazione non può essere 0");
        }
        //Imposto il segno
        if(num*den<0)
            positivo = false;
        else
            positivo = true;
        
        num = Math.abs(num);
        den = Math.abs(den);
        
        if(num==0){
            this.num=0;
            this.den=1;
            this.positivo = true;
        }else{
            int m = mcd(num, den);
            this.num = num/m;
            this.den = den/m;
        }
        
        
        
    }
    /**
     * Calcola il MCD tra a e b 
     * @param a intero posito
     * @param b intero positivo
     * @return 
     */
    private static int mcd(int a, int b){
        while(a!=b)
            if(a>b)
                a-=b;
            else
                b-=a;
        return a;
    }
    
    public Frazione mult(Frazione f){
        int n = this.num*f.num;
        int d = this.den*f.den;
        if(this.positivo != f.positivo)
            n*=-1;
        return new Frazione(n, d);
    }
    
    public Frazione sub(Frazione f){
        return this.add ( ( new Frazione (-1, 1) ).mult(f) );
    }
    
    public Frazione pot(Frazione e){
        int esp = e.num / e.den;
        return new Frazione((int)Math.pow(this.num, esp), (int)Math.pow(this.den, esp));
    }
    
    /**
     * Restituisce la divisione tra this e f
     * @param f frazione divisore
     * @return frazione divisione
     * @throws ArithmeticException 
     */
    public Frazione div(Frazione f)throws ArithmeticException{
        
        return this.mult(f.reciproco());
    }
    
    public Frazione add(Frazione f){
        int d, n, segno1=1, segno2=1;
        d=this.den*f.den/mcd(this.den, f.den);
        if(!this.positivo)
            segno1=-1;
        if(!f.positivo)
            segno2=-1;
        n=d/this.den*this.num*segno1+d/f.den*f.num*segno2;
        return new Frazione(n, d);
    }
    
    
    
    
    /**
     * restituisce la frazione reciproco 
     * @return 
     */
    public Frazione reciproco() throws ArithmeticException{
        int segno=1;
        if(!positivo)
            segno = -1;
        return new Frazione(den*segno, num);
    }    
    
    @Override
    public String toString(){
        String s = "";
        if(!positivo)
            s="-";
        s+= num;
        if(den>1)
            s+="/" + den;
        return s;
    }
    
    
}
