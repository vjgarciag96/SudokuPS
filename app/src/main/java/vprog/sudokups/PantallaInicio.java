package vprog.sudokups;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by victor on 25/08/16.
 */
public class PantallaInicio extends AppCompatActivity {

    private final int duracionSplash=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_inicio);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(PantallaInicio.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, duracionSplash);
    }
}
