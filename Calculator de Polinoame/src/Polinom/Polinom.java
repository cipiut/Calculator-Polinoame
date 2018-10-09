package Polinom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polinom {

    private List<Monom> polinom = new ArrayList<>();

    public Polinom(){}

    public Polinom(String n){
        this.readPolinom(n);
    }

    public List<Monom> getPolinom(){
        return polinom;
    }

    private void readPolinom(String n){//metoda prin care se citeste polinomul deja verificat
        this.polinom.removeAll(polinom);
        String[] f= new String[n.length()];//dupa metoda de verificare aici vor fi stocati termeni polinomului
        if(!vfReg(n,f)||n.equals("0"))return; // daca metoda de verificare nu este buna se anuleaza procesarea
        int i=0;
        double coef=1,exp=0;
        //in ciclul while se despar monoamele in coeficienti si exponenti
        while(i<f.length && f[i]!=null){
            if(f[i].indexOf('x')==f[i].length()-1){
                exp=1;
                if(f[i].equals("x")) coef = 1;
                else if(f[i].equals("-x")) coef = -1;
                else coef = Double.parseDouble(f[i].substring(0,f[i].indexOf('x')));
            }else if(f[i].indexOf('x')!=-1){
                if(f[i].indexOf('x')==0)coef=1;
                else if(f[i].indexOf('x')==1 &&f[i].indexOf('-')==0)coef=-1;
                else coef = Double.parseDouble(f[i].substring(0,f[i].indexOf('x')));
                exp = Double.parseDouble(f[i].substring(f[i].indexOf('x')+1,f[i].length()));
            }else if(f[i].indexOf('x')==-1){
                coef = Double.parseDouble(f[i]);
                exp = 0;
            }
            i++;
            this.polinom.add(new Monom(coef,exp));//se adauga un nou monom dupa ce sau procesat
            //coeficienti si exponentii
        }
        Collections.sort(this.polinom);//ca polinomul sa arate frumos se sorteaza
    }

    private boolean vfReg(String n,String[] x){
        if(n.isEmpty())return false;
        String myStr= n.replace(" ","");// se elimina spatiile
        String reg = "(^([-]?((\\d{0,100})|(\\d{1,100}([*]?)))[xX][\\\\^]\\d{1,100})|(^([-+]?)((\\d{0,100})|(\\d{1,100}([*]?)))[xX])|(^[-+]?\\d{1,100}))" +
                "|([-+]((\\d{0,100})|(\\d{1,100}([*]?)))[xX][\\\\^]\\d{1,100})|([-+]((\\d{0,100})|(\\d{1,100}([*]?)))[xX])|([-+])\\d{1,100}";//monom bun
        //se foloseste RegEx pentru verificarea Monoamelor si indentificarea Expresilor ilegale
        Pattern pattern = Pattern.compile(reg);
        Matcher m = pattern.matcher(myStr);
        StringBuilder f= new StringBuilder();
        //variabila f este un String prin care se vor pune la loc toate Monoamele si se verifica daca expresia e legala
        int i=0;
        while(m.find()) {
            String f1=m.group();
            f1=f1.replaceAll("[\\\\^+*]","").replace("X","x");
            //se elimina din group toate semnele pentru o usoara citire a numerelor si se inlocuieste X cu x
            x[i++]=f1;//Se adauga in tablou String-ul procesat
            f.append(m.group());
        }
        return f.toString().equals(myStr);//returneaza fals in cazul in care expresia este invalida
    }

    public String afisPolinom(){//se formeaza un String din monoamele avute in polinom
        String rez="";
        for(Monom m :polinom){
            if(polinom.get(0).equals(m))rez+=m.afisMonom().replace("+","");
                //in cazul in care suntem la primul element ,se renunta la semn daca ceoficientul este pozitiv
            else rez+=m.afisMonom();
        }
        return (this.polinom.size()==0)?"0":rez;
    }

    public Polinom adunare(Polinom p){
        Polinom rezultat = new Polinom();
        this.reducere();
        p.reducere();
        rezultat.coppyArray(p.polinom);//se adauga toti termeni din p in rezultat pentru a evita uitarea unor termeni
        boolean ok ;
        for(Monom m : this.polinom){
            ok = true;
            for(Monom x : rezultat.polinom)
                if(m.getExp()==x.getExp()) {
                    x.setCoef(x.getCoef()+m.getCoef());
                    ok=false;
                }
            if(ok)rezultat.polinom.add(new Monom(m.getCoef(), m.getExp()));//in cazul in care un termen exista dar nu poate fi
            //adunat se adauga in noul polinom
        }
        rezultat.reducere();
        return rezultat;
    }

    private void coppyArray(List<Monom> x){//copiaza element cu element fiecare Monom din x in this
        this.polinom.removeAll(this.polinom);//elementele de care nu mai este nevoie se sterg
        for(Monom m : x)this.polinom.add(new Monom(m.getCoef(),m.getExp()));
    }

    public Polinom scadere(Polinom p){//pe acelasi principiu cu adunarea doar ca aici coeficienti sunt cu -
        Polinom rezultat = new Polinom();
        this.reducere();
        p.reducere();
        rezultat.coppyArray(this.polinom);
        boolean ok ;
        for(Monom m : p.polinom){
            ok = true;
            for(Monom x : rezultat.polinom)
                if(m.getExp()==x.getExp()) {
                    x.setCoef(x.getCoef()-m.getCoef());// Atentie: this - p NU: p - this
                    ok=false;
                }
            if(ok)rezultat.polinom.add(new Monom(-m.getCoef(), m.getExp()));
        }
        rezultat.reducere();
        return rezultat;
    }

    public Polinom inmultire(Polinom p){
        Polinom s = new Polinom();
        this.reducere();
        p.reducere();
        for(Monom m : this.polinom){
            for(Monom x : p.polinom)
                s.polinom.add(new Monom(m.getCoef()*x.getCoef(),m.getExp()+x.getExp()));
        }//in aceste for-uri se inmultesc toti coeficienti intre cei doi polinomi si se aduna exponenti
        Polinom c = new Polinom();
        c.coppyArray(s.polinom);
        for(Monom m : c.polinom)if(m.getCoef()==0)s.polinom.remove(m);//este necesara eliminarea elementelor nule
        s.polinom=c.polinom;
        s.reducere();
        // cu toate ca sau eliminat elementele nule este necesara o reducere pentru adunarea elementelor de acelas grad
        return s;
    }

    public void reducere(){//reduce termeni asemenea din polinom
        Collections.sort(this.polinom);
        Polinom s=new Polinom();
        Monom f = new Monom(0,0);
        for(Monom m :this.polinom){
            if(m.getExp()==f.getExp())f.setCoef(m.getCoef()+f.getCoef());//se iau termeni cu aceasi exponent si se aduna
            else {
                if(f.getCoef()!=0) s.polinom.add(f);
                f=m;
            }
        }
        this.polinom=s.polinom;
        if(f.getCoef()!=0)this.polinom.add(f);//in caz ca polinomul este gol este necesara adaugarea ultimului element
        // care intotdeauna va fi 0
    }

    public ArrayList impartire(Polinom impartitor){
        this.reducere();
        impartitor.reducere();
        if(impartitor.polinom.size()==0||this.polinom.size()==0)return new ArrayList(0);//in caz ca polinomul e invalid se returneaza 0
        double exp2=impartitor.polinom.get(0).getExp() , exp1=this.polinom.get(0).getExp();
        double coef2=impartitor.polinom.get(0).getCoef() , coef1=this.polinom.get(0).getCoef();
        Polinom deimpartit= new Polinom();
        Polinom aux= new Polinom();
        deimpartit.coppyArray(this.polinom);
        Polinom cat= new Polinom();
        while (exp1>=exp2){//in caz ca exponentul deimpartitului este mai mare sau egal cu cel al impartitorului se continua algoritmul
            cat.polinom.add(new Monom(coef1/coef2,exp1-exp2));//se adauga in Cat deimpartit/impartitor
            aux.polinom.add(cat.polinom.get(cat.polinom.size()-1));//in polinomul auxiliar se adauga monomul cu
            //grad minim pentru calcularea restului
            aux=aux.inmultire(impartitor);
            deimpartit=deimpartit.scadere(aux);//se scade din deimpartit (monomul cu grad minim)*impartitor
            Collections.sort(deimpartit.polinom);//sorteaza astfel incat sa se aduca la forma normala a unui polinom
            Collections.sort(impartitor.polinom);
            if(deimpartit.polinom.size()==0)break;//ca sa nu se produca vreo eroarea la asignari se intrerupe while-ul
            exp2=impartitor.polinom.get(0).getExp();
            exp1=deimpartit.polinom.get(0).getExp();
            coef2=impartitor.polinom.get(0).getCoef();
            coef1=deimpartit.polinom.get(0).getCoef();
            aux.polinom.removeAll(aux.polinom);// in aux ne intereseaza doar ultimul element din Cat
        }
        ArrayList<Polinom> n = new ArrayList<>();//construieste Catul si Restul
        n.add(cat);
        n.add(deimpartit);//deimpartitul ramas devine restul impartiri
        return n;
    }

    public Polinom derivPolinom(){//deriveaza dupa fiecare monom
        Polinom p = new Polinom();
        this.reducere();
        p.coppyArray(this.polinom);
        for(Monom m : p.polinom)m.derivMonom();
        p.reducere();//necesar pentru eliminarea zerourilor extra
        return p;
    }

    public Polinom integPolinom(){//integreaza dupa fiecare monom
        Polinom p = new Polinom();
        this.reducere();
        p.coppyArray(this.polinom);
        for(Monom m : p.polinom)m.integMonom();
        return p;
    }

    public double f(double x){
        double rez=0;
        for(Monom m: this.polinom){
            rez+=(m.getCoef()*Math.pow(x,m.getExp()));
        }
        return rez;
    }
}