package com.example.lazismuapp.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazismuapp.R;

import java.text.NumberFormat;
import java.util.Locale;

public class Calculator extends AppCompatActivity {
    private EditText etSalary, etOtherIncome, etDebt;
    private EditText etDeposit, etGold, etProperty, etOtherAssets, etAnnualDebt;
    private TextView tvTotalAssetsMonthly, tvResultMonthly, tvTotalAssetsAnnual, tvResultAnnual;
    private Button btnCalculate;

    private static final double NISAB_BULANAN = 7968750; // Nisab bulanan
    private static final double NISAB_TAHUNAN = 95625000; // Nisab tahunan
    private static final double ZAKAT_PERCENTAGE = 0.025;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        etSalary = findViewById(R.id.et_salary);
        etOtherIncome = findViewById(R.id.et_other_income);
        etDebt = findViewById(R.id.et_debt);

        etDeposit = findViewById(R.id.et_deposit);
        etGold = findViewById(R.id.et_gold);
        etProperty = findViewById(R.id.et_property);
        etOtherAssets = findViewById(R.id.et_other_assets);
        etAnnualDebt = findViewById(R.id.et_annual_debt);

        tvTotalAssetsMonthly = findViewById(R.id.tv_total_assets_monthly);
        tvResultMonthly = findViewById(R.id.tv_result_monthly);
        tvTotalAssetsAnnual = findViewById(R.id.tv_total_assets_annual);
        tvResultAnnual = findViewById(R.id.tv_result_annual);

        btnCalculate = findViewById(R.id.btn_calculate);

        // Setup format uang untuk EditText yang membutuhkan
        setupCurrencyWatcher(etSalary);
        setupCurrencyWatcher(etOtherIncome);
        setupCurrencyWatcher(etDebt);
        setupCurrencyWatcher(etDeposit);
        setupCurrencyWatcher(etGold);
        setupCurrencyWatcher(etProperty);
        setupCurrencyWatcher(etOtherAssets);
        setupCurrencyWatcher(etAnnualDebt);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateZakat();
            }
        });
    }

    private void setupCurrencyWatcher(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    editText.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.\\s]", "");

                    // Jika cleanString tidak kosong, format angka dengan pemisah ribuan
                    if (!TextUtils.isEmpty(cleanString)) {
                        double parsed = Double.parseDouble(cleanString) / 100;
                        String formatted = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(parsed);

                        current = formatted;
                        editText.setText(formatted);
                        editText.setSelection(formatted.length());
                    } else {
                        current = "";
                        editText.setText("");
                    }

                    editText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void calculateZakat() {
        // Ambil nilai numerik dari EditText yang sudah diformat
        double salary = getNumericValue(etSalary);
        double otherIncome = getNumericValue(etOtherIncome);
        double debt = getNumericValue(etDebt);
        double deposit = getNumericValue(etDeposit);
        double gold = getNumericValue(etGold);
        double property = getNumericValue(etProperty);
        double otherAssets = getNumericValue(etOtherAssets);
        double annualDebt = getNumericValue(etAnnualDebt);

        // Perhitungan Zakat Profesi (Bulanan)
        double totalAssetsMonthly = salary + otherIncome - debt;
        tvTotalAssetsMonthly.setText("Total Harta Bulanan: " + formatCurrency(totalAssetsMonthly));

        double zakatMonthly = 0;
        if (totalAssetsMonthly >= NISAB_BULANAN) {
            zakatMonthly = totalAssetsMonthly * ZAKAT_PERCENTAGE;
        }
        tvResultMonthly.setText("Nilai Zakat Bulanan: " + formatCurrency(zakatMonthly));

        // Perhitungan Zakat Maal (Tahunan)
        double totalAssetsAnnual = deposit + gold + property + otherAssets - annualDebt;
        tvTotalAssetsAnnual.setText("Total Harta Tahunan: " + formatCurrency(totalAssetsAnnual));

        double zakatAnnual = 0;
        if (totalAssetsAnnual >= NISAB_TAHUNAN) {
            zakatAnnual = totalAssetsAnnual * ZAKAT_PERCENTAGE;
        }
        tvResultAnnual.setText("Nilai Zakat Tahunan: " + formatCurrency(zakatAnnual));

        // Total zakat yang harus dibayar
        double zakatMonthlyValue = zakatMonthly;
        double zakatAnnualValue = zakatAnnual;
        double totalZakat = zakatMonthlyValue * 12 + zakatAnnualValue;
        Toast.makeText(this, "Total Zakat yang harus dibayar: " + formatCurrency(totalZakat), Toast.LENGTH_LONG).show();
    }

    private double getNumericValue(EditText editText) {
        String cleanString = editText.getText().toString().replaceAll("[Rp,.\\s]", "");

        // Hapus dua karakter terakhir dari string jika panjangnya cukup
        if (cleanString.length() > 2) {
            cleanString = cleanString.substring(0, cleanString.length() - 2);
        } else {
            // Jika panjang string tidak cukup, set nilai cleanString menjadi kosong
            cleanString = "";
        }

        try {
            return Double.parseDouble(cleanString);
        } catch (NumberFormatException e) {
            return 0;
        }
    }



    private String formatCurrency(double value) {
        // Menggunakan format uang Rupiah
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formatted = formatter.format(value);

        // Hilangkan digit desimal yang ditambahkan otomatis
        if (formatted.endsWith(",00")) {
            formatted = formatted.substring(0, formatted.length() - 3);
        }

        return formatted;
    }
}
