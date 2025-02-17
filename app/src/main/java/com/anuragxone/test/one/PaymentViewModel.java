package com.anuragxone.test.one;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaymentViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Payment>> payments = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Double> totalAmount = new MutableLiveData<>(0.0);
    private final MutableLiveData<List<String>> paymentTypes = new MutableLiveData<>(new ArrayList<>(Arrays.asList("Cash", "Bank Transfer", "Credit Card")));
    private final PaymentRepository repository = new PaymentRepository(getApplication());

    public PaymentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<String>> getPaymentTypes() {
        return paymentTypes;
    }

    public LiveData<List<Payment>> getPayments() {
        return payments;
    }

    public LiveData<Double> getTotalAmount() {
        return totalAmount;
    }

    public void addPayment(Payment payment) {
        List<Payment> currentPayments = new ArrayList<>(payments.getValue());
        currentPayments.add(payment);
        payments.setValue(currentPayments);
        updateTotalAmount(currentPayments);
    }

    public void removePayment(Payment payment) {
        List<Payment> currentPayments = new ArrayList<>(payments.getValue());
        currentPayments.remove(payment);
        payments.setValue(currentPayments);
        updateTotalAmount(currentPayments);
    }

    public void removePaymentType(String type) {
        List<String> currentTypes = new ArrayList<>(paymentTypes.getValue());
        currentTypes.remove(type);
        paymentTypes.setValue(currentTypes);
    }

    private void updateTotalAmount(List<Payment> currentPayments) {
        double total = 0;
        for (Payment p : currentPayments) {
            total += p.getAmount();
        }
        totalAmount.setValue(total);
    }

    public void savePaymentsToFile(Context context) {
        repository.savePayments(context, payments.getValue());
    }

    public void loadPaymentsFromFile(Context context) {
        List<Payment> savedPayments = repository.loadPayments(context);
        if (savedPayments != null) {
            for (Payment payment : savedPayments) {
                removePaymentType(payment.getType());
            }
            payments.setValue(savedPayments);
            updateTotalAmount(savedPayments);
        }
    }

    public void addPaymentType(String type) {
        List<String> currentTypes = new ArrayList<>(paymentTypes.getValue());
        currentTypes.add(type);
        paymentTypes.setValue(currentTypes);
    }
}
