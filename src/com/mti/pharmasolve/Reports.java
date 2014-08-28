package com.mti.pharmasolve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Reports extends Activity {

	/************* Dates Declaration *****************/
	static int year, month, day;
	boolean fromDateFrom;

	Button btnTargetProducts;
	CheckBox chkTargetProducts;

	EditText edtFrom, edtTo;
	ImageView imgFrom, imgTo;
	
	String dateFrom = "", dateTo = "";

	void InitializeEverything() {
		fromDateFrom = false;
		edtFrom = (EditText) findViewById(R.id.reports_edtDateFrom);
		edtTo = (EditText) findViewById(R.id.reports_edtDateTo);
		edtFrom.setEnabled(false);
		edtTo.setEnabled(false);

		imgFrom = (ImageView) findViewById(R.id.reports_imgFrom);
		imgTo = (ImageView) findViewById(R.id.reports_imgTo);
		
		btnTargetProducts = (Button) findViewById(R.id.reports_btnTargetProducts);
		btnTargetProducts.setEnabled(false);
		
		chkTargetProducts = (CheckBox) findViewById(R.id.reports_chkTargetProducts);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reports);
		InitializeEverything();
		
		chkTargetProducts.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
					btnTargetProducts.setEnabled(true);
				else
					btnTargetProducts.setEnabled(false);
			}
		});

		imgFrom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				year = 2014;
				month = 6;
				day = 20;
				fromDateFrom = true;
				DialogFragment aDialogFragment = new StartDatePicker();
				aDialogFragment.show(getFragmentManager(), "start_date_picker");
			}
		});
		
		imgTo.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				

				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_YEAR);
				
				DialogFragment myDialogFragment = new StartDatePicker();
				myDialogFragment.show(getFragmentManager(), "start_date_picker");
			}
		});

		btnTargetProducts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dateFrom = edtFrom.getText().toString();
				dateTo = edtTo.getText().toString();
				
				//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date startDate = null, endDate = null;
				try {
					startDate = sdf.parse(dateFrom);
					endDate = sdf.parse(dateTo);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Toast.makeText(Reports.this, dateFrom + "\n" + dateTo , Toast.LENGTH_SHORT).show();
				

				if(dateFrom.equals("") && dateTo.equals(""))
				{
					Intent startTargetProductReport = new Intent(Reports.this, TargetProducts.class);
					startActivity(startTargetProductReport);
					finish();
				}
				else if(dateFrom.equals("") || dateTo.equals(""))
				{
					Toast.makeText(Reports.this, "Please Chose Both Dates!", Toast.LENGTH_LONG).show();
				}
				else if(endDate.before(startDate))
				{
					Toast.makeText(Reports.this, "Invalid Date Range!", Toast.LENGTH_SHORT).show();
					
				}
				else{
					Intent startTargetProductReport = new Intent(Reports.this, TargetProducts.class);
					startTargetProductReport.putExtra("putFrom", dateFrom);
					startTargetProductReport.putExtra("putTo", dateTo);
					startActivity(startTargetProductReport);
					finish();
				}
			}
		});
	}

	class StartDatePicker extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			// Use the current date as the default date in the picker
			DatePickerDialog dialog = new DatePickerDialog(Reports.this, this,
					year, month, day);
			return dialog;

		}

		public void onDateSet(DatePicker view, int yearOfDate, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			// Do something with the date chosen by the user
			
			
			
			if(fromDateFrom)
			{
				edtFrom.setText(yearOfDate +"-"+ (monthOfYear+1) + "-" + dayOfMonth);
				fromDateFrom = false;
			}else{
				edtTo.setText(yearOfDate +"-"+ (monthOfYear+1) + "-" + dayOfMonth);
			}
			
		}
	}

}
