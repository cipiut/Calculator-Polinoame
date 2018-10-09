package Polinom;

public class Monom implements Comparable<Monom> {

    private double coef;
    private double exp;

    public Monom (double coef ,double exp){
        this.coef=coef;
        this.exp=exp;
    }

    public String afisMonom(){//functia de construire a String-ului pentru monom
        long x= (long) coef, y = (long) exp;//in cazul in care coef si exp sunt intregi nu se vor afisa cu zecimale
        String e=String.valueOf(y);
        String c;
        if(coef%1==0.0f)c=String.valueOf(x);
        else c=String.format("%."+variablePrecision(coef)+"f",coef);//am construit o functie care determina precizia
        //cu care se pot afisa elementele in cazul in care sunt cu virgula
        if(exp==1)return (coef>1)?"+"+c+"x":((coef<-1)?c+"x":((coef==-1)?"-x":((coef<1&&coef>0)?"+"+c+"x":((coef>-1&&coef<0)?c+"x":"+x"))));
        // pentru exp =1 se va afisa doar x . NU x^1 . Am determinat String-ul pentru fiecare coef
        else if(exp==0)return (coef>0)?"+"+c:c;//pentru x^0 se afiseaza doar coeficientul
        else if(coef<1&&coef>-1) return (coef<1&&coef>0)?"+"+c+"x^"+e:c+"x^"+e;//pentru 0.xx se adauga [+-]0.xxX^e
        else return (coef==1)?"+x^"+e:((coef==-1)?"-x^"+e:((coef>1)?"+"+c+"x^"+e:c+"x^"+e));
        // cand coeficientul este 1 se va afisa doar x .NU 1x sau 1*x
    }

    public int compareTo(Monom o) {//se face descrecator dupa exponent
        if(this.exp<o.exp)return 1;
        else if(this.exp>o.exp)return -1;
        return 0;
    }

    public void derivMonom(){//derivare monom
        this.coef*=this.exp;
        this.exp--;
    }

    public void integMonom(){//integrare monom
        this.exp++;
        this.coef/=this.exp;
    }

    public double getCoef() {
        return coef;
    }

    public void setCoef(double coef) {
        this.coef = coef;
    }

    public double getExp() {
        return exp;
    }

    private int variablePrecision(double x){//determina precizia lui x
        int precision=1;
        if(x%1==0)return 1;//in cazul in care x este 0 se retuneaz precizia 1 . Adica are doar o zecimala
        x=(x%1);
        while(true){
            if((long)(x*=10)!=0)break;//se verifica cand ajunge x sa aiba macar o zecimala diferita de 0
            precision++;
        }
        return precision+1;//precizia minima este 2
    }
}