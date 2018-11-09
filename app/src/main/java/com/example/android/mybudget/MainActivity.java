package com.example.android.mybudget;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
//import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
//import android.content.AsyncTaskLoader;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
//import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mybudget.data.BudgetContract;
import com.example.android.mybudget.data.BudgetDBHelper;
import com.example.android.mybudget.widget.BudgetWidgetProvider;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mTextMessage;
    @BindView(R2.id.budget) TextView mBudget;
    @BindView(R2.id.fab_add_expense) FloatingActionButton mFABView;
    @BindView(R2.id.adView) AdView mAdView;
    @BindView(R2.id.sign_in_button) SignInButton mSignInButton;
    @BindView(R2.id.sign_out) Button mSignOutButton;
    @BindView(R2.id.greeting) TextView mGreeting;

    private Cursor mCursor;
    private Cursor mCursorExpense;

    private GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    GoogleSignInAccount mAccount;

//    private static final String GET_BUDGET_EXTRA = "get_budget";
//    private static final String GET_EXPENSES_EXTRA = "get_expenses";
//
//    private static final String GET_BUDGET_LOADER = "get_expenses";
//    private static final String GET_EXPENSES_LOADER = "get_expenses";

    public static final int GET_BUDGET_LOADER = 101;
    public static final int GET_EXPENSES_LOADER = 102;

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        SQLiteDatabase mDatabase;
        BudgetDBHelper dbHelper = new BudgetDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();


        getSupportLoaderManager().initLoader(GET_BUDGET_LOADER, null,this);
//        if(!getLoaderManager().getLoader(GET_BUDGET_LOADER).isReset()) {
//            getLoaderManager().restartLoader(GET_BUDGET_LOADER, null, this);
//        }
        getSupportLoaderManager().initLoader(GET_EXPENSES_LOADER, null,this);
//        if(!getLoaderManager().getLoader(GET_EXPENSES_LOADER).isReset()) {
//            getLoaderManager().restartLoader(GET_EXPENSES_LOADER, null, this);
//        }

//        setBudget();

//        Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_LONG).show();

//        Bundle dbBundle = new Bundle();
//        dbBundle.putString(GET_BUDGET_EXTRA, GET_BUDGET_LOADER);
//        dbBundle.putString(GET_EXPENSES_EXTRA, GET_EXPENSES_LOADER);
//
//        LoaderManager loaderManager = getSupportLoaderManager();
//        Loader<Cursor> dbLoader = loaderManager.getLoader(DB_LOADER);
//
//        if(dbLoader == null) {
//            loaderManager.initLoader(DB_LOADER, dbBundle, this);
//        } else {
//            loaderManager.restartLoader(DB_LOADER, dbBundle, this);
//        }


        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SetBudgetIntent = new Intent(MainActivity.this, SetBudgetActivity.class);
                startActivity(SetBudgetIntent);

                MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        mFABView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AddExpenseIntent = new Intent(MainActivity.this, AddExpenseActivity.class);
                startActivity(AddExpenseIntent);

                MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        //Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

//        Toast.makeText(getBaseContext(), getString(R.string.default_web_client_id), Toast.LENGTH_LONG).show();

//         Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

        mAuth = FirebaseAuth.getInstance();

//        mSignInButton.setOnClickListener(this);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_history) {

            Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(historyIntent);

            MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private void setBudget() {

//        Toast.makeText(getBaseContext(), "setBudget", Toast.LENGTH_LONG).show();
//
////        mCursor = getBudget();
//
//
//        List<String> expenseList = new ArrayList<>();
//        List<String> expenseDateList = new ArrayList<>();
//
////        mCursorExpense = getAllExpenses();
//
//        double totalExpenseForMonth = 0;
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat formatToDigit = new SimpleDateFormat("MM");
//
//        if(mCursorExpense != null) {
//            mCursorExpense.moveToFirst();
//
//            for(int i=0; i<mCursorExpense.getCount(); i++) {
//                String expense = mCursorExpense.getString(mCursorExpense.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_EXPENSE));
//                String expenseDate = mCursorExpense.getString(mCursorExpense.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_TIMESTAMP_EXPENSE));
//                expenseList.add(expense);
//                expenseDateList.add(expenseDate);
//
//                try {
//                    Date date = Calendar.getInstance().getTime();
//                    Date date2 = format.parse(expenseDate);
////                    Date date2 = formatToDigit.parse(expenseDate);
//
//                    String CurrentMonth = formatToDigit.format(date);
//                    String formattedMonth = formatToDigit.format(date2);
////                    Toast.makeText(getBaseContext(), String.valueOf(date), Toast.LENGTH_LONG).show();
//                    if(Integer.parseInt(formattedMonth) == Integer.parseInt(CurrentMonth)) {
//                        totalExpenseForMonth = totalExpenseForMonth + Double.parseDouble(expense);
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                mCursorExpense.moveToNext();
//
//            }
//
//
//            //mCursor.close();
//        }
//
////        Toast.makeText(getBaseContext(), String.valueOf(totalExpenseForMonth), Toast.LENGTH_LONG).show();
//
//        if(mCursor != null) {
//            if(mCursor.moveToLast()) {
//
//                String budget = mCursor.getString(mCursor.getColumnIndex(BudgetContract.BudgetEntry.COLUMN_BUDGET));
//                budget = String.valueOf(Double.parseDouble(budget) - totalExpenseForMonth);
//
//
//                double remBudgetNumber = Double.parseDouble(budget);
//                String remBudgetString = String.format("%.2f", remBudgetNumber);
//
//                String value = getResources().getString(R.string.currency_value, remBudgetString);
//
//                mBudget.setText(value);
//
//                Context context = this;
//                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_budget);
//                ComponentName thisWidget = new ComponentName(context, BudgetWidgetProvider.class);
//                remoteViews.setTextViewText(R.id.widget_budget, value);
//                appWidgetManager.updateAppWidget(thisWidget, remoteViews);
//                //mCursor.close();
//
//            }
//
//        }

//    }

//    private Cursor getBudget() {
//
//        return getContentResolver().query(BudgetContract.BudgetEntry.CONTENT_URI_BUDGET,
//                null,
//                null,
//                null,
//                BudgetContract.BudgetEntry.COLUMN_TIMESTAMP_BUDGET);
//
//    }
//
//    private Cursor getAllExpenses() {
//
//        return getContentResolver().query(BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE,
//                null,
//                null,
//                null,
//                BudgetContract.ExpenseEntry.COLUMN_TIMESTAMP_EXPENSE);
//    }

    @Override
    protected void onRestart() {
//        Toast.makeText(getBaseContext(), "onRestart", Toast.LENGTH_LONG).show();
        getSupportLoaderManager().initLoader(GET_BUDGET_LOADER, null,this);
        getSupportLoaderManager().initLoader(GET_EXPENSES_LOADER, null,this);
//        setBudget();
        super.onRestart();
    }

    @Override
    protected void onStart() {

//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
//
//        super.onStart();

        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null) {
            mSignInButton.setVisibility(View.INVISIBLE);

            mSignOutButton.setVisibility(View.VISIBLE);

            FirebaseUser user = mAuth.getCurrentUser();
            String name = getResources().getString(R.string.greeting, user.getDisplayName());
            mGreeting.setText(name);
            mGreeting.setVisibility(View.VISIBLE);

        }
        else {
            mSignOutButton.setVisibility(View.INVISIBLE);
            mGreeting.setVisibility(View.INVISIBLE);

            mSignInButton.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
            case R.id.sign_out:
                signOut();
                break;
        }
    }

    private void signIn() {
//        Toast.makeText(getBaseContext(), "Button clicked", Toast.LENGTH_LONG).show();
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
        Toast.makeText(getBaseContext(), "Signed out", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
//                Toast.makeText(getBaseContext(), "Google sign in failed", Toast.LENGTH_LONG).show();
                // ...
            }
        }

    }

