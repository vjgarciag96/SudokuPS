package vprog.sudokups;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by victor on 25/08/16.
 */
public class JuegoResuelto extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego_resuelto);
        TextView tv=(TextView) findViewById(R.id.textView3);
        String font_path="fonts/Maaliskuu.ttf";
        Typeface TF=Typeface.createFromAsset(getAssets(), font_path);
        tv.setTypeface(TF);
    }
}
