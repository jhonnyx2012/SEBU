package uneg.software.sebu.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uneg.software.sebu.R;
import uneg.software.sebu.interfaces.ApiRestInterface;
import uneg.software.sebu.models.BaseResponse;
import uneg.software.sebu.views.CustomFontEditText;


/**
 * Created by Jhonny on 01-08-2015.
 */
public class RecuperarDialogFragment extends DialogFragment implements View.OnClickListener, Validator.ValidationListener {
    public static final String TAG = RecuperarDialogFragment.class.getSimpleName();
    @InjectView(R.id.solicitar)
    Button solicitar_button;
    @InjectView(R.id.email)
    @NotEmpty
    @Email
    CustomFontEditText email;
    private Validator validator;
    private ProgressDialog progressDialog;


    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_AppCompat_DropDownUp;
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_recuperar, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        ButterKnife.inject(this, view);
        solicitar_button.setOnClickListener(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        return view;
    }


    public static RecuperarDialogFragment newInstance() {
        return new RecuperarDialogFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.solicitar:
                validator.validate();
                break;
        }
    }



    @Override
    public void onValidationSucceeded() {
        solicitarCambio();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }


    private void solicitarCambio()
    {
        mostrarEspera("Solicitando cambio de contraseña...");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.url))
                .build();
        restAdapter.create(ApiRestInterface.class).solicitarCambioPass(
                email.getText().toString().trim(),
                mCallBack);
    }

    private void mostrarEspera(String s)
    {
        progressDialog = ProgressDialog.show(getActivity(), "", s, true);
    }

    private void ocultarEspera()
    {
        progressDialog.dismiss();
    }

    private Callback<BaseResponse> mCallBack=new Callback<BaseResponse>() {
        @Override
        public void success(BaseResponse r, Response response) {
            ocultarEspera();
            if(r.isStatus())dismiss();
            Toast.makeText(getActivity(),r.getMessage(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void failure(RetrofitError error) {
            ocultarEspera();
            Log.i("RecuperarDialog",error.getMessage());
            Toast.makeText(getActivity(),"Ha habido un problema",Toast.LENGTH_SHORT).show();
        }
    };
}