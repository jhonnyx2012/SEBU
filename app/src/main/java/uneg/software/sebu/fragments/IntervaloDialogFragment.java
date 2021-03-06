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

import java.util.List;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import butterknife.ButterKnife;
import butterknife.InjectView;
import uneg.software.sebu.R;
import uneg.software.sebu.utils.UserSessionManager;


/**
 * Created by Jhonny on 01-08-2015.
 */
public class IntervaloDialogFragment extends DialogFragment implements View.OnClickListener, Validator.ValidationListener {
    public static final String TAG = IntervaloDialogFragment.class.getSimpleName();
    @InjectView(R.id.cambiar_mensaje)
    Button cambiar_intervalo;
    @InjectView(R.id.mensaje)
    MaterialNumberPicker intervalo;
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
        View view = inflater.inflate(R.layout.dialog_fragment_intervalo, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        ButterKnife.inject(this, view);
        session=new UserSessionManager(getActivity());

        if(!session.getIntervalo().equals("")){

            intervalo.setValue(Integer.parseInt(session.getIntervalo()));
        }

        cambiar_intervalo.setOnClickListener(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        return view;
    }


    public static IntervaloDialogFragment newInstance() {
        return new IntervaloDialogFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.cambiar_mensaje:
                session.setIntervalo(String.valueOf(intervalo.getValue()));
                this.dismiss();
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
        session.setIntervalo(String.valueOf(intervalo.getValue()));
        this.dismiss();

    }

}