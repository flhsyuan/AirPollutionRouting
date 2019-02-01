package airpollutionrouting.com.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class MoreInfoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        ImageButton airCasting = (ImageButton) findViewById(R.id.btn_aircasting);
        airCasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreInfoActivity.this,AircastingActivity.class);
                String url = "http://www.aircasting.org/";
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });


    }
}
