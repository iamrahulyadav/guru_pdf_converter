package tajcse37.createpdf.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.eftimoff.viewpagertransformers.DepthPageTransformer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import tajcse37.createpdf.R;
import tajcse37.createpdf.adapter.PreviewAdapter;
import tajcse37.createpdf.adapter.PreviewImageOptionsAdapter;
import tajcse37.createpdf.model.PreviewImageOptionItem;
import tajcse37.createpdf.util.Constants;
import tajcse37.createpdf.util.ImageSortUtils;

import static tajcse37.createpdf.util.Constants.PREVIEW_IMAGES;

public class PreviewActivity extends AppCompatActivity implements PreviewImageOptionsAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private ArrayList<String> mImagesArrayList;
    private static final int INTENT_REQUEST_REARRANGE_IMAGE = 1;
    private PreviewAdapter mPreviewAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        adsRequestInit();
        ButterKnife.bind(this);
        // Extract mImagesArrayList uri array from the intent
        Intent intent = getIntent();
        mImagesArrayList = intent.getStringArrayListExtra(PREVIEW_IMAGES);

        mViewPager = findViewById(R.id.viewpager);
        mPreviewAdapter = new PreviewAdapter(this, mImagesArrayList);
        mViewPager.setAdapter(mPreviewAdapter);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        Objects.requireNonNull(getSupportActionBar()).hide();
        showOptions();
    }

    private void showOptions() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        PreviewImageOptionsAdapter adapter = new PreviewImageOptionsAdapter(this, getOptions(),
                getApplicationContext());
        mRecyclerView.setAdapter(adapter);
    }

    private ArrayList<PreviewImageOptionItem> getOptions() {
        ArrayList<PreviewImageOptionItem> mOptions = new ArrayList<>();
        mOptions.add(new PreviewImageOptionItem(R.drawable.ic_rearrange, getString(R.string.rearrange_text)));
        mOptions.add(new PreviewImageOptionItem(R.drawable.ic_sort, getString(R.string.sort)));
        return mOptions;
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0:
                reArrangeImages();
                break;
            case 1:
                sortImages();
                break;
        }
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
    private void sortImages() {
        new MaterialDialog.Builder(this)
                .title(R.string.sort_by_title)
                .items(R.array.sort_options_images)
                .itemsCallback((dialog, itemView, position, text) -> {
                    ImageSortUtils.performSortOperation(position, mImagesArrayList);
                    mPreviewAdapter.setData(new ArrayList<>(mImagesArrayList));
                    mViewPager.setAdapter(mPreviewAdapter);
                })
                .negativeText(R.string.cancel)
                .show();
    }

    private void reArrangeImages() {
        Intent intent = new Intent(this, RearrangeImages.class);
        intent.putStringArrayListExtra(PREVIEW_IMAGES, mImagesArrayList);
        startActivityForResult(intent, INTENT_REQUEST_REARRANGE_IMAGE);
    }

    private void passUris() {
        Intent returnIntent = new Intent();
        returnIntent.putStringArrayListExtra(Constants.RESULT, mImagesArrayList);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_REQUEST_REARRANGE_IMAGE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        try {
                            mImagesArrayList = data.getStringArrayListExtra(Constants.RESULT);
                            mPreviewAdapter.setData(mImagesArrayList);
                            mViewPager.setAdapter(mPreviewAdapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        passUris();
    }
}