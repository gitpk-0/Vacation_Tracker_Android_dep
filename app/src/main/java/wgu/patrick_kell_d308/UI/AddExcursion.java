package wgu.patrick_kell_d308.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import wgu.patrick_kell_d308.Database.Repository;
import wgu.patrick_kell_d308.Entities.Excursion;
import wgu.patrick_kell_d308.R;

/**
 * @author Patrick Kell
 */
public class AddExcursion extends AppCompatActivity {

    EditText excursionTitle;
    String title;
    String date;
    int excursionId;
    int vacationId;

    Intent fromVacationDetails;
    int associatedVacationID;

    Button excursionDatePickerBtn;

    Repository repo = new Repository(getApplication());

    String dateFormat = "MM/dd/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    String currentDate = sdf.format(new Date());
    final Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        fromVacationDetails = getIntent();
        associatedVacationID = Integer.parseInt(fromVacationDetails.getStringExtra("id"));

        excursionTitle = findViewById(R.id.excursionTitle);
        excursionDatePickerBtn = findViewById(R.id.excursionDatePickerBtn);
        excursionDatePickerBtn.setText(currentDate);

        excursionDatePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddExcursion.this, dateListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel(excursionDatePickerBtn);
            }
        };
    }

    private void updateLabel(Button datePicker) {
        datePicker.setText(sdf.format(calendar.getTime()));
    }

    public void onSave(View view) {
        excursionId = getIntent().getIntExtra("id", -1);
        title = excursionTitle.getText().toString();
        date = excursionDatePickerBtn.getText().toString();
        fromVacationDetails = getIntent();
        vacationId = Integer.parseInt(fromVacationDetails.getStringExtra("id"));


        if (excursionId == -1) {
            Excursion newExcursion = new Excursion(0, title, date, associatedVacationID);
            repo.insert(newExcursion);
            Toast.makeText(this, "New Excursion Added", Toast.LENGTH_LONG).show();
        } else {
            Excursion updatedExcursion = new Excursion(excursionId, title, date, associatedVacationID);
            repo.update(updatedExcursion);
            Toast.makeText(this, "Excursion Updated", Toast.LENGTH_LONG).show();
        }


        Intent backToExcursionDashboard = new Intent(AddExcursion.this, ExcursionDashboard.class);
        String associatedVacationID = fromVacationDetails.getStringExtra("id");
        backToExcursionDashboard.putExtra("id", associatedVacationID);
        startActivity(backToExcursionDashboard);
    }
}
