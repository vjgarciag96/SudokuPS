package vprog.sudokups;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;

/**
 * Created by victor on 20/08/16.
 */
public class Game extends AppCompatActivity {

    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_MEDIUM =1;
    public static final int DIFFICULTY_HARD = 2;

    private int puzzle[]=new int[9*9];
    private boolean iniciales[]=new boolean[9*9];
    private int dificultadActual;
    private GameView puzzleView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        for(int i=0;i<9*9;i++){
            iniciales[i]=false;
        }
        dificultadActual=getIntent().getIntExtra("level",DIFFICULTY_EASY);
        crearJuego(dificultadActual);
        setContentView(R.layout.menusudoku);

        LinearLayout ll=(LinearLayout)findViewById(R.id.huecoVista);
        puzzleView=new GameView(this);
        setContentView(puzzleView);
        puzzleView.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.newStart){
            reiniciarJuego();
        }
        if(id==R.id.solve){
            resolverJuego();
        }
        return super.onOptionsItemSelected(item);
    }

    private void crearJuego(int lv){
        InputStream flujo=null;
        BufferedReader lector;
        String archivo="default.txt";
        int numPuzzle=(int) Math.random()*10+1;
        switch (lv){
            case DIFFICULTY_EASY:
               archivo= "facil"+numPuzzle+".txt";
                break;
            case DIFFICULTY_MEDIUM:
               archivo="medio"+numPuzzle+".txt";
                break;
            case DIFFICULTY_HARD:
                archivo="dificil"+numPuzzle+".txt";
                break;
            default:
                break;
        }
        try {
            AssetManager am=this.getAssets();
            InputStream is=am.open(archivo);
            String juego;
            int size=is.available();
            byte[] buffer=new byte[size];
            is.read(buffer);
            is.close();
            juego=new String(buffer);
            for(int i=0;i<juego.length()-1;i++) {
                puzzle[i] = juego.charAt(i) - '0';
            }
            int permutarTablero=(int) Math.random()*3+1;
            alterarTablero(permutarTablero);
            for(int i=0;i<81;i++){
                if(puzzle[i]!=0)
                    iniciales[i]=true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void reiniciarJuego(){
        for(int i=0;i<81;i++){
            if(!iniciales[i])
                puzzle[i]=0;
        }
        puzzleView=new GameView(this);
        setContentView(puzzleView);
        puzzleView.requestFocus();
    }

    private boolean resolverPuzzle(int etapa){
        boolean resuelto=false;
        if(!esCasillaInicial(etapa/9, etapa%9)){
            for(int num=1;num<=9;num++){
                if(esPosibleColocar(etapa/9, etapa%9, num)){
                    ponerValor(etapa%9, etapa/9, num);
                    if(etapa==80)
                        resuelto=true;
                    else{
                        if(resolverPuzzle(etapa+1))
                            resuelto=true;
                        else
                            borrarCasilla(etapa/9, etapa%9);
                    }
                }
            }
        }
        else{
            if(etapa!=80)
                resuelto=resolverPuzzle(etapa+1);
            else
                resuelto=true;
        }
        return resuelto;
    }

    private void resolverJuego(){
        if(resolverPuzzle(0)){
            setContentView(puzzleView);
            puzzleView.requestFocus();
        }
    }
    private int getCasilla(int x, int y){
        return puzzle[y*9+x];
    }

    protected String getCasillaString(int x, int y){
        String res="";
        int valor=getCasilla(x, y);
        if(valor!=0)
            res=String.valueOf(valor);
        return res;
    }

    private boolean estaVaciaTablero(int fila, int columna){
        return puzzle[fila*9+columna]==0;
    }
    private boolean comprobarFila(int fila, int valor){
        boolean enc=false;
        int i=0;
        while(i<9 && !enc){
            if(puzzle[fila*9+i]==valor)
                enc=true;
            else
                i++;
        }
        return enc;
    }

    private boolean comprobarColumna(int columna, int valor){
        boolean enc=false;
        int i=0;
        while(i<9 && !enc){
            if(puzzle[i*9+columna]==valor)
                enc=true;
            else
                i++;
        }
        return enc;
    }

    private boolean comprobarRegion(int fila, int columna, int valor){
        boolean enc=false;
        int ff=(fila/3)*3;//ff es la fila del extremo de la region
        int cc=(columna/3)*3;//cc es la columna del extremo de la region
        int i, j;
        i=0;
        while(i<3 && !enc){
            j=0;
            while (j<3 && !enc){
                if(puzzle[(ff+i)*9+cc+j]==valor)
                    enc=true;
                else
                    j++;

            }

            i++;
        }
        return enc;
    }

    protected boolean esPosibleColocar(int fila, int columna, int valor){
        boolean posible=true;
        if(!estaVaciaTablero(fila, columna) || comprobarFila(fila, valor) || comprobarColumna(columna, valor) || comprobarRegion(fila, columna, valor))
            posible=false;
        return posible;
    }

    private int[] valoresNoPosibles(int fila, int columna) {
        int[] noPosible = new int[9];
        int j=0;
        for (int i = 0; i < 9; i++) {
            noPosible[i]=0;
        if (!esPosibleColocar(fila, columna, i + 1)) {
            noPosible[j] =i+1;
            j++;
        }
    }
        return noPosible;
    }


    protected void mostrarTeclado(int x, int y){
        if(esCasillaInicial(y,x) || (estaVaciaTablero(y, x) &&
                !hayNumerosPosibles(y, x))){
            Toast toast=Toast.makeText(this, "No es posible colocar ningún numero", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else{
            if(!hayNumerosPosibles(y, x) || !estaVaciaTablero(y, x)){
                int[] noPosibles;
                int numActual=getCasilla(x, y);
                borrarCasilla(y, x);
                noPosibles=valoresNoPosibles(y, x);
                ponerValor(x, y, numActual);
                Dialog v=new Teclado(this, puzzleView, noPosibles, true);
                v.show();
            }
            else {
                Dialog v = new Teclado(this, puzzleView, valoresNoPosibles(y, x), false);
                v.show();
            }
        }
    }

    protected boolean esCasillaInicial(int fila, int columna){
        return iniciales[fila*9+columna];
    }

    protected boolean ponerValor(int x, int y, int value){
        if(value!=0)
            puzzle[y*9+x]=value;
        return true;
    }

    protected boolean hayNumerosPosibles(int fila, int columna){
        boolean hayNums=false;
        int i=0;
        while (i<9 && !hayNums){
            if(esPosibleColocar(fila, columna, i+1))
                hayNums=true;
            else
                i++;
        }
        return hayNums;
    }

    protected void borrarCasilla(int fila, int columna){
        if(iniciales[fila*9+columna]!=true)
            puzzle[fila*9+columna]=0;
    }

    protected void sudokuResuelto(){
        boolean resuelto=true;
        int i=0;
        while(i<81 && resuelto){
            if(puzzle[i]==0)
                resuelto=false;
            else
                i++;
        }
        if(resuelto){
                Intent intent=new Intent(Game.this, JuegoResuelto.class);
                startActivity(intent);
                finish();
            }
        }

    //Metodos para generar tableros aleatorios

    private void intercambiarFilas(int fila1, int fila2){
        int aux;
        for(int i=0;i<9;i++) {
            aux = puzzle[fila1 * 9 + i];
            puzzle[fila1 * 9 + i] = puzzle[fila2 * 9 + i];
            puzzle[fila2 * 9 + i] = aux;
        }
    }

    private void intercambiarColumnas(int col1, int col2){
        int aux;
        for(int i=0;i<9;i++) {
            aux = puzzle[i * 9 + col1];
            puzzle[i*9+col1] = puzzle[i*9+col2];
            puzzle[i*9+col2] = aux;
        }
    }

    private void cambioFilasRegion(){
        int numAleat1 = (int) (Math.random() * 3);
        int numAleat2 = (int) (Math.random() * 3);
        for(int i=0;i<3;i++) {
            while(numAleat1==numAleat2)
                numAleat2=(int) (Math.random() * 3);
            intercambiarFilas(i*3+numAleat1, i*3+numAleat2);
        }
    }

    private void cambioColumnasRegion(){
        int numAleat1 = (int) (Math.random() * 3);
        int numAleat2 = (int) (Math.random() * 3);
        for(int i=0;i<3;i++) {
            while(numAleat1==numAleat2)
                numAleat2=(int) (Math.random() * 3);
            intercambiarColumnas(i*3+numAleat1, i*3+numAleat2);
        }
    }

    private void intercambiarRegionesFila(){
        int numAleat1 = (int) (Math.random() * 3);
        int numAleat2 = (int) (Math.random() * 3);
        while(numAleat1==numAleat2)
            numAleat2=(int) (Math.random() * 3);
        for(int i=0;i<3;i++)
            intercambiarFilas(numAleat1*3+i, numAleat2*3+i);
    }

    private void intercambiarRegionesColumna(){
        int numAleat1 = (int) (Math.random() * 3);
        int numAleat2 = (int) (Math.random() * 3);
        while(numAleat1==numAleat2)
            numAleat2=(int) (Math.random() * 3);
        for(int i=0;i<3;i++)
            intercambiarColumnas(i*3+numAleat1, i*3+numAleat2);
    }

    private void intercambiarNumeros(int num1, int num2){
        for(int i=0;i<81;i++){
            if(puzzle[i]==num1)
                puzzle[i]=num2;
            else
                if(puzzle[i]==num2)
                    puzzle[i]=num1;
        }
    }

    private void intercambiarParejasNumeros(int numParejas){
        for(int i=0;i<numParejas;i++){
            int numAleat1 = (int) (Math.random() * 9+1);
            int numAleat2 = (int) (Math.random() * 9+1);
            while(numAleat1==numAleat2)
                numAleat2=(int) (Math.random() * 9+1);
            intercambiarNumeros(numAleat1, numAleat2);
        }
    }

    private void generar1(){
        cambioFilasRegion();
        intercambiarRegionesColumna();
        intercambiarParejasNumeros(4);
    }

    private void generar2(){
        cambioColumnasRegion();
        intercambiarRegionesFila();
        intercambiarParejasNumeros(4);
    }

    private void generar3(){
        cambioFilasRegion();
        cambioColumnasRegion();
        intercambiarRegionesFila();
        intercambiarRegionesColumna();
        intercambiarParejasNumeros(4);
    }

    private void alterarTablero(int num){
        switch(num){
            case 1:
                generar1();
                break;
            case 2:
                generar2();
                break;
            case 3:
                generar3();
                break;
            default:
                generar3();
                break;
        }
    }
}