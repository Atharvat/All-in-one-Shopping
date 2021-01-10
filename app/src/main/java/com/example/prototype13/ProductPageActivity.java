package com.example.prototype13;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductPageActivity extends AppCompatActivity {
    private static final String TAG = "ProductPageActivity";

    public Product product;

    public String brand;
    public String seller;
    public String delivery;
    public String name;
    public String noOfReviews;
    public String noOfRatings;
    public String price;
    public String stars;
    public String returnReplacement;
    public String author;
    public String productID;
    public String originalPrice;
    public String sellerInfo;
    public String shippingCharges;

    public List<String> specTitle;
    public List<String> specValue;
    public String description;
    public String featurePara;
    public List<String> features;
    public List<String> highlights;
    public List<String> services;
    public List<String> offerTitles;
    public List<String> offerValues;
    public List<String> bankOffers;
    public List<String> otherInfo;


    public List<String> imageURLs;
    public String reviewLink;
    public String questionLink;
    public String brandImage;
    public String url;

    public boolean exchange;
    public boolean isFulfilled;
    public boolean inStock;
    public ArrayList<ArrayList<String>> selectibles;

    Element addToCart;
    Element buyNow;
    Element pincodeInput;
    Element pincodeEnter;

    List<String> buyingOptions;
    Elements buyingOptionsCheckBoxes;
    String fitInfo;
    Element moreSpecs;
    String productCode;

    TextView productName;
    ImageView productImage;
    TextView productDescription;
    ImageView productFulfillment;
    TextView OriginalProductPrice;
    TextView productPrice;
    TextView productDelivery;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        getSupportActionBar().hide();

        product = getIntent().getParcelableExtra("com.dream.atharva.dream.key");

        //productImage = findViewById(R.id.product_thumbnail);

        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
      //  productFulfillment = findViewById(R.id.product_fulfillment);
        productDescription = findViewById(R.id.product_description);
        productDelivery = findViewById(R.id.delivery_info);
        OriginalProductPrice = findViewById(R.id.original_price);

        OriginalProductPrice.setPaintFlags(OriginalProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        //productDescription.setText(product.getDescription());

        try { Glide.with(this).load(product.getThumbnailURL()).into(productImage);
        } catch (Exception e) {e.printStackTrace(); }

        productPrice.setText("₹"+product.getPrice());

        if (product.getDomain().equals("www.flipkart.com")) {
            getDataFromFlipkart(product.getJson());
        } else { getDataFromAmazon(product.getUrl());
        }


        Button buyNow = findViewById(R.id.buy_now);
        if(product.getDomain().equals("www.flipkart.com")){
        buyNow.setOnClickListener(v -> startWebview(url));}
        else {
            buyNow.setOnClickListener(v -> {
                try {
                    startWebview(url);
                } catch (Exception e) {
                    startWebview(product.getUrl());
                }
            });}

    }

    private void getDataFromFlipkart(final String mainString){
        new Thread(() -> {
            JsonElement jelement = new JsonParser().parse(mainString);
            JsonObject jobject = jelement.getAsJsonObject().getAsJsonObject("productBaseInfoV1");
            name = jobject.get("title").getAsString();
            description = jobject.get("productDescription").getAsString();
            originalPrice = jobject.get("flipkartSellingPrice").getAsJsonObject().get("amount").getAsString();
            price = jobject.get("flipkartSpecialPrice").getAsJsonObject().get("amount").getAsString();
            url = jobject.get("productUrl").getAsString();
            brand = jobject.get("productBrand").getAsString();
            if (jobject.get("inStock")!=null){
                inStock = true;
            }else{inStock = false;}
            JsonObject jobject2 = jelement.getAsJsonObject().getAsJsonObject("productBaseInfoV1");
            try {
                shippingCharges = jobject2.get("shippingCharges").getAsJsonObject().get("amount").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                seller = jobject2.get("sellerName").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                sellerInfo ="sellerAverageRating: "+jobject2.get("sellerAverageRating").getAsString()+
                        "sellerNoOfRatings: "+jobject2.get("sellerNoOfRatings").getAsString()
                        +          "sellerNoOfReviews: "+jobject2.get("sellerNoOfReviews").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            url = "http://www.amazon.in/gp/product/"+productID+"?tag=atharvataga08-21";

            //String[] OfferValues = jobject.getAsJsonObject("offers").getAsJsonArray();


            URL iurl = null;
            try { iurl = new URL(product.getThumbnailURL()); }
            catch (MalformedURLException e) { e.printStackTrace(); }
            Bitmap bmp = null;
            try { bmp = BitmapFactory.decodeStream(iurl.openConnection().getInputStream());
            } catch (IOException e) { e.printStackTrace(); }
            Double ratio = (double)(bmp.getHeight())/(double)(bmp.getWidth());

            runOnUiThread(() -> {
                productName.setText(name);
                if(name.length()>100){
                    productName.setTextSize(2,14);
                }else if(name.length()>60){
                    productName.setTextSize(2,16);
                }
                productPrice.setText("₹"+price);
                if (product.getFulfillment() == 1) {

                    // productFulfillment.setVisibility(View.VISIBLE);
                }

                ViewPager mViewPager = findViewById(R.id.viewPager);


                mCardAdapter = new CardPagerAdapter();
                mCardAdapter.addCardItem(new CardItem(product.getThumbnailURL()));
                mCardAdapter.addCardItem(new CardItem(product.getThumbnailURL()));
                mCardAdapter.addCardItem(new CardItem(product.getThumbnailURL()));
                mViewPager.setAdapter(mCardAdapter);


                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics outMetrics = new DisplayMetrics();
                display.getMetrics(outMetrics);

                float density  = getResources().getDisplayMetrics().density;
                float dpWidth  = outMetrics.widthPixels / density;


                ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
                if(ratio<1.3)
                    params.height = (int)Math.round((dpWidth-80)*ratio*density);
                if(ratio>=1.3)
                    params.height = (int)Math.round((dpWidth-80)*1.3*density);

                mViewPager.setLayoutParams(params);

                mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
                mViewPager.setPageTransformer(false, mCardShadowTransformer);
                mViewPager.setOffscreenPageLimit(3);
                mViewPager.setPageMargin(-50);
                Log.i("margin", String.valueOf(mViewPager.getPageMargin()));
                mCardShadowTransformer.enableScaling(true);




                //Picasso.get().load(product.getThumbnailURL()).into(productImage);
                //Glide.with(getApplicationContext()).load(product.getBrand().replaceAll("400","800"))
                //        .into(productImage);
                Log.i(TAG, product.getBrand().replaceAll("400","800"));
                productDelivery.setText(new StringBuilder().append("Brand: ").append(brand).append(" Merchant: ").append(seller).toString());
                productDescription.setText(description);

                ConstraintLayout c = findViewById(R.id.wait2);
                c.setVisibility(View.GONE);

            });

        }).start();
    }

    private void getDataFromAmazon(final String mainString) {
        new Thread(() -> {
            final StringBuilder builder = new StringBuilder();

            try {
                Document doc = Jsoup.connect(mainString).userAgent("Mozilla/5.0 (Linux; U; Android 2.2) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1").get();

                Elements specTitles = doc.select("#productDetails_techSpec_section_1>tbody>tr>th");
                Elements specValues = doc.select("#productDetails_techSpec_section_1>tbody>tr>td");
                if (specTitles != null) {
                    specTitle = specTitles.eachText();
                    specValue = specValues.eachText(); }
                //text as description
                try { description = doc.select("#productDescription_fullView>div>p").first().text(); } catch (Exception e) { e.printStackTrace(); }
                try { description = doc.select("#productDescription>p").first().text(); } catch (Exception e) { e.printStackTrace(); }

                //for features in paragraph, text as description
                //featurePara = doc.select("#iframeContent").first().text();
                //for features in bullets, having texts as features
                Elements aa = doc.select("#feature-bullets > ul > li");
                if(aa!=null) features = aa.eachText();
                //text as price without rupee sign
                Element ab = doc.select(".buyingPrice").first();
                if(ab!=null) price = ab.text();//without rupee sign
                name = doc.select("#title").first().text();
                Element ac =  doc.select(".restOfPrice.a-text-strike").first();
                if(ac!=null)originalPrice = ac.text();//with rupee sign
                if(doc.select(".fbaBadge") != null)isFulfilled = true;
                Element ad = doc.selectFirst("#acrCustomerReviewLink>.a-size-mini");
                if(ad!=null)noOfReviews = ad.text();//simple number

                Element ba = doc.selectFirst("#productDetails_detailBullets_sections>li>span>span:nth-child(2)");
                if(ba!=null)productID = ad.text();//simple number

                try {
                    stars = doc.selectFirst("#acrCustomerReviewLink>i>span.a-icon-alt").text();//eg: 4.5 out of 5 stars
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //use same for brand
                //Keep upto "https://images-na.ssl-images-amazon.com/images/I/41%2B%2BHzstqcL." and add _SX720_.jpg (for720p)
                Elements images = doc.select("div[data-a-image-name=\"immersiveViewMainImage\"]");
                for (int i = 0; i < images.size(); i++) {
                    List<String> Images= images.eachAttr("data-a-image-source");
                    imageURLs = Images;
                }
                /*String Image = Images.get(i).substring(0, 65) + "SX720_.jpg";
                try { imageURLs.add(Image); } catch (Exception e) { e.printStackTrace(); }*/
               /* if (images.size() == 0) {
                    Element ae = doc.selectFirst("#main-image");
                    if(ae!=null) {
                        String a1 = ae.attr("data-a-image-source");
                        try {String Image = a1.substring(0, 65) + "SX720_.jpg";
                            imageURLs.add(Image); } catch (Exception e) { e.printStackTrace();}
                    } }  //for one images*/
                Element af =  doc.selectFirst(".emiStringPadding");
                try { bankOffers.add(af.text()); } catch (Exception e) { e.printStackTrace(); }
                if(doc.select(".fbaBadge") != null)isFulfilled = true;
                Element ag =  doc.select("#merchant-info>a").first();
                if(ag!=null) seller = ag.text();
                sellerInfo = doc.select("#merchant-info").text();
                try { otherInfo.add(doc.select("#warrantyInformation_primaryViewDiv").first().text()); } catch (Exception e) { e.printStackTrace(); }
                if(doc.select("#availability>span")!= null)inStock=true;
                try { questionLink = "https://www.amazon.in" + doc.select(".askPage>div>div.a-box-group").attr("href"); } catch (Exception e) { e.printStackTrace(); }
                try { reviewLink = "https://www.amazon.in" + doc.select(".cr-widget-SeeAllReviews>div>div>a").attr("href"); } catch (Exception e) { e.printStackTrace(); }
                if(specTitle != null)builder.append(specTitle);
                if(specValue != null)builder.append(specValue);
                if(features != null)builder.append(features);
                if(description != null)builder.append(description);
                if(highlights != null)builder.append(highlights);
                if(imageURLs!=null)builder.append(imageURLs);
                builder.append(price);
                builder.append(originalPrice);
                builder.append(seller);
                builder.append(brand);
                builder.append(reviewLink);
                if(doc.select("#landing-image-wrapper")!=null){
                    showToast();
                }


            } catch (IOException e) {
                builder.append("Error : ").append(e.getMessage()).append("\n");
            }

            runOnUiThread(() -> {
                productName.setText(name);
                productPrice.setText("₹"+price);
                if (product.getFulfillment() == 1) {
                   // productFulfillment.setVisibility(View.VISIBLE);
                }


                ViewPager mViewPager = findViewById(R.id.viewPager);

                mCardAdapter = new CardPagerAdapter();
                for(int i=0;i<imageURLs.size();i++)
                { mCardAdapter.addCardItem(new CardItem(imageURLs.get(i))); }
                mViewPager.setAdapter(mCardAdapter);

                mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
                mViewPager.setPageTransformer(false, mCardShadowTransformer);
                mViewPager.setOffscreenPageLimit(3);
                mCardShadowTransformer.enableScaling(true);


//                    Picasso.get().load(imageURLs.get(0)).into(productImage);
                productDelivery.setText(new StringBuilder().append("Brand: ").append(brand).append(" Merchant: ").append(seller).toString());
                productDescription.setText(description);

                ConstraintLayout c = findViewById(R.id.wait2);
                c.setVisibility(View.GONE);
            });
        }).start();
    }

    private void showToast() {
        Log.v(TAG, "Done!!!");
    }

    public void startWebview(String url){
        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra("URL",url);
        intent.putExtra("domain",product.getDomain());
        startActivity(intent);
    }


}