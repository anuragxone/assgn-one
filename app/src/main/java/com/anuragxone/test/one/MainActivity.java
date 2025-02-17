package com.anuragxone.test.one;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PaymentViewModel paymentViewModel;
    private TextView totalAmount;
    private LinearLayout chipGroup;
    private List<String> paymentTypesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        chipGroup = findViewById(R.id.chipGroup);
        totalAmount = findViewById(R.id.totalAmount);
        Button saveButton = findViewById(R.id.saveButton);
        TextView addPayment = findViewById(R.id.addPayment);

        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);

        paymentViewModel.loadPaymentsFromFile(this);

        final Observer<List<Payment>> paymentObserver = payments -> {
            chipGroup.removeAllViews();
            for (Payment payment : payments) {
                addPaymentChip(payment);
            }

        };

        final Observer<List<String>> paymentTypesObserver = paymentTypes -> {
            paymentTypesArray = paymentTypes;
            addPayment.setOnClickListener(v -> openAddPaymentDialog(paymentTypesArray));
        };

        paymentViewModel.getTotalAmount().observe(this, amount -> totalAmount.setText(" " + amount));
        paymentViewModel.getPayments().observe(this, paymentObserver);
        paymentViewModel.getPaymentTypes().observe(this, paymentTypesObserver);


        saveButton.setOnClickListener(v -> paymentViewModel.savePaymentsToFile(this));
    }

    private void addPaymentChip(Payment payment) {
        Chip chip = new Chip(this);
        chip.setText(payment.getType() + " = Rs. " + payment.getAmount());
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            paymentViewModel.removePayment(payment);
            paymentViewModel.addPaymentType(payment.getType());
            chipGroup.removeView(chip);
        });
        chipGroup.addView(chip);
    }


    private void openAddPaymentDialog(List<String> paymentTypes) {
        if (!paymentTypes.isEmpty()) {
            AddPaymentDialog dialog = new AddPaymentDialog(this, paymentViewModel, paymentTypes);
            dialog.show();
        }

    }
}