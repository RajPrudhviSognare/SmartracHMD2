package com.experis.smartrac_HMD2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;

public class KYCActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    private EditText KYCPagePANNoValueEditTextID;
    private EditText KYCPageAadharNoValueEditTextID;
    private EditText KYCPageHighestQualificationValueEditTextID;
    private EditText KYCPageTotalExperienceValueEditTextID;
    private EditText KYCPageLastOrganizationNameValueEditTextID;
    private EditText KYCPageLastOrganizationemploymentperiodValueEditTextID;
    private EditText KYCPageLastOrganizationSalaryCTCValueEditTextID;
    //private EditText KYCPageLastOrganizationSalaryGROSSValueEditTextID;
    //private EditText KYCPageLastOrganizationSalaryNTHValueEditTextID;

    private RadioGroup MaritalStatusRadioGroupID;
    private RadioButton MaritalStatusUnmarriedRadioButtonID;
    private RadioButton MaritalStatusMarriedRadioButtonID;

    private EditText KYCPageSpouseDetailsNameValueEditTextID;
    private EditText KYCPageSpouseDetailsGenderValueEditTextID;
    private EditText KYCPageSpouseDetailsDOBValueEditTextID;

    private EditText KYCPageChild1DetailsNameValueEditTextID;
    private EditText KYCPageChild1DetailsGenderValueEditTextID;
    private EditText KYCPageChild1DetailsDOBValueEditTextID;

    private EditText KYCPageChild2DetailsNameValueEditTextID;
    private EditText KYCPageChild2DetailsGenderValueEditTextID;
    private EditText KYCPageChild2DetailsDOBValueEditTextID;

    private EditText KYCPageAlternateContactNumberValueEditTextID;
    private EditText KYCPageAlternateContactNumberNameValueEditTextID;
    private EditText KYCPageAlternateContactNumberRelationValueEditTextID;

    private ImageView KYCPageSubmitImageViewID;
    private ImageView KYCtopbarBackiconImageViewID;

    private LinearLayout FamilyDetailsLinearLayoutID;

    private Spinner KYCPagePANNoSpinnerID;
    private LinearLayout KYCPagePANNoLinearLayoutID;
    private Spinner KYCPageAadharNoSpinnerID;
    private LinearLayout KYCPageAadharNoLinearLayoutID;
    private Spinner KYCPageHighestQualificationSpinnerID;
    private EditText changePasswordPageNewPasswordEditTextID;
    private EditText changePasswordPageConfirmPasswordEditTextID;

    private RestFullClient client;
    private String TAG_STATUS = "status";
    private String TAG_ERROR = "error";
    private String TAG_MESSAGE = "message";

    private String STATUS = "";
    private String ERROR = "";
    private String MESSAGE = "";

    private Calendar calendar;
    private int year, month, day;
    private String date = null;

    private String PANNO = "";
    private String AADHARNO = "";
    //private String HIGHEST_QUALIFICATION = "";
    private String TOTAL_EXP = "";
    private String LAST_ORGANIZATION_NAME = "";
    private String LAST_ORGANIZATION_EMP_PERIOD = "";
    private String LAST_ORGANIZATION_SAL_CTC = "";
    private String LAST_ORGANIZATION_SAL_GROSS = "";
    private String LAST_ORGANIZATION_SAL_NTH = "";
    private String MARITAL_STATUS = "Single";

    private String SPOUSE_NAME = "";
    private String SPOUSE_GENDER = "";
    private String SPOUSE_DOB = "";

    private String CHILD1_NAME = "";
    private String CHILD1_GENDER = "";
    private String CHILD1_DOB = "";

    private String CHILD2_NAME = "";
    private String CHILD2_GENDER = "";
    private String CHILD2_DOB = "";

    private String ALTERNATE_NAME = "";
    private String ALTERNATE_CONTACTNO = "";
    private String ALTERNATE_RELATION = "";

    private String PAN_STATUS = "applied_for";
    private String AADHAR_STATUS = "applied_for";
    private String HIGHEST_QUALIFICATION = "10th Pass";
    private String NEW_PASSWORD = "";
    private String NEW_PASSWORD1 = "";
    private String NEW_PASSWORD2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);

        initAllViews();

        //Single/Married Checker
        MaritalStatusRadioGroupID.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.MaritalStatusUnmarriedRadioButtonID) {
                    //Toast.makeText(KYCActivity.this, "Single", Toast.LENGTH_SHORT).show();
                    FamilyDetailsLinearLayoutID.setVisibility(View.GONE);
                    MARITAL_STATUS = "Single";
                    System.out.println("MARITAL_STATUS: "+MARITAL_STATUS);
                }
                else if(checkedId == R.id.MaritalStatusMarriedRadioButtonID) {
                    //Toast.makeText(KYCActivity.this, "Married", Toast.LENGTH_SHORT).show();
                    FamilyDetailsLinearLayoutID.setVisibility(View.VISIBLE);
                    MARITAL_STATUS = "Married";
                    System.out.println("MARITAL_STATUS: "+MARITAL_STATUS);
                }
            }
        });

        //Spouse DOB
        KYCPageSpouseDetailsDOBValueEditTextID.setOnClickListener(new EditText.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(KYCActivity.this,datePickerListener, year, month, day);
                dialog.show();
            }
        });
        KYCPageSpouseDetailsDOBValueEditTextID.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DatePickerDialog dialog = new DatePickerDialog(KYCActivity.this, datePickerListener, year, month, day);
                dialog.show();
            }
        });

        //Child-1 DOB
        KYCPageChild1DetailsDOBValueEditTextID.setOnClickListener(new EditText.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(KYCActivity.this,datePickerListener1, year, month, day);
                dialog.show();
            }
        });
        KYCPageChild1DetailsDOBValueEditTextID.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DatePickerDialog dialog = new DatePickerDialog(KYCActivity.this, datePickerListener1, year, month, day);
                dialog.show();
            }
        });

        //Child-2 DOB
        KYCPageChild2DetailsDOBValueEditTextID.setOnClickListener(new EditText.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(KYCActivity.this,datePickerListener2, year, month, day);
                dialog.show();
            }
        });
        KYCPageChild2DetailsDOBValueEditTextID.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DatePickerDialog dialog = new DatePickerDialog(KYCActivity.this, datePickerListener2, year, month, day);
                dialog.show();
            }
        });

        //Submit Button Click
        KYCPageSubmitImageViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(CommonUtils.isInternelAvailable(KYCActivity.this)){
                        validateData();
                    }
                    else{
                        Toast.makeText(KYCActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        //Back Button Click Logic
        KYCtopbarBackiconImageViewID.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //PAN Spinner
        KYCPagePANNoSpinnerID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    PAN_STATUS = "applied_for";
                    System.out.println("PAN_STATUS: "+PAN_STATUS);
                    KYCPagePANNoValueEditTextID.setVisibility(View.GONE);
                    KYCPagePANNoLinearLayoutID.setVisibility(View.GONE);
                    PANNO = "";
                }
                if(position==1){
                    PAN_STATUS = "number";
                    System.out.println("PAN_STATUS: "+PAN_STATUS);
                    KYCPagePANNoValueEditTextID.setVisibility(View.VISIBLE);
                    KYCPagePANNoLinearLayoutID.setVisibility(View.VISIBLE);
                }
                if(position==2){
                    PAN_STATUS = "does_not_have";
                    System.out.println("PAN_STATUS: "+PAN_STATUS);
                    KYCPagePANNoValueEditTextID.setVisibility(View.GONE);
                    KYCPagePANNoLinearLayoutID.setVisibility(View.GONE);
                    PANNO = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //AADHAR Spinner
        KYCPageAadharNoSpinnerID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    AADHAR_STATUS = "applied_for";
                    System.out.println("AADHAR_STATUS: "+AADHAR_STATUS);
                    KYCPageAadharNoValueEditTextID.setVisibility(View.GONE);
                    KYCPageAadharNoLinearLayoutID.setVisibility(View.GONE);
                    AADHARNO = "";
                }
                if(position==1){
                    AADHAR_STATUS = "number";
                    System.out.println("AADHAR_STATUS: "+AADHAR_STATUS);
                    KYCPageAadharNoLinearLayoutID.setVisibility(View.VISIBLE);
                    KYCPageAadharNoValueEditTextID.setVisibility(View.VISIBLE);
                }
                if(position==2){
                    AADHAR_STATUS = "does_not_have";
                    System.out.println("AADHAR_STATUS: "+AADHAR_STATUS);
                    KYCPageAadharNoValueEditTextID.setVisibility(View.GONE);
                    KYCPageAadharNoLinearLayoutID.setVisibility(View.GONE);
                    AADHARNO = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Highest Qualification Spinner
        KYCPageHighestQualificationSpinnerID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    HIGHEST_QUALIFICATION = "10th Pass";
                    System.out.println("HIGHEST_QUALIFICATION: "+HIGHEST_QUALIFICATION);
                }
                if(position==1){
                    HIGHEST_QUALIFICATION = "12th Pass";
                    System.out.println("HIGHEST_QUALIFICATION: "+HIGHEST_QUALIFICATION);
                }
                if(position==2){
                    HIGHEST_QUALIFICATION = "Graduate";
                    System.out.println("HIGHEST_QUALIFICATION: "+HIGHEST_QUALIFICATION);
                }
                if(position==3){
                    HIGHEST_QUALIFICATION = "Post Graduate";
                    System.out.println("HIGHEST_QUALIFICATION: "+HIGHEST_QUALIFICATION);
                }
                if(position==4){
                    HIGHEST_QUALIFICATION = "Diploma";
                    System.out.println("HIGHEST_QUALIFICATION: "+HIGHEST_QUALIFICATION);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }//onCreate()

    private void initAllViews() {
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME, MODE_PRIVATE);
        prefsEditor = prefs.edit();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        KYCPagePANNoValueEditTextID = (EditText)findViewById(R.id.KYCPagePANNoValueEditTextID);
        KYCPageAadharNoValueEditTextID = (EditText)findViewById(R.id.KYCPageAadharNoValueEditTextID);
        KYCPageHighestQualificationValueEditTextID = (EditText)findViewById(R.id.KYCPageHighestQualificationValueEditTextID);
        KYCPageTotalExperienceValueEditTextID = (EditText)findViewById(R.id.KYCPageTotalExperienceValueEditTextID);
        KYCPageLastOrganizationNameValueEditTextID = (EditText)findViewById(R.id.KYCPageLastOrganizationNameValueEditTextID);
        KYCPageLastOrganizationemploymentperiodValueEditTextID = (EditText)findViewById(R.id.KYCPageLastOrganizationemploymentperiodValueEditTextID);
        KYCPageLastOrganizationSalaryCTCValueEditTextID = (EditText)findViewById(R.id.KYCPageLastOrganizationSalaryCTCValueEditTextID);
        //KYCPageLastOrganizationSalaryGROSSValueEditTextID = (EditText)findViewById(R.id.KYCPageLastOrganizationSalaryGROSSValueEditTextID);
        //KYCPageLastOrganizationSalaryNTHValueEditTextID = (EditText)findViewById(R.id.KYCPageLastOrganizationSalaryNTHValueEditTextID);

        KYCPageSpouseDetailsNameValueEditTextID = (EditText)findViewById(R.id.KYCPageSpouseDetailsNameValueEditTextID);
        KYCPageSpouseDetailsGenderValueEditTextID = (EditText)findViewById(R.id.KYCPageSpouseDetailsGenderValueEditTextID);
        KYCPageSpouseDetailsDOBValueEditTextID = (EditText)findViewById(R.id.KYCPageSpouseDetailsDOBValueEditTextID);

        KYCPageChild1DetailsNameValueEditTextID = (EditText)findViewById(R.id.KYCPageChild1DetailsNameValueEditTextID);
        KYCPageChild1DetailsGenderValueEditTextID = (EditText)findViewById(R.id.KYCPageChild1DetailsGenderValueEditTextID);
        KYCPageChild1DetailsDOBValueEditTextID = (EditText)findViewById(R.id.KYCPageChild1DetailsDOBValueEditTextID);

        KYCPageChild2DetailsNameValueEditTextID = (EditText)findViewById(R.id.KYCPageChild2DetailsNameValueEditTextID);
        KYCPageChild2DetailsGenderValueEditTextID = (EditText)findViewById(R.id.KYCPageChild2DetailsGenderValueEditTextID);
        KYCPageChild2DetailsDOBValueEditTextID = (EditText)findViewById(R.id.KYCPageChild2DetailsDOBValueEditTextID);

        KYCPageAlternateContactNumberValueEditTextID = (EditText)findViewById(R.id.KYCPageAlternateContactNumberValueEditTextID);
        KYCPageAlternateContactNumberNameValueEditTextID = (EditText)findViewById(R.id.KYCPageAlternateContactNumberNameValueEditTextID);
        KYCPageAlternateContactNumberRelationValueEditTextID = (EditText)findViewById(R.id.KYCPageAlternateContactNumberRelationValueEditTextID);

        KYCtopbarBackiconImageViewID = (ImageView)findViewById(R.id.KYCtopbarBackiconImageViewID);
        KYCPageSubmitImageViewID = (ImageView)findViewById(R.id.KYCPageSubmitImageViewID);

        MaritalStatusRadioGroupID = (RadioGroup)findViewById(R.id.MaritalStatusRadioGroupID);
        MaritalStatusUnmarriedRadioButtonID = (RadioButton)findViewById(R.id.MaritalStatusUnmarriedRadioButtonID);
        MaritalStatusMarriedRadioButtonID = (RadioButton)findViewById(R.id.MaritalStatusMarriedRadioButtonID);

        FamilyDetailsLinearLayoutID = (LinearLayout)findViewById(R.id.FamilyDetailsLinearLayoutID);

        KYCPagePANNoSpinnerID = (Spinner)findViewById(R.id.KYCPagePANNoSpinnerID);
        KYCPagePANNoLinearLayoutID = (LinearLayout)findViewById(R.id.KYCPagePANNoLinearLayoutID);
        KYCPageAadharNoSpinnerID = (Spinner)findViewById(R.id.KYCPageAadharNoSpinnerID);
        KYCPageAadharNoLinearLayoutID = (LinearLayout)findViewById(R.id.KYCPageAadharNoLinearLayoutID);
        KYCPageHighestQualificationSpinnerID = (Spinner)findViewById(R.id.KYCPageHighestQualificationSpinnerID);

        changePasswordPageNewPasswordEditTextID = (EditText)findViewById(R.id.changePasswordPageNewPasswordEditTextID);
        changePasswordPageConfirmPasswordEditTextID = (EditText)findViewById(R.id.changePasswordPageConfirmPasswordEditTextID);

        //Progress Dialog
        progressDialog = new ProgressDialog(KYCActivity.this);
    }

    //Validate Data locally(Checks whether the fields are empty or not)
    private void validateData() {
        boolean cancel = false;
        View focusView = null;

        if(AADHAR_STATUS.equalsIgnoreCase("number")){
            if(TextUtils.isEmpty(KYCPageAadharNoValueEditTextID.getText().toString()))
            {
                KYCPageAadharNoValueEditTextID.setError("Required field!");
                focusView = KYCPageAadharNoValueEditTextID;
                cancel = true;
            }
        }

        if(PAN_STATUS.equalsIgnoreCase("number")){
            if(TextUtils.isEmpty(KYCPagePANNoValueEditTextID.getText().toString()))
            {
                KYCPagePANNoValueEditTextID.setError("Required field!");
                focusView = KYCPagePANNoValueEditTextID;
                cancel = true;
            }
        }

        /*if(TextUtils.isEmpty(KYCPageHighestQualificationValueEditTextID.getText().toString()))
        {
            KYCPageHighestQualificationValueEditTextID.setError("Required field!");
            focusView = KYCPageHighestQualificationValueEditTextID;
            cancel = true;
        }*/

        if(TextUtils.isEmpty(KYCPageTotalExperienceValueEditTextID.getText().toString()))
        {
            KYCPageTotalExperienceValueEditTextID.setError("Required field!");
            focusView = KYCPageTotalExperienceValueEditTextID;
            cancel = true;
        }
        else if(TextUtils.isEmpty(KYCPageLastOrganizationNameValueEditTextID.getText().toString()))
        {
            KYCPageLastOrganizationNameValueEditTextID.setError("Required field!");
            focusView = KYCPageLastOrganizationNameValueEditTextID;
            cancel = true;
        }
        else if(TextUtils.isEmpty(KYCPageLastOrganizationemploymentperiodValueEditTextID.getText().toString()))
        {
            KYCPageLastOrganizationemploymentperiodValueEditTextID.setError("Required field!");
            focusView = KYCPageLastOrganizationemploymentperiodValueEditTextID;
            cancel = true;
        }
        else if(TextUtils.isEmpty(KYCPageLastOrganizationSalaryCTCValueEditTextID.getText().toString()))
        {
            KYCPageLastOrganizationSalaryCTCValueEditTextID.setError("Required field!");
            focusView = KYCPageLastOrganizationSalaryCTCValueEditTextID;
            cancel = true;
        }
        /*else if(TextUtils.isEmpty(KYCPageLastOrganizationSalaryGROSSValueEditTextID.getText().toString()))
        {
            KYCPageLastOrganizationSalaryGROSSValueEditTextID.setError("Required field!");
            focusView = KYCPageLastOrganizationSalaryGROSSValueEditTextID;
            cancel = true;
        }*/
        /*else if(TextUtils.isEmpty(KYCPageLastOrganizationSalaryNTHValueEditTextID.getText().toString()))
        {
            KYCPageLastOrganizationSalaryNTHValueEditTextID.setError("Required field!");
            focusView = KYCPageLastOrganizationSalaryNTHValueEditTextID;
            cancel = true;
        }*/

        else if(TextUtils.isEmpty(KYCPageAlternateContactNumberValueEditTextID.getText().toString()))
        {
            KYCPageAlternateContactNumberValueEditTextID.setError("Required field!");
            focusView = KYCPageAlternateContactNumberValueEditTextID;
            cancel = true;
        }
        else if(TextUtils.isEmpty(KYCPageAlternateContactNumberNameValueEditTextID.getText().toString()))
        {
            KYCPageAlternateContactNumberNameValueEditTextID.setError("Required field!");
            focusView = KYCPageAlternateContactNumberNameValueEditTextID;
            cancel = true;
        }
        else if(TextUtils.isEmpty(KYCPageAlternateContactNumberRelationValueEditTextID.getText().toString()))
        {
            KYCPageAlternateContactNumberRelationValueEditTextID.setError("Required field!");
            focusView = KYCPageAlternateContactNumberRelationValueEditTextID;
            cancel = true;
        }
        else if(TextUtils.isEmpty(changePasswordPageNewPasswordEditTextID.getText().toString()))
        {
            changePasswordPageNewPasswordEditTextID.setError("Enter New Password!");
            focusView = changePasswordPageNewPasswordEditTextID;
            cancel = true;
        }
        else if(TextUtils.isEmpty(changePasswordPageConfirmPasswordEditTextID.getText().toString()))
        {
            changePasswordPageConfirmPasswordEditTextID.setError("Retype New Password!");
            focusView = changePasswordPageConfirmPasswordEditTextID;
            cancel = true;
        }



        if(MARITAL_STATUS.equalsIgnoreCase("Married")){
            if(TextUtils.isEmpty(KYCPageSpouseDetailsNameValueEditTextID.getText().toString()))
            {
                KYCPageSpouseDetailsNameValueEditTextID.setError("Required field!");
                focusView = KYCPageSpouseDetailsNameValueEditTextID;
                cancel = true;
            }
            else if(TextUtils.isEmpty(KYCPageSpouseDetailsGenderValueEditTextID.getText().toString()))
            {
                KYCPageSpouseDetailsGenderValueEditTextID.setError("Required field!");
                focusView = KYCPageSpouseDetailsGenderValueEditTextID;
                cancel = true;
            }
            else if(TextUtils.isEmpty(KYCPageSpouseDetailsDOBValueEditTextID.getText().toString()))
            {
                KYCPageSpouseDetailsDOBValueEditTextID.setError("Required field!");
                focusView = KYCPageSpouseDetailsDOBValueEditTextID;
                cancel = true;
            }
        }

        if(cancel){
            focusView.requestFocus();
        }
        else
        {
            getTextValues();
        }

    }//validateData

    //Get the values from EditText
    private void getTextValues() {

        if(AADHAR_STATUS.equalsIgnoreCase("number")){
            AADHARNO = KYCPageAadharNoValueEditTextID.getText().toString();
        }
        if(PAN_STATUS.equalsIgnoreCase("number")){
            PANNO = KYCPagePANNoValueEditTextID.getText().toString();
        }

        //HIGHEST_QUALIFICATION = KYCPageHighestQualificationValueEditTextID.getText().toString();
        TOTAL_EXP = KYCPageTotalExperienceValueEditTextID.getText().toString();
        LAST_ORGANIZATION_NAME = KYCPageLastOrganizationNameValueEditTextID.getText().toString();
        LAST_ORGANIZATION_EMP_PERIOD = KYCPageLastOrganizationemploymentperiodValueEditTextID.getText().toString();
        LAST_ORGANIZATION_SAL_CTC = KYCPageLastOrganizationSalaryCTCValueEditTextID.getText().toString();
        //LAST_ORGANIZATION_SAL_GROSS = KYCPageLastOrganizationSalaryGROSSValueEditTextID.getText().toString();
        //LAST_ORGANIZATION_SAL_NTH = KYCPageLastOrganizationSalaryNTHValueEditTextID.getText().toString();

        if(MARITAL_STATUS.equalsIgnoreCase("Married")){
            SPOUSE_NAME = KYCPageSpouseDetailsNameValueEditTextID.getText().toString();
            SPOUSE_GENDER = KYCPageSpouseDetailsGenderValueEditTextID.getText().toString();
            SPOUSE_DOB = KYCPageSpouseDetailsDOBValueEditTextID.getText().toString();

            CHILD1_NAME = KYCPageChild1DetailsNameValueEditTextID.getText().toString();
            CHILD1_GENDER = KYCPageChild1DetailsGenderValueEditTextID.getText().toString();
            CHILD1_DOB = KYCPageChild1DetailsDOBValueEditTextID.getText().toString();

            CHILD2_NAME = KYCPageChild2DetailsNameValueEditTextID.getText().toString();
            CHILD2_GENDER = KYCPageChild2DetailsGenderValueEditTextID.getText().toString();
            CHILD2_DOB = KYCPageChild2DetailsDOBValueEditTextID.getText().toString();
        }

        ALTERNATE_CONTACTNO  = KYCPageAlternateContactNumberValueEditTextID.getText().toString();
        ALTERNATE_NAME = KYCPageAlternateContactNumberNameValueEditTextID.getText().toString();
        ALTERNATE_RELATION = KYCPageAlternateContactNumberRelationValueEditTextID.getText().toString();

        NEW_PASSWORD1 = changePasswordPageNewPasswordEditTextID.getText().toString();
        NEW_PASSWORD2 = changePasswordPageConfirmPasswordEditTextID.getText().toString();

        if(NEW_PASSWORD1.equals(NEW_PASSWORD2)){

            try {
                if(CommonUtils.isInternelAvailable(KYCActivity.this)){
                    sendDataForKYCUpdate();
                }
                else{
                     Toast.makeText(KYCActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }//if
        else{
             Toast.makeText(KYCActivity.this, "Confirm password does not match with New password!", Toast.LENGTH_LONG).show();
        }
    }

    //For Login
    private void sendDataForKYCUpdate(){

        progressDialog.setMessage("Updating KYC Details... Please wait!");
        progressDialog.setCancelable(false);
        progressDialog.show();
        client = new RestFullClient(Constants.BASE_URL+Constants.SET_KYC_DETAILS_RELATIVE_URI);

        client.AddParam("associate_id", prefs.getString("USERID",""));
        System.out.println("associate_id:"+prefs.getString("USERID",""));

        client.AddParam("password", CommonUtils.md5(NEW_PASSWORD1));
        System.out.println("password:"+CommonUtils.md5(NEW_PASSWORD1));

        client.AddParam("pan_status", PAN_STATUS);
        System.out.println("pan_status:"+PAN_STATUS);

        client.AddParam("pan", PANNO);
        System.out.println("pan:"+PANNO);

        client.AddParam("aadhar_status", AADHAR_STATUS);
        System.out.println("aadhar_status:"+AADHAR_STATUS);

        client.AddParam("aadhar_no", AADHARNO);
        System.out.println("aadhar_no:"+AADHARNO);

        client.AddParam("highest_qualification", HIGHEST_QUALIFICATION);
        System.out.println("highest_qualification:"+HIGHEST_QUALIFICATION);

        client.AddParam("total_exp_samsung", TOTAL_EXP);
        System.out.println("total_exp_samsung:"+TOTAL_EXP);

        client.AddParam("last_org_name", LAST_ORGANIZATION_NAME);
        System.out.println("last_org_name:"+LAST_ORGANIZATION_NAME);

        client.AddParam("last_org_period", LAST_ORGANIZATION_EMP_PERIOD);
        System.out.println("last_org_period:"+LAST_ORGANIZATION_EMP_PERIOD);

        client.AddParam("salary_ctc", LAST_ORGANIZATION_SAL_CTC);
        System.out.println("salary_ctc:"+LAST_ORGANIZATION_SAL_CTC);

        /*client.AddParam("salary_gross", LAST_ORGANIZATION_SAL_GROSS);
        System.out.println("salary_gross:"+LAST_ORGANIZATION_SAL_GROSS);*/

        /*client.AddParam("salary_nth", LAST_ORGANIZATION_SAL_NTH);
        System.out.println("salary_nth:"+LAST_ORGANIZATION_SAL_NTH);*/

        client.AddParam("marital_status", MARITAL_STATUS);
        System.out.println("marital_status:"+MARITAL_STATUS);

        client.AddParam("spouse_name", SPOUSE_NAME);
        System.out.println("spouse_name:"+SPOUSE_NAME);

        client.AddParam("spouse_gender", SPOUSE_GENDER);
        System.out.println("spouse_gender:"+SPOUSE_GENDER);

        client.AddParam("spouse_dob", SPOUSE_DOB);
        System.out.println("spouse_dob:"+SPOUSE_DOB);

        client.AddParam("child1_name", CHILD1_NAME);
        System.out.println("child1_name:"+CHILD1_NAME);

        client.AddParam("child1_gender", CHILD1_GENDER);
        System.out.println("child1_gender:"+CHILD1_GENDER);

        client.AddParam("child1_dob", CHILD1_DOB);
        System.out.println("child1_dob:"+CHILD1_DOB);

        client.AddParam("child2_name", CHILD2_NAME);
        System.out.println("child2_name:"+CHILD2_NAME);

        client.AddParam("child2_gender", CHILD2_GENDER);
        System.out.println("child2_gender:"+CHILD2_GENDER);

        client.AddParam("child2_dob", CHILD2_DOB);
        System.out.println("child2_dob:"+CHILD2_DOB);

        client.AddParam("alt_contact_no", ALTERNATE_CONTACTNO);
        System.out.println("alt_contact_no:"+ALTERNATE_CONTACTNO);

        client.AddParam("alt_name", ALTERNATE_NAME);
        System.out.println("alt_name:"+ALTERNATE_NAME);

        client.AddParam("alt_relation", ALTERNATE_RELATION);
        System.out.println("alt_relation:"+ALTERNATE_RELATION);

        new Thread(new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                     client.Execute(1); //POST Request
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                receiveDataForServerResponse(client.jObj);
                handler.sendEmptyMessage(0);

            }

        }).start();

    }
    private void receiveDataForServerResponse(JSONObject jobj){

        try{

            if(client.responseCode==200){

                STATUS =  jobj.getString(TAG_STATUS);
                MESSAGE =  jobj.getString(TAG_MESSAGE);

                System.out.println("STATUS: responseCode==200: "+ STATUS);
                System.out.println("MESSAGE: responseCode==200: "+ MESSAGE);

                if(STATUS.equalsIgnoreCase("true")){

                }//if(STATUS.equalsIgnoreCase("true"))

            }//if(client.responseCode==200)

            if(client.responseCode!=200){

                STATUS =  jobj.getString(TAG_STATUS);
                MESSAGE =  jobj.getString(TAG_MESSAGE);

                System.out.println("STATUS: responseCode!=200: "+ STATUS);
                System.out.println("MESSAGE: responseCode!=200: "+ MESSAGE);

            }//if(client.responseCode!=200)

        }
        catch(Exception e){}

    }
    Handler handler = new Handler(){

        public void handleMessage(Message msg){

            try {
                if((progressDialog != null) && progressDialog.isShowing() ){
                    progressDialog.dismiss();
                }
            }catch (final Exception e) {
                e.printStackTrace();
            }

            //Success
            if(client.responseCode==200){
                //Success
                if(STATUS.equalsIgnoreCase("true")){
                    //showSuccessDialog();
                    Toast.makeText(KYCActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                    prefsEditor.putString("KYC_STATUS", "yes");
                    prefsEditor.commit();

                    Intent i = new Intent(KYCActivity.this,DashBoardActivity.class);
                    KYCActivity.this.finish();
                    startActivity(i);
                    overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                }
                //Failed
                if(STATUS.equalsIgnoreCase("false")){
                    showFailureDialog();
                }

            }
            //Failed
            if(client.responseCode!=200){
                Toast.makeText(KYCActivity.this, MESSAGE, Toast.LENGTH_SHORT).show();
                showFailureDialog();
            }

        }//handleMessage(Message msg)

    };

    //Show failure Dialog
    private void showFailureDialog(){
        //Alert Dialog Builder
        final AlertDialog.Builder aldb = new AlertDialog.Builder(KYCActivity.this);
        aldb.setTitle("Failed!");
        aldb.setMessage("\nReason: "+MESSAGE);
        aldb.setPositiveButton("OK", null);
        aldb.show();
    }

    //Spouse DOB
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            KYCPageSpouseDetailsDOBValueEditTextID.setText(new StringBuilder().append(year1).append("-")
                    .append(month1).append("-").append(day1));
        }
    };

    //Child-1 DOB
    private DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            KYCPageChild1DetailsDOBValueEditTextID.setText(new StringBuilder().append(year1).append("-")
                    .append(month1).append("-").append(day1));
        }
    };

    //Child-2 DOB
    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            KYCPageChild2DetailsDOBValueEditTextID.setText(new StringBuilder().append(year1).append("-")
                    .append(month1).append("-").append(day1));
        }
    };

    @Override
    public void onBackPressed() {
        showLogoutDialog();
    }

    //Logout Dialog
    private void showLogoutDialog(){

        final AlertDialog.Builder aldb = new AlertDialog.Builder(KYCActivity.this);
        //aldb.setTitle("Exiting the app!");
        aldb.setCancelable(false);
        aldb.setMessage("You are about to leave 'KYC' page, This is a Mandatory form which to be filled & submitted by you, without this you are not allowed to use the Dashboard.\n\n" +
                "Thank you.");
        aldb.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                prefsEditor.putString("LOGGEDIN","no");
                prefsEditor.commit();

                Toast.makeText(KYCActivity.this, "You have been Logged out successfully!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(KYCActivity.this,LoginActivity.class);
                KYCActivity.this.finish();
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        aldb.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        aldb.show();

    }

    @Override
    protected void onDestroy() {
        try {
            if((progressDialog != null) && progressDialog.isShowing() ){
                progressDialog.dismiss();
            }
        }catch (final Exception e) {
            e.printStackTrace();
        } finally {
            progressDialog = null;
        }

        super.onDestroy();

    }

}//Main Class
