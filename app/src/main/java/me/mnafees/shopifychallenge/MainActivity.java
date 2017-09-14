package me.mnafees.shopifychallenge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.batz_spent)
    TextView mBatzTotalSpent;
    @BindView(R.id.total_bags_spent)
    TextView mTotalBagsSold;

    private boolean mFoundNapoleonBatz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://shopicruit.myshopify.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ShopifyService shopifyService = retrofit.create(ShopifyService.class);
        shopifyService.getJSON()
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            JsonArray ordersArray = json.getAsJsonArray("orders");
                            int totalBagsSold = 0;
                            for (int i = 0; i < ordersArray.size(); ++i) {
                                JsonObject order = ordersArray.get(i).getAsJsonObject();

                                if (!mFoundNapoleonBatz) { // run only once
                                    JsonObject customer = order.getAsJsonObject("customer");
                                    if (customer.get("email").getAsString()
                                            .equals("napoleon.batz@gmail.com")) {
                                        String totalSpent = customer.get("total_spent").getAsString();
                                        mBatzTotalSpent.setText(totalSpent + " CAD");
                                        mFoundNapoleonBatz = true;
                                    }
                                }

                                JsonArray items = order.getAsJsonArray("line_items");
                                for (int j = 0; j < items.size(); ++j) {
                                    JsonObject item = items.get(j).getAsJsonObject();
                                    if (item.get("title").getAsString().equals("Awesome Bronze Bag")) {
                                        totalBagsSold++;
                                    }
                                }
                            }
                            mTotalBagsSold.setText(Integer.toString(totalBagsSold));
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
    }
}
