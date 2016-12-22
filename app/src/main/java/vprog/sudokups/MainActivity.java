package vprog.sudokups;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        TextView tv1=(TextView)findViewById(R.id.textView2);
        Button[] botones=new Button[3];
        botones[0]=(Button)findViewById(R.id.button);
        botones[1]=(Button)findViewById(R.id.button2);
        botones[2]=(Button)findViewById(R.id.button3);
        String font_path="fonts/Maaliskuu.ttf";
        String font_path2="fonts/HelveticaNeue.otf";
        Typeface TF=Typeface.createFromAsset(getAssets(), font_path);
        Typeface TF2=Typeface.createFromAsset(getAssets(), font_path2);
        tv1.setTypeface(TF);
        for(int i=0;i<3;i++)
            botones[i].setTypeface(TF2);
    }

    public void salir(View view){
        finish();
    }

    public void selLV(View view){
        Intent intent=new Intent(MainActivity.this, Options.class);
        startActivity(intent);
    }

    public void acercaDe(View view){
        Intent intent=new Intent(MainActivity.this, AcercaDe.class);
        startActivity(intent);
    }

}
