package com.example.prototype13;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
/*import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;*/

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private List<Product> mProductList;
    private Context mContext;
    //public String jsonResponse;

    private static final String TAG = "ProductsAdapter";

    ProductsAdapter(Context context, List<Product> productList) {
        mContext = context;
        mProductList = productList; }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_grid_item, parent, false);
        return new MyViewHolder(itemView); }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        //Log.d(TAG, "onBindViewHolder: called.");
        final Product product = mProductList.get(holder.getAdapterPosition());
        holder.title.setText(product.getName());
        holder.price.setText("₹" + product.getPrice());
        holder.brand.setText(product.getBrand());
        Double d = product.getRating();
        holder.rating.setText(String.valueOf(d));
        holder.originalPrice.setText(product.getOriginalPrice());
        holder.reviews.setText(String.valueOf(product.getReviews()));
        if (product.getDomain().equals("www.amazon.com")) {
            if(product.getFulfillment() == 1){
                Picasso.get().load(R.drawable.amazon_prime).into(holder.fulfillment);
                holder.fulfillment.setVisibility(View.VISIBLE); }
        }
        if(product.getDomain().equals("www.flipkart.com")){
            if(product.getFulfillment() == 1){
                Picasso.get().load(R.drawable.flipkart_assured).into(holder.fulfillment);
                holder.fulfillment.setVisibility(View.VISIBLE); }
            new Thread(() -> {
                String a = null;
                try {
                    URL url = new URL("https://affiliate-api.flipkart.net/affiliate/1.0/product.json?id=" +product.getProductID().substring(4));
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setRequestProperty("Fk-Affiliate-Id","tagalpall");
                        urlConnection.setRequestProperty("Fk-Affiliate-Token","b567e502f7e446b7a187f6190d2a43c9");
                        urlConnection.setReadTimeout(0);
                        urlConnection.setConnectTimeout(0);
                        urlConnection.connect();
                        a = convertString(new BufferedInputStream(urlConnection.getInputStream()));
                    }
                    catch (IOException e) {e.printStackTrace();
                    } finally { if (urlConnection != null)  urlConnection.disconnect();  }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                product.setJson(a);
                a = getImageURL(a);
                product.setThumbnailURL(a);
                Log.i(TAG, a);

                Handler mainHandler = new Handler(Looper.getMainLooper());

                final String finalA = a;

                Runnable myRunnable = () -> {try {
                    Glide.with(mContext).load(finalA).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thumbnail)
                    ; }
                catch (Exception e) { e.printStackTrace(); }
                };
                mainHandler.post(myRunnable);
            }).start();
        } else{
            try {
                Picasso.get().load(product.getThumbnailURL()).into(holder.thumbnail); }
            catch (Exception e) { e.printStackTrace(); }}



        holder.parentLayout.setOnClickListener(view -> {
            Product clickedProduct = mProductList.get(holder.getAdapterPosition());
            Intent intent = new Intent(mContext, ProductPageActivity.class);
            /*View t = holder.title;
            t.setTransitionName("name");
            View i = holder.thumbnail;
            i.setTransitionName("image");

            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation((ProductGridActivity)mContext, i, "image");
            mContext.startActivity(intent, options.toBundle());*/
            intent.putExtra("com.dream.atharva.dream.key", clickedProduct);
            mContext.startActivity(intent);


        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title, price, brand, rating, originalPrice, reviews;
        ImageView thumbnail, fulfillment;
        LinearLayout parentLayout;

        MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            brand = itemView.findViewById(R.id.brand);
            rating = itemView.findViewById(R.id.rating);
            fulfillment = itemView.findViewById(R.id.fulfillment);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            parentLayout = itemView.findViewById(R.id.product_grid_item);
            originalPrice = itemView.findViewById(R.id.original_price);
            reviews = itemView.findViewById(R.id.reviews);
        }

    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }



    private String getImageURL(String json){
        JsonElement jelement = new JsonParser().parse(json);
        JsonObject jobject = jelement.getAsJsonObject().getAsJsonObject("productBaseInfoV1").getAsJsonObject("imageUrls");
        return jobject.get("400x400").getAsString();
    }

    private String convertString(InputStream i) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = i.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        StandardCharsets.UTF_8.name();
        return result.toString("UTF-8");
    }
        /*private String makeHTTPRequest(final String productID){
       Thread t1 =  new Thread(new Runnable() {
           @Override
           public void run() {
               URL url;
               try { url = new URL("https://affiliate-api.flipkart.net/affiliate/1.0/product.json?id=" + productID);

                   HttpURLConnection urlConnection = null;
                   try {
                       urlConnection = (HttpURLConnection) url.openConnection();
                       urlConnection.setRequestMethod("GET");
                       urlConnection.setRequestProperty("Fk-Affiliate-Id","tagalpall");
                       urlConnection.setRequestProperty("Fk-Affiliate-Token","b567e502f7e446b7a187f6190d2a43c9");
                       urlConnection.setReadTimeout(10000);
                       urlConnection.setConnectTimeout(15000);
                       urlConnection.connect();
                       i = new BufferedInputStream(urlConnection.getInputStream());
                       jsonResponse = convertString(i);
                   }
                   catch (IOException e) {e.printStackTrace();
                   } finally { if (urlConnection != null)  urlConnection.disconnect();  }
               } catch (MalformedURLException e) { e.printStackTrace(); }

           }
       });

       t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace(); }

        return jsonResponse;
    }*/

    /* private String startHTTPRequest(final String productID) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> result = es.submit(new Callable<String>() {
            public String call() throws Exception {
                URL url;
                try { url = new URL("https://affiliate-api.flipkart.net/affiliate/1.0/product.json?id=" + productID);

                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setRequestProperty("Fk-Affiliate-Id","tagalpall");
                        urlConnection.setRequestProperty("Fk-Affiliate-Token","b567e502f7e446b7a187f6190d2a43c9");
                        urlConnection.setReadTimeout(10000);
                        urlConnection.setConnectTimeout(15000);
                        urlConnection.connect();
                        jsonResponse = convertString(new BufferedInputStream(urlConnection.getInputStream()));
                    }
                    catch (IOException e) {e.printStackTrace();
                    } finally { if (urlConnection != null)  urlConnection.disconnect();  }
                } catch (MalformedURLException e) { e.printStackTrace(); }
                return jsonResponse;
            }
        });

        return result.get();
    }*/


}