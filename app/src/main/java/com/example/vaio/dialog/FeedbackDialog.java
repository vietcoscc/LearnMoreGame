package com.example.vaio.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vaio.learnmoregame.R;

/**
 * Created by TungMai on 28/11/2016.
 */

public class FeedbackDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "FeedbackDialog";
    private View view;
    private EditText edtInput;
    private Button btnOk;
    private Button btnCancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_feedback, container, false);
        getDialog().setTitle("Phản hồi");
        initViews();
        return view;
    }

    private void initViews() {
        edtInput = (EditText) view.findViewById(R.id.edt_input);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                getDialog().dismiss();
                break;
            case R.id.btn_ok:
                String input = edtInput.getText().toString();
                if (input.isEmpty() || input.trim().isEmpty()) {
                    Toast.makeText(getDialog().getContext(), "Bạn chưa nhập phản hồi", Toast.LENGTH_SHORT).show();
                    return;
                }

                getDialog().dismiss();
                break;
        }
    }
}
