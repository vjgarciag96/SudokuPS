package vprog.sudokups;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by victor on 21/08/16.
 */
public class Teclado extends Dialog {

    private final View teclas[]=new View[9];
    private final GameView puzzle;
    private View teclado;
    private View teclaBorrar;
    private int[] valoresNoPosibles;
    private boolean esPosibleBorrar;

    public Teclado(Context context, GameView puzzle, int[] valoresNoPosibles, boolean esPosibleBorrar){
        super(context);
        this.puzzle=puzzle;
        this.valoresNoPosibles=valoresNoPosibles;
        this.esPosibleBorrar=esPosibleBorrar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setTitle("Teclado");
        setContentView(R.layout.teclado);
        encontrarViews();
        for(int i=0;i<valoresNoPosibles.length;i++){
            if(valoresNoPosibles[i]!=0)
            teclas[valoresNoPosibles[i]-1].setVisibility(View.INVISIBLE);
        }
        if(!esPosibleBorrar)
            teclaBorrar.setVisibility(View.INVISIBLE);
        ponerListeners();
    }

    private void encontrarViews(){
        teclado=findViewById(R.id.teclado);
        teclaBorrar=findViewById(R.id.teclaBorrar);
        teclas[0]=findViewById(R.id.tecla1);
        teclas[1]=findViewById(R.id.tecla2);
        teclas[2]=findViewById(R.id.tecla3);
        teclas[3]=findViewById(R.id.tecla4);
        teclas[4]=findViewById(R.id.tecla5);
        teclas[5]=findViewById(R.id.tecla6);
        teclas[6]=findViewById(R.id.tecla7);
        teclas[7]=findViewById(R.id.tecla8);
        teclas[8]=findViewById(R.id.tecla9);
    }

    private void ponerListeners(){
        for(int i=0;i<teclas.length;i++){
            final int t=i+1;
            teclas[i].setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    returnResult(t);
                }
            });
        }
        teclado.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                returnResult(0);
            }
        });
        teclaBorrar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                borrarCasilla();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int tile = 0;
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE: tile = 0; break;
            case KeyEvent.KEYCODE_1:	 tile = 1; break;
            case KeyEvent.KEYCODE_2:	 tile = 2; break;
            case KeyEvent.KEYCODE_3:	 tile = 3; break;
            case KeyEvent.KEYCODE_4:	 tile = 4; break;
            case KeyEvent.KEYCODE_5:	 tile = 5; break;
            case KeyEvent.KEYCODE_6:	 tile = 6; break;
            case KeyEvent.KEYCODE_7:	 tile = 7; break;
            case KeyEvent.KEYCODE_8:	 tile = 8; break;
            case KeyEvent.KEYCODE_9:	 tile = 9; break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        returnResult(tile);
        return true;
    }

    private void returnResult(int casilla){
        puzzle.setValorSeleccionado(casilla);
        dismiss();
    }

    private void borrarCasilla(){
        puzzle.setCasillaVacia();
        dismiss();
    }


}
