package com.imagelib.app;

import java.util.ArrayList;

import com.imagelib.R;
import com.imagelib.R.layout;
import com.imagelib.data.ImageLoader2;
import com.imagelib.data.ImageMgr;
import com.imagelib.data.ImageLoader2.ImageCallback;
import com.imagelib.data.ImageMgr.ImageInfo;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class ActivityPlay extends Activity {
	private static ArrayList<String> mArray ;
	private ImageSwitcher mSwitcher;
	private static int mIndex = 0;
	
	public static void setArray(ArrayList<String> array,int index)
	{
		mArray = array;
		mIndex = index;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);

		
		


		mSwitcher.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				ImageView imageView = new ImageView(ActivityPlay.this);
				imageView.setBackgroundColor(0xff0000);
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				return imageView;
			}
		});

		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));
		

		
		mSwitcher.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FinishWidthResult(mIndex-1);
			}

		});
		
	}
	private boolean mPause = false;
	private void showNext(int time) {
		if(mPause)
			return;
		Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				
				if (mIndex == mArray.size()) {
					FinishWidthResult(mIndex-1);
					
				} else {
					DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);

					ImageInfo info = ImageMgr.GetInstance().getImage(
							mArray.get(mIndex));
					if(info != null)
					{
						ImageLoader2.GetInstance().requestImage(info.path,
								dm.widthPixels, dm.heightPixels, ImageLoader2.quality_normal, callback);	
					}
					
					mIndex ++;
				}
			}
		};
		handler.postDelayed(runnable, time);//
	}
	private void FinishWidthResult(int index)
	{
		Intent intent = new Intent();
		intent.putExtra("path", mArray.get(index));
		this.setResult(1, intent);
		ActivityPlay.this.finish();
	}
	private ImageCallback callback = new ImageCallback() {

		@Override
		public void onImageLoadSuccess(Bitmap bitmap, String path,int width,int height) {
			// TODO Auto-generated method stub
			mSwitcher.setImageDrawable(new BitmapDrawable(bitmap));
			
			showNext(2000);
		}

		@Override
		public void onImageRotate(String path) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onImageLoadFail(String path, int width, int height) {
			// TODO Auto-generated method stub
			
			showNext(2000);
		}

	};

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mPause = false;
		showNext(0);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mPause = true;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}
