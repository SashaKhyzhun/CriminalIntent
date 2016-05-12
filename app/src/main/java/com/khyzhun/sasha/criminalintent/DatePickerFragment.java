package com.khyzhun.sasha.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.jameskbride.criminalIntent.date";

    private Date crimeDate;

    public static DatePickerFragment newInstance(Date date) {
        DatePickerFragment datePickerFragment = new DatePickerFragment();

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        datePickerFragment.setArguments(args);

        return datePickerFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        crimeDate = (Date)getArguments().getSerializable(EXTRA_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(crimeDate);

        View dialogDateView = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);

        initializeDatePicker(calendar, dialogDateView);

        return new AlertDialog.Builder(getActivity())
                .setView(dialogDateView)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, crimeDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private void initializeDatePicker(Calendar calendar, View view) {
        DatePicker datePicker = (DatePicker)view.findViewById(R.id.dialog_date_picker);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker picker, int year, int month, int day) {
                        crimeDate = new GregorianCalendar(year, month, day).getTime();
                        getArguments().putSerializable(EXTRA_DATE, crimeDate);
                    }
                }
        );
    }
}