package tajcse37.createpdf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import tajcse37.createpdf.R;
import tajcse37.createpdf.adapter.RearrangeImagesAdapter;
import tajcse37.createpdf.util.Constants;

import static tajcse37.createpdf.util.Constants.PREVIEW_IMAGES;

public class RearrangeImages extends AppCompatActivity implements RearrangeImagesAdapter.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private ArrayList<String> mImages;
    private RearrangeImagesAdapter mRearrangeImagesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rearrange_images);
        adsRequestInit();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mImages = intent.getStringArrayListExtra(PREVIEW_IMAGES);
        initRecyclerView(mImages);
    }
    private InterstitialAd mInterstitialAd;
    private static final String INTERESTITIAL_ADS_ID = "ca-app-pub-6554363642445395/4010149938";

    void adsRequestInit() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(INTERESTITIAL_ADS_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    void showAds() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        showAds();
    }
    private void initRecyclerView(ArrayList<String> images) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        mRearrangeImagesAdapter = new RearrangeImagesAdapter(this, images, this);
        recyclerView.setAdapter(mRearrangeImagesAdapter);
    }

    @Override
    public void onUpClick(int position) {
        mImages.add(position - 1, mImages.remove(position));
        mRearrangeImagesAdapter.positionChanged(mImages);
    }

    @Override
    public void onDownClick(int position) {
        mImages.add(position + 1, mImages.remove(position));
        mRearrangeImagesAdapter.positionChanged(mImages);

    }

    private void passUris() {
        Intent returnIntent = new Intent();
        returnIntent.putStringArrayListExtra(Constants.RESULT, mImages);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        passUris();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                passUris();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

