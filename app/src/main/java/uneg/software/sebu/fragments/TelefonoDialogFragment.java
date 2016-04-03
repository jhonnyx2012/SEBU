package uneg.software.sebu.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.orm.SugarContext;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uneg.software.sebu.R;
import uneg.software.sebu.adapters.TelefonosAdapter;
import uneg.software.sebu.interfaces.RecyclerItemClickListener;
import uneg.software.sebu.models.bd.Telefono;
import uneg.software.sebu.views.CustomFontEditText;

/**
 * Created by Kenny Perroni on 03/04/2016.
 */
public class TelefonoDialogFragment extends DialogFragment implements View.OnClickListener, Validator.ValidationListener, RecyclerItemClickListener {

    public static final String TAG = TelefonoDialogFragment.class.getSimpleName();
    private Validator validator;
    @InjectView(R.id.almacenar_telefonos)
    Button almacenarTelefonos;
    @InjectView(R.id.telefono)
    @NotEmpty
    CustomFontEditText telefono;
    @InjectView(R.id.telefonos)
    RecyclerView lista;
    private RecyclerView.LayoutManager mLayoutManager;
    private TelefonosAdapter adapter;
    
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.Animation_AppCompat_DropDownUp;
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_telefono, null);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        ButterKnife.inject(this, view);
        almacenarTelefonos.setOnClickListener(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
        lista.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        lista.setLayoutManager(mLayoutManager);
        refresh();
        return view;
    }

    private void refresh() {
        SugarContext.init(getActivity());
        List<Telefono> telefonos = Telefono.listAll(Telefono.class);
        SugarContext.terminate();
        adapter = new TelefonosAdapter(getActivity(), telefonos, this);
        lista.setAdapter(adapter);

    }


    public static TelefonoDialogFragment newInstance() {
        return new TelefonoDialogFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.almacenar_telefonos:
                validator.validate();
                break;
        }
    }

    private void guardarTelefono(){


        SugarContext.init(getActivity());
        new Telefono(telefono.getText().toString().trim()).save();
        SugarContext.terminate();


    }


    @Override
    public void onValidationSucceeded() {
        guardarTelefono();
        refresh();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

    }

    @Override
    public void onItemClick(Telefono item) {

        SugarContext.init(getActivity());
        item.delete();
        SugarContext.terminate();
        refresh();

    }
}
