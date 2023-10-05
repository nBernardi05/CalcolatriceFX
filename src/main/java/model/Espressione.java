package model;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 *
 * @author Bernardi Nicola
 */
public final class Espressione {

    private String strExpr;
    private ArrayList tokensList;
    private ArrayList rpnExpr;
    private Frazione risultato;

    public Espressione(String strExpr) throws EspressioneNonValidaException{
        if(!strExpr.endsWith("=")){ // se mi dimentico l'= alla fine me lo aggiunge automaticamente
            strExpr += "=";
        }
        this.strExpr = strExpr;
        tokensList = new ArrayList();
        rpnExpr = new ArrayList();
        parser();
        fromInfixToRpn();
        calcResult();
    }

    public Frazione getRisultato() {
        return risultato;
    }

    public ArrayList getTokensList() {
        return tokensList;
    }


    public void parser() throws EspressioneNonValidaException {
        String alfabeto = "0123456789+-*/^()=";
        int stato = 0;   //stato iniziale 
        int numeratore = 0;
        int segno = 1;
        String msg="";
        for (char c : strExpr.toCharArray()) {
            if (alfabeto.indexOf(c) < 0) {
                msg="Il carattere " + c + " non è valido";
                throw new EspressioneNonValidaException(msg);
            }
            switch (stato){
                case 0:
                    switch (c){
                        case '*', '/', '^', ')':
                            msg = "Syntax Error: " + c + " not allowed here.";
                            throw new EspressioneNonValidaException(msg);
                        case '-':
                            segno = -1;
                        case '+':
                            stato = 1;
                            tokensList.add(Operatore.getOperatore(c));
                            break;
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                            numeratore = Character.getNumericValue(c);
                            stato = 2;
                            break;
                        case '(':
                            stato = 3;
                            tokensList.add(Parentesi.APERTA);
                            break;
                        case '=':
                            break;
                    }
                    break;
                case 1:
                    switch (c){
                        case '+', '-', '*', '/', '^', '=', ')':
                            msg = "Syntax Error: " + c + " not allowed here.";
                            throw new EspressioneNonValidaException(msg);
                        case '(':
                            stato = 3;
                            tokensList.add(Parentesi.APERTA);
                            break;
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                            numeratore = numeratore * 10 + Character.getNumericValue(c);
                            stato = 2;
                            break;
                    }
                    break;
                case 2:
                    switch (c){
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                            numeratore = numeratore * 10 + Character.getNumericValue(c);
                            stato = 2;
                            break;
                        case '+', '-', '*', '/', '^':
                            //Operatore.getOperatore(c);
                            tokensList.add(new Frazione(numeratore, 1));
                            numeratore = 0;
                            tokensList.add(Operatore.getOperatore(c));
                            stato = 1;
                            break;
                        case ')':
                            stato = 4;
                            tokensList.add(new Frazione(numeratore, 1));
                            numeratore = 0;
                            tokensList.add(Parentesi.CHIUSA);
                            break;
                        case '(':
                            msg = "Syntax Error: " + c + " not allowed here.";
                            throw new EspressioneNonValidaException(msg);
                        case '=':
                            tokensList.add(new Frazione(numeratore, 1));
                            break;
                    }
                    break;
                case 3:
                    switch (c){
                        case '*', '/', '^', ')', '=':
                            msg = "Syntax Error: " + c + " not allowed here.";
                            throw new EspressioneNonValidaException(msg);
                            
                        case '(':
                            stato = 3;
                            tokensList.add(Parentesi.APERTA);
                            break;
                        case '+', '-':
                            stato = 1;
                            tokensList.add(Operatore.getOperatore(c));
                            break;
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                            stato = 2;
                            numeratore = numeratore * 10 + Character.getNumericValue(c);
                            break;
                    }
                    break;
                case 4:
                    switch (c){
                        case '=':
                            break;
                        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                            msg = "Syntax Error: " + c + " not allowed here.";
                            throw new EspressioneNonValidaException(msg);
                        case '+', '-', '/', '*', '^':
                            stato = 1;
                            tokensList.add(Operatore.getOperatore(c));
                            break;
                        case ')':
                            stato = 4;
                            tokensList.add(Parentesi.CHIUSA);
                            break;
                    }
            }

        }
    }
    /**
     * Da un arraylist con il formato normale dei numeri lo porta ad un arraylist
     * con il formato rpn
     */
    public void fromInfixToRpn(){
        ArrayDeque stackOperatori = new ArrayDeque();
        for(Object c: tokensList){
            if(c instanceof Frazione){
                rpnExpr.add(c);
            }else if(c instanceof Operatore){
                while(stackOperatori.peek() instanceof Operatore){
                    if(((Operatore)stackOperatori.peek()).getPriority() > ((Operatore) c).getPriority() || 
                            (((Operatore)stackOperatori.peek()).getPriority() == ((Operatore) c).getPriority() && 
                            ((Operatore)stackOperatori.peek()).getAssociativity() == ((Operatore) c).getAssociativity())){
                        rpnExpr.add(stackOperatori.pop());
                    }else{
                        break;
                    }
                }
                stackOperatori.push((Operatore)c);
            }else if(c == Parentesi.APERTA){
                stackOperatori.push((Parentesi) c);
            }else if(c == Parentesi.CHIUSA){
                while(stackOperatori.peek() != Parentesi.APERTA && stackOperatori.peek()!= null){
                    rpnExpr.add(stackOperatori.pop());
                }
                stackOperatori.pop();
            }
        }
        while(stackOperatori.peek() != null){
            rpnExpr.add(stackOperatori.pop());
        }
    }
    
    /**
     * Calcola il risultato dal formato rpn
     */
    public void calcResult(){
        Frazione op1, op2;
        ArrayDeque<Frazione> stackOperandi = new ArrayDeque<>();
        for(Object po: rpnExpr){
            if(po instanceof Frazione){
                stackOperandi.push((Frazione)po);   
            }else{
                op1 = stackOperandi.pop();
                op2 = stackOperandi.pop();
                switch ((Operatore)po){
                    case ADD:
                        stackOperandi.push(op1.add(op2));
                        break;
                    case SUB:
                        stackOperandi.push(op2.sub(op1));
                        break;
                    case MULT:
                        stackOperandi.push(op1.mult(op2));
                        break;
                    case DIV:
                        //stackOperandi.push(op2.div(op1));
                        try{
                            stackOperandi.push(op2.div(op1));
                        }catch(ArithmeticException ex){
                            System.out.println("Math Error: " + ex.toString());
                            return;
                        }
                        stackOperandi.push(op2.div(op1));
                        break;
                    case POW:
                        stackOperandi.push(op2.pot(op1));
                        
                }
            }
        }
        risultato = stackOperandi.pop();
    }

    @Override
    public String toString() {
        return risultato.toString();
    }
    

}