//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if(result.isSuccess()) {
//            mAccount = result.getSignInAccount();
//
//            mSignOutButton.setVisibility(View.VISIBLE);
//            String name = getResources().getString(R.string.currency_value, mAccount.getDisplayName());
//            mGreeting.setText(name);
//            mGreeting.setVisibility(View.VISIBLE);
//
//            Toast.makeText(getBaseContext(), "Hello, " + mAccount.getDisplayName(), Toast.LENGTH_LONG).show();
//
//        }
//    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(getBaseContext(), "Authentication Failed.", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {

                if (id == GET_BUDGET_LOADER) {

                    return getContentResolver().query(BudgetContract.BudgetEntry.CONTENT_URI_BUDGET,
                            null,
                            null,
                            null,
                            BudgetContract.BudgetEntry.COLUMN_TIMESTAMP_BUDGET);
                }
                else if (id == GET_EXPENSES_LOADER) {
                    return getContentResolver().query(BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE,
                            null,
                            null,
                            null,
                            BudgetContract.ExpenseEntry.COLUMN_TIMESTAMP_EXPENSE);
                }

                return null;
            }
         };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int id = loader.getId();
        if(id == GET_BUDGET_LOADER) {
            mCursor = cursor;

        }
        if(id == GET_EXPENSES_LOADER) {
            mCursorExpense = cursor;
        }

        List<String> expenseList = new ArrayList<>();
        List<String> expenseDateList = new ArrayList<>();

//        mCursorExpense = getAllExpenses();

        double totalExpenseForMonth = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatToDigit = new SimpleDateFormat("MM");

        if(mCursorExpense != null) {
            mCursorExpense.moveToFirst();

            for(int i=0; i<mCursorExpense.getCount(); i++) {
                String expense = mCursorExpense.getString(mCursorExpense.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_EXPENSE));
                String expenseDate = mCursorExpense.getString(mCursorExpense.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_TIMESTAMP_EXPENSE));
                expenseList.add(expense);
                expenseDateList.add(expenseDate);

                try {
                    Date date = Calendar.getInstance().getTime();
                    Date date2 = format.parse(expenseDate);
//                    Date date2 = formatToDigit.parse(expenseDate);

                    String CurrentMonth = formatToDigit.format(date);
                    String formattedMonth = formatToDigit.format(date2);
//                    Toast.makeText(getBaseContext(), String.valueOf(date), Toast.LENGTH_LONG).show();
                    if(Integer.parseInt(formattedMonth) == Integer.parseInt(CurrentMonth)) {
                        totalExpenseForMonth = totalExpenseForMonth + Double.parseDouble(expense);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mCursorExpense.moveToNext();

            }


            //mCursor.close();
        }

//        Toast.makeText(getBaseContext(), String.valueOf(totalExpenseForMonth), Toast.LENGTH_LONG).show();

        if(mCursor != null) {
            if(mCursor.moveToLast()) {

                String budget = mCursor.getString(mCursor.getColumnIndex(BudgetContract.BudgetEntry.COLUMN_BUDGET));
                budget = String.valueOf(Double.parseDouble(budget) - totalExpenseForMonth);


                double remBudgetNumber = Double.parseDouble(budget);
                String remBudgetString = String.format("%.2f", remBudgetNumber);

                String value = getResources().getString(R.string.currency_value, remBudgetString);

                mBudget.setText(value);

                Context context = this;
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_budget);
                ComponentName thisWidget = new ComponentName(context, BudgetWidgetProvider.class);
                remoteViews.setTextViewText(R.id.widget_budget, value);
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);
                //mCursor.close();

            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
