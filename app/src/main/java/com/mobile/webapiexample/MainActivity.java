package com.mobile.webapiexample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.android.networking.AndroidNetworking;
import com.amitshekhar.android.networking.common.ANRequest;
import com.amitshekhar.android.networking.common.Priority;
import com.amitshekhar.android.networking.error.ANError;
import com.amitshekhar.android.networking.interfaces.ParsedRequestListener;
import com.squareup.picasso.Picasso;


import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "avEIu9isoHu9ysZ9krb2JPmwBIDZThwu";
    private final View.OnClickListener localListener = v -> {
        TextView tickerTextView = findViewById(R.id.editTextTicker);
        String ticker = tickerTextView.getText().toString();
        String imageURL = "https://financialmodelingprep.com/image-stock/AAPL.png";
        ImageView iv = findViewById(R.id.logoView);
        Picasso.get().load(imageURL).into(iv);
        makeRequest(ticker);

        Toast.makeText(v.getContext(),"Hello puppies", Toast.LENGTH_LONG).show();
    };

    private void makeRequest(String ticker){
        // https://financialmodelingprep.com/api/v3/quote/AAPL?apikey=demo
        ANRequest req = AndroidNetworking.get("https://financialmodelingprep.com/stable/quote")
                .addPathParameter("symbol", ticker)
                .addQueryParameter("apikey", API_KEY)
                .setPriority(Priority.LOW)
                .build();
                req.getAsObjectList(Quote.class, new ParsedRequestListener<List<Quote>>() {
            @Override
            public void onResponse(List<Quote> quotes) {
                String TAG = "FINANCIAL";
                Log.i(TAG, "userList size : " + quotes.size());
                for (Quote quote : quotes) {
                    Log.i(TAG, "symbol : " + quote.getSymbol());
                    Log.i(TAG, "name : " + quote.getName());
                    Log.i(TAG, "price : " + quote.getPrice());
                    String dayRange = "[" + String.format(Locale.US,"%.2f", quote.getDayLow()) + " - " + quote.getDayHigh() + "]";
                    String yearRange = "[" + String.format(Locale.US,"%.2f", quote.getYearLow()) + " - " + quote.getYearHigh() + "]";

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ENGLISH);
                    LocalDate localDate = LocalDate.parse("2020-12-20T00:00:00.000Z", formatter);
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    ((TextView) findViewById(R.id.tickerText)).setText(quote.getSymbol());
                    ((TextView) findViewById(R.id.companyNameText)).setText(quote.getName());
                    ((TextView) findViewById(R.id.exchangeText)).setText(quote.getExchange());
                    ((TextView) findViewById(R.id.currentPriceText)).setText(String.format(Locale.US,"%.2f", quote.getPrice()));
                    ((TextView) findViewById(R.id.changeText)).setText(String.format(Locale.US,"%.2f", quote.getChange()));
                    ((TextView) findViewById(R.id.changePercentText)).setText(String.format(Locale.US,"%.2f %%", quote.getChangesPercentage()));
                    ((TextView) findViewById(R.id.priceAvg50Text)).setText(String.format(Locale.US,"%.2f", quote.getPriceAvg50()));
                    ((TextView) findViewById(R.id.dayRangeText)).setText(dayRange);
                    ((TextView) findViewById(R.id.yearRangeText)).setText(yearRange);
                    //((TextView) findViewById(R.id.epsText)).setText(String.format(Locale.US,"%.3f", quote.getEps()));
                   // ((TextView) findViewById(R.id.sharesText)).setText(quote.getSharesOutstanding());
                    ((TextView) findViewById(R.id.earningsText)).setText(date.toString());
                    String toastText = "A share of " + quote.getName() + "is currently at $" + quote.getPrice();
                    Toast.makeText(getApplicationContext(),toastText, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onError(ANError anError) {
                // handle error
                Toast.makeText(getApplicationContext(),"Error on getting data ", Toast.LENGTH_LONG).show();
                Log.e("ERROR", anError.toString());
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        Button submitButton = findViewById(R.id.button);
        submitButton.setOnClickListener(localListener);

    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            bmImage.setImageBitmap(result);
        }
    }


}
