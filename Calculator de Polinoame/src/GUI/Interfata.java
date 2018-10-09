package GUI;

import Polinom.Polinom;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.util.ArrayList;

public class Interfata extends Application implements EventHandler<ActionEvent>{
    private Button adunare ,scadere ,imparitire , inmultire,integrare ,derivare,graph;
    private TextField xRead,yRead;
    private TextArea result;
    private Polinom x,y;
    private NumberAxis xAxis ,yAxis;
    private LineChart<Number,Number> lineChart;
    private XYChart.Series seriesX ,seriesY;

    private void initValues(){
        graph = new Button("Graf");
        adunare = new Button("X+Y");
        scadere = new Button("X-Y");
        inmultire = new Button("X*Y");
        imparitire = new Button("X/Y");
        integrare = new Button("∫X ∫Y");
        derivare = new Button("X' Y'");
        xRead =new TextField();
        xRead.setPromptText("ex.: x^2+1");
        xRead.setPrefColumnCount(50);
        yRead =new TextField();
        yRead.setPrefColumnCount(50);
        yRead.setPromptText("ex.: 2x^3+2x");
        result= new TextArea();
        result.setPrefColumnCount(52);
        result.setPrefRowCount(2);
        result.setPromptText("Rezultat");
        result.setEditable(false);
        result.setBackground(Background.EMPTY);
        adunare.setOnAction(this);
        scadere.setOnAction(this);
        inmultire.setOnAction(this);
        imparitire.setOnAction(this);
        integrare.setOnAction(this);
        derivare.setOnAction(this);
        graph.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent event) {
        String x1 = xRead.getText().toLowerCase();
        String y1 = yRead.getText().toLowerCase();
        if(y1.equals(""))y1="0";
        if(x1.equals(""))x1="0";
        x = new Polinom(x1);
        y = new Polinom(y1);
        if(!x1.equals("0") && x.afisPolinom().equals("0") || !y1.equals("0") && y.afisPolinom().equals("0"))
            result.setText("Polinomul introdus nu este bun!");
        else{
            if(event.getSource()== graph)showGraph();
            if (event.getSource() == adunare) result.setText(x.adunare(y).afisPolinom());
            if (event.getSource() == scadere) result.setText(x.scadere(y).afisPolinom());
            if (event.getSource() == inmultire) result.setText(x.inmultire(y).afisPolinom());
            if (event.getSource() == integrare) result.setText("∫X: "+x.integPolinom().afisPolinom()+"\n∫Y: "+y.integPolinom().afisPolinom());
            if (event.getSource() == derivare) result.setText("X`: "+x.derivPolinom().afisPolinom()+"\nY`: "+y.derivPolinom().afisPolinom());
            if (event.getSource() == imparitire) {
                ArrayList<Polinom> s = x.impartire(y);
                if (s.size() == 0) result.setText("0");
                else result.setText("Cat: " + s.get(0).afisPolinom() + "\nRest : " + s.get(1).afisPolinom());
            }
        }
    }

    @Override public void start(Stage stage) {
        initValues();
        stage.setTitle("Calculator Polinoame");
        stage.setResizable(false);
        Label pol= new Label("X :");
        Label pol1 = new Label("Y :");
        GridPane calc= new GridPane();
        calc.setPadding(new Insets(10,10,10,10));
        calc.setVgap(5);
        calc.setHgap(5);
        calc.add(pol,0,0);
        calc.add(pol1,0,1);
        calc.add(xRead,1,0);
        calc.add(yRead,1,1);
        HBox h= new HBox();
        HBox res= new HBox();
        h.setSpacing(10);
        res.setSpacing(10);
        h.setPadding(new Insets(10,10,10,10));
        res.setPadding(new Insets(10,10,10,10));
        res.getChildren().addAll(result);
        h.getChildren().addAll(adunare, scadere,inmultire,imparitire,integrare,derivare,graph);
        VBox v = new VBox();
        v.getChildren().addAll(calc,h,res);
        initGraph();
        v.getChildren().add(lineChart);
        lineChart.getData().addAll(seriesX,seriesY);
        Scene scene = new Scene(v,710,610);
        stage.setScene(scene);
        stage.show();
    }

    private void initGraph(){
        xAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(-20);
        xAxis.setUpperBound(20);
        yAxis = new NumberAxis();
        yAxis.setLowerBound(-20);
        yAxis.setAutoRanging(false);
        yAxis.setUpperBound(20);
        yAxis.setLabel("f(x)");
        xAxis.setLabel("x");
        lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setTitle("Polinoame");
        seriesX= new XYChart.Series();
        seriesY = new XYChart.Series();
        seriesX.setName("X");
        seriesY.setName("Y");
        lineChart.setCreateSymbols(false);
    }

    private XYChart.Series scaleGraph(Polinom f) {
        XYChart.Series s=new XYChart.Series();
        if (f.afisPolinom().equals("0")) return new XYChart.Series();
        int n =(int)xAxis.getUpperBound();
        double x=n/100f;
        for(double j=-n;j<=n;j+=x) {
            s.getData().add(new XYChart.Data(j, f.f(j)));
        }
        return s;
    }

    private void graphBounds(Polinom f, Polinom s) {
        f.reducere();
        s.reducere();
        int n=0,scalarF=0,scalarS=0,m=0,expF=0,expS=0;
        if(!f.afisPolinom().equals("0")){
            n = f.getPolinom().size() - 1;
            scalarF = Math.abs((int) f.getPolinom().get(n).getCoef());
            expF=(int)f.getPolinom().get(n).getExp();
        }
        if(!s.afisPolinom().equals("0")) {
            m = s.getPolinom().size() - 1;
            scalarS = Math.abs((int) s.getPolinom().get(m).getCoef());
            expS=(int)s.getPolinom().get(m).getExp();
        }
        if(scalarF<scalarS)scalarF=scalarS;
        if(scalarF>10) {
            if (expF == 0 || expS == 0) {
                xAxis.setLowerBound(-20 - scalarF);
                xAxis.setUpperBound(20 + scalarF);
                yAxis.setLowerBound(-20 - scalarF);
                yAxis.setUpperBound(20 + scalarF);
            }
        }
        else {
            xAxis.setLowerBound(-20);
            xAxis.setUpperBound(20);
            yAxis.setLowerBound(-20);
            yAxis.setUpperBound(20);
        }
    }

    private void showGraph(){
        lineChart.getData().removeAll(seriesX,seriesY);
        graphBounds(x,y);
        seriesX=scaleGraph(x);
        seriesX.setName("X");
        seriesY=scaleGraph(y);
        seriesY.setName("Y");
        lineChart.getData().addAll(seriesX,seriesY);
        result.setText("");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
