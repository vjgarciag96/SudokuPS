package vprog.sudokups;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by victor on 19/08/16.
 */
public class Options extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Button[] botones=new Button[3];
        botones[0]=(Button)findViewById(R.id.button4);
        botones[1]=(Button)findViewById(R.id.button5);
        botones[2]=(Button)findViewById(R.id.button6);
        String font_path="fonts/HelveticaNeue.otf";
        Typeface TF=Typeface.createFromAsset(getAssets(), font_path);
        for(int i=0;i<3;i++)
            botones[i].setTypeface(TF);
    }

    public void levelEasy(View view){
        Intent intent=new Intent(Options.this, Game.class);
        intent.putExtra("level", 0);
        startActivity(intent);
        finish();
    }

    public void levelMedium(View view){
        Intent intent=new Intent(Options.this, Game.class);
        intent.putExtra("level", 1);
        startActivity(intent);
        finish();
    }

    public void levelHard(View view){
        Intent intent=new Intent(Options.this, Game.class);
        intent.putExtra("level", 2);
        startActivity(intent);
        finish();
    }
}
