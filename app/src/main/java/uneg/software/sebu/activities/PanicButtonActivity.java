package uneg.software.sebu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uneg.software.sebu.R;
import uneg.software.sebu.utils.UserSessionManager;

public class PanicButtonActivity extends AppCompatActivity implements View.OnClickListener {
    @InjectView(R.id.cerrarSesion)
    FloatingActionButton cerrarSesion;
    @InjectView(R.id.menu)
    FloatingActionMenu menu;
    private UserSessionManager session;
    @InjectView(R.id.username)
    TextView username;
    @InjectView(R.id.nombre)
    TextView nombre;
    @InjectView(R.id.nivel)
    TextView nivel;
    @InjectView(R.id.panicButton)
    Button panicButton;
    @InjectView(R.id.icon)
    ImageView icon;
    private Animation pulse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_button);
        ButterKnife.inject(this);
        session=new UserSessionManager(this);
        username.setText(session.getUsername());
        nombre.setText(session.getNombreCompleto());
        nivel.setText(session.getDescripcionNivel());
        cerrarSesion.setOnClickListener(this);
        panicButton.setOnClickListener(this);
        panicButton.setFreezesText(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.cerrarSesion:
                session.logoutUser();
                Intent i=new Intent(this,LoginActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.panicButton:
                if(panicButton.getFreezesText())//se usa esta propiedad para no hacer una bandera
                {
                    panicButton.setFreezesText(false);
                    panicButton.startAnimation(pulse);
                    icon.startAnimation(pulse);
                }else
                {
                    panicButton.setFreezesText(true);
                    panicButton.clearAnimation();
                    icon.clearAnimation();
                }
                break;
        }
    }
}