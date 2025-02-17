package com.anuragxone.test.one;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class PaymentRepository {
    private static final String FILE_NAME = "LastPayment.txt";
    private final Gson gson = new Gson();
    private final Context context;

    public PaymentRepository(Context context) {
        this.context = context;
    }

    public void savePayments(Context context, List<Payment> payments) {
        String json = gson.toJson(payments);
        File file = new File(context.getFilesDir(), FILE_NAME);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
            Toast.makeText(context, "Payments saved successfully!", Toast.LENGTH_SHORT).show();
            Log.d("PaymentRepository", "Payments saved successfully!");
        } catch (IOException e) {
            Toast.makeText(context, "Error saving payments!", Toast.LENGTH_SHORT).show();
            Log.e("PaymentRepository", "Error saving payments", e);
        }
    }

    public List<Payment> loadPayments(Context context) {
        File file = new File(context.getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            Toast.makeText(context, "No saved payments found.", Toast.LENGTH_SHORT).show();
            Log.d("PaymentRepository", "No previous payments found.");
            return null;
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Payment>>() {
            }.getType();
            List<Payment> savedPayments = gson.fromJson(reader, listType);
            Toast.makeText(context, "Payments loaded successfully!", Toast.LENGTH_SHORT).show();
            Log.d("PaymentRepository", "Payments loaded successfully!");
            return savedPayments;
        } catch (IOException e) {
            Toast.makeText(context, "Error loading payments!", Toast.LENGTH_SHORT).show();
            Log.e("PaymentRepository", "Error loading payments", e);
            return null;
        }
    }

}
