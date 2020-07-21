package curiousfreaks.com.gre333;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class BuyCoffee extends AppCompatActivity {
    Button buyButton;
    private final String item="acoffee";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_coffee);
        buyButton=findViewById(R.id.buyCoffee);

       /* String base64EncodedKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArOR1nr04Aa/GB5B7tMG9/3gngqD/oukINSHXSpxPSFB7uVA7A8mY1TNmEfdR/wxeGQdnx9wwpm8YVRgbLCPoNgCMs5yK8tHE4sN28EzGkrw7Iwwkng9xVdoYJOMC02yRByKHxREGwQDjHvntgDKXHfXweSWnNbLONUQrYdecx/rrt4CvkrAFUm20plwa/rA32P6gxFeTMGO2NjsJu0s0ceIGDCam8TuV3zaPZXYy3G1mlF3xPKYY+k8jTKaLspyOWumZken//ub8//FBISRjuxUaLstaIhyN0Adfbx6FOqvGqYsZ+nlmgPJn7JsW/2D88+3uznZ7GJvAiHGTYBinIQIDAQAB";
        mHelper=new IabHelper(getApplicationContext(),base64EncodedKey);
       *//* mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener(){
            @Override
            public void onIabSetupFinished(IabResult result) {
                if(result.isSuccess())
                {
                    //Toast.makeText(getApplicationContext(),"Connection Success",Toast.LENGTH_SHORT).show();
                }
                if(result.isFailure())
                {
                    //Toast.makeText(getApplicationContext(),"Connection Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });*//*

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mHelper.launchPurchaseFlow(BuyCoffee.this, item, 99, purchaseFinishedListener, "myCoffee");

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!mHelper.handleActivityResult(requestCode,resultCode,data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    private final IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener=new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            try {
                if (info.getSku().equals(item)) {
                    mHelper.queryInventoryAsync(inventoryListener);
                }
                if (!info.getSku().equals(item)) {
                    Toast.makeText(getApplicationContext(), "There is some issue in purchasing coffee.", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };
    private final IabHelper.QueryInventoryFinishedListener inventoryListener =new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
            try {
                if (!result.isFailure()) {
                    mHelper.consumeAsync(inv.getPurchase(item), consumeFinishLitener);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };
    private final IabHelper.OnConsumeFinishedListener consumeFinishLitener=new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if(result.isSuccess())
            {
                Toast.makeText(getApplicationContext(),"Thanks you for purchasing me a coffee.",Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mHelper != null) {
                mHelper.dispose();
                mHelper = null;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }*/
    }
}