package TestareUnitara;

import org.junit.Test;
import Polinom.Polinom;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
public class TestareUnitara {

    private Polinom x ;
    private Polinom y ;

    @Test
    public void input(){
        x=new Polinom("X^2+1");
        assertEquals(x.afisPolinom(),"x^2+1");

        x=new Polinom("X^x+1");
        assertEquals(x.afisPolinom(),"0");

        x=new Polinom("0");
        assertEquals(x.afisPolinom(),"0");

        x=new Polinom("X");
        assertEquals(x.afisPolinom(),"x");

        x=new Polinom("2x+asd");
        assertEquals(x.afisPolinom(),"0");

        x=new Polinom("2*x^2+3*x");
        assertEquals(x.afisPolinom(),"2x^2+3x");
    }
    @Test
    public void adunare(){
        x=new Polinom("X^2+1");
        y=new Polinom("X^2+1");
        assertEquals(x.adunare(y).afisPolinom(),"2x^2+2");
    }

    @Test
    public void scadere(){
        x=new Polinom("2X^2+1");
        y=new Polinom("X^2+1");
        assertEquals(x.scadere(y).afisPolinom(),"x^2");
    }

    @Test
    public void inmultire(){
        x=new Polinom("X+1");
        y=new Polinom("X+1");
        assertEquals(x.inmultire(y).afisPolinom(),"x^2+2x+1");
    }

    @Test
    public void impartire(){
        x=new Polinom("X^2+2x+1");
        y=new Polinom("X+1");
        ArrayList<Polinom> a=x.impartire(y);
        String s=a.get(0).afisPolinom()+" "+a.get(1).afisPolinom();
        assertEquals(s,"x+1 0");
    }

    @Test
    public void integrare(){
        x=new Polinom("2x+1");
        assertEquals(x.integPolinom().afisPolinom(),"x^2+x");
    }

    @Test
    public void derivare(){
        x=new Polinom("2x+1");
        assertEquals(x.derivPolinom().afisPolinom(),"2");
    }

}