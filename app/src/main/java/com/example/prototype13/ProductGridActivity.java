package com.example.prototype13;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class ProductGridActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private ProductsAdapter adapter;
    private List<Product> productList;
    RecyclerView recyclerView;

    public String searchWords;
    public String keyWords;

    public int state = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_grid);
        productList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new ProductsAdapter(this, productList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Button button =findViewById(R.id.search_button);
        button.setOnClickListener(v -> {
            EditText editText = ProductGridActivity.this.findViewById(R.id.search_bar);
            searchWords = editText.getText().toString();
            keyWords = searchWords.trim().replaceAll(" ", "+");
            ConstraintLayout c = ProductGridActivity.this.findViewById(R.id.wait);
            c.setVisibility(View.VISIBLE);
            ProductGridActivity.this.initiateAmazonSearch(keyWords);
            ProductGridActivity.this.initiateFlipkartSearch(keyWords);
        });

        prepareProducts();
    }

    private void prepareProducts() {


        Product a = new Product("One plus 6", 40000, getString(R.string.oneplus6tImageUrl), "OnePlus", 4.5, "https://www.amazon.in/OnePlus-Mirror-Black-128GB-Storage/dp/B07DJD1Y3Q/");
        a.setDomain("");
        productList.add(a);
        a.setDomain("");
        a = new Product("One plus 6", 39990,getString(R.string.oneplus6tImageUrl), "OnePlus", 4.5, getString(R.string.oneplus_six_url));
        a.setDomain("");
        productList.add(a);
        a.setDomain("");
        a = new Product("One plus 6", 39990,getString(R.string.oneplus6tImageUrl), "OnePlus", 4.5, getString(R.string.oneplus_six_url));
        a.setDomain("");
        a.setDomain("");productList.add(a);
        a = new Product("One plus 6", 39990,getString(R.string.oneplus6tImageUrl), "OnePlus", 4.5, getString(R.string.oneplus_six_url));
        a.setDomain("");productList.add(a);
        a = new Product("One plus 6", 39990, getString(R.string.oneplus6tImageUrl), "OnePlus", 4.5, getString(R.string.oneplus_six_url));
        a.setDomain("");productList.add(a);
        a = new Product("One plus 6", 39990, getString(R.string.oneplus6tImageUrl), "OnePlus", 4.5, getString(R.string.oneplus_six_url));
        a.setDomain("");productList.add(a);
        a = new Product("One plus 6", 39990, getString(R.string.oneplus6tImageUrl), "OnePlus", 4.5, getString(R.string.oneplus_six_url));
        a.setDomain("");productList.add(a);
        a = new Product("One plus 6", 39990, getString(R.string.oneplus6tImageUrl), "OnePlus", 4.5, getString(R.string.oneplus_six_url));
        a.setDomain("");productList.add(a);
        a = new Product("One plus 6", 39990,getString(R.string.oneplus6tImageUrl), "OnePlus", 4.5, getString(R.string.oneplus_six_url));
        a.setDomain("");productList.add(a);
        a = new Product("One plus 6", 39990, getString(R.string.oneplus6tImageUrl), "OnePlus", 4.5, getString(R.string.oneplus_six_url));
        a.setDomain("");productList.add(a);

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public void initiateAmazonSearch(final String keywords){
        new Thread(() -> {

            productList.clear();
            ArrayList<Document> items = new ArrayList<>();

            Document doc;
            try {
                doc = Jsoup.connect("https://www.amazon.in/gp/aw/s/ref=nb_sb_noss?k=" + keywords).userAgent("Mozilla/5.0 (Linux; U; Android 2.2) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1").get();


                String listItemURL = ".a-section.a-spacing-medium";
                Elements listItems = doc.select(listItemURL);
                int length = listItems.size();

                for (int i = 0; i < length; i++) {
                    Document d = Jsoup.parse(listItems.get(i).html());
                    items.add(i, d);
                }

                ArrayList<String> Titles = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    Titles.add(i, items.get(i).select(".a-section.a-spacing-none>h2>span").first().text());
                }

                ArrayList<String> Brands = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    try {
                        //Brands.add(i, items.get(i).select("a>div>div.sx-table-detail>div.a-row.a-color-secondary>span:nth-child(2)").first().text());
                        Brands.add(i, "Nothing");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                ArrayList<Integer> Prices = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    try {
                        Prices.add(i, Integer.parseInt(items.get(i).select(".a-section.a-spacing-mini>div>div>span>span>span:nth-child(2)").first().text().replaceAll(",", "")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Prices.add(i, 0);
                    }
                }

                ArrayList<String> OriginalPrices = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    try {
                        OriginalPrices.add(i, items.get(i).select(".a-section.a-spacing-mini>div>div>span>span>span:nth-child(2)").get(1).text());
                    } catch (Exception e) {
                        e.printStackTrace();
                        OriginalPrices.add(i, "0");
                    }
                }

                ArrayList<Integer> Fulfillment = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    if (items.get(i).select(".a-section.a-spacing-none>div>div>span>span>i") != null)
                        Fulfillment.add(i, 1);
                    else
                        Fulfillment.add(i, 0);
                }

            /*ArrayList<String> Delivery1 = new ArrayList<>();
            for(int i =0; i<length; i++){
                try { Delivery1.add(i, items.get(i).select("a>div>div.sx-table-detail>div.a-spacing-micro.a-section>div:nth-child(2)").first().text()); }
                catch (Exception e) { e.printStackTrace();} }

            ArrayList<String> Delivery2 = new ArrayList<>();
            for(int i =0; i<length; i++){
                try { Delivery2.add(i, items.get(i).select("a>div>div.sx-table-detail>div.a-spacing-micro.a-section>div:nth-child(3)").first().text()); }
                catch (Exception e) { e.printStackTrace();} }*/

                ArrayList<Double> Stars = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    try {
                        Double d = Double.parseDouble(items.get(i).select(".a-section.a-spacing-none.a-spacing-top-mini>div>i>span").first().text().substring(0, 3));
                        Stars.add(i, d);
                    } catch (Exception e) { /*e.printStackTrace();*/
                        try {
                            Double d = Double.parseDouble(items.get(i).select(".a-section.a-spacing-none.a-spacing-top-mini>div>i>span").first().text().substring(0, 1));
                            Stars.add(i, d);
                        } catch (Exception f) { /*f.printStackTrace();*/
                            Stars.add(i, 0d);
                        }
                    }
                }

                ArrayList<Integer> Reviews = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    try {
                        String s = items.get(i).select(".a-section.a-spacing-none.a-spacing-top-mini>div>span").first().text();
                        Reviews.add(i, Integer.parseInt(s
                                //.substring(1, s.length() - 1)
                                .replaceAll(",", "").trim()));
                        //Log.i("Reviews:",s.substring(1,s.length()-1));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Reviews.add(i, 0);
                    }
                }

                ArrayList<String> ImageURLs = new ArrayList<>();
                /*for (int i = 0; i < 3; i++) {
                    try {
                        String a1 = items.get(i).select(".sx-product-image").first().attr("data-search-image-source-set");
                        int a2 = a1.indexOf(",");
                        String a3 = a1.substring(0, a2 - 22) + "150.jpg";
                        ImageURLs.add(i, a3);
                    } catch (Exception g) {
                        g.printStackTrace();
                    }
                }*/
                for (int i = 0; i < length; i++) {
                    try {
                        String a4 = items.get(i).select(".sg-col-4-of-12.sg-col>div>a>div>span>img").first().attr("src");
                        ImageURLs.add(i, a4.substring(0, a4.length() - 19) + "150.jpg");
                        Log.i(TAG, ImageURLs.get(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                ArrayList<String> ProductURLs = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    try {
                        ProductURLs.add(i, "https://www.amazon.in" + items.get(i).select("a").first().attr("href"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                for (int i = 0; i < length; i++) {
                    Product a = new Product(
                            Titles.get(i),
                            Prices.get(i),
                            ImageURLs.get(i),
                            Brands.get(i),
                            Stars.get(i),
                            Reviews.get(i),
                            ProductURLs.get(i),
                            Fulfillment.get(i),
                            OriginalPrices.get(i));
                    a.setDomain("www.amazon.in");
                    a.setValue((int) Math.round(Stars.get(i) * Reviews.get(i)));
                    productList.add(i, a);

                    state++;
                    //sortProducts();
                }
                ProductGridActivity.this.runOnUiThread(() -> {

                    adapter.notifyDataSetChanged();
                    ConstraintLayout c = ProductGridActivity.this.findViewById(R.id.wait);
                    c.setVisibility(View.GONE);
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void initiateFlipkartSearch(final String keywords){
        new Thread(() -> {
            int type = 1;
            productList.clear();
            ArrayList<Document> items = new ArrayList<>();
            Document doc;
            try {
                doc = Jsoup.connect("https://www.flipkart.com/search?q=" + keywords)
                        .ignoreContentType(true)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .referrer("http://www.google.com")
                        .timeout(0)
                        .followRedirects(true)
                        .get();

                String listItemURL = "body>div>div>div>div>div>div>div.col-12-12.bhgxx2>div>div>div";
                Elements listItems = null;
                try {
                    listItems = doc.select(listItemURL);
                } catch (Exception e) {
                    try {
                        listItems = doc.select("._31qSD5");
                        type = 3;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                int length = listItems.size();
                for (int i = 0; i < length; i++) {
                    Document d = Jsoup.parse(listItems.get(i).html());
                    items.add(i, d);
                }

                ArrayList<String> Titles = new ArrayList<>();
                try {
                    for (int i = 0; i < length; i++) {
                        Titles.add(i, items.get(i).select("._2cLu-l").first().text());
                        type = 1;
                    }
                } catch (Exception e) {
                    try {
                        for (int i = 0; i < length; i++) {
                            Titles.add(i, items.get(i).select("._2mylT6").first().text());
                            type = 2;
                        }
                    } catch (Exception e1) {
                        for (int i = 0; i < length; i++) {
                            Titles.add(i, items.get(i).select("._3wU53n").first().text());
                        }
                        type = 3;
                    }
                }

                ArrayList<Integer> Prices;
                Prices = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    try {
                        Prices.add(i, Integer.parseInt(items.get(i).select("._1vC4OE").first().text().substring(1).replaceAll(",", "")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Prices.add(i, 0);
                    }
                }

                ArrayList<String> OriginalPrices = null;
                ArrayList<Integer> Fulfillment = null;
                ArrayList<Double> Stars = null;
                ArrayList<Integer> Reviews = null;
                ArrayList<String> ProductURLs = null;
                ArrayList<String> ProductIDs = null;
                ArrayList<String> Brands = null;
                if (type == 1) {
                    OriginalPrices = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            OriginalPrices.add(i, items.get(i).select("._3auQ3N").first().text());
                        } catch (Exception e) {
                            e.printStackTrace();
                            OriginalPrices.add(i, "0");
                        }
                    }

                    Fulfillment = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        if (items.get(i).select("._3LWrw9>img") != null)
                            Fulfillment.add(i, 1);
                        else
                            Fulfillment.add(i, 0);
                    }

                    Stars = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            Double d = Double.parseDouble(items.get(i).select(".hGSR34").first().text());
                            Stars.add(i, d);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Stars.add(i, 0d);
                        }
                    }

                    Reviews = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            String s = items.get(i).select("._38sUEc").first().text();
                            Reviews.add(i, Integer.parseInt(s.substring(1, s.length() - 1).replaceAll(",", "").trim()));
                            //Log.i("Reviews:",s.substring(1,s.length()-1));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Reviews.add(i, 0);
                        }
                    }


                    ProductURLs = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            ProductURLs.add(i, "https://www.flipkart.in" + items.get(i).select("a").first().attr("href"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    ProductIDs = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            String a4 = ProductURLs.get(i);
                            a4 = a4.substring(a4.indexOf("pid="));
                            ProductIDs.add(i, a4.substring(0, a4.indexOf("&")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    Brands = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        Brands.add(i, "Brand");
                    }
                } else if (type == 2) {

                    OriginalPrices = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            OriginalPrices.add(i, items.get(i).select("._3auQ3N").first().text());
                        } catch (Exception e) {
                            e.printStackTrace();
                            OriginalPrices.add(i, "0");
                        }
                    }

                    Fulfillment = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        if (items.get(i).select("._3AqcXr>img") != null)
                            Fulfillment.add(i, 1);
                        else
                            Fulfillment.add(i, 0);
                    }

                    Stars = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        Stars.add(i, 0d);
                    }

                    Reviews = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        Reviews.add(i, 0);
                    }


                    ProductURLs = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            ProductURLs.add(i, "https://www.flipkart.in" + items.get(i).select("a").first().attr("href"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    ProductIDs = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            String a4 = ProductURLs.get(i);
                            a4 = a4.substring(a4.indexOf("pid="));
                            ProductIDs.add(i, a4.substring(0, a4.indexOf("&")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    Brands = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            Brands.add(i, items.get(i).select("._2B_pmu").first().text());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else if (type == 3) {

                    OriginalPrices = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            OriginalPrices.add(i, items.get(i).select("._2GcJzG").first().text());
                        } catch (Exception e) {
                            e.printStackTrace();
                            OriginalPrices.add(i, "0");
                        }
                    }

                    Fulfillment = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        if (items.get(i).select("._3n6o0t>img") != null)
                            Fulfillment.add(i, 1);
                        else
                            Fulfillment.add(i, 0);
                    }

                    Stars = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            Double d = Double.parseDouble(items.get(i).select(".hGSR34").first().text());
                            Stars.add(i, d);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Stars.add(i, 0d);
                        }
                    }

                    Reviews = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            String s = items.get(i).select("._38sUEc>span>span:nth-child(1)").first().text();
                            Reviews.add(i, Integer.parseInt(s.substring(0, s.indexOf("Ratings") - 1).replaceAll(",", "").trim()));
                            //Log.i("Reviews:",s.substring(1,s.length()-1));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Reviews.add(i, 0);
                        }
                    }

                    ProductURLs = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            ProductURLs.add(i, "https://www.flipkart.in" + items.get(i).select("a").first().attr("href"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    ProductIDs = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        try {
                            String a4 = ProductURLs.get(i);
                            a4 = a4.substring(a4.indexOf("pid="));
                            ProductIDs.add(i, a4.substring(0, a4.indexOf("&")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    Brands = new ArrayList<>();
                    for (int i = 0; i < length; i++) {
                        Brands.add(i, "Brand");
                    }
                }


                for (int i = 0; i < length; i++) {
                    Product a = new Product(
                            Titles.get(i),
                            Prices.get(i),
                            "",
                            Brands.get(i),
                            Stars.get(i),
                            Reviews.get(i),
                            ProductURLs.get(i),
                            Fulfillment.get(i),
                            OriginalPrices.get(i));
                    a.setDomain("www.flipkart.com");
                    a.setProductID(ProductIDs.get(i));
                    a.setValue((int) Math.round(Stars.get(i) * Reviews.get(i)));
                    productList.add(i, a);
                    state++;
                    // sortProducts();
                }

                ProductGridActivity.this.runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                    ConstraintLayout c = ProductGridActivity.this.findViewById(R.id.wait);
                    c.setVisibility(View.GONE);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void sortProducts(){
            for (int i = 0; i < productList.size(); i++)
            {
                for (int j = i + 1; j < productList.size(); j++)
                {
                    if ((productList.get(i).getValue() < productList.get(j).getValue())
                    && (!productList.get(i).getDomain().equals(productList.get(j).getDomain())))
                    {
                        Product temp = productList.get(i);
                        productList.set(i,productList.get(j));
                        productList.set(j,temp);
                    }
                }
            }

        String[] words = keyWords.split("\\W+");
        List<Boolean> matchesKeyword = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++)
        {   matchesKeyword.add(i,true);
            for (String word : words) {
                matchesKeyword.set(i, (matchesKeyword.get(i) && productList.get(i).getName().contains(word)));
            }
        }

        List<Product> topProducts = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++){
            if(matchesKeyword.get(i)){
                topProducts.add(productList.get(i));
                productList.remove(i);
                i--;
            }
        }

        for (int i = 0; i < topProducts.size(); i++)
        {
            for (int j = i + 1; j < topProducts.size(); j++)
            {
                if ((topProducts.get(i).getValue() < topProducts.get(j).getValue())
                        && (!topProducts.get(i).getDomain().equals(topProducts.get(j).getDomain())))
                {
                    Product temp = topProducts.get(i);
                    topProducts.set(i,topProducts.get(j));
                    topProducts.set(j,temp);
                }
            }
        }

        for (int i=0;i<topProducts.size();i++) {
            productList.add(i,topProducts.get(i));
        }

        runOnUiThread(() -> {
            adapter.notifyDataSetChanged();
            ConstraintLayout c = ProductGridActivity.this.findViewById(R.id.wait);
            c.setVisibility(View.GONE);

        });

    }
}