package com.rmrdevelopment.lifeleadership.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import bipin.wheel.widget.WheelView;
import bipin.wheel.widget.adapters.AbstractWheelTextAdapter;

import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rmrdevelopment.lifeleadership.LLApplication;
import com.rmrdevelopment.lifeleadership.R;
import com.rmrdevelopment.lifeleadership.SQLiteHelper;
import com.rmrdevelopment.lifeleadership.slider.HorizontalSideScrollView;
import com.rmrdevelopment.lifeleadership.slider.HorizontalSideScrollView.SizeCallback;
import com.rmrdevelopment.lifeleadership.util.Constant;
import com.rmrdevelopment.lifeleadership.util.StreamingMediaPlayer;

@SuppressLint("SetJavaScriptEnabled")
public class LifeLeadershipMainActivity extends BaseActivity implements
		OnGestureListener {

	private LayoutInflater inflater;
	static float scale = 0f;
	private Context mContext;
	private  HorizontalSideScrollView horizontalScrollView;

	// gesture
	private GestureDetector gestureDetector;
	@SuppressWarnings("unused")
	private  View.OnTouchListener gestureListener;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	// MenuView
	private View menuView;
	private static boolean menuOut = false;
	private ExpandableListView expListView;
	private ExpandableListAdapter listAdapter;
	private List<String> listDataHeader;
	private ArrayList<Integer> listImgs;
	private HashMap<String, ArrayList<HashMap<String, String>>> mapDataChild;

	// AppView
	private View applicationView;
	private Button btnSlider;
	private Button btnRadio;
	private Button btnDone;
	private Button btnCancel;
	private Button btnPlay;
	private Button btnLike;
	private Button player_btn1;
	private Button player_btn2; 
	private Button player_btn4;
	
	private Animation animBottomToTop;
	private Animation animTopToBottom;
	
	private RelativeLayout btnRelativeNext;
	private RelativeLayout btnRelativeRewind;
	private RelativeLayout relativeBottom;
	private RelativeLayout relativeTop;
	private RelativeLayout relativeSpeakerSearch;
	private RelativeLayout relativeSubjectSearch;
	
	private LinearLayout linearAppSection4;
	private LinearLayout linearAppSection3;
	
	private boolean booleanClicked = false;
	
	private TextView txtNext;
	private TextView txtArtist;
	private TextView txtTitle;
	private TextView txtStationname;
	private TextView txtSpeakerSearch;
	private TextView txtSubjectSearch;
	private TextView txtCurrentTime;
	private ImageView imgThumb;
	public static TextView txtTotalTime;
	private StreamingMediaPlayer audioStreamer = null;
	private int currentStationPos = 0;
	
	private ProgressBar progressBar;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private LinearLayout linearMystations;
	private WebView webView;
	private WebView webviewIdCard;
	
	//ImageView imgIdCard;
	private boolean booleanFlagFav = false;
	private boolean booleanFlagLiked = false;
	private boolean booleanFlagText = false;
	private Typeface typeFace1;
	private Typeface typeFace2;
	public static LifeLeadershipMainActivity lifeObj;
	public int currentAdPos;

	// Wheel:
	@SuppressWarnings("unused")
	private boolean booleanScrolling = false;
	private WheelView wheelSelectSpeaker;
	private int curPoswheel1 = 0;
	private int curPoswheel2 = 0;
	private int curSelectedwheel = 0;
	
	// new for Subscription
	private int selectedFavPos = -1;
	private boolean iSFavSelected = false;

	private ArrayList<HashMap<String, String>> arrayofspeakers = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> arrayofsubjects = new ArrayList<HashMap<String, String>>();
	
	//Telephony Manager:
	private TelephonyManager mgr ;

	private SQLiteHelper helper;
	private SQLiteDatabase db= null;
	
	private boolean isInitial = true;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isOnline()) {
			getSkipCount();
		} else {
			Toast.makeText(getApplicationContext(),
					"" + Constant.network_error, Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(progressDialog!=null){ //new 
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
		}
		EasyTracker.getInstance(this).activityStop(this); 
	}
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 

		lifeObj = this;
		mContext = this;
		scale = getResources().getDisplayMetrics().density;
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));

		options = new DisplayImageOptions.Builder().showStubImage(0)
				.showImageForEmptyUri(0).cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		typeFace1 = Typeface.createFromAsset(getAssets(), "dsdigi.ttf");
		typeFace2 = Typeface.createFromAsset(getAssets(),
				"librebaskervilleItalic.ttf");
		
		//new
		helper = new SQLiteHelper(this, "lifeleadership.sqlite");
		helper.createDatabase();
		db = helper.openDatabase();
		//
		

		init();
		onClickEvents();
		// setArray();

		CallAsynchronous();

		if (isOnline()) {
			getSpeakers();
		} else {
			Toast.makeText(getApplicationContext(),
					"" + Constant.network_error, Toast.LENGTH_SHORT).show();
		}

	}

	@SuppressLint("NewApi")
	private void init() {
		// TODO Auto-generated method stub
		inflater = LayoutInflater.from(this);

		horizontalScrollView = (HorizontalSideScrollView) inflater.inflate(
				R.layout.slider_main_scr, null);
		setContentView(horizontalScrollView);

		applicationView = (View) inflater.inflate(
				R.layout.activity_life_leadership_main, null);
		btnSlider = (Button) applicationView.findViewById(R.id.buttonSlide);
		btnRadio = (Button) applicationView.findViewById(R.id.radiobtn);

		linearAppSection3 = (LinearLayout) applicationView
				.findViewById(R.id.relativeAppSection3);
		linearAppSection4 = (LinearLayout) applicationView
				.findViewById(R.id.relativeAppSection4);

		relativeSpeakerSearch = (RelativeLayout) applicationView
				.findViewById(R.id.relativeSpeakerSearch);
		relativeSubjectSearch = (RelativeLayout) applicationView
				.findViewById(R.id.relativeSubjectSearch);
		linearMystations = (LinearLayout) applicationView
				.findViewById(R.id.mystations);
		txtSpeakerSearch = (TextView) applicationView
				.findViewById(R.id.speaker_search);
		txtSubjectSearch = (TextView) applicationView
				.findViewById(R.id.subject_search);
		btnDone = (Button) applicationView.findViewById(R.id.done);
		btnCancel = (Button) applicationView.findViewById(R.id.cancel);
		relativeBottom = (RelativeLayout) applicationView
				.findViewById(R.id.bottom);
		relativeTop = (RelativeLayout) applicationView.findViewById(R.id.top);
		btnPlay = (Button) applicationView.findViewById(R.id.play);
		btnLike = (Button) applicationView.findViewById(R.id.like);
		btnRelativeNext = (RelativeLayout) applicationView.findViewById(R.id.next);
		btnRelativeRewind= (RelativeLayout) applicationView.findViewById(R.id.layoutRewind);
		txtNext = (TextView) applicationView.findViewById(R.id.txtNext);
		player_btn1 = (Button) applicationView.findViewById(R.id.player_btn1);
		player_btn2 = (Button) applicationView.findViewById(R.id.player_btn2);

		player_btn1.setVisibility(View.GONE);
		player_btn2.setVisibility(View.GONE);

		player_btn4 = (Button) applicationView.findViewById(R.id.player_btn4);
		imgThumb = (ImageView) applicationView.findViewById(R.id.thumg_img);
		txtArtist = (TextView) applicationView.findViewById(R.id.artist);
		txtTitle = (TextView) applicationView.findViewById(R.id.title);
		txtStationname = (TextView) applicationView
				.findViewById(R.id.stationname);
		txtStationname.setTypeface(typeFace1);
		txtSpeakerSearch.setTypeface(typeFace2);
		txtSubjectSearch.setTypeface(typeFace2);
		txtCurrentTime = (TextView) applicationView.findViewById(R.id.time1);
		txtTotalTime = (TextView) applicationView.findViewById(R.id.time2);

		menuView = (View) inflater.inflate(R.layout.side_menu, null);

		expListView = (ExpandableListView) menuView.findViewById(R.id.lst);
		expListView.setDivider(null);
		expListView.setDividerHeight(0);

		prepareListData();

		final View[] children = new View[] { menuView, applicationView };
		int scrollToViewIdx = 1;
		horizontalScrollView.initViews(children, scrollToViewIdx,
				new SizeCallbackForMenu(btnSlider));
		btnSlider.setOnClickListener(new ClickListenerForScrolling(
				horizontalScrollView, menuView));

		wheelSelectSpeaker = (WheelView) applicationView
				.findViewById(R.id.wheel1);
		wheelSelectSpeaker.setViewAdapter(new Wheel1Adapter(this,
				arrayofspeakers));

		animBottomToTop = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.enter_from_right);
		animTopToBottom = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.enter_left_view);

		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// Log.d("Gesture Touch", "true");
				return gestureDetector.onTouchEvent(event);
			}
		};

		relativeTop.setBackgroundColor(Color.parseColor("#D22129"));

		// WebView:
		webView = (WebView) findViewById(R.id.webview);
		webView.setBackgroundColor(0x00000000);
		//webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		webView.getSettings().setJavaScriptEnabled(true);
		//webView.setVerticalScrollBarEnabled(true);
		
		webviewIdCard = (WebView) findViewById(R.id.webviewIdCard);
		webviewIdCard.setBackgroundColor(0x00000000);
		webviewIdCard.getSettings().setJavaScriptEnabled(true);
		//webviewIdCard.setHorizontalScrollBarEnabled(false);
		//webviewIdCard.setVerticalScrollBarEnabled(true);
		webviewIdCard.loadUrl(Constant.idCardUrl+"uid="+LLApplication.getUserId());
		
		//IDCard
		//imgIdCard=(ImageView)findViewById(R.id.imageViewIdCard);
		
		 mgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		if (mgr != null) {
			mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		}
		
	}

	
	
	PhoneStateListener phoneStateListener = new PhoneStateListener() {
	    @Override
	    public void onCallStateChanged(int state, String incomingNumber) {
	    	if(btnPlay.getBackground().getConstantState().equals
                    (getResources().getDrawable(R.drawable.pausebtn).getConstantState())){
	    		if (state == TelephonyManager.CALL_STATE_RINGING) {
		            //Incoming call: Pause music
		        	Log.e("TelephonyManager","CALL_STATE_RINGING");
		        	if (txtTotalTime.getText().length() > 0) {
						if (LLApplication.getStationLists().size() > currentStationPos) {
							if (isOnline()) {
								playStreaming();
							} else {
								Toast.makeText(getApplicationContext(),
										"" + Constant.network_error,
										Toast.LENGTH_SHORT).show();
							}
						}
					}
		        } else if(state == TelephonyManager.CALL_STATE_IDLE) {
		            //Not in call: Play music
		        	Log.e("TelephonyManager","CALL_STATE_IDLE");
		        	
		        } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
		            //A call is dialing, active or on hold
		        	Log.e("TelephonyManager","CALL_STATE_OFFHOOK");
		        	if (txtTotalTime.getText().length() > 0) {
						if (LLApplication.getStationLists().size() > currentStationPos) {
							if (isOnline()) {
								playStreaming();
							} else {
								Toast.makeText(getApplicationContext(),
										"" + Constant.network_error,
										Toast.LENGTH_SHORT).show();
							}
						}
					}
		        }
        	}
	        super.onCallStateChanged(state, incomingNumber);
	    }
	};
	
	private void onClickEvents() {

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(
						getApplicationContext(),
						listDataHeader.get(groupPosition)
								+ " : "
								+ mapDataChild.get(
										listDataHeader.get(groupPosition)).get(
										childPosition), Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});

		relativeSpeakerSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!booleanClicked) {
					booleanClicked = true;
					relativeBottom.setVisibility(View.VISIBLE);
					relativeBottom.startAnimation(animBottomToTop);
					wheelSelectSpeaker.setViewAdapter(new Wheel1Adapter(
							LifeLeadershipMainActivity.this, arrayofspeakers));
					wheelSelectSpeaker.setCurrentItem(curPoswheel1);
					curSelectedwheel = 0;
				}
			}
		});

		relativeSubjectSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!booleanClicked) {
					booleanClicked = true;
					relativeBottom.setVisibility(View.VISIBLE);
					relativeBottom.startAnimation(animBottomToTop);
					wheelSelectSpeaker.setViewAdapter(new Wheel1Adapter(
							LifeLeadershipMainActivity.this, arrayofsubjects));
					wheelSelectSpeaker.setCurrentItem(curPoswheel2);
					curSelectedwheel = 1;
				}
			}
		});

		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(arrayofspeakers.size()>0 && arrayofsubjects.size()>0){// new
					if (curSelectedwheel == 0) {
						curPoswheel1 = wheelSelectSpeaker.getCurrentItem();
						txtSpeakerSearch.setText(""
								+ arrayofspeakers.get(curPoswheel1).get("Name"));
					} else {
						curPoswheel2 = wheelSelectSpeaker.getCurrentItem();
						txtSubjectSearch.setText(""
								+ arrayofsubjects.get(curPoswheel2).get("Name"));
					}
					// new Subscription - replaced == with <=
					if (curPoswheel1 <= -1)
						curPoswheel1 = 0;
					if (curPoswheel2 <= -1)
						curPoswheel2 = 0;

					if (curPoswheel1 == 0 && curPoswheel2 == 0) {
						player_btn4.setVisibility(View.GONE);
					} else {
						player_btn4.setVisibility(View.VISIBLE);
					}

					relativeBottom.setVisibility(View.GONE);
					relativeBottom.startAnimation(animTopToBottom);
					booleanClicked = false;
					
					//new Subscription
					iSFavSelected = false;
					
					callmyStations(true);
				}
				
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				relativeBottom.setVisibility(View.GONE);
				relativeBottom.startAnimation(animTopToBottom);
				booleanClicked = false;
			}
		});

		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (txtTotalTime.getText().length() > 0) {
					if (LLApplication.getStationLists().size() > currentStationPos) {
						if (isOnline()) {
							playStreaming();
						} else {
							Toast.makeText(getApplicationContext(),
									"" + Constant.network_error,
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		});
		
		player_btn4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!LLApplication.getStationInfo().get("StationID")
						.equals("0")
						&& LLApplication.getStationLists().size() > 0) {
					if (!booleanFlagFav) {
						if (isOnline()) {
							addStationToFavorites();
						} else {
							Toast.makeText(getApplicationContext(),
									"" + Constant.network_error,
									Toast.LENGTH_SHORT).show();
						}
					} else {
						if (isOnline()) {
							RemoveStation(
									LLApplication.getStationInfo().get(
											"StationID"), false, -1);
						} else {
							Toast.makeText(getApplicationContext(),
									"" + Constant.network_error,
									Toast.LENGTH_SHORT).show();
						}
					}
				}else{
					String strMsg = "Can't add/remove this station from favorite.";
					AlertDialog.Builder alert = new AlertDialog.Builder(
							LifeLeadershipMainActivity.this);
					alert.setTitle("Alert");
					alert.setMessage(strMsg);
					alert.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									dialog.dismiss();
								}
							});
					alert.show();
				}
			}
		});

		btnRadio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.e("MenuOut",">>>"+menuOut);
				if(horizontalScrollView.getScrollX()!=0){
					Log.e("MenuOut",">>>"+menuOut);
					slideMenuRight();
					menuOut= false;
				}
					
				btnRadio.setVisibility(View.GONE);
				webView.setVisibility(View.GONE);
				webviewIdCard.setVisibility(View.GONE);
				//imgIdCard.setVisibility(View.GONE);
				relativeTop.setBackgroundColor(Color.parseColor("#D22129"));
				linearMystations.setVisibility(View.VISIBLE);
				linearMystations.startAnimation(animBottomToTop);
			}
		});

		btnLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (LLApplication.getStationLists().size() > currentStationPos) {
					if (isOnline()) {
						likeAudio();
					} else {
						Toast.makeText(getApplicationContext(),
								"" + Constant.network_error, Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});

		btnRelativeNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LLApplication.setFlagSkipCount(0);
				PlayNextAudio();
			}
		});
		
		btnRelativeRewind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Rewind Functionality
				Log.d("relativeRewind", "click 1");
				if(audioStreamer!=null){
					Log.d("relativeRewind", "click 2");
					audioStreamer.rewindAudio();
				}
			}
		});
	}

	public void PlayNextAudio() {
		// TODO Auto-generated method stub
		if (LLApplication.getSkipcount() > 0) {
			if (LLApplication.getStationLists().size() > currentStationPos) {
				if (!booleanClicked) {
					if (currentStationPos != LLApplication.getStationLists()
							.size() - 1) {
						currentStationPos++;
						booleanClicked = true;
						if (isOnline()) {
							startStreamingAudio(Constant.cdnPath
									+ LLApplication.getStationLists()
											.get(currentStationPos)
											.get("FileName") + ".mp3");
							playStreaming();
							if (LLApplication.getFlagSkipCount() == 0)
								updateSkipCount();
						}
					}
				}
			}
		}
	}

	private void getSkipCount() {
		// TODO Auto-generated method stub
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("func", "getSkipCount");
				map.put("userID", ""+LLApplication.getUserId());

				String tempResponse = getResponse(map);
				Log.i("response", "" + tempResponse);
				Update_getSkipCount(tempResponse);
			}
		});
		t.start();
	}

	protected void Update_getSkipCount(final String tempResponse) {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (tempResponse != null) {
					try {
						json_str = new JSONObject(tempResponse);
						int count = json_str.getInt("skipcount");
						if (count > 0) {
							LLApplication.setSkipcount(count);

						} else {
							LLApplication.setSkipcount(0);
						}
						txtNext.setText("" + LLApplication.getSkipcount());

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
	}

	private void updateSkipCount() {
		// TODO Auto-generated method stub
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("func", "updateSkipCount");
				map.put("userID", ""+LLApplication.getUserId());

				String tempResponse = getResponse(map);
				Log.i("response", "" + tempResponse);
				Update_getSkipCount(tempResponse);
			}
		});
		t.start();
	}

	protected void Update_SkipCount(final String tempResponse) {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (tempResponse != null) {
					try {
						json_str = new JSONObject(tempResponse);
						int count = json_str.getInt("skipcount");
						if (count > 0) {
							LLApplication.setSkipcount(count);

						} else {
							LLApplication.setSkipcount(0);
						}
						txtNext.setText("" + LLApplication.getSkipcount());

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
	}

	protected void getSpeakers() {
		// TODO Auto-generated method stub

		if(!((LifeLeadershipMainActivity) context).isFinishing())
		{
		    //show dialog
			progressDialog = ProgressDialog.show(LifeLeadershipMainActivity.this,
					null, "Loading...	", true, false);
		}
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("func", "getSpeakers");
				map.put("userid", "" + LLApplication.getUserId());

				response = getResponse(map);
				Log.i("response", "" + response);
				Update_getSpeakers();
			}
		});
		t.start();
	}

	protected void Update_getSpeakers() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (response != null) {
					try {
						json_str = new JSONObject(response);
						data_array = json_str.getString("Options");
						array = new JSONArray(data_array);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if(array!=null){
						arrayofspeakers.clear();

						HashMap<String, String> map1 = new HashMap<String, String>();

						map1.put("AttrID", "0");
						map1.put("Name", "SPEAKER SEARCH");
						map1.put("Description", "");
						map1.put("Type", "");
						map1.put("Image", "");
						map1.put("Selected", "");
						map1.put("Primary", "");

						arrayofspeakers.add(map1);

						for (int i = 0; i < array.length(); i++) {
							JSONObject obj;
							try {
								obj = array.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();

								map.put("AttrID", "" + obj.getString("AttrID"));
								map.put("Name", "" + obj.getString("Name"));
								map.put("Description",
										"" + obj.getString("Description"));
								map.put("Type", "" + obj.getString("Type"));
								map.put("Image", "" + obj.getString("Image"));
								map.put("Selected", "" + obj.getString("Selected"));
								map.put("Primary", "" + obj.getString("Primary"));

								arrayofspeakers.add(map);

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						if (isOnline()) {
							getSubjects();
						} else {
							Toast.makeText(getApplicationContext(),
									"" + Constant.network_error, Toast.LENGTH_SHORT)
									.show();
						}
					}else{
						Toast.makeText(getApplicationContext(),
								"Unable to Load Speakers" , Toast.LENGTH_SHORT)
								.show();
					}
					
					
				}
			}
		});
	}

	protected void getSubjects() {
		// TODO Auto-generated method stub

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("func", "getSubjects");
				map.put("userid", "" + LLApplication.getUserId());

				response = getResponse(map);
				Log.i("response", "" + response);
				Update_getSubjects();
			}
		});
		t.start();
	}

	protected void Update_getSubjects() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (response != null) {
					try {
						json_str = new JSONObject(response);
						data_array = json_str.getString("Options");
						array = new JSONArray(data_array);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					arrayofsubjects.clear();

					HashMap<String, String> map1 = new HashMap<String, String>();

					map1.put("AttrID", "0");
					map1.put("Name", "SUBJECT SEARCH");
					map1.put("Description", "");
					map1.put("Type", "");
					map1.put("Image", "");
					map1.put("Selected", "");
					map1.put("Primary", "");

					arrayofsubjects.add(map1);

					for (int i = 0; i < array.length(); i++) {
						JSONObject obj;
						try {
							obj = array.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();

							map.put("AttrID", "" + obj.getString("AttrID"));
							map.put("Name", "" + obj.getString("Name"));
							map.put("Description",
									"" + obj.getString("Description"));
							map.put("Type", "" + obj.getString("Type"));
							map.put("Image", "" + obj.getString("Image"));
							map.put("Selected", "" + obj.getString("Selected"));
							map.put("Primary", "" + obj.getString("Primary"));

							arrayofsubjects.add(map);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if (isOnline()) {
						getCommercials();
					} else {
						Toast.makeText(getApplicationContext(),
								"" + Constant.network_error, Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});
	}

	protected void getCommercials() {
		// TODO Auto-generated method stub

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("func", "getCommercials");

				response = getResponse(map);
				Log.i("Commercial response", "" + response);
				Update_getCommercials();
			}
		});
		t.start();
	}

	protected void Update_getCommercials() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if(progressDialog!=null){
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}

				if (response != null) {
					try {
						json_str = new JSONObject(response);
						data_array = json_str.getString("files");
						array = new JSONArray(data_array);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					LLApplication.getCommercialsLists().clear();
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj;
						try {
							obj = array.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();

							map.put("AudioID", "" + obj.getString("AudioID"));
							map.put("Name", "" + obj.getString("Name"));
							map.put("FileName", "" + obj.getString("FileName"));
							map.put("Stamp", "" + obj.getString("Stamp"));
							map.put("ProductRef",
									"" + obj.getString("ProductRef"));
							map.put("Active", "" + obj.getString("Active"));
							map.put("PlayCount",
									"" + obj.getString("PlayCount"));

							LLApplication.getCommercialsLists().add(map);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					Log.i("getCommercialsLists ", ""
							+ LLApplication.getCommercialsLists().size());

					if (isOnline()) {
						getFavoriteStations(false);
					} else {
						Toast.makeText(getApplicationContext(),
								"" + Constant.network_error, Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});
	}

	private void UpdateUI() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(progressDialog!=null){
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
					}
				}
				startAudio();
			}
		});
	}

	public void getFavoriteStations(final boolean flagPb) {
		// TODO Auto-generated method stub

		if (!flagPb) {
			if(!((LifeLeadershipMainActivity) context).isFinishing())
			{
				//show dialog
				progressDialog = ProgressDialog.show(LifeLeadershipMainActivity.this,
						null, "Loading...	", true, false);
			}
		}
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("func", "getFavs");
				map.put("userid", "" + LLApplication.getUserId());

				response = getResponse(map);
				
				//temp Subscription for testing
				//response = Constant.testJson;
				
				Log.i("response", "" + response);
				Update_getFavoriteStations(flagPb);
			}
		});
		t.start();
	}

	private void Update_getFavoriteStations(final boolean flagPb) {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (flagPb) {
					if(progressDialog!=null){
						if (progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					}
				}
				if (response != null) {
					try {
						json_str = new JSONObject(response);
						data_array = json_str.getString("stations");
						array = new JSONArray(data_array);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					LLApplication.getFavstationLists().clear();
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj;
						try {
							obj = array.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();

							map.put("UserID", "" + obj.getString("UserID"));
							map.put("Name", "" + obj.getString("Name"));
							map.put("StationID",
									"" + obj.getString("StationID"));
							map.put("AttrID1", "" + obj.getString("AttrID1"));
							map.put("AttrID2", "" + obj.getString("AttrID2"));
							map.put("LastPlayed",
									"" + obj.getString("LastPlayed"));
							map.put("Deleted", "" + obj.getString("Deleted"));
							map.put("PlayCount",
									"" + obj.getString("PlayCount"));

							LLApplication.getFavstationLists().add(map);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					prepareListData();
					if (!flagPb){
						if (isInitial) {
							if(progressDialog!=null){
								if(progressDialog.isShowing()){
									progressDialog.dismiss();
								}
								progressDialog = null;
							}
							callShuffle();
							isInitial = false;
						}else{
							callmyStations(false);
						}
					}
						
				}
			}
		});
	}
	
	private void callmyStations(final boolean flagPb) {
		// TODO Auto-generated method stub

		android.os.Handler hn = new android.os.Handler();
		hn.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (isOnline()) {
					getStation(flagPb);
				} else {
					Toast.makeText(getApplicationContext(),
							"" + Constant.network_error, Toast.LENGTH_SHORT)
							.show();
				}
			}
		}, 500);
	}

	private void getStation(boolean flagPb) {
		// TODO Auto-generated method stub

		linearAppSection3.setVisibility(View.GONE);
		linearAppSection4.setVisibility(View.VISIBLE);

		if (flagPb) {
			if(!((LifeLeadershipMainActivity) context).isFinishing())
			{
			    //show dialog
				progressDialog = ProgressDialog.show(LifeLeadershipMainActivity.this,
						null, "Loading...	", true, false);
			}
		}
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("func", "getStation");
				// new for Subscription
				if(curPoswheel1 < -1 || curPoswheel2 < -1){
					map.put("attrid1", ""+curPoswheel1);
					map.put("attrid2", ""+curPoswheel2);
				} else if (curPoswheel1 == -1) { //old
					map.put("attrid1", "-1");
					map.put("attrid2", "-1");
				} else {
					if (curPoswheel2 == -1)
						curPoswheel2 = 0;
					map.put("attrid1", ""
							+ arrayofspeakers.get(curPoswheel1).get("AttrID"));
					map.put("attrid2", ""
							+ arrayofsubjects.get(curPoswheel2).get("AttrID"));
				}
				map.put("userid", "" + LLApplication.getUserId());
				Log.d("getStationMap", ">> "+map.toString());
				response = getResponse(map);
				Log.i("response getStationMap", "" + response);
				Update();
			}
		});
		t.start();
	}

	private void Update() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(progressDialog!=null){
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}
				if (response != null) {
					try {
						json_str = new JSONObject(response);
						LLApplication.getStationInfo().put("Name",
								"" + json_str.getString("Name"));
						LLApplication.getStationInfo().put("StationID",
								"" + json_str.getString("StationID"));
						LLApplication.getStationInfo().put("AttrID1",
								"" + json_str.getString("AttrID1"));
						LLApplication.getStationInfo().put("AttrID2",
								"" + json_str.getString("AttrID2"));
						LLApplication.getStationInfo().put("PlayCount",
								"" + json_str.getString("PlayCount"));
						LLApplication.getStationInfo().put("LastPlayed",
								"" + json_str.getString("LastPlayed"));
						LLApplication.getStationInfo().put("Deleted",
								"" + json_str.getString("Deleted"));
						data_array = json_str.getString("files");
						array = new JSONArray(data_array);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					LLApplication.getStationLists().clear();
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj;
						try {
							obj = array.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();

							map.put("AudioID", "" + obj.getString("AudioID"));
							map.put("Name", "" + obj.getString("Name"));
							map.put("FileName", "" + obj.getString("FileName"));
							map.put("Stamp", "" + obj.getString("Stamp"));
							map.put("ProductRef",
									"" + obj.getString("ProductRef"));
							map.put("Active", "" + obj.getString("Active"));
							map.put("PlayCount",
									"" + obj.getString("PlayCount"));

							LLApplication.getStationLists().add(map);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					booleanFlagFav = false;
					for (int i = 0; i < LLApplication.getFavstationLists()
							.size(); i++) {
						Log.e("StationID: "+LLApplication.getStationInfo().get("StationID"), "FavStationID: "+LLApplication.getFavstationLists().get(i).get("StationID"));
						if (LLApplication
								.getStationInfo()
								.get("StationID")
								.equals(""
										+ LLApplication.getFavstationLists()
												.get(i).get("StationID")))
							booleanFlagFav = true;
					}
					Log.e("booleanFlagFav", ""+booleanFlagFav);
					
					//new Subscription - replaced booleanFlagFav with (booleanFlagFav || iSFavSelected)
					if (booleanFlagFav || iSFavSelected){
						player_btn4.setBackgroundResource(R.drawable.favbtn_gray);
					}						
					else{
						player_btn4.setBackgroundResource(R.drawable.favbtn_red);
					}

					int size = LLApplication.getStationLists().size();

					if (size > 0) {

						if (isOnline()) {
							currentStationPos = 0;
							
							startStreamingAudio(Constant.cdnPath
									+ LLApplication.getStationLists()
											.get(currentStationPos)
											.get("FileName") + ".mp3"  );
						}
					} else {
						String strMsg = "I’m sorry but that station is unavailable at this time";
						AlertDialog.Builder alert = new AlertDialog.Builder(
								LifeLeadershipMainActivity.this);
						alert.setTitle("Alert");
						alert.setMessage(strMsg);
						alert.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
									}
								});
						alert.show();
					}
				}
			}
		});
	}

	private void startStreamingAudio(String urlstring) {
		Log.i("StartStreamingAudio", "==>"+urlstring);
		
		try {
			Button streamButton = new Button(getApplicationContext());
			progressBar = (ProgressBar) applicationView
					.findViewById(R.id.progressBar1);

			if (audioStreamer != null) {
				audioStreamer.interrupt();
				btnPlay.setBackgroundResource(R.drawable.playbtn_big_red);
			}
			audioStreamer = new StreamingMediaPlayer(this, txtCurrentTime,
					btnPlay, streamButton, progressBar);
			
			if(!((LifeLeadershipMainActivity) context).isFinishing())
			{
			    //show dialog
				progressDialog = ProgressDialog.show(LifeLeadershipMainActivity.this,
						null, "Calculating time...	", true, false);
			}
			
			audioStreamer.startStreaming(urlstring, 5208, 216, progressDialog);
			// streamButton.setEnabled(false);

			if (isOnline()) {
				getAudioInfo();
			} else {
				Toast.makeText(getApplicationContext(),
						"" + Constant.network_error, Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(getClass().getName(), "Error starting to stream audio.", e);
		}
	}

	private void playStreaming() {
		// TODO Auto-generated method stub

		linearAppSection3.setVisibility(View.VISIBLE);
		linearAppSection4.setVisibility(View.GONE);

		if (btnPlay.isEnabled() && audioStreamer != null) {
			try {
				if (audioStreamer.getMediaPlayer().isPlaying()) {
					audioStreamer.getMediaPlayer().pause();
					btnPlay.setBackgroundResource(R.drawable.playbtn_big_red);
					// sb.setEnabled(false);
				} else {
					startAudio();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else {
			if(!((LifeLeadershipMainActivity) context).isFinishing())
			{
				//show dialog
				progressDialog = ProgressDialog.show(
						LifeLeadershipMainActivity.this, null, "Streaming...",
						true, true);
			}
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (btnPlay.isEnabled() == false) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						UpdateUI();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}).start();
		}
	}

	private void startAudio() {
		// TODO Auto-generated method stub
		if (audioStreamer != null) {
		audioStreamer.getMediaPlayer().start();
		audioStreamer.startPlayProgressUpdater();
		btnPlay.setBackgroundResource(R.drawable.pausebtn);
		// sb.setEnabled(true);

		booleanClicked = false;
		}
	}

	private void getAudioInfo() {
		// TODO Auto-generated method stub
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				Log.i("AudioID",
						""
								+ LLApplication.getStationLists()
										.get(currentStationPos).get("AudioID"));
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("func", "getAudioInfo");
				map.put("audioid",
						""
								+ LLApplication.getStationLists()
										.get(currentStationPos).get("AudioID"));
				map.put("nextOne",
						""
								+ LLApplication.getStationLists()
										.get(currentStationPos).get("AudioID"));
				map.put("nextTwo",
						""
								+ LLApplication.getStationLists()
										.get(currentStationPos).get("AudioID"));
				map.put("userid", "" + LLApplication.getUserId());

				response = getResponse(map);
				Log.i("response", "" + response);
				UpdateInfo();
			}
		});
		t.start();
	}

	protected void UpdateInfo() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (response != null) {
					try {
						// json_str = new JSONObject(response);
						// data_array = json_str.getString("files");
						data_array = response;
						array = new JSONArray(data_array);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					JSONObject obj;
					try {
						obj = array.getJSONObject(0);

						LLApplication.getAudioInfo().put("liked",
								"" + obj.getString("liked"));
						LLApplication.getAudioInfo().put("speaker",
								"" + obj.getString("speaker"));

						data_array = "" + obj.getString("images");
						array = new JSONArray(data_array);

						// for (int i = 0; i < array.length(); i++) {
						obj = array.getJSONObject(0);

						LLApplication.getAudioInfo().put("image",
								"" + obj.getString("image"));
						// }

						txtTitle.setText(""
								+ LLApplication.getStationLists()
										.get(currentStationPos).get("Name"));
						txtArtist.setText(""
								+ LLApplication.getAudioInfo().get("speaker"));
						imageLoader.displayImage(Constant.imgPath
								+ LLApplication.getAudioInfo().get("image"),
								imgThumb, options);
						if (LLApplication.getAudioInfo().get("liked")
								.equals("False")) {
							btnLike.setBackgroundResource(R.drawable.like);
							booleanFlagLiked = false;
						} else {
							btnLike.setBackgroundResource(R.drawable.like_gray);
							booleanFlagLiked = true;
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	protected void likeAudio() {
		// TODO Auto-generated method stub

		if(!((LifeLeadershipMainActivity) context).isFinishing())
		{
			//show dialog
			progressDialog = ProgressDialog.show(LifeLeadershipMainActivity.this,
					null, "Loading...	", true, false);
		}
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("func", "likeAudio");
				map.put("audioid",
						""
								+ LLApplication.getStationLists()
										.get(currentStationPos).get("AudioID"));
				map.put("userid", "" + LLApplication.getUserId());
				if (booleanFlagLiked) {
					map.put("btnLike", "False");
				} else {
					map.put("btnLike", "True");
				}

				response = getResponse(map);
				Log.i("response", "" + response);
				Update_likeAudio();
			}
		});
		t.start();
	}

	protected void Update_likeAudio() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if(progressDialog!=null){
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}

				if (response != null) {
					if (booleanFlagLiked) {
						btnLike.setBackgroundResource(R.drawable.like);
						booleanFlagLiked = false;
					} else {
						btnLike.setBackgroundResource(R.drawable.like_gray);
						booleanFlagLiked = true;
					}
				}
			}
		});
	}

	protected void addStationToFavorites() {
		// TODO Auto-generated method stub
		if(!((LifeLeadershipMainActivity) context).isFinishing())
		{
			//show dialog
			progressDialog = ProgressDialog.show(LifeLeadershipMainActivity.this,
					null, "Loading...	", true, false);
		}
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("func", "saveUserStation");
				map.put("stationid",
						"" + LLApplication.getStationInfo().get("StationID"));
				map.put("userid", "" + LLApplication.getUserId());

				response = getResponse(map);
				Log.i("response", "" + response);
				Update_addStationToFavorites();
			}
		});
		t.start();
	}

	protected void Update_addStationToFavorites() {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				/*
				 * if (progressDialog.isShowing()) { progressDialog.dismiss(); }
				 */

				if (response != null) {
					booleanFlagFav = true;
					player_btn4.setBackgroundResource(R.drawable.favbtn_gray);
					if (isOnline()) {
						getFavoriteStations(true);
					} else {
						Toast.makeText(getApplicationContext(),
								"" + Constant.network_error, Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});
	}

	protected void RemoveStation(final String StationID,
			final boolean flagList, final int childPosition) {
		// TODO Auto-generated method stub
		if(!((LifeLeadershipMainActivity) context).isFinishing())
		{
			//show dialog
			progressDialog = ProgressDialog.show(LifeLeadershipMainActivity.this,
					null, "Loading...	", true, false);
		}
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("func", "removeUserStation");
				map.put("stationid", "" + StationID);
				map.put("userid", "" + LLApplication.getUserId());

				response = getResponse(map);
				Log.i("response", "" + response);
				Update_RemoveStation(childPosition, flagList);
			}
		});
		t.start();
	}

	protected void Update_RemoveStation(final int childPosition,
			boolean flagList) {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				if (response != null) {
					if (childPosition == -1
							|| LLApplication
									.getFavstationLists()
									.get(childPosition)
									.get("StationID")
									.equals(""
											+ LLApplication.getStationInfo()
													.get("StationID"))) {
						player_btn4
								.setBackgroundResource(R.drawable.favbtn_red);
						booleanFlagFav = false;
					}

					if (isOnline()) {
						getFavoriteStations(true);
					} else {
						Toast.makeText(getApplicationContext(),
								"" + Constant.network_error, Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});
	}

	

	protected void CallAsynchronous() {
		// TODO Auto-generated method stub
		TimerTask doAsynchronousTask;
		final Handler handler = new Handler();
		Timer timer = new Timer();

		final Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new AccelerateInterpolator()); // and this
		fadeIn.setDuration(1000);

		final Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
		fadeOut.setDuration(1000);

		fadeOut.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				// new Subscription
				if (curPoswheel1 < -1 || curPoswheel2 < -1){
					//Don't set anything as it's already set
				}
				else if (curPoswheel1 == -1) { //old
					txtStationname.setText("LIKED AUDIO");
				} else {
					String spkr = txtSpeakerSearch.getText().toString();
					String subj = txtSubjectSearch.getText().toString();

					if (spkr.equals("SPEAKER SEARCH")
							&& subj.equals("SUBJECT SEARCH")) {
						if(iSFavSelected){
							txtStationname.setText(LLApplication.getFavstationLists().get(selectedFavPos).get("Name"));
						}else{
							txtStationname.setText("Shuffle");
						}
						
						
					} else {
						if (booleanFlagText) {
							booleanFlagText = false;
							if (spkr.equals("SPEAKER SEARCH"))
								spkr = subj;
							txtStationname.setText("" + spkr);
						} else {
							booleanFlagText = true;
							if (subj.equals("SUBJECT SEARCH"))
								subj = spkr;
							txtStationname.setText("" + subj);
						}
					}
				}
				txtStationname.startAnimation(fadeIn);
			}
		});

		doAsynchronousTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						txtStationname.startAnimation(fadeOut);
						if (LLApplication.getTotalTime().length() > 0)
							txtTotalTime.setText(""
									+ LLApplication.getTotalTime());
					}
				});
			}
		};

		timer.schedule(doAsynchronousTask, 0, 3000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.life_leadership_main, menu);
		return true;
	}

	class ClickListenerForScrolling implements OnClickListener {
		HorizontalScrollView scrollView;
		View menuView;

		public ClickListenerForScrolling(HorizontalScrollView scrollView,
				View menuView) {
			super();
			this.scrollView = scrollView;
			this.menuView = menuView;
		}

		@Override
		public void onClick(View v) {
			int menuWidth = menuView.getMeasuredWidth();
			// Ensure menu is visible
			if (!menuOut) {
				menuView.setVisibility(View.VISIBLE);
				int left = 0;
				scrollView.smoothScrollTo(left, 0);
			} else {
				menuView.setVisibility(View.GONE);
				int left = menuWidth;
				scrollView.smoothScrollTo(left, 0);
			}
			menuOut = !menuOut;

		}
	}

	static class SizeCallbackForMenu implements SizeCallback {
		int btnWidth;
		View btnSlide;

		public SizeCallbackForMenu(View btnSlide) {
			super();
			this.btnSlide = btnSlide;
		}

		@Override
		public void onGlobalLayout() {
			btnWidth = btnSlide.getMeasuredWidth();
		}

		@Override
		public void getViewSize(int idx, int w, int h, int[] dims) {
			dims[0] = w;
			dims[1] = h;
			final int menuIdx = 0;
			if (idx == menuIdx) {
				dims[0] = (int) (w - btnWidth - (30 * scale + 0.5f));
			}
		}
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// Log.d("onFling", "true");
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (menuOut) {
						int menuWidth = menuView.getMeasuredWidth();
						menuView.setVisibility(View.GONE);
						int left = menuWidth;
						horizontalScrollView.smoothScrollTo(left, 0);
						menuOut = !menuOut;
					}
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (!menuOut) {
						menuView.setVisibility(View.VISIBLE);
						int left = 0;
						horizontalScrollView.smoothScrollTo(left, 0);
						menuOut = !menuOut;
					}
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	}

	public class ExpandableListAdapter extends BaseExpandableListAdapter {

		private Context _context;
		private List<String> _listDataHeader; // header titles
		ArrayList<Integer> listImg;
		// child data in format of header title, child title
		private HashMap<String, ArrayList<HashMap<String, String>>> _listDataChild;

		public ExpandableListAdapter(
				Context context,
				List<String> listDataHeader,
				ArrayList<Integer> listimg,
				HashMap<String, ArrayList<HashMap<String, String>>> listChildData) {
			this._context = context;
			this._listDataHeader = listDataHeader;
			this.listImg = listimg;
			this._listDataChild = listChildData;
		}

		@Override
		public Object getChild(int groupPosition, int childPosititon) {
			return this._listDataChild.get(
					this._listDataHeader.get(groupPosition))
					.get(childPosititon);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getChildView(final int groupPosition,
				final int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {

			final HashMap<String, String> childText = (HashMap<String, String>) getChild(
					groupPosition, childPosition);

			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.list_item, null);
			}

			TextView txtListChild = (TextView) convertView
					.findViewById(R.id.lblListItem);

			txtListChild.setText(childText.get("Name"));

			Button delete = (Button) convertView.findViewById(R.id.delete);
			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (isOnline()) {
						RemoveStation(
								LLApplication.getFavstationLists()
										.get(childPosition).get("StationID"),
								true, childPosition);
					} else {
						Toast.makeText(getApplicationContext(),
								"" + Constant.network_error, Toast.LENGTH_SHORT)
								.show();
					}
				}
			});

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					player_btn4.setVisibility(View.VISIBLE);
					btnRadio.setVisibility(View.GONE);
					webView.setVisibility(View.GONE);
					webviewIdCard.setVisibility(View.GONE);
					relativeTop.setBackgroundColor(Color.parseColor("#D22129"));
					linearMystations.setVisibility(View.VISIBLE);

					// new for Subscription
					curPoswheel1 = 0;
					curPoswheel2 = 0;
					selectedFavPos = childPosition;
					iSFavSelected = true;
					
					slideMenuRight();

					String id1 = LLApplication.getFavstationLists()
							.get(childPosition).get("AttrID1");
					String id2 = LLApplication.getFavstationLists()
							.get(childPosition).get("AttrID2");
					
					Log.d("selected ID1", "== "+id1);
					Log.d("selected ID2", "== "+id2);
					
					//new for Subscription
					if((id1.contains("-") || id2.contains("-")) &&
							((Integer.parseInt(id1) < -1) || (Integer.parseInt(id2) < -1))){
						curPoswheel1 = Integer.parseInt(id1);
						curPoswheel2 = Integer.parseInt(id2);
						txtStationname.setText(LLApplication.getFavstationLists()
							.get(childPosition).get("Name"));
						txtSpeakerSearch.setText("SPEAKER SEARCH");
						txtSubjectSearch.setText("SUBJECT SEARCH");
						
					}else{ // old
						boolean temp = false;
						for (int i = 0; i < arrayofspeakers.size(); i++) {
							if (arrayofspeakers.get(i).get("AttrID").equals(id1)) {
								curPoswheel1 = i;
								temp = true;
							}
						}

						// patch:
						if (!temp) {
							for (int i = 0; i < arrayofsubjects.size(); i++) {
								if (arrayofsubjects.get(i).get("AttrID")
										.equals(id1)) {
									curPoswheel1 = 0;
									curPoswheel2 = i;
								}
							}
						} else {
							for (int i = 0; i < arrayofsubjects.size(); i++) {
								if (arrayofsubjects.get(i).get("AttrID")
										.equals(id2))
									curPoswheel2 = i;
							}
						}

						Log.i("AttrID1", "" + id1 + ".." + curPoswheel1);
						Log.i("AttrID2", "" + id2 + ".." + curPoswheel2);

						txtSpeakerSearch.setText(""
								+ arrayofspeakers.get(curPoswheel1).get("Name"));
						txtSubjectSearch.setText(""
								+ arrayofsubjects.get(curPoswheel2).get("Name"));
					}
					
					callmyStations(true);
				}
			});

			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return this._listDataChild.get(
					this._listDataHeader.get(groupPosition)).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return this._listDataHeader.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return this._listDataHeader.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View row = convertView;

			row = getLayoutInflater()
					.inflate(R.layout.side_menu_list_row, null);

			ImageView img = (ImageView) row.findViewById(R.id.img);
			TextView text = (TextView) row.findViewById(R.id.text);

			text.setText("" + listDataHeader.get(groupPosition));
			img.setImageResource(listImg.get(groupPosition));

			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (groupPosition == 0) { // My Stations
						btnRadio.setVisibility(View.GONE);
						webView.setVisibility(View.GONE);
						webviewIdCard.setVisibility(View.GONE);
						relativeTop.setBackgroundColor(Color
								.parseColor("#D22129"));
						linearMystations.setVisibility(View.VISIBLE);

						slideMenuRight();
					} else if (groupPosition == 1) { // Shuffle
						player_btn4.setVisibility(View.GONE);
						btnRadio.setVisibility(View.GONE);
						webView.setVisibility(View.GONE);
						webviewIdCard.setVisibility(View.GONE);
						relativeTop.setBackgroundColor(Color
								.parseColor("#D22129"));
						linearMystations.setVisibility(View.VISIBLE);

						slideMenuRight();

						callShuffle();
						

					} else if (groupPosition == 2) { //Liked
						player_btn4.setVisibility(View.GONE);
						btnRadio.setVisibility(View.GONE);
						webView.setVisibility(View.GONE);
						webviewIdCard.setVisibility(View.GONE);
						relativeTop.setBackgroundColor(Color
								.parseColor("#D22129"));
						linearMystations.setVisibility(View.VISIBLE);

						slideMenuRight();

						curPoswheel1 = 0;
						curPoswheel2 = 0;
						if(arrayofspeakers!=null && arrayofspeakers.size()>0){
							txtSpeakerSearch
							.setText(""
									+ arrayofspeakers.get(curPoswheel1)
									.get("Name"));
							txtSubjectSearch
							.setText(""
									+ arrayofsubjects.get(curPoswheel2)
									.get("Name"));
							//new Subscription
							iSFavSelected = false;

							curPoswheel1 = -1;
							curPoswheel2 = -1;
							callmyStations(true);
						}

					} else if (groupPosition == 3) { //Help
						btnRadio.setVisibility(View.VISIBLE);
						linearMystations.setVisibility(View.GONE);
						webView.setVisibility(View.VISIBLE);
						webviewIdCard.setVisibility(View.GONE);
						relativeTop.setBackgroundColor(Color
								.parseColor("#B8B8B8"));
						webView.loadUrl("file:///android_res/raw/help.html");

						slideMenuRight();
					} else if (groupPosition == 4) { //Terms
						btnRadio.setVisibility(View.VISIBLE);
						linearMystations.setVisibility(View.GONE);
						webView.setVisibility(View.VISIBLE);
						webviewIdCard.setVisibility(View.GONE);
						relativeTop.setBackgroundColor(Color
								.parseColor("#B8B8B8"));
						webView.loadUrl("file:///android_res/raw/terms.html");

						slideMenuRight();
					} else if (groupPosition == 5) { // Privacy
						btnRadio.setVisibility(View.VISIBLE);
						linearMystations.setVisibility(View.GONE);
						webView.setVisibility(View.VISIBLE);
						webviewIdCard.setVisibility(View.GONE);
						relativeTop.setBackgroundColor(Color
								.parseColor("#B8B8B8"));
						webView.loadUrl("file:///android_res/raw/privacy.html");

						slideMenuRight();
					} else if (groupPosition == 6) { //ID Card

						btnRadio.setVisibility(View.VISIBLE);
						linearMystations.setVisibility(View.GONE);
						webView.setVisibility(View.GONE);
						webviewIdCard.setVisibility(View.VISIBLE);
						
						relativeTop.setBackgroundColor(Color
								.parseColor("#B8B8B8"));
						
						slideMenuRight();
					} else if (groupPosition == 7) { //Contact

						Intent emailIntent = new Intent(
								android.content.Intent.ACTION_SEND);
						emailIntent.setType("message/rfc822");

						emailIntent.putExtra(
								android.content.Intent.EXTRA_SUBJECT,
								Constant.Alert_Name);
						emailIntent
								.putExtra(
										android.content.Intent.EXTRA_EMAIL,
										new String[] { "lifesupport@life-leadership-home.com" });
						startActivity(Intent.createChooser(emailIntent,
								"Email:"));

					} else if (groupPosition == 8) { //Logout
						AlertDialog.Builder alert = new AlertDialog.Builder(
								mContext);
						alert.setTitle(Constant.Alert_Name);
						alert.setMessage("Are you sure you want to logout?");
						alert.setPositiveButton("YES",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();

										ContentValues values = new ContentValues();
										values.put("username", ""
												+ LLApplication.getUsername());
										values.put("password", ""
												+ LLApplication.getPassword());
										values.put("Guid",
												"" + LLApplication.getGuid());
										values.put("UserID",
												"" + LLApplication.getUserId());
										values.put("remember", ""
												+ LLApplication.getRemember());
										values.put("userloggedin", "0");
										db.update("user", values, "pk=1", null);

										android.os.Handler hn = new android.os.Handler();
										hn.postDelayed(new Runnable() {

											@Override
											public void run() {
												// TODO Auto-generated method
												// stub
												LLApplication
														.setUserloggedin(0);
												finish();
												Intent intent = new Intent(
														LifeLeadershipMainActivity.this,
														LoginActivity.class);
												intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												startActivity(intent);
												overridePendingTransition(
														R.anim.hold_top,
														R.anim.exit_in_bottom);
											}
										}, 200);
									}
								});
						alert.setNegativeButton("NO",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.cancel();
									}
								});
						alert.create();
						alert.show();
					}
				}
			});

			return row;

		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	private void callShuffle(){
		curPoswheel1 = 0;
		curPoswheel2 = 0;
		if(arrayofspeakers!=null && arrayofspeakers.size()>0){
			Log.e("Shuffle click","==========");
			Log.e("arrayOfSpeakers size:"+arrayofspeakers.size(), "Name : "+arrayofspeakers.get(curPoswheel1).get("Name"));
			txtSpeakerSearch.setText(""+ arrayofspeakers.get(curPoswheel1).get("Name"));
			txtSubjectSearch.setText(""+ arrayofsubjects.get(curPoswheel2).get("Name"));
			//new Subscription
			iSFavSelected = false;
			callmyStations(true);
		}else{
			Log.e("Shuffle click","Unable to parse arrayOfSpeakers.");
		}
	}
	
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		mapDataChild = new HashMap<String, ArrayList<HashMap<String, String>>>();

		// Adding Header data

		listDataHeader.add("MY STATIONS");
		listDataHeader.add("SHUFFLE");
		listDataHeader.add("LIKED AUDIO");
		listDataHeader.add("HELP");
		listDataHeader.add("TERMS");
		listDataHeader.add("PRIVACY");
		listDataHeader.add("ID Card");
		listDataHeader.add("CONTACT");
		listDataHeader.add("Logout");

		listImgs = new ArrayList<Integer>();
		listImgs.add(R.drawable.fav);
		listImgs.add(R.drawable.shuffle);
		listImgs.add(R.drawable.liked);
		listImgs.add(R.drawable.help);
		listImgs.add(R.drawable.terms);
		listImgs.add(R.drawable.privacy);
		listImgs.add(R.drawable.contact);
		listImgs.add(R.drawable.contact);
		listImgs.add(R.drawable.logout);

		// Adding child data
		@SuppressWarnings("unused")
		List<String> likedAudio = new ArrayList<String>();

		ArrayList<HashMap<String, String>> dummy = new ArrayList<HashMap<String, String>>();

		mapDataChild.put(listDataHeader.get(0), dummy); // Header, Child data
		mapDataChild.put(listDataHeader.get(1), dummy);
		mapDataChild.put(listDataHeader.get(2),	LLApplication.getFavstationLists());
		mapDataChild.put(listDataHeader.get(3), dummy);
		mapDataChild.put(listDataHeader.get(4), dummy);
		mapDataChild.put(listDataHeader.get(5), dummy);
		mapDataChild.put(listDataHeader.get(6), dummy);
		mapDataChild.put(listDataHeader.get(7), dummy);
		mapDataChild.put(listDataHeader.get(8), dummy);
		
		listAdapter = new ExpandableListAdapter(this, listDataHeader, listImgs,	mapDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);

		// if (booleanFlagExpand)
		expListView.expandGroup(2);
	}
	
	private class Wheel1Adapter extends AbstractWheelTextAdapter {
		// Countries names
		private ArrayList<HashMap<String, String>> list;

		/**
		 * Constructor
		 */
		protected Wheel1Adapter(Context context,
				ArrayList<HashMap<String, String>> list) {
			super(context, R.layout.wheel1_layout, NO_RESOURCE);
			this.list = list;
			setItemTextResource(R.id.country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index).get("Name");
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (menuOut) {
			slideMenuRight();
		} else {
			ContentValues values = new ContentValues();
			values.put("username", "" + LLApplication.getUsername());
			values.put("password", "" + LLApplication.getPassword());
			values.put("Guid", "" + LLApplication.getGuid());
			values.put("UserID", "" + LLApplication.getUserId());
			values.put("remember", "" + LLApplication.getRemember());
			values.put("userloggedin", "0");
			db.update("user", values, "pk=1", null);

			android.os.Handler hn = new android.os.Handler();
			hn.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method
					// stub
					LLApplication.setUserloggedin(0);
					finish();
				}
			}, 200);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		helper.close();
		if (audioStreamer != null) {
			audioStreamer.interrupt();
		}
		if(mgr != null) {
		    mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
		}
		
		if(progressDialog!=null){
			if(progressDialog.isShowing()){
				progressDialog.dismiss();
			}
			progressDialog = null;
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub

		try {
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;
			// right to left swipe
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

				slideMenuLeft();
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

				slideMenuRight();
			}

		} catch (Exception e) {
			// nothing
			e.printStackTrace();
		}

		return true;

	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if (menuOut)
			slideMenuRight();
		return gestureDetector.onTouchEvent(me);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		super.dispatchTouchEvent(ev);
		return gestureDetector.onTouchEvent(ev);

	}

	protected void slideMenuRight() {
		// TODO Auto-generated method stub
		int menuWidth = menuView.getMeasuredWidth(); // Ensure menu
		// isvisible
		menuView.setVisibility(View.VISIBLE);
		int left = menuWidth;
		horizontalScrollView.smoothScrollTo(left, 0);
		menuOut = !menuOut;
	}

	private void slideMenuLeft() {
		// TODO Auto-generated method stub
		menuView.setVisibility(View.VISIBLE);
		int left = 0;
		horizontalScrollView.smoothScrollTo(left, 0);
		Log.i("leftSwipe", "" + menuOut);
		menuOut = !menuOut;
	}
	
}
