package vprog.sudokups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by victor on 20/08/16.
 */
public class GameView extends View {

    private final Game game;
    private final Rect tablero=new Rect();//representacion grafica del cuadrado donde estará el tablero

    private float anchoCuadro;
    private float altoCuadro;
    private int anchoCuadroInt;
    private int altoCuadroInt;
    private int selX;
    private int selY;

    public GameView(Context context) {
        super(context);
        this.game = (Game) context;
        setFocusable(true);
        setFocusableInTouchMode(true);


    }

    @Override protected void onSizeChanged(int ancho, int alto, int ancho_anterior, int alto_anterior){
        anchoCuadro=ancho/9f;
        altoCuadro=alto/9f;
        anchoCuadroInt=ancho/9;
        altoCuadroInt=alto/9;
        getRect(selX, selY, tablero);
       // super.onSizeChanged(ancho, alto, ancho_anterior, alto_anterior);
    }

    private void getRect(int x, int y, Rect rect){
        rect.set((int) (x * anchoCuadro), (int) (y * anchoCuadro), (int) (x * anchoCuadro + anchoCuadro), (int) (y * anchoCuadro + anchoCuadro));
    }

    @Override protected void onDraw(Canvas canvas){
        Paint fondo=new Paint();
        fondo.setColor(getResources().getColor(R.color.blanco));
        canvas.drawRect(0, 0, getWidth(), getHeight(), fondo);

        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));

        Paint hilite = new Paint();
        hilite.setColor(getResources().getColor(R.color.puzzle_hilite));

        Paint light = new Paint();
        light.setColor(getResources().getColor(R.color.puzzle_light));

        for (int i = 0; i < 9; i++) {
            canvas.drawLine(0, i*altoCuadro, getWidth(), i*altoCuadro, light);
            canvas.drawLine(0, i * altoCuadro + 1, getWidth(), i * altoCuadro + 1, light);
            canvas.drawLine(i * anchoCuadro, 0, i * anchoCuadro, getHeight(), light);
            canvas.drawLine(i * anchoCuadro + 1, 0, i * anchoCuadro + 1, getHeight(), light);
        }

        for (int i = 0; i < 9; i++) {
            if (i % 3  != 0)
                continue;
            canvas.drawLine(0, i * altoCuadroInt, getWidth(), i * altoCuadroInt, dark);
            canvas.drawLine(0, i * altoCuadro + 1, getWidth(), i * altoCuadroInt + 1, hilite);
            canvas.drawLine(i * anchoCuadroInt, 0, i * anchoCuadroInt, getHeight(), dark);
            canvas.drawLine(i * anchoCuadroInt + 1, 0, i * anchoCuadroInt + 1, getHeight(), hilite);
        }

        // Draw the numbers
        // Define color and style for numbers
        Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
        foreground.setStyle(Paint.Style.FILL);
        foreground.setTextSize(altoCuadroInt * 0.75f);
        foreground.setTextScaleX(anchoCuadroInt
                / altoCuadroInt);
        foreground.setTextAlign(Paint.Align.CENTER);

        Paint noIniciales = new Paint(Paint.ANTI_ALIAS_FLAG);
        noIniciales.setColor(getResources().getColor(R.color.colorPrimaryDark));
        noIniciales.setStyle(Paint.Style.FILL);
        noIniciales.setTextSize(altoCuadroInt * 0.75f);
        noIniciales.setTextScaleX(anchoCuadroInt
                / altoCuadroInt);
        noIniciales.setTextAlign(Paint.Align.CENTER);



        // Draw the number in the center of the tile
        Paint.FontMetrics fm = foreground.getFontMetrics();
        // Centering on X: use alignment (and X at midpoint)
        float x = anchoCuadroInt / 2;
        // Centering on Y: measure ascent/descent first
        float y = altoCuadroInt / 2 - (fm.ascent + fm.descent) / 2;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(game.esCasillaInicial(j, i))//pintamos los numeros iniciales
                    canvas.drawText(this.game.getCasillaString(i, j), i * anchoCuadroInt + x, j * altoCuadroInt + y, foreground);
                else //pintamos los numeros no iniciales
                    canvas.drawText(this.game.getCasillaString(i, j), i * anchoCuadroInt + x, j * altoCuadroInt + y, noIniciales);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("onKeyDown" +
                "","x="+selX+", y="+selY);
        switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                select(selX, selY - 1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                select(selX, selY + 1);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                select(selX - 1, selY);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                select(selX + 1, selY);
                break;
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE: setValorSeleccionado(0); break;
            case KeyEvent.KEYCODE_1:     setValorSeleccionado(1); break;
            case KeyEvent.KEYCODE_2:     setValorSeleccionado(2); break;
            case KeyEvent.KEYCODE_3:     setValorSeleccionado(3); break;
            case KeyEvent.KEYCODE_4:     setValorSeleccionado(4); break;
            case KeyEvent.KEYCODE_5:     setValorSeleccionado(5); break;
            case KeyEvent.KEYCODE_6:     setValorSeleccionado(6); break;
            case KeyEvent.KEYCODE_7:     setValorSeleccionado(7); break;
            case KeyEvent.KEYCODE_8:     setValorSeleccionado(8); break;
            case KeyEvent.KEYCODE_9:     setValorSeleccionado(9); break;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                game.mostrarTeclado(selX, selY);
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    private void select(int x, int y) {
        invalidate(tablero);
        selX = Math.min(Math.max(x, 0), 8);
        selY = Math.min(Math.max(y, 0), 8);
        getRect(selX, selY, tablero);
        invalidate(tablero);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);
        select((int) (event.getX() / anchoCuadro),
                (int) (event.getY() / altoCuadro));
        game.mostrarTeclado(selX, selY);
        return true;
    }

    public void setValorSeleccionado(int casilla) {
        if (game.ponerValor(selX, selY, casilla)) {
            invalidate();//may change hints
            game.sudokuResuelto();
        } else {
            // Number is not valid for this til
            // e
            startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake));
        }
    }

    public void setCasillaVacia(){
        game.borrarCasilla(selY, selX);
        invalidate();
    }
}
