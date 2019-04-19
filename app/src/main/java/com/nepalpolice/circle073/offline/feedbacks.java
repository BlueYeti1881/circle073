package com.nepalpolice.circle073.offline;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.nepalpolice.circle073.R;
import com.nepalpolice.circle073.feedback.SpreadsheetWebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class feedbacks extends Fragment {
    private EditText feedbackInputField;
   private EditText emailInputField;
       private ProgressDialog pd;
 @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_feedback, container, false);





        feedbackInputField = (EditText) view.findViewById(R.id.feedback_input);

        emailInputField = (EditText) view.findViewById(R.id.email_input);


        view.findViewById(R.id.buttonSubmit).setOnClickListener(

            new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                        validateInput();
                      pd = new ProgressDialog(getActivity());
                         pd.setTitle("Submitting Details");
                        pd.setMessage("Please wait....");
                        pd.show();

                    }
                }
        );
        return view;

    }


     private void validateInput() { // Validate text input

        if (feedbackInputField.getText().toString().trim().length() == 0 && emailInputField.getText().toString().trim().length() == 0) {

            feedbackInputField.setError("Enter your Feedback!");

            emailInputField.setError("Enter your email!");
            Toast.makeText(getActivity(), "Please fill in the required fields!", Toast.LENGTH_LONG).show();
            } else {
                validateEmail();
            }
    }

    private void validateEmail() {
        if (Patterns.EMAIL_ADDRESS.matcher(emailInputField.getText()).matches())
            sendData();
        else {
            emailInputField.setError("Enter a valid email!");
            Toast.makeText(getActivity(), "Please fill in a Valid Email Address!", Toast.LENGTH_LONG).show();
        }
    }

    private void sendData() { // Send feedback to Google Spreadsheet if text input is valid

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://docs.google.com/forms/d/e/") // Your spreadsheet URL
                .build();
        final SpreadsheetWebService spreadsheetWebService = retrofit.create(SpreadsheetWebService.class);


        String feedbackInput = feedbackInputField.getText().toString();

        String emailInput = emailInputField.getText().toString();

        Call<Void> feedbackCall = spreadsheetWebService.feedbackSend(feedbackInput,emailInput);
        feedbackCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("XXX", "Submitted. " + response);
                Toast.makeText(getActivity(),"Thank you For your Feedback", Toast.LENGTH_LONG).show();
                pd.hide();
                // Clear all fields after submitting

               feedbackInputField.setText("");

                emailInputField.setText("");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("XXX", "Failed", t);
                Toast.makeText(getActivity(),"There was an error!", Toast.LENGTH_LONG).show();
            }
        });
    }


}


