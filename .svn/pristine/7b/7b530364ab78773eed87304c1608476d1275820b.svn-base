package com.jingdong.app.mall.more;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jingdong.app.mall.MyApplication;
import com.jingdong.app.mall.barcode.BarcodeActivity;
import com.jingdong.app.mall.entity.Keyword;
import com.jingdong.app.mall.search.CameraPurchaseActivity;
import com.jingdong.app.mall.utils.CommonUtil;
import com.jingdong.app.mall.utils.DPIUtil;
import com.jingdong.app.mall.utils.GlobalInitialization;
import com.jingdong.app.mall.utils.GlobalInitialization.ConfigLoadedListener;
import com.jingdong.app.mall.utils.HttpGroup;
import com.jingdong.app.mall.utils.HttpGroup.HttpError;
import com.jingdong.app.mall.utils.HttpGroup.HttpResponse;
import com.jingdong.app.mall.utils.HttpGroup.HttpSetting;
import com.jingdong.app.mall.utils.HttpGroup.OnAllListener;
import com.jingdong.app.mall.utils.JSONArrayPoxy;
import com.jingdong.app.mall.utils.JSONObjectProxy;
import com.jingdong.app.mall.utils.Log;
import com.jingdong.app.mall.utils.MyActivity;
import com.jingdong.app.mall.utils.MySimpleAdapter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends MyActivity
  implements View.OnClickListener, SensorListener
{
  private static int DISPLAY_HEIGHT = 0;
  private static int DISPLAY_WIDTH = 0;
  public static final int REQUEST_SEARCH_CODE = 272;
  public static final int RESET_ANIMATION_FLAG = 110;
  private static final String SEARCH_HOT_WORDS_FOUCTION_ID = "hotKeyword";
  private static final int SHAKE_THRESHOLD = 800;
  protected static final int SHOW_HOT_WORDS = 111;
  private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
  public static String currentWord = null;
  private static int horizontal;
  public static String hotWordsJson = null;
  private static int vertical;
  public final String KEY_WORDS = "keywords";
  AutoCompleteTextView autoCompleteTextView;
  View btnContainer;
  private AlertDialog.Builder dialogBuilder;
  boolean fromMenuFlag;
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
      case 110:
      case 111:
      }
      while (true)
      {
        return;
        SearchActivity.this.isAnimationRunning = false;
        continue;
        SearchActivity.this.init();
        SearchActivity.this.showHotKeyWords();
      }
    }
  };
  private RelativeLayout head;
  private ArrayList<HotWord> hotWords = new ArrayList();
  private LinearLayout hotWordsContent;
  private LinearLayout hotWordsScroll;
  public HashSet<String> hotWordsSet = new HashSet();
  private ArrayList<TextView> hotWordsView = new ArrayList();
  private int index = 0;
  private boolean isAnimationRunning = false;
  private Boolean isResultTooLarge = null;
  private boolean isShowResult = false;
  private long lastUpdate = -1L;
  private float last_x;
  private float last_y;
  private float last_z;
  private AlertDialog listDialog;
  ListView listView;
  ClickListener listener;
  private boolean needSensor = true;
  private int otherHeight;
  private Random random = new Random();
  private ArrayList<LinearLayout> raws = new ArrayList();
  Button searchButton;
  ImageButton searchCleanButton;
  private SensorManager sensorMgr;
  private TextView tip;
  private float x;
  private float y;
  private float z;

  private void generatorContentViews()
  {
    Iterator localIterator = this.hotWordsView.iterator();
    LinearLayout localLinearLayout;
    int i;
    float f1;
    float f2;
    float f3;
    int j;
    float f7;
    if (!localIterator.hasNext())
    {
      this.hotWordsView.clear();
      localLinearLayout = new LinearLayout(this);
      localLinearLayout.setOrientation(0);
      localLinearLayout.setGravity(17);
      LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-1, -2);
      localLinearLayout.setLayoutParams(localLayoutParams1);
      i = -5 + DISPLAY_WIDTH;
      f1 = 0.0F;
      f2 = 0.0F;
      f3 = 0.0F;
      j = this.index;
      if (j < this.hotWords.size())
        break label165;
      if ((localLinearLayout != null) && (localLinearLayout.getChildCount() > 0))
      {
        f7 = (i - f1) / localLinearLayout.getChildCount();
        if (f7 <= 0.0F);
      }
    }
    for (int m = 0; ; m++)
    {
      if (m >= localLinearLayout.getChildCount())
      {
        this.raws.add(localLinearLayout);
        label165: TextView localTextView1;
        while (true)
        {
          return;
          ((TextView)localIterator.next());
          break;
          try
          {
            localHotWord = (HotWord)this.hotWords.get(j);
            localTextView1 = new TextView(this);
            localTextView1.setBackgroundResource(2130837707);
            localTextView1.setTextSize(2, localHotWord.getTextSize().intValue());
            localTextView1.setMaxLines(1);
            localTextView1.setTextColor(localHotWord.getColor());
            TextPaint localTextPaint = localTextView1.getPaint();
            localTextView1.setOnClickListener(this);
            localTextPaint.setTextSize(localTextView1.getTextSize());
            localTextView1.setText(localHotWord.getText());
            f4 = localTextPaint.measureText(localHotWord.getText().toString()) + 2 * horizontal;
            localTextView1.setTag(Float.valueOf(f4));
            f1 += f4;
            if (f1 > i)
            {
              f5 = (f4 + (i - f1)) / localLinearLayout.getChildCount();
              if (f5 >= 0.0F)
              {
                k = 0;
                if (k < localLinearLayout.getChildCount());
              }
              else
              {
                f3 += f2 + 2 * vertical;
                f2 = 0.0F;
                if (f3 + this.otherHeight <= DISPLAY_HEIGHT)
                  break label591;
                this.index = (j - localLinearLayout.getChildCount());
                if (this.index < 0)
                  this.index = (this.hotWords.size() + this.index);
                this.isResultTooLarge = Boolean.valueOf(true);
              }
            }
          }
          catch (Exception localException)
          {
            float f4;
            while (true)
            {
              float f5;
              int k;
              j = 0;
              HotWord localHotWord = (HotWord)this.hotWords.get(0);
              continue;
              TextView localTextView2 = (TextView)localLinearLayout.getChildAt(k);
              localTextView2.setGravity(17);
              localTextView2.getPaint().setTextSize(localTextView2.getTextSize());
              Paint.FontMetrics localFontMetrics1 = localTextView2.getPaint().getFontMetrics();
              float f6 = (float)Math.ceil(localFontMetrics1.descent - localFontMetrics1.top);
              if (f6 > f2)
                f2 = f6;
              LinearLayout.LayoutParams localLayoutParams3 = new LinearLayout.LayoutParams((int)(f5 + (((Float)localTextView2.getTag()).floatValue() - horizontal)), (int)f6 + vertical);
              localLayoutParams3.setMargins(horizontal / 2, vertical / 2, horizontal / 2, vertical / 2);
              localTextView2.setLayoutParams(localLayoutParams3);
              k++;
            }
            label591: this.raws.add(localLinearLayout);
            localLinearLayout = new LinearLayout(this);
            localLinearLayout.setOrientation(0);
            localLinearLayout.setGravity(17);
            LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(-1, -2);
            localLinearLayout.setLayoutParams(localLayoutParams2);
            f1 = 0.0F + f4;
            this.tip.setVisibility(0);
            if (0 != 0)
              break label706;
          }
        }
        if ((j == -1 + this.hotWords.size()) && (this.isResultTooLarge == null))
          this.isResultTooLarge = null;
        while (true)
        {
          localLinearLayout.addView(localTextView1);
          this.hotWordsView.add(localTextView1);
          j++;
          break;
          label706: this.needSensor = true;
          if (j != -1 + this.hotWords.size())
            continue;
          j = -1;
        }
      }
      TextView localTextView3 = (TextView)localLinearLayout.getChildAt(m);
      localTextView3.setGravity(17);
      localTextView3.getPaint().setTextSize(localTextView3.getTextSize());
      Paint.FontMetrics localFontMetrics2 = localTextView3.getPaint().getFontMetrics();
      float f8 = (float)Math.ceil(localFontMetrics2.descent - localFontMetrics2.top);
      LinearLayout.LayoutParams localLayoutParams4 = new LinearLayout.LayoutParams((int)(f7 + (((Float)localTextView3.getTag()).floatValue() - horizontal)), (int)f8 + vertical);
      localLayoutParams4.setMargins(horizontal / 2, vertical / 2, horizontal / 2, vertical / 2);
      localTextView3.setLayoutParams(localLayoutParams4);
    }
  }

  private void generatorHotWords()
  {
    this.hotWords.clear();
    int i;
    Iterator localIterator;
    if ((this.hotWordsSet.size() > 0) && (this.hotWords.size() != this.hotWordsSet.size()))
    {
      i = 0;
      localIterator = this.hotWordsSet.iterator();
    }
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      HotWord localHotWord = new HotWord(1, (String)localIterator.next(), i % 6, false);
      this.hotWords.add(localHotWord);
      i++;
    }
  }

  private void getHotWords()
  {
    int i = 0;
    if (hotWordsJson != null);
    try
    {
      initHotWordsString(new JSONArrayPoxy(new JSONArray(hotWordsJson)));
      showKeyWords();
      i = 1;
      hotWordsJson = null;
      if (i == 0)
        getHttpGroupaAsynPool().add("hotKeyword", new JSONObject(), new HttpGroup.OnAllListener()
        {
          public void onEnd(HttpGroup.HttpResponse paramHttpResponse)
          {
            JSONArrayPoxy localJSONArrayPoxy = paramHttpResponse.getJSONObject().getJSONArrayOrNull("keywords");
            if ((localJSONArrayPoxy != null) && (localJSONArrayPoxy.length() > 0))
              SearchActivity.hotWordsJson = localJSONArrayPoxy.toString();
            SearchActivity.this.isResultTooLarge = null;
            SearchActivity.this.handler.sendEmptyMessage(111);
          }

          public void onError(HttpGroup.HttpError paramHttpError)
          {
          }

          public void onProgress(int paramInt1, int paramInt2)
          {
          }

          public void onStart()
          {
          }
        });
      return;
    }
    catch (JSONException localJSONException)
    {
      while (true)
      {
        if (!Log.E)
          continue;
        localJSONException.printStackTrace();
        i = 0;
      }
    }
  }

  private Animation getTextViewAnimation()
  {
    if (this.random.nextInt(2) > 0);
    for (Animation localAnimation = AnimationUtils.loadAnimation(this, 2130968577); ; localAnimation = AnimationUtils.loadAnimation(this, 2130968579))
    {
      localAnimation.setDuration(1000 + 1000 * this.random.nextInt(2));
      localAnimation.setStartOffset(300L);
      return localAnimation;
    }
  }

  private void init()
  {
    DISPLAY_HEIGHT = DPIUtil.getHeight();
    DISPLAY_WIDTH = DPIUtil.getWidth();
    vertical = DISPLAY_HEIGHT / 30;
    horizontal = DISPLAY_WIDTH / 20;
    Rect localRect = new Rect();
    getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
    this.otherHeight = (20 + (localRect.top + this.head.getBottom() + this.tip.getBottom()));
  }

  private void initHotWordsString(JSONArrayPoxy paramJSONArrayPoxy)
  {
    this.hotWordsSet.clear();
    int i = 0;
    while (true)
    {
      if (i >= paramJSONArrayPoxy.length())
        return;
      try
      {
        this.hotWordsSet.add(paramJSONArrayPoxy.getString(i));
        i++;
      }
      catch (JSONException localJSONException)
      {
        while (true)
        {
          if (!Log.E)
            continue;
          localJSONException.printStackTrace();
        }
      }
    }
  }

  private boolean paiSwitch(String paramString, View paramView)
  {
    int i = 0;
    if (TextUtils.isEmpty(paramString))
      return i;
    if (paramString.equals("1"))
    {
      paramView.setVisibility(0);
      paramView.setOnClickListener(this.listener);
    }
    while (true)
    {
      i = 1;
      break;
      if (!paramString.equals("0"))
        continue;
      paramView.setVisibility(8);
    }
  }

  private void searchSubmit(String paramString)
  {
    if ((TextUtils.isEmpty(paramString)) && (paramString.trim().length() == 0));
    while (true)
    {
      return;
      ((InputMethodManager)getSystemService("input_method")).hideSoftInputFromWindow(this.autoCompleteTextView.getWindowToken(), 0);
      currentWord = paramString;
      setResult(-1, getIntent().putExtra("keyWord", paramString));
      this.autoCompleteTextView.clearComposingText();
      finish();
    }
  }

  private void showDialog()
  {
    if (getPackageManager().queryIntentActivities(new Intent("android.speech.action.RECOGNIZE_SPEECH"), 0).size() != 0)
      startVoiceRecognitionActivity();
    while (true)
    {
      return;
      this.dialogBuilder.setTitle(2131165465);
      this.dialogBuilder.setMessage(2131165467);
      this.dialogBuilder.setPositiveButton(2131165446, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://market.android.com/details?id=com.google.android.voicesearch.x"));
          SearchActivity.this.startActivityNoException(localIntent);
          paramDialogInterface.dismiss();
        }
      });
      this.dialogBuilder.setNegativeButton(2131165447, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramDialogInterface, int paramInt)
        {
          paramDialogInterface.dismiss();
        }
      });
      post(new Runnable()
      {
        public void run()
        {
          SearchActivity.this.dialogBuilder.show();
        }
      });
    }
  }

  private void showHotKeyWords()
  {
    if (this.otherHeight < 1)
      this.handler.sendEmptyMessageDelayed(111, 2L);
    while (true)
    {
      return;
      this.isAnimationRunning = true;
      this.isShowResult = false;
      this.listView.setVisibility(8);
      this.hotWordsContent.setVisibility(0);
      this.hotWordsScroll.setVisibility(0);
      this.btnContainer.setVisibility(0);
      getHotWords();
    }
  }

  private void showKeyWords()
  {
    generatorHotWords();
    this.raws.clear();
    generatorContentViews();
    this.hotWordsContent.removeAllViews();
    Iterator localIterator1 = this.raws.iterator();
    Iterator localIterator2;
    if (!localIterator1.hasNext())
      localIterator2 = this.hotWordsView.iterator();
    while (true)
    {
      if (!localIterator2.hasNext())
      {
        this.handler.sendEmptyMessageDelayed(110, 3000L);
        return;
        LinearLayout localLinearLayout = (LinearLayout)localIterator1.next();
        this.hotWordsContent.addView(localLinearLayout);
        break;
      }
      ((TextView)localIterator2.next()).startAnimation(getTextViewAnimation());
    }
  }

  private void showSearchResult()
  {
    this.isShowResult = true;
    this.hotWordsContent.setVisibility(8);
    this.hotWordsScroll.setVisibility(8);
    this.listView.setVisibility(0);
    this.btnContainer.setVisibility(8);
  }

  private void startVoiceRecognitionActivity()
  {
    Intent localIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
    localIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
    localIntent.putExtra("android.speech.extra.PROMPT", "");
    startActivityForResult(localIntent, 1234);
  }

  public void onAccuracyChanged(int paramInt1, int paramInt2)
  {
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    ArrayList localArrayList;
    String[] arrayOfString;
    if ((paramInt1 == 1234) && (paramInt2 == -1))
    {
      localArrayList = paramIntent.getStringArrayListExtra("android.speech.extra.RESULTS");
      arrayOfString = new String[localArrayList.size()];
    }
    for (int i = 0; ; i++)
    {
      if (i >= localArrayList.size())
      {
        this.dialogBuilder.setTitle(2131165466);
        this.dialogBuilder.setItems(arrayOfString, new DialogInterface.OnClickListener(arrayOfString)
        {
          public void onClick(DialogInterface paramDialogInterface, int paramInt)
          {
            Log.d("voice", "content:" + paramInt + " " + this.val$items[paramInt] + " ");
            String str = this.val$items[paramInt].replace("¡£", "");
            SearchActivity.this.searchSubmit(str);
            SearchActivity.this.listDialog.dismiss();
          }
        });
        post(new Runnable()
        {
          public void run()
          {
            SearchActivity.this.listDialog = SearchActivity.this.dialogBuilder.show();
          }
        });
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        return;
      }
      arrayOfString[i] = ((String)localArrayList.get(i));
    }
  }

  public void onClick(View paramView)
  {
    if (!(paramView instanceof TextView));
    while (true)
    {
      return;
      searchSubmit(((TextView)paramView).getText().toString());
    }
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    init();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903185);
    this.btnContainer = findViewById(2131427679);
    this.listener = new ClickListener(null);
    findViewById(2131427475).setOnClickListener(this.listener);
    findViewById(2131427476).setOnClickListener(this.listener);
    findViewById(2131427477).setOnClickListener(this.listener);
    if (!paiSwitch(CommonUtil.getJdSharedPreferences().getString("paiswitch", ""), findViewById(2131427477)))
      GlobalInitialization.getGlobalInitializationInstance().addConfigLoadedListener(new GlobalInitialization.ConfigLoadedListener()
      {
        public void onLoaded()
        {
          SearchActivity.this.post(new Runnable()
          {
            public void run()
            {
              SearchActivity.this.paiSwitch(CommonUtil.getJdSharedPreferences().getString("paiswitch", "0"), SearchActivity.this.findViewById(2131427477));
            }
          });
        }
      });
    this.searchButton = ((Button)findViewById(2131428111));
    this.searchButton.setOnClickListener(this.listener);
    this.fromMenuFlag = getIntent().getBooleanExtra("fromMenuFlag", false);
    this.dialogBuilder = new AlertDialog.Builder(this);
    this.searchCleanButton = ((ImageButton)findViewById(2131428112));
    this.sensorMgr = ((SensorManager)getSystemService("sensor"));
    this.sensorMgr.registerListener(this, 2, 1);
    this.autoCompleteTextView = ((AutoCompleteTextView)findViewById(2131427677));
    this.listView = ((ListView)findViewById(2131428114));
    this.hotWordsContent = ((LinearLayout)findViewById(2131428117));
    this.hotWordsScroll = ((LinearLayout)findViewById(2131428115));
    this.head = ((RelativeLayout)findViewById(2131427676));
    this.tip = ((TextView)findViewById(2131428116));
    this.otherHeight = -1;
    this.autoCompleteTextView.setOnKeyListener(new View.OnKeyListener()
    {
      public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
      {
        if (paramInt == 66)
          SearchActivity.this.searchSubmit(SearchActivity.this.autoCompleteTextView.getText().toString());
        for (int i = 1; ; i = 0)
          return i;
      }
    });
    this.autoCompleteTextView.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        SearchActivity.this.autoCompleteTextView.requestFocus();
        return false;
      }
    });
    this.autoCompleteTextView.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramEditable)
      {
      }

      public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
      }

      public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
      {
        if (TextUtils.isEmpty(paramCharSequence))
        {
          SearchActivity.this.searchCleanButton.setVisibility(8);
          SearchActivity.this.listView.setVisibility(8);
          SearchActivity.this.btnContainer.setVisibility(0);
          if ((!TextUtils.isEmpty(paramCharSequence)) || (paramCharSequence.toString().trim().length() != 0))
            break label116;
          SearchActivity.this.listView.post(new Runnable()
          {
            public void run()
            {
              if (TextUtils.isEmpty(SearchActivity.this.autoCompleteTextView.getText()))
                SearchActivity.this.showHotKeyWords();
            }
          });
        }
        while (true)
        {
          return;
          SearchActivity.this.searchCleanButton.setVisibility(0);
          SearchActivity.this.searchCleanButton.setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramView)
            {
              SearchActivity.this.autoCompleteTextView.setText(null);
              SearchActivity.this.searchCleanButton.setVisibility(8);
            }
          });
          break;
          label116: JSONObject localJSONObject = new JSONObject();
          try
          {
            localJSONObject.put("keyword", paramCharSequence.toString());
            HttpGroup.HttpSetting localHttpSetting = new HttpGroup.HttpSetting();
            localHttpSetting.setFunctionId("tip");
            localHttpSetting.setEffect(0);
            localHttpSetting.setJsonParams(localJSONObject);
            localHttpSetting.setListener(new HttpGroup.OnAllListener()
            {
              public void onEnd(HttpGroup.HttpResponse paramHttpResponse)
              {
                try
                {
                  JSONArrayPoxy localJSONArrayPoxy = paramHttpResponse.getJSONObject().getJSONArray("tip");
                  if (localJSONArrayPoxy.length() >= 1)
                  {
                    ArrayList localArrayList = Keyword.toList(localJSONArrayPoxy, 0);
                    SearchActivity localSearchActivity = SearchActivity.this;
                    String[] arrayOfString = new String[2];
                    arrayOfString[0] = "name";
                    arrayOfString[1] = "countString";
                    int[] arrayOfInt = new int[2];
                    arrayOfInt[0] = 2131427683;
                    arrayOfInt[1] = 2131427684;
                    MySimpleAdapter localMySimpleAdapter = new MySimpleAdapter(localSearchActivity, localArrayList, 2130903108, arrayOfString, arrayOfInt);
                    SearchActivity.this.listView.post(new Runnable(localMySimpleAdapter)
                    {
                      public void run()
                      {
                        if (!TextUtils.isEmpty(SearchActivity.this.autoCompleteTextView.getText().toString()))
                          SearchActivity.this.showSearchResult();
                        SearchActivity.this.listView.setAdapter(this.val$arrayAdapter);
                        SearchActivity.this.listView.invalidate();
                      }
                    });
                    SearchActivity.this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                      public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
                      {
                        Keyword localKeyword = (Keyword)((Adapter)paramAdapterView.getAdapter()).getItem(paramInt);
                        SearchActivity.this.autoCompleteTextView.setText(localKeyword.getName());
                        SearchActivity.this.searchSubmit(localKeyword.getName());
                        SearchActivity.this.listView.setVisibility(8);
                      }
                    });
                  }
                }
                catch (JSONException localJSONException)
                {
                  if (Log.V)
                    Log.v("HomeActivity", localJSONException.getMessage());
                }
              }

              public void onError(HttpGroup.HttpError paramHttpError)
              {
                if (Log.D)
                  Log.d("Temp", "error -->> " + paramHttpError);
              }

              public void onProgress(int paramInt1, int paramInt2)
              {
                if (Log.D)
                  Log.d("Temp", "max -->> " + paramInt1);
                if (Log.D)
                  Log.d("Temp", "progress -->> " + paramInt2);
              }

              public void onStart()
              {
              }
            });
            SearchActivity.this.getHttpGroupaAsynPool().add(localHttpSetting);
          }
          catch (JSONException localJSONException)
          {
            while (true)
            {
              if (!Log.V)
                continue;
              Log.v("SearchActivity", localJSONException.getMessage());
            }
          }
        }
      }
    });
    String str1 = getIntent().getStringExtra("keyword");
    String str2 = getIntent().getStringExtra("hotword");
    String str3 = getIntent().getStringExtra("type");
    if (!TextUtils.isEmpty(str3))
      getWindow().setSoftInputMode(3);
    post(new Runnable(str1, str2, str3)
    {
      public void run()
      {
        AutoCompleteTextView localAutoCompleteTextView = SearchActivity.this.autoCompleteTextView;
        if (TextUtils.isEmpty(this.val$keyword));
        for (String str = this.val$hotword; ; str = this.val$keyword)
        {
          localAutoCompleteTextView.setText(str);
          if (!TextUtils.isEmpty(this.val$type))
            SearchActivity.this.showDialog();
          return;
        }
      }
    }
    , 50);
  }

  protected void onResume()
  {
    this.autoCompleteTextView.requestFocus();
    if (currentWord != null)
      this.autoCompleteTextView.setHint(currentWord);
    super.onResume();
  }

  public void onSensorChanged(int paramInt, float[] paramArrayOfFloat)
  {
    if (!this.needSensor);
    while (true)
    {
      return;
      if (paramInt == 2)
      {
        long l1 = System.currentTimeMillis();
        if (l1 - this.lastUpdate <= 100L)
          continue;
        long l2 = l1 - this.lastUpdate;
        this.lastUpdate = l1;
        this.x = paramArrayOfFloat[0];
        this.y = paramArrayOfFloat[1];
        this.z = paramArrayOfFloat[2];
        if ((10000.0F * (Math.abs(this.x + this.y + this.z - this.last_x - this.last_y - this.last_z) / (float)l2) > 800.0F) && (!this.isAnimationRunning) && (!this.isShowResult))
          this.handler.sendEmptyMessage(111);
        this.last_x = this.x;
        this.last_y = this.y;
        this.last_z = this.z;
        continue;
      }
    }
  }

  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    init();
  }

  private class ClickListener
    implements View.OnClickListener
  {
    private ClickListener()
    {
    }

    public void onClick(View paramView)
    {
      switch (paramView.getId())
      {
      default:
      case 2131428111:
      case 2131427476:
      case 2131427475:
      case 2131427477:
      }
      while (true)
      {
        return;
        if (SearchActivity.this.autoCompleteTextView == null)
          continue;
        Editable localEditable = SearchActivity.this.autoCompleteTextView.getText();
        String str;
        if (localEditable == null)
          str = SearchActivity.this.autoCompleteTextView.getHint().toString();
        while (true)
        {
          SearchActivity.this.searchSubmit(str);
          break;
          str = localEditable.toString();
          if (!TextUtils.isEmpty(str))
            continue;
          str = SearchActivity.this.autoCompleteTextView.getHint().toString();
        }
        SearchActivity.this.showDialog();
        continue;
        if (MyApplication.getInstance().getCurrentMyActivity() == null)
          continue;
        SearchActivity.this.finish();
        MyApplication.getInstance().getCurrentMyActivity().startActivityInFrame(new Intent(SearchActivity.this, BarcodeActivity.class));
        continue;
        if (MyApplication.getInstance().getCurrentMyActivity() == null)
          continue;
        SearchActivity.this.finish();
        MyApplication.getInstance().getCurrentMyActivity().startActivityInFrame(new Intent(SearchActivity.this, CameraPurchaseActivity.class));
      }
    }
  }

  class HotWord
  {
    private Integer color = Integer.valueOf(0);
    private Spanned htmlText = null;
    private Integer level = Integer.valueOf(1);
    private Boolean needBold = Boolean.valueOf(false);
    private CharSequence text;

    public HotWord(int paramCharSequence, CharSequence paramInt1, int paramBoolean, boolean arg5)
    {
      this.level = Integer.valueOf(paramCharSequence);
      this.text = paramInt1;
      this.color = Integer.valueOf(paramBoolean);
      boolean bool;
      this.needBold = Boolean.valueOf(bool);
    }

    public int getColor()
    {
      if (this.color == null)
        this.color = Integer.valueOf(1);
      int i;
      switch (this.color.intValue())
      {
      default:
        i = -16764007;
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      }
      while (true)
      {
        return i;
        i = -3407872;
        continue;
        i = -16686115;
        continue;
        i = -1156608;
        continue;
        i = -12864767;
        continue;
        i = -6618937;
        continue;
        i = -12566464;
      }
    }

    public Integer getLevel()
    {
      if (this.level == null)
        this.level = Integer.valueOf(1);
      return this.level;
    }

    public CharSequence getText()
    {
      return this.text;
    }

    public Integer getTextSize()
    {
      if (this.level == null)
        this.level = Integer.valueOf(1);
      Integer localInteger;
      switch (this.level.intValue())
      {
      default:
        localInteger = Integer.valueOf(12);
      case 0:
      case 1:
      case 2:
      case 3:
      }
      while (true)
      {
        return localInteger;
        localInteger = Integer.valueOf(20);
        continue;
        localInteger = Integer.valueOf(18);
        continue;
        localInteger = Integer.valueOf(16);
        continue;
        localInteger = Integer.valueOf(14);
      }
    }

    public boolean isNeedBold()
    {
      return this.needBold.booleanValue();
    }
  }
}

