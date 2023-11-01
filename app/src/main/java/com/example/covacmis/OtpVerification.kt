package com.example.covacmis


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class OtpVerification : AppCompatActivity() {

    private lateinit var sendOTPBtn : Button
    private lateinit var phoneNumberET : EditText
    private lateinit var number : String
    private lateinit var mProgressBar : ProgressBar
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)


        sendOTPBtn = findViewById(R.id.sendOTPBtn)
        phoneNumberET = findViewById(R.id.phoneEditTextNumber)
        auth = Firebase.auth

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token

                val intent = Intent(this@OtpVerification,OtpActivity::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                startActivity(intent)
            }
        }
        // [END phone_auth_callbacks]
//        init()
//        sendOTPBtn.setOnClickListener {
//            number = phoneNumberET.text.trim().toString()
//            if (number.isNotEmpty()){
//                if (number.length == 10){
//                    number = "+91$number"
//                    mProgressBar.visibility = View.VISIBLE
//                    val options = PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber(number)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // Activity (for callback binding)
//                        .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
//                        .build()
//                    PhoneAuthProvider.verifyPhoneNumber(options)
//
//                }else{
//                    Toast.makeText(this , "Please Enter correct Number" , Toast.LENGTH_SHORT).show()
//                }
//            }else{
//                Toast.makeText(this , "Please Enter Number" , Toast.LENGTH_SHORT).show()
//
//            }
//        }
//    }
//
//    private fun init(){
//        mProgressBar = findViewById(R.id.phoneProgressBar)
//        mProgressBar.visibility = View.INVISIBLE
//        sendOTPBtn = findViewById(R.id.sendOTPBtn)
//        phoneNumberET = findViewById(R.id.phoneEditTextNumber)
//        auth = FirebaseAuth.getInstance()
//    }
//
//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Toast.makeText(this , "Authenticate Successfully" , Toast.LENGTH_SHORT).show()
//                    sendToMain()
//                } else {
//                    // Sign in failed, display a message and update the UI
//                    Log.d("TAG", "signInWithPhoneAuthCredential: ${task.exception.toString()}")
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                        // The verification code entered was invalid
//                    }
//                    // Update UI
//                }
//                mProgressBar.visibility = View.INVISIBLE
//            }
//    }
//
//    private fun sendToMain(){
//        startActivity(Intent(this , MainActivity::class.java))
//    }
//    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//            // This callback will be invoked in two situations:
//            // 1 - Instant verification. In some cases the phone number can be instantly
//            //     verified without needing to send or enter a verification code.
//            // 2 - Auto-retrieval. On some devices Google Play services can automatically
//            //     detect the incoming verification SMS and perform verification without
//            //     user action.
//            signInWithPhoneAuthCredential(credential)
//        }
//
//        override fun onVerificationFailed(e: FirebaseException) {
//            // This callback is invoked in an invalid request for verification is made,
//            // for instance if the the phone number format is not valid.
//
//            if (e is FirebaseAuthInvalidCredentialsException) {
//                // Invalid request
//                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
//            } else if (e is FirebaseTooManyRequestsException) {
//                // The SMS quota for the project has been exceeded
//                Log.d("TAG", "onVerificationFailed: ${e.toString()}")
//            }
//            mProgressBar.visibility = View.VISIBLE
//            // Show a message and update the UI
//        }
//
//        override fun onCodeSent(
//            verificationId: String,
//            token: PhoneAuthProvider.ForceResendingToken
//        ) {
//            // The SMS verification code has been sent to the provided phone number, we
//            // now need to ask the user to enter the code and then construct a credential
//            // by combining the code with a verification ID.
//            // Save verification ID and resending token so we can use them later
//            val intent = Intent(this@OtpVerification , OtpActivity::class.java)
//            intent.putExtra("OTP" , verificationId)
//            intent.putExtra("resendToken" , token)
//            intent.putExtra("phoneNumber" , number)
//            startActivity(intent)
//            mProgressBar.visibility = View.INVISIBLE
//        }
//    }
//
//
//    override fun onStart() {
//        super.onStart()
//        if (auth.currentUser != null){
//            startActivity(Intent(this , MainActivity::class.java))
//        }

        sendOTPBtn.setOnClickListener {

            startPhoneNumberVerification(phoneNumberET.text.toString())
        }

    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91"+phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user

                    val intent = Intent(this@OtpVerification,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
    // [END sign_in_with_phone]

    private fun updateUI(user: FirebaseUser? = auth.currentUser) {
    }

    companion object {
        private const val TAG = "PhoneAuthActivity"
    }
}