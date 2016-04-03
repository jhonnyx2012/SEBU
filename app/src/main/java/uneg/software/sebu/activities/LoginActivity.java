package uneg.software.sebu.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uneg.software.sebu.R;
import uneg.software.sebu.fragments.RecuperarDialogFragment;
import uneg.software.sebu.interfaces.ApiRestInterface;
import uneg.software.sebu.models.LoginResponse;
import uneg.software.sebu.utils.PermissionsDispatcher;
import uneg.software.sebu.utils.UserSessionManager;
import uneg.software.sebu.views.CustomFontEditText;
import uneg.software.sebu.views.CustomFontTextView;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener, Validator.ValidationListener {

    @InjectView(R.id.olvide)
    CustomFontTextView olvide;
    @InjectView(R.id.iniciar_sesion)
    Button login;

    @InjectView(R.id.username)
    @NotEmpty
    CustomFontEditText username;

    @InjectView(R.id.password)
    @NotEmpty
    CustomFontEditText password;
    private Validator validator;

    ProgressDialog progressDialog;


    private void mostrarEspera(String s)
    {
        progressDialog = ProgressDialog.show(this, "",s, true);
    }

    private void ocultarEspera()
    {
        progressDialog.dismiss();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserSessionManager session=new UserSessionManager(this);
        if(session.isUserLoggedIn())
        {
            Intent i = new Intent(this, PanicButtonActivity.class);
            startActivity(i);
            finish();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        login.setOnClickListener(this);
        olvide.setOnClickListener(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        PermissionsDispatcher.showDialogPermissions(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iniciar_sesion:

                validator.validate();
                break;
            case R.id.olvide:
                showDialogFragment(RecuperarDialogFragment.newInstance());
                break;
        }
    }


    private void showDialogFragment(DialogFragment dialogFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(dialogFragment, dialogFragment.getTag());
        ft.commitAllowingStateLoss();
    }

    private void next()
    {
        UserSessionManager sesion=new UserSessionManager(this);
        Intent i=new Intent(this,PanicButtonActivity.class);
        startActivity(i);
        finish();
    }
    private void doLoging()
    {
        mostrarEspera("Iniciando sesión...");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.url))
                .build();
        restAdapter.create(ApiRestInterface.class).doLogin(
                username.getText().toString(),
                password.getText().toString(),
                mCallBack);
    }

    private Callback<LoginResponse> mCallBack=new Callback<LoginResponse>() {
        @Override
        public void success(LoginResponse r, Response response) {
            ocultarEspera();
                if(r.isStatus())
                {
                    if(!r.getUser().getNivel().getId().equals("5"))
                    {
                        Toast.makeText(LoginActivity.this,"Usuario no autorizado",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    UserSessionManager sesion=new UserSessionManager(LoginActivity.this);
                    sesion.setCorreo(r.getUser().getCorreo());
                    sesion.setIdUser(r.getUser().getId());
                    sesion.setUsername(r.getUser().getUsername());
                    sesion.setNombre(r.getUser().getNombre());
                    sesion.setApellido(r.getUser().getApellido());
                    sesion.setTelefono(r.getUser().getTelefono());
                    if(r.getUser().getNivel()!=null)
                    {
                        sesion.setNivelUser(r.getUser().getNivel().getId());
                        sesion.setDescripcionNivel(r.getUser().getNivel().getDescripcion());
                    }

                    sesion.setUserLoggedIn(true);
                    next();
                }
            Toast.makeText(LoginActivity.this,r.getMessage(),Toast.LENGTH_SHORT).show();



        }

        @Override
        public void failure(RetrofitError error) {
            Log.i("LOGIN",error.getMessage());
            Toast.makeText(LoginActivity.this,"Ha habido un problema",Toast.LENGTH_SHORT).show();
            ocultarEspera();
        }
    };


    @Override
    public void onValidationSucceeded() {
        doLoging();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
