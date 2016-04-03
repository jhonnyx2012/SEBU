package uneg.software.sebu.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uneg.software.sebu.R;
import uneg.software.sebu.utils.UserSessionManager;
import uneg.software.sebu.views.CustomFontEditText;


/**
 * Created by Jhonny on 01-08-2015.
 */
public class MensajeDialogFragment extends DialogFragment implements View.OnClickListener, Validator.ValidationListener {
    public static final String TAG = MensajeDialogFragment.class.getSimpleName();
    @InjectView(R.id.cambiar_mensaje)
    Button cambiar_mensaje;
    @InjectView(R.id.mensaje)
    @NotEmpty
    CustomFontEditText mensaje;
    private Validator validator;
    UserSessionManager session;


    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_AppCompat_DropDownUp;
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_mensaje, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        ButterKnife.inject(this, view);
        session=new UserSessionManager(getActivity());

        if(!session.getMensaje().equals("")){

            mensaje.setText(session.getMensaje());
        }

        cambiar_mensaje.setOnClickListener(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        return view;
    }


    public static MensajeDialogFragment newInstance() {
        return new MensajeDialogFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.cambiar_mensaje:
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
        session.setgetMensaje(mensaje.getText().toString());
        this.dismiss();

    }

}