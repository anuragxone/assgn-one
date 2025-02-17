package com.anuragxone.test.one;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import java.util.List;

public class AddPaymentDialog extends Dialog {
    private final PaymentViewModel paymentViewModel;
    private final List<String> paymentTypes;

    public AddPaymentDialog(@NonNull Context context, PaymentViewModel viewModel, List<String> paymentTypes) {
        super(context);
        this.paymentViewModel = viewModel;
        this.paymentTypes = paymentTypes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_payment);

        EditText amountInput = findViewById(R.id.amountInput);
        EditText providerInput = findViewById(R.id.providerInput);
        EditText tranxInput = findViewById(R.id.transactionRefInput);
        Button okButton = findViewById(R.id.okButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        Spinner paymentTypeSpinner = findViewById(R.id.paymentTypeSpinner);
        LinearLayout extraFieldsLayout = findViewById(R.id.extraFieldsLayout);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, paymentTypes);
        paymentTypeSpinner.setAdapter(adapter);


        cancelButton.setOnClickListener(v -> dismiss());

        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            int cashPos = adapter.getPosition("Cash");

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != cashPos) {
                    extraFieldsLayout.setVisibility(View.VISIBLE);
                } else {
                    extraFieldsLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        okButton.setOnClickListener(v -> {
            String type = paymentTypeSpinner.getSelectedItem().toString();
            Payment payment;
            if (amountInput.getText().toString().isEmpty()) {
                dismiss();
            } else {
                double amount = Double.parseDouble(amountInput.getText().toString());
                if (type != "Cash") {
                    payment = new Payment(type, amount, providerInput.getText().toString(), tranxInput.getText().toString());
                } else {
                    payment = new Payment(type, amount);
                }

                paymentViewModel.addPayment(payment);
                paymentViewModel.removePaymentType(type);
                dismiss();
            }

        });

    }


}