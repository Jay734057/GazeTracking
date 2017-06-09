package org.opencv.samples.tutorial1;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//import java.util.zip.Inflater;

//import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.LightingColorFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;    
//import android.text.Layout;
//import android.os.HandlerThread;
//import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ContextThemeWrapper;
//import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
//import android.widget.LinearLayout;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Point;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.core.Size;
import org.opencv.core.Range;
import org.opencv.highgui.Highgui;
import org.opencv.core.Scalar;
import org.opencv.core.RotatedRect;




import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;


public class Tutorial1Activity extends Activity implements CvCameraViewListener2 {
	private static final String TAG = "OCVSample::Activity";
    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    ///////////////////////menu///////////////////////////////
    //private MenuItem             mItemSwitchCamera = null;
    private MenuItem             track = null;
    private MenuItem             eye = null;
    private MenuItem             exit = null;
    private MenuItem             calib = null;
    private MenuItem			 app = null;
    //private MenuItem 			 back = null;
   // private MenuItem             texttest = null;            ////////////////////////////////////////////作死尝试代码
   // private static Handler handler=new Handler();
 ////////////////flag///////////////////////   
    private boolean Track_flag = true;
    private boolean eye_flag = false;
    private boolean calib_flag = false;
    private int app_flag = 0;
    //private boolean extra_eye = false;
    //private Button exitout;
    //private Button graypro;
    //private Button extra_eyecenter;
 /////////////全局变量///////////////////////////
    private static final int g = 225;//光斑二值化阈值
    private static final int g1 = 100;//瞳孔二值化阈值
    private static final int SS = 5; //标定点个数
    private static final int WW = 1; //权函数
 ////////////////////////eye/////////////////
    public Point eyecenter_acc = new Point(-1000,-1000) ;//精确瞳孔中心accurate
    public Point eyeroi = new Point(200,100);
    public Point eyecenter_app;//大致瞳孔中心approximate
    public Point pcrvector;
    
    public CascadeClassifier eyecascade;//分类器用于找出瞳孔中心
    public Mat frame;
    public Mat tmpframe;
    public Mat exteye;
    public Mat exteye99;
    public Mat dst_gray;
    public Mat dst_rgb;
    
    public List<Point> eyecontours;
    public List<RotatedRect> spotscenter;//亮斑向量
    
    public Imgproc process;//处理器
    
    RotatedRect box;//瞳孔范围
/////////////////calib////////////////////////////////////
    public Mat calibration_background;
    private int calibration_index0;
    private int calibration_index1;
    private int calibration_index2;
    public int Height;
    public int Width;
	double[] calibration_screenx;
    double[] calibration_screeny;
    double calibration_r1 ;
    double calibration_r2 ;
    double calibration_x;
    double calibration_y;
    List<CvCameraViewFrame> calibration_mat;
    List<Point> calib_mat = new ArrayList<Point>(); 
//////////////////////////////////////////////////////////
    Mat input_x_mat;
    Mat input_y_mat;
    Mat svd_x_coefficient_right;
    Mat svd_y_coefficient_right;
///////////////////gaze///////////////////////////////////
    private int position_flag = -1;
    private int position_count = 0;
    public Mat gaze_background;
    int gaze_index;
    List<CvCameraViewFrame> GazeTrack_mat;
    List<Point> gaze_mat;
    Point gaze_point;
    int point_count;
    int windowSize = 29;
    List<Point> GT;
    List<Double> Velocity;
    private static final int max_dispersion = 80;
    double dist_pixel;
    double dist2mm;
    double Height_p;
    double Width_p;
    private static double dist_to_screen = 60;
    private static int max_AngularSpeed = 40;//最大角速度
    double angularVelocity; //angular velocity between 2 consecutive samples
    double dist_Degrees; //distance converted to degrees of visual angle
    double start_time;
    double end_time;
    double time;
    double time_mark;
    private static int smooth_level = 50;//计算权重时要用到的平滑水平
//////////////////thread///////////////////////////////// 
    //MyHandlerThread handlerThread;  
    Handler handler;  
    AlertDialog.Builder dialog;
    Thread action;
    /////////////////////////////////////////////////////////calculator
    private Mat black;
    private TextView MyTextView = null;
    private String MyString = new String();
    private Button Button1 = null;
    private Button Button2 = null;
    private Button Button3 = null;
    private Button Button4 = null;
    private Button Button5 = null;
    private Button Button6 = null;
    private Button Button7 = null;
    private Button Button8 = null;
    private Button Button9 = null;
    private Button Button0 = null;
    private Button plus_Button = null;
    private Button minus_Button = null;
    private Button mul_Button = null;
    private Button div_Button = null;
    private Button dot_Button = null;
    private Button neg_Button = null;
    private Button equ_Button = null;
    private Button C_Button = null;
    private Button CE_Button = null;
    private Button esc_Button = null;
    ////////////////////////////////////mainlayout
    private Button main2cal = null;
    private Button main2quit = null;
    private Button main2player = null;
    private Button main2music = null;
    private Button main2photo = null;
    private Button main2recal = null;
    private int process_count = 0;
    private LinearLayout ccc;
    private LinearLayout aaa;
    private LinearLayout ppp;
    private LinearLayout mmm;
    private LinearLayout iii;
    ///////////////////////////////////player
    private Button bt_play, bt_quit, bt_stop;
	private SurfaceView sv_video;
	private static MediaPlayer mediaPlayer;
	private int currentPosition;
	private boolean flag = false;
    /////////////////////////////////////music
	private TextView myMusicName;
	private static MediaPlayer musicPlayer;
	private Button ms_play,ms_next,ms_last,ms_quit;
	private List<String> myMusicList=new ArrayList<String>();
	private int currentListItem=0;
	private static final String MUSIC_PATH=new String("/sdcard/track/");
	///////////////////////////////////////////photo
	private Button ph_next,ph_last,ph_quit;
	private ImageView img;
	private int pos = 0;
	Integer[] imageIDs = {
			R.drawable.pic1,
			R.drawable.pic2,
			R.drawable.pic3
			};
	///////////////////////////////////////////
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @SuppressLint("SdCardPath") 
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    ////////////////////////////////读取文件
                    eyecascade = new CascadeClassifier(   
                            "/storage/sdcard/track/haarcascade_righteye_2splits.xml");     
                    Log.i(TAG, "CascadeClassifier loaded successfully");
                    
                    exteye = Highgui.imread("/sdcard/track/eye.bmp");
                    Imgproc.cvtColor(exteye, exteye, Imgproc.COLOR_RGB2GRAY);
                    exteye99 = Highgui.imread("/sdcard/track/eye99.bmp");
                    Imgproc.cvtColor(exteye99, exteye99, Imgproc.COLOR_RGB2GRAY);
                    calibration_background = Highgui.imread("/sdcard/track/calibration.jpg");
                    Log.i(TAG, "Bmp loaded successfully");
                    black = Highgui.imread("/sdcard/track/black.jpg");
                    ////////////////////////////////设置参数
                    Height = calibration_background.t().height();
                    Width =  calibration_background.t().width();
                    calibration_screenx = new double[]{80,80,Width-80,Width/2,80,Width-80};
                    calibration_screeny = new double[]{Height-80,80,80,Height/2,Height-80,Height-80};
                    //////////////////////////物理尺寸
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    display.getMetrics(displayMetrics);
                    Width_p = displayMetrics.widthPixels;
                    Height_p = displayMetrics.heightPixels;
                    /////////////////////////////////
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public Tutorial1Activity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

	/** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.tutorial1_surface_view);
        if (mIsJavaCamera)
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        else
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        
        mOpenCvCameraView.setCvCameraViewListener(this);
		////////////////////////////////////////////////////
        handler = new Handler();
        //LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        //View view = inflater.inflate(R.layout.keyboard, null);
        MyTextView = (TextView)findViewById(R.id.loading);
        //calculate = new LinearLayout(R.id.calculator);
        /////////////////////calculator//////////////////////
        process_count = 0;
        Button1 = (Button)findViewById(R.id.button_1);
        Button2 = (Button)findViewById(R.id.button_2);
        Button3 = (Button)findViewById(R.id.button_3);
        minus_Button = (Button)findViewById(R.id.button_minus);
        
        Button4 = (Button)findViewById(R.id.button_4);
        Button5 = (Button)findViewById(R.id.button_5);
        Button6 = (Button)findViewById(R.id.button_6);
        plus_Button = (Button)findViewById(R.id.button_plus);
        
        Button7 = (Button)findViewById(R.id.button_7);
        Button8 = (Button)findViewById(R.id.button_8);
        Button9 = (Button)findViewById(R.id.button_9);
        mul_Button = (Button)findViewById(R.id.button_mul);
        
        Button0 = (Button)findViewById(R.id.button_0);
        div_Button = (Button)findViewById(R.id.button_div);
        dot_Button = (Button)findViewById(R.id.button_dot);
        neg_Button = (Button)findViewById(R.id.button_neg);
        equ_Button = (Button)findViewById(R.id.button_equ);
        C_Button = (Button)findViewById(R.id.button_C);
        CE_Button = (Button)findViewById(R.id.Button_CE);
        esc_Button = (Button)findViewById(R.id.button_esc);
        /////////////////////////////////////////////////////////////////////
        ccc = (LinearLayout)findViewById(R.id.calculator);
        aaa = (LinearLayout)findViewById(R.id.runmain);
        button_invisible();
        ////////////////////maininterface//////////////////////////////
        main2recal = (Button)findViewById(R.id.main_recal);
        main2music = (Button)findViewById(R.id.main_music);
        main2photo = (Button)findViewById(R.id.main_photo);
        main2cal = (Button)findViewById(R.id.main_cal);
        main2quit = (Button)findViewById(R.id.main_out);
        main2player = (Button)findViewById(R.id.main_player);
        
        main2cal.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				aaa.setVisibility(View.GONE);
				app_flag = 2;
	        	ccc.setVisibility(View.VISIBLE);
	        	button_visible();
	        	esc_Button.setText("Esc");
	        	process_count = 4;
	        	position_count = 0;

			}
        	
        });
        
        main2player.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				aaa.setVisibility(View.GONE);
				ccc.setVisibility(View.GONE);
				app_flag = 3;
				ppp.setVisibility(View.VISIBLE);
				sv_video.setVisibility(View.VISIBLE);
				position_count = 0;
			}
        	
        });
        
        main2quit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
        	
        });
        
        main2music.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				aaa.setVisibility(View.GONE);
				ccc.setVisibility(View.GONE);
				app_flag = 4;
				mmm.setVisibility(View.VISIBLE);
				position_count = 0;
			}
        	
        });
        
        main2recal.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				aaa.setVisibility(View.GONE);
				ccc.setVisibility(View.VISIBLE);
				app_flag = 0;
				calib_flag = false;
				Track_flag = true;
				eye_flag = false;
				button_invisible();
				process_count = 0;
				esc_Button.setText("Next");
				position_count = 0;
			}
        	
        });
        
        main2photo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				aaa.setVisibility(View.GONE);
				ccc.setVisibility(View.GONE);
				app_flag = 5;
				iii.setVisibility(View.VISIBLE);
				position_count = 0;
			}
        	
        });
        
        Button1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Button1.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "1";
				MyTextView.setText(MyString);
				Button1.getBackground().clearColorFilter();
			}
        	
        });
        
        Button2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Button2.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "2";
				MyTextView.setText(MyString);
				Button2.getBackground().clearColorFilter();
			}
        	
        });
        
        Button3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Button3.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "3";
				MyTextView.setText(MyString);
				Button3.getBackground().clearColorFilter();
			}
        	
        });
        
        Button4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Button4.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "4";
				MyTextView.setText(MyString);
				Button4.getBackground().clearColorFilter();
			}
        	
        });
        
        Button5.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Button5.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "5";
				MyTextView.setText(MyString);
				Button5.getBackground().clearColorFilter();
			}
        	
        });
        
        Button6.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Button6.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "6";
				MyTextView.setText(MyString);
				Button6.getBackground().clearColorFilter();
			}
        	
        });
        
        Button7.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Button7.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "7";
				MyTextView.setText(MyString);
				Button7.getBackground().clearColorFilter();
			}
        	
        });
        
        Button8.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Button8.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "8";
				MyTextView.setText(MyString);
				Button8.getBackground().clearColorFilter();
			}
        	
        });
        
        Button9.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Button9.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "9";
				MyTextView.setText(MyString);
				Button9.getBackground().clearColorFilter();
			}
        	
        });
        
        Button0.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "0";
				MyTextView.setText(MyString);
				Button0.getBackground().clearColorFilter();
			}
        	
        });
        
        dot_Button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				dot_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + ".";
				MyTextView.setText(MyString);
				dot_Button.getBackground().clearColorFilter();
			}
        	
        });
        
        plus_Button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				plus_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "+";
				MyTextView.setText(MyString);
				plus_Button.getBackground().clearColorFilter();
			}
        	
        });
        
        minus_Button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				minus_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "-";
				MyTextView.setText(MyString);
				minus_Button.getBackground().clearColorFilter();
			}
        	
        });
        
        mul_Button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mul_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "×";
				MyTextView.setText(MyString);
				mul_Button.getBackground().clearColorFilter();
			}
        	
        });
        
        div_Button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				div_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "÷";
				MyTextView.setText(MyString);
				div_Button.getBackground().clearColorFilter();
			}
        	
        });
        
        neg_Button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				neg_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				MyString = MyString + "_";
				MyTextView.setText(MyString);
				neg_Button.getBackground().clearColorFilter();
			}
        	
        });
        
        equ_Button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				equ_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				char [] symbol = new char[100];
				double[] number_equ = new double[100];
				int[] pos = new int[100];
				int k = 0;
				boolean dot = false;
				int p = -1;
				for (int i = 0; i < MyString.length(); i++){
					char j = MyString.charAt(i);
					Log.e(TAG, "enter char " + j);
					if(j == '+'||j == '-'||j == '×'||j == '÷'){
						symbol[k] = j;
						Log.e(TAG, "get symbol " + symbol[k]);
						dot = false;
						k++;
						p = -1;
					}else if (j == '_'){
						Log.e(TAG, "get negative");
						pos[k] = -1;
					}else if(j == '.'){
						dot = true;
					}else{
						if(!dot){
							number_equ[k] = number_equ[k]*10 + j - 48;//ASCII转换
							Log.e(TAG, "get number " + number_equ[k]);
						}else{
							number_equ[k] = number_equ[k] + (j-48)*Math.pow(10, p);
							Log.e(TAG, "get number " + number_equ[k]);
							p--;
						}
					}
				}
				Log.e(TAG, "k=" + k);
				symbol[k] = '=';
				for(int q = 0; q <= k; q++){
					if (pos[q] == -1)number_equ[q] = number_equ[q]*(-1);
					Log.e(TAG, "get number " + number_equ[q]);
					pos[q] = 0;
				}
				if (k == 0) MyTextView.setText(MyString + "=" + Double.toString(number_equ[0]));
				else{
					for (int q = k; q > 0; q--){
						if (symbol[q-1] == '×'){
							number_equ[q-1] = number_equ[q-1]*number_equ[q];
							Log.e(TAG, "get result " + number_equ[q-1]);
							pos[q-1] = pos[q-1] + 2;
						}else if (symbol[q-1] == '÷'){
							number_equ[q-1] = number_equ[q-1]/number_equ[q];
							Log.e(TAG, "get result " + number_equ[q-1]);
							pos[q-1] = pos[q-1] + 2;
						}
					}
				
					for (int q = 0; q <= k; q++){
						if (pos[q] == 0){
							if (symbol[q] == '+'){
								number_equ[0] =  number_equ[0] + number_equ[q+1];
								Log.e(TAG, "get result " + number_equ[0]);
							}
							else if (symbol[q] == '-'){
								number_equ[0] =  number_equ[0] - number_equ[q+1];
								Log.e(TAG, "get result " + number_equ[0]);
							}
							
						}
					}
					MyTextView.setText(MyString + "=" + Double.toString(number_equ[0]));
					equ_Button.getBackground().clearColorFilter();
				}
				//MyString = MyString + "=";
			}
        	
        });

        C_Button.setOnClickListener(new OnClickListener(){

			@SuppressLint("NewApi") @Override
			public void onClick(View v) {
				C_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = (String) MyTextView.getText();
				if (!MyString.isEmpty()){
					MyString = MyString.substring(0, MyString.length()-1);
				}
				MyTextView.setText(MyString);
				C_Button.getBackground().clearColorFilter();
			}
        });
        
        CE_Button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				CE_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				MyString = new String();
				MyTextView.setText(MyString);
				CE_Button.getBackground().clearColorFilter();
			}
        
        });
        
        esc_Button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				esc_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
				process_count ++;
				switch(process_count){
					case 1:
						//Intent intent = new Intent(Tutorial1Activity.this, FuckActivity.class);
						//startActivity(intent);
						//process_count = 0;
						//mOpenCvCameraView.setVisibility(SurfaceView.INVISIBLE);
						//aaa.setVisibility(View.VISIBLE);
						press_eyeregion_button();
						break;
					case 2:
						press_calibration_button();
						break;
					case 3:
						press_track_button();
						break;
					case 4:
						app_flag = 1;
						aaa.setVisibility(View.VISIBLE);
						//button_visible();
						//esc_Button.setText("Esc");
						break;
					case 5:
						//app_flag = 0;
						//calib_flag = false;
						//Track_flag = true;
						//eye_flag = false;
						//button_invisible();
						//process_count = 0;
						//esc_Button.setText("Next");
						ccc.setVisibility(View.INVISIBLE);
						aaa.setVisibility(View.VISIBLE);
						app_flag = 1;
						position_count = 0;
						break;
				}
				esc_Button.getBackground().clearColorFilter();
			}
        	
        });
        ////////////////////////////////////////////////////
        ppp = (LinearLayout)findViewById(R.id.runplay);
        bt_play = (Button) this.findViewById(R.id.bt_play);
		bt_quit = (Button) this.findViewById(R.id.bt_quit);
		bt_stop = (Button) this.findViewById(R.id.bt_stop);
		sv_video = (SurfaceView) this.findViewById(R.id.sv_video);
		bt_play.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 播放状态
				play_video(0);
			}
		});
		bt_stop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stop_video();
			}
			
		});
		bt_quit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stop_video();
				ppp.setVisibility(View.INVISIBLE);
				aaa.setVisibility(View.VISIBLE);
				app_flag = 1;
				position_count = 0;
				sv_video.setVisibility(View.INVISIBLE);
			}
			
		});
    
		sv_video.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		sv_video.getHolder().addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder arg0) {
				System.out.println("surfaceDestroyed........");
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					currentPosition = mediaPlayer.getCurrentPosition();
				}
			}

			@Override
			public void surfaceCreated(SurfaceHolder arg0) {
				System.out.println("surfaceCreated........");
				if (currentPosition > 0) {
					play_video(currentPosition);
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
					int arg3) {
				System.out.println("surfaceChanged........");
			}
		});
		
		
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			this.bt_play.setText("Pause");
		} else if (mediaPlayer != null) {
			this.bt_play.setText("Play");
		}
    //////////////////////////////////////////////////////////msuic
		mmm = (LinearLayout)findViewById(R.id.runmusic);
		myMusicName = (TextView) findViewById(R.id.music_name);
		ms_play = (Button) this.findViewById(R.id.music_play);
		ms_last = (Button) this.findViewById(R.id.music_last);
		ms_next = (Button) this.findViewById(R.id.music_next);
		ms_quit = (Button) this.findViewById(R.id.music_quit);
		
		ms_play.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (musicPlayer != null && musicPlayer.isPlaying()){
					musicPlayer.pause();
					Resources resources = getResources();
					ms_play.setBackgroundDrawable(resources.getDrawable(R.drawable.musicplay));
				}else if (musicPlayer != null){
					musicPlayer.start();
					Resources resources = getResources();
					ms_play.setBackgroundDrawable(resources.getDrawable(R.drawable.musicstop));
				}else {
					myMusicName.setText(myMusicList.get(currentListItem));
					playMusic(MUSIC_PATH+myMusicList.get(currentListItem));
				}
			}
			
		});
		
		ms_next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nextMusic();
			}
			
		});
		
		ms_last.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lastMusic();
			}
		});
		
		ms_quit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopMusic();
				mmm.setVisibility(View.INVISIBLE);
				aaa.setVisibility(View.VISIBLE);
				app_flag = 1;
				position_count = 0;
			}
		});
		
		musicList();
		////////////////////////////////////////////////////photo
		iii = (LinearLayout)findViewById(R.id.runimage);
		ph_next = (Button) this.findViewById(R.id.Next_ph);
		ph_last = (Button) this.findViewById(R.id.Last_ph);
		ph_quit = (Button) this.findViewById(R.id.Quit_ph);
		img = (ImageView) findViewById(R.id.imageView);
		
		ph_quit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iii.setVisibility(View.INVISIBLE);
				aaa.setVisibility(View.VISIBLE);
				app_flag = 1;
				position_count = 0;
			}
			
		});
		
		ph_next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pos = (pos + 1) % imageIDs.length;
				img.setImageResource(imageIDs[pos]);
			}
			
		});
		
		ph_last.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pos = (pos - 1) ;//% imageIDs.length;
				if (pos < 0) pos = imageIDs.length - 1;
				img.setImageResource(imageIDs[pos]);
			}
			
		});
    }
    
    private void musicList(){
    	//File home=new File(MUSIC_PATH);
        //if(home.listFiles(new MusicFilter()).length>0){
        //    for(File file:home.listFiles(new MusicFilter())){
                myMusicList.add("给我一个理由忘记.mp3");
                myMusicList.add("幸福了然后呢.mp3");
                myMusicList.add("乱世巨星.mp3");
        //    }
        //}
    }
    
    private void playMusic(String path){
    	 try { 
    		 musicPlayer = new MediaPlayer();
    		 musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    		 musicPlayer.setDataSource(path);
    		 musicPlayer.prepare();
    		 musicPlayer.start();
    		 musicPlayer.setOnCompletionListener(new OnCompletionListener() {
    	            
    	            @Override
    	            public void onCompletion(MediaPlayer mp) {
    	                // TODO Auto-generated method stub
    	                nextMusic();
    	            }
    	        });
    	    } catch (Exception e) {
    	        // TODO: handle exception
    	        e.printStackTrace();
    	    }
    	 Resources resources = getResources();
    	 ms_play.setBackgroundDrawable(resources.getDrawable(R.drawable.musicstop));
    }
    
    void nextMusic(){
    	if(musicPlayer != null){
    		musicPlayer.stop();
    		musicPlayer.release();
    		musicPlayer = null;
    	}
    	currentListItem = currentListItem + 1;
        if(currentListItem>=myMusicList.size()){
            currentListItem=0;
        }       
        	myMusicName.setText(myMusicList.get(currentListItem));
            playMusic(MUSIC_PATH+myMusicList.get(currentListItem));
    }
    
    void lastMusic(){
    	if(musicPlayer != null){
    		musicPlayer.stop();
    		musicPlayer.release();
    		musicPlayer = null;
    	}
        if(currentListItem!=0){
        	currentListItem = currentListItem - 1;
        }else currentListItem = myMusicList.size()-1;
    	myMusicName.setText(myMusicList.get(currentListItem));
        playMusic(MUSIC_PATH+myMusicList.get(currentListItem));
    }
    
    private void stopMusic(){
    	if (musicPlayer != null){
    		musicPlayer.stop();
    		musicPlayer.release();
    		musicPlayer = null;
    	}
    	Resources resources = getResources();
    	ms_play.setBackgroundDrawable(resources.getDrawable(R.drawable.musicplay));
    }
    
    private void play_video(int currentPosition){
		// 播放状态
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			bt_play.setText("Play");
		} else if (mediaPlayer != null) {
			mediaPlayer.start();
			bt_play.setText("Pause");
		} else {
			//smtech 硬代码！
			String path = "sdcard/track/VID_20150410_170714.3gp";
			File file = new File(path);
			if (file.exists()) {
				try {
					mediaPlayer = new MediaPlayer();
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaPlayer.setDisplay(sv_video.getHolder());
					mediaPlayer.setDataSource(path);
					mediaPlayer.prepare();
					mediaPlayer.start();
					int duration = mediaPlayer.getDuration();
//					sb.setMax(duration);
					flag = true;
					new Thread() {
						@Override
						public void run() {
							while (flag) {
								int position = mediaPlayer.getCurrentPosition();
//								sb.setProgress(position);
								try {
									sleep(500);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}
					}.start();
					mediaPlayer.seekTo(currentPosition);
					mediaPlayer
							.setOnCompletionListener(new OnCompletionListener() {
								@Override
								public void onCompletion(MediaPlayer arg0) {
									Toast.makeText(getApplication(), "Completed", 0)
											.show();
									stop_video();
								}
							});
					this.bt_play.setText("Pause");
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "Error", 0).show();
				}
			} else {
				Toast.makeText(this, "Unexist", 0).show();
			}
		}
    }
    
	private void stop_video() {
		if (mediaPlayer != null) {
			flag = false;
//			sb.setProgress(0);
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
			bt_play.setText("Play");
		}
	}
    
    
    private void button_invisible(){
    	MyTextView.setVisibility(View.INVISIBLE);
    	Button1.setVisibility(View.INVISIBLE);
        Button2.setVisibility(View.INVISIBLE);
        Button3.setVisibility(View.INVISIBLE);
        Button4.setVisibility(View.INVISIBLE);
        Button5.setVisibility(View.INVISIBLE);
        Button6.setVisibility(View.INVISIBLE);
        Button7.setVisibility(View.INVISIBLE);
        Button8.setVisibility(View.INVISIBLE);
        Button9.setVisibility(View.INVISIBLE);
        Button0.setVisibility(View.INVISIBLE);
        plus_Button.setVisibility(View.INVISIBLE);
        minus_Button.setVisibility(View.INVISIBLE);
        mul_Button.setVisibility(View.INVISIBLE);
        div_Button.setVisibility(View.INVISIBLE);
        dot_Button.setVisibility(View.INVISIBLE);
        neg_Button.setVisibility(View.INVISIBLE);
        equ_Button.setVisibility(View.INVISIBLE);
        C_Button.setVisibility(View.INVISIBLE);
        CE_Button.setVisibility(View.GONE);
        //esc_Button.setVisibility(View.GONE);
    	
    }
    
    private void button_visible(){
    	MyTextView.setVisibility(View.VISIBLE);
    	Button1.setVisibility(View.VISIBLE);
        Button2.setVisibility(View.VISIBLE);
        Button3.setVisibility(View.VISIBLE);
        Button4.setVisibility(View.VISIBLE);
        Button5.setVisibility(View.VISIBLE);
        Button6.setVisibility(View.VISIBLE);
        Button7.setVisibility(View.VISIBLE);
        Button8.setVisibility(View.VISIBLE);
        Button9.setVisibility(View.VISIBLE);
        Button0.setVisibility(View.VISIBLE);
        plus_Button.setVisibility(View.VISIBLE);
        minus_Button.setVisibility(View.VISIBLE);
        mul_Button.setVisibility(View.VISIBLE);
        div_Button.setVisibility(View.VISIBLE);
        dot_Button.setVisibility(View.VISIBLE);
        neg_Button.setVisibility(View.VISIBLE);
        equ_Button.setVisibility(View.VISIBLE);
        C_Button.setVisibility(View.VISIBLE);
        CE_Button.setVisibility(View.VISIBLE);
        //esc_Button.setVisibility(View.VISIBLE);
    	
    }
    
    private void clearColor_cal(){
    	new Thread(){  
            public void run(){
                handler.post(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Button1.getBackground().clearColorFilter();
				    	Button2.getBackground().clearColorFilter();
				        Button3.getBackground().clearColorFilter();
				        Button4.getBackground().clearColorFilter();
				        Button5.getBackground().clearColorFilter();
				        Button6.getBackground().clearColorFilter();
				        Button7.getBackground().clearColorFilter();
				        Button8.getBackground().clearColorFilter();
				        Button9.getBackground().clearColorFilter();
				        Button0.getBackground().clearColorFilter();
				        plus_Button.getBackground().clearColorFilter();
				        minus_Button.getBackground().clearColorFilter();
				        mul_Button.getBackground().clearColorFilter();
				        div_Button.getBackground().clearColorFilter();
				        dot_Button.getBackground().clearColorFilter();
				        neg_Button.getBackground().clearColorFilter();
				        equ_Button.getBackground().clearColorFilter();
				        C_Button.getBackground().clearColorFilter();
				        CE_Button.getBackground().clearColorFilter();
					}
                });   
            }                     
        }.start();         
    	
    }
    
    private void clearColor_main(){
    	new Thread(){  
            public void run(){
                handler.post(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						main2cal.getBackground().clearColorFilter();
						main2player.getBackground().clearColorFilter();
						main2music.getBackground().clearColorFilter();
						main2photo.getBackground().clearColorFilter();
						main2recal.getBackground().clearColorFilter();
						main2quit.getBackground().clearColorFilter();
					}
                });   
            }                     
        }.start();         
    	
    }
    
    private void clearColor_player(){
    	new Thread(){  
            public void run(){
                handler.post(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						bt_play.getBackground().clearColorFilter();
						bt_stop.getBackground().clearColorFilter();
						bt_quit.getBackground().clearColorFilter();
					}
                });   
            }                     
        }.start();         
    }
    
    private void clearColor_music(){
    	new Thread(){  
            public void run(){
                handler.post(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						ms_play.getBackground().clearColorFilter();
						ms_quit.getBackground().clearColorFilter();
						ms_next.getBackground().clearColorFilter();
						ms_last.getBackground().clearColorFilter();
					}
                });   
            }                     
        }.start();    
    }
    
    private void clearColor_image(){
    	new Thread(){  
            public void run(){
                handler.post(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						ph_next.getBackground().clearColorFilter();
						ph_last.getBackground().clearColorFilter();
						ph_quit.getBackground().clearColorFilter();
					}
                });   
            }                     
        }.start();  
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        //mItemSwitchCamera = menu.add("Toggle Native/Java camera");
        eye = menu.add("eye");
        track = menu.add("track");
        exit = menu.add("exit");
        calib = menu.add("calib");
        app = menu.add("app");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        //if (item == mItemSwitchCamera) {
         //   mOpenCvCameraView.setVisibility(SurfaceView.GONE);
        //    mIsJavaCamera = !mIsJavaCamera;
//
          //  if (mIsJavaCamera) {
         //       mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
         //       toastMesage = "Java Camera";
         //   } else {
         //       mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_native_surface_view);
         //       toastMesage = "Native Camera";
        //    }

          //  mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
         //   mOpenCvCameraView.setCvCameraViewListener(this);
          //  mOpenCvCameraView.enableView();
         //   Toast toast = Toast.makeText(this, toastMesage, Toast.LENGTH_LONG);
         //   toast.show();
        //}
        //else 
        if(item == track){
        	if (svd_x_coefficient_right == null){
        		press_calibration_button();
        	}else press_track_button();
        }
        else if (item == eye){
        	press_eyeregion_button();
        }
        else if (item == calib){
        	if (svd_x_coefficient_right == null){
        		press_calibration_button();
        	}else dialog_calib2track();
        }
        else if(item == exit){
        	dialog_exit();
        }
        else if (item == app){
        	app_flag = 1;
        	aaa.setVisibility(View.VISIBLE);
        	//ccc.setVisibility(View.VISIBLE);
        	//button_visible();
        	//esc_Button.setText("Esc");
        	//process_count = 4;
        	
        	//press_track_button();
        }
        
       // else if (item == texttest){//666果断成功
        //	new Thread (new Runnable(){
        //		@Override 
        //		public void run(){
        //			handler.post(new Runnable(){
        //				@Override
        //				public void run(){
        //					Toast toast = Toast.makeText(getApplicationContext(), "aaaaaaaaaa", Toast.LENGTH_LONG);
        //		            toast.show();
        //				}
        //			});
        //		}
        //	}).start();
        //}
        return true;
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
    	Log.e(TAG, "你哥掉毛");
    	if (calib_flag){
    		Mat output = calibration_background.t();
    		if (calibration_index1 == 0){
    			calibration_x = calibration_screenx[calibration_index0 - 1];
    			calibration_y = calibration_screeny[calibration_index0 - 1];
    		}
    		if (calibration_index0 <= SS){
    			return Init_calib_show(output,inputFrame);	
    		}
    		/////////////////////////////////////////////////
    		if(input_x_mat == null){
    			return calibrationprocess(output);
    		}
    		else {
    			Mat Imgscreen = output;
    			for (int i = 0; i < SS; i ++){
    	        	int x = (int) (svd_x_coefficient_right.get(0, 0)[0]
    	        			+ svd_x_coefficient_right.get(1, 0)[0] * calib_mat.get(i).x
    	        			+ svd_x_coefficient_right.get(2, 0)[0] * calib_mat.get(i).y
    	        		    + svd_x_coefficient_right.get(3, 0)[0] * calib_mat.get(i).x * calib_mat.get(i).x
    	        		    );
    	        	int y = (int) (svd_y_coefficient_right.get(0, 0)[0]
    	        			+ svd_y_coefficient_right.get(1, 0)[0] * calib_mat.get(i).x
    	        			+ svd_y_coefficient_right.get(2, 0)[0] * calib_mat.get(i).y
    	        		    + svd_y_coefficient_right.get(3, 0)[0] * calib_mat.get(i).y * calib_mat.get(i).y
    	        		    );
    	        	Core.circle(Imgscreen, new Point(calibration_screenx[i+1],calibration_screeny[i+1])
    	        	, 20, new Scalar(0,255,255,0), -1, 8, 0);
    	        	Core.circle(Imgscreen, new Point(x,y), 20, new Scalar(0,0,255,0), -1, 8, 0);
    	        }
    			Log.e(TAG, "coefficient:  " + Double.toString(svd_x_coefficient_right.get(0, 0)[0]) + "  "
    					 + Double.toString(svd_x_coefficient_right.get(1, 0)[0]) + "  "
    					 + Double.toString(svd_x_coefficient_right.get(2, 0)[0]) + "  "
    					 + Double.toString(svd_x_coefficient_right.get(3, 0)[0]));
    			/////////////////////////////10000//////////////////////////////////////////
    			//new Thread(new Runnable(){
    		 	//		@Override 
    		 	//		public void run(){
    		 	//			try {
    		 	//				Log.i(TAG, "sleep");
				//				HandlerThread.sleep(1000);
				//			} catch (InterruptedException e) {
				//				// TODO Auto-generated catch block
				//				e.printStackTrace();
				//			}
    		 	//		}
    		 	//	}).start();
    			////////////////////////////////////////////////////////////////////////////
    			////////////////////////////////////////////////////////////////////////////
    			return Imgscreen;
    		}
    		//return output;	
    	}
    	else if (eye_flag) {//return inputFrame.gray();
    		///////获取特征
    		Point c = CaculateEyecenterAppro(inputFrame);
    		caculate_eye_center(1); 
    		Point e = caculate_spot_center(1,1);
    		///////输出
    		Log.e(TAG, "appropriate area:  x  " + Double.toString(c.x) + "  y  " + Double.toString(c.y));
    		Log.e(TAG, "pupil area:  x  " + Double.toString(box.center.x) + "  y  " + Double.toString(box.center.y));
    		Log.e(TAG, "spot area:  x  " + Double.toString(e.x) + "  y  " + Double.toString(e.y));
    		///////图像返回
    		Mat output = inputFrame.rgba();
    		Core.rectangle(output, new Point( (int)(c.x - exteye.cols()),(int)(c.y - exteye.rows())),
    				new Point( (int)(c.x+exteye.cols()) ,(int)(c.y+exteye.cols())),
    				new Scalar(0,255,0,0),2,8,0);
    		Core.ellipse(output, box, new Scalar(255,0,0,0));
    		Core.line(output, new Point(e.x,0), new Point(e.x,Height), new Scalar(0,0,255,0));
    		Core.line(output, new Point(0,e.y), new Point(Width,e.y), new Scalar(0,0,255,0));
    		return output;
    		}
    	else if(!Track_flag){
    		
    		Point eye = CaculateEyecenterAppro(inputFrame);
            caculate_eye_center(1);
            Point s = caculate_spot_center(1,1);
            calculate_pcr(1);
            int x = 0;
            int y = 0;
            if (Math.abs(pcrvector.x) < 100 && Math.abs(pcrvector.y) < 100){
            	x = (int) (svd_x_coefficient_right.get(0, 0)[0]
	        			+ svd_x_coefficient_right.get(1, 0)[0] * pcrvector.x
	        			+ svd_x_coefficient_right.get(2, 0)[0] * pcrvector.y
	        		    + svd_x_coefficient_right.get(3, 0)[0] * pcrvector.x * pcrvector.x
	        		    );
	        	y = (int) (svd_y_coefficient_right.get(0, 0)[0]
	        			+ svd_y_coefficient_right.get(1, 0)[0] * pcrvector.x
	        			+ svd_y_coefficient_right.get(2, 0)[0] * pcrvector.y
	        		    + svd_y_coefficient_right.get(3, 0)[0] * pcrvector.y * pcrvector.y
	        		    );
            }
            ///////////////////////////////////////////////////////////////////////////////////////
            Point position = new Point(x,y);
            
            //        	position = GuassSmooth(position);
            ////////输出
            Log.e(TAG, "eye area:  x  " + Double.toString(eye.x) + "  y  " + Double.toString(eye.y));
            Log.e(TAG, "pupil area:  x  " + Double.toString(box.center.x) + "  y  " + Double.toString(box.center.y));
            Log.e(TAG, "spot area:  x  " + Double.toString(s.x) + "  y  " + Double.toString(s.y));
            Log.e(TAG, "pcr:  x  " + Double.toString(pcrvector.x) + "  y  " + Double.toString(pcrvector.y));
            Log.e(TAG, "coefficient:  " + Double.toString(svd_x_coefficient_right.get(0, 0)[0]) + "  "
					 + Double.toString(svd_x_coefficient_right.get(1, 0)[0]) + "  "
					 + Double.toString(svd_x_coefficient_right.get(2, 0)[0]) + "  "
					 + Double.toString(svd_x_coefficient_right.get(3, 0)[0]));
            Log.e(TAG, "Target Point:  x  " + Double.toString(position.x) + "  y  " + Double.toString(position.y));
            
            ////////修正
            if (position.x > Width) position.x = Width;
			else if (position.x < 0) position.x = 0;
			if (position.y > Height) position.y = Height;
			else if (position.y < 0) position.y = 0;
			Log.e(TAG, "Target Point:  x  " + Double.toString(position.x) + "  y  " + Double.toString(position.y));
			
			//position = new Point(240,638);
			if (app_flag == 2){
				eye_control_calculator(position);
			}else if (app_flag == 1){
				eye_control_main(position);
			}else if (app_flag == 3){
				eye_control_player(position);
			}else if (app_flag == 4){
				eye_control_music(position);
			}else if (app_flag == 5){
				eye_control_photo(position);
			}
		
			
			
    		//////////////////////////////////////////////////////////////
    		//	Point p = gazetrack(inputFrame);
    		//	if (p.x > Width) p.x = Width;
    		//	else if (p.x < 0) p.x = 0;
    		//	if (p.y > Height) p.y = Height;
    		//	else if (p.y < 0) p.y = 0;
    		//////////////////////////////////////////////////////////////
    			//if(p != null){
    				//Mat output = calibration_background.t();
    			                       	//眼睛
    			                       	//Core.rectangle(output, new Point( (int)(eyecenter_app.x - exteye.cols()),(int)(eyecenter_app.y - exteye.rows())),
        		                       	//		new Point( (int)(eyecenter_app.x+exteye.cols()) ,(int)(eyecenter_app.y+exteye.cols())),
        								//		new Scalar(0,255,0,0),2,8,0);
    									//瞳孔
    									//Core.ellipse(output, box, new Scalar(255,0,0,0));
    									//斑点
    									//Core.circle(output, new Point(spotscenter.get(0).center.x + eyecenter_app.x - 45
    									//		, spotscenter.get(0).center.y + eyecenter_app.y - 40)
    									//, 8, new Scalar(0,0,255,0),2,8,0);
			
			//????????????????//图像返回
			if (app_flag > 0){
				return black.t();
			}else{
				Mat output = calibration_background.t();
				Core.circle(output, position, 20, new Scalar(255,255,0,0), -1, 8, 0);
				return output;
			}
    	}else if (app_flag > 0){
    		return black.t();
    	}
    	return inputFrame.rgba();	
    }
    
    //public Mat RefreshFrame(CvCameraViewFrame inputFrame){
    //	Mat frame = inputFrame.rgba();
    //	Imgproc.cvtColor(inputFrame.rgba() , frame , Imgproc.COLOR_RGB2GRAY);
    //	return frame;
    //}
    
    private void eye_control_photo(Point position){
    	if (position.y < 240){
    		if(position.x > 480){
    			if (position_flag != 0) {
    				clearColor_image();
    				new Thread(){  
    	                public void run(){
    	                    handler.post(new Runnable(){
    							@Override
    							public void run() {
    								// TODO Auto-generated method stub
    								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    								ph_quit.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    							}
    	                    });   
    	                }                     
    	            }.start();
    			} 
    		}
    		p_count(0);
    	}else{
    		if(position.x > 480){
    			if (position_flag != 1) {
    				clearColor_image();
    				new Thread(){  
    	                public void run(){
    	                    handler.post(new Runnable(){
    							@Override
    							public void run() {
    								// TODO Auto-generated method stub
    								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    								ph_next.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    							}
    	                    });   
    	                }                     
    	            }.start();
    			} 
    			p_count(1);
    		}else if (position.x < 160){
    			if (position_flag != 2) {
    				clearColor_image();
    				new Thread(){  
    	                public void run(){
    	                    handler.post(new Runnable(){
    							@Override
    							public void run() {
    								// TODO Auto-generated method stub
    								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    								ph_last.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    							}
    	                    });   
    	                }                     
    	            }.start();
    			} 
    			p_count(2);
    		}
    	}
    	if (position_count == 8){
    		position_count = 0;
			switch(position_flag){
				case 0: 
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									ph_quit.getBackground().clearColorFilter();
									ph_quit.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
				case 1: 
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									ph_next.getBackground().clearColorFilter();
									ph_next.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
				case 2: 
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									ph_last.getBackground().clearColorFilter();
									ph_last.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
			}
    	}
    }
    
    private void eye_control_music(Point position){
    	if (position.y > 360){
    		if (position.x < 160){
    			if (position_flag != 0) {
    				clearColor_music();
    				new Thread(){  
    	                public void run(){
    	                    handler.post(new Runnable(){
    							@Override
    							public void run() {
    								// TODO Auto-generated method stub
    								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    								ms_last.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x0000FF00));
    							}
    	                    });   
    	                }                     
    	            }.start();
    			}
    			p_count(0);
    		}else if (position.x < 320){
    			if (position_flag != 1) {
    				clearColor_music();
    				new Thread(){  
    	                public void run(){
    	                    handler.post(new Runnable(){
    							@Override
    							public void run() {
    								// TODO Auto-generated method stub
    								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    								ms_play.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x0000FF00));
    							}
    	                    });   
    	                }                     
    	            }.start();
    			}
    			p_count(1);
    		}else if (position.x < 480){
    			if (position_flag != 2) {
    				clearColor_music();
    				new Thread(){  
    	                public void run(){
    	                    handler.post(new Runnable(){
    							@Override
    							public void run() {
    								// TODO Auto-generated method stub
    								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    								ms_next.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x0000FF00));
    							}
    	                    });   
    	                }                     
    	            }.start();
    			}
    			p_count(2);
    		}else{
    			if (position_flag != 3) {
    				clearColor_music();
    				new Thread(){  
    	                public void run(){
    	                    handler.post(new Runnable(){
    							@Override
    							public void run() {
    								// TODO Auto-generated method stub
    								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    								ms_quit.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x0000FF00));
    							}
    	                    });   
    	                }                     
    	            }.start();
    			}
    			p_count(3);
    		}
    	}
    	if (position_count == 8){
    		position_count = 0;
			switch(position_flag){
				case 0: 
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									ms_last.getBackground().clearColorFilter();
									ms_last.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
				case 1: 
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									ms_play.getBackground().clearColorFilter();
									ms_play.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
				case 2: 
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									ms_next.getBackground().clearColorFilter();
									ms_next.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
				case 3:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									ms_quit.getBackground().clearColorFilter();
									ms_quit.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
			}
    	}
    }
    
    private void eye_control_player(Point position){
    	if (position.y > 320){
    		if (position.x < 640/3){
    			if (position_flag != 0) {
    				clearColor_player();
    				new Thread(){  
    	                public void run(){
    	                    handler.post(new Runnable(){
    							@Override
    							public void run() {
    								// TODO Auto-generated method stub
    								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    								bt_play.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    							}
    	                    });   
    	                }                     
    	            }.start();
        		}
    			p_count(0);
    		}else if(position.x < 640*2/3){
    			if (position_flag != 1) {
    				clearColor_player();
    				new Thread(){  
    	                public void run(){
    	                    handler.post(new Runnable(){
    							@Override
    							public void run() {
    								// TODO Auto-generated method stub
    								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    								bt_stop.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    							}
    	                    });   
    	                }                     
    	            }.start();
        		}
    			p_count(1);
    		}else {
    			if (position_flag != 2) {
    				clearColor_player();
    				new Thread(){  
    	                public void run(){
    	                    handler.post(new Runnable(){
    							@Override
    							public void run() {
    								// TODO Auto-generated method stub
    								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    								bt_quit.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
    							}
    	                    });   
    	                }                     
    	            }.start();
        		}
    			p_count(2);
    		}
    	}
    	if (position_count == 8){
			position_count = 0;
			switch(position_flag){
				case 0: 
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									bt_play.getBackground().clearColorFilter();
									bt_play.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
				case 1: 
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									bt_stop.getBackground().clearColorFilter();
									bt_stop.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
				case 2: 
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									bt_quit.getBackground().clearColorFilter();
									bt_quit.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
			}
    	}
    }
    
    private void eye_control_main(Point position){
    	if (position.y<240 && position.x<640/3){
    		if (position_flag != 0) {
    			clearColor_main();
    			new Thread(){  
	                public void run(){
	                    handler.post(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								main2cal.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
							}
	                    });   
	                }                     
	            }.start();
    		}
    		p_count(0);
    	}else if(position.y<240 && position.x<640*2/3){
    		if (position_flag != 1) {
    			clearColor_main();
    			new Thread(){  
	                public void run(){
	                    handler.post(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								main2player.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
							}
	                    });   
	                }                     
	            }.start();
    		}
    		p_count(1);
    	}else if(position.y<240){
    		if (position_flag != 2) {
    			clearColor_main();
    			new Thread(){  
	                public void run(){
	                    handler.post(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								main2music.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
							}
	                    });   
	                }                     
	            }.start();
    		}
    		p_count(2);
    	}else if(position.y>240 && position.x<640/3){
    		if (position_flag != 3) {
    			clearColor_main();
    			new Thread(){  
	                public void run(){
	                    handler.post(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								main2photo.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
							}
	                    });   
	                }                     
	            }.start();
    		}
    		p_count(3);
    	}else if(position.y>240 && position.x<640*2/3){
    		if (position_flag != 4) {
    			clearColor_main();
    			new Thread(){  
	                public void run(){
	                    handler.post(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								main2recal.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
							}
	                    });   
	                }                     
	            }.start();
    		}
    		p_count(4);
    	}else if(position.y>240){
    		if (position_flag != 5) {
    			clearColor_main();
    			new Thread(){  
	                public void run(){
	                    handler.post(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								main2quit.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
							}
	                    });   
	                }                     
	            }.start();
    			
    		}
    		p_count(5);
    	}
    	if (position_count == 8){
			position_count = 0;
			switch(position_flag){
				case 0: 
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									main2cal.getBackground().clearColorFilter();
									main2cal.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
				case 1:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									main2player.getBackground().clearColorFilter();
									main2player.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
				case 2:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									main2music.getBackground().clearColorFilter();
									main2music.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
				case 3:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									main2photo.getBackground().clearColorFilter();
									main2photo.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
				case 4:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									main2recal.getBackground().clearColorFilter();
									main2recal.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					break;
				case 5:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									main2quit.getBackground().clearColorFilter();
									main2quit.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					//Button0.performClick();
					break;
			}
    	}
    }
      
    private void eye_control_calculator(Point position) {
		if(position.y > 480 - 256/3){
			if (position.x < 128){
				if(position_flag != 10){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									dot_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(10);
				
				//dot_Button.performClick();
			}else if(position.x < 256){
				if(position_flag != 0){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(0);
				
				//Button0.performClick();
			}else if(position.x < 384){
				if(position_flag != 11){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									neg_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(11);
				
				//neg_Button.performClick();
			}else if(position.x < 512){
				if(position_flag != 15){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									div_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(15);
				
				//div_Button.performClick();
			}else{
				if(position_flag != 19){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									esc_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(19);
				
			}
		}else if(position.y > 480 - 512/3){
			if (position.x < 128){
				if(position_flag != 7){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button7.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(7);
				
				
				//Button7.performClick();
			}else if(position.x < 256){
				if(position_flag != 8){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button8.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(8);
				
				
				//Button8.performClick();
			}else if(position.x < 384){
				if(position_flag != 9){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button9.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(9);
				
				
				//Button9.performClick();
			}else if(position.x < 512){
				if(position_flag != 14){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									mul_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(14);
				
				
				//mul_Button.performClick();
			}else{
				if(position_flag != 18){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									equ_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(18);
				
				
				//equ_Button.performClick();
			}
		}else if(position.y > 480 - 768/3){
			if (position.x < 128){
				if(position_flag != 4){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button4.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(4);
				
				//Button4.performClick();
			}else if(position.x < 256){
				if(position_flag != 5){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button5.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(5);
				
				
				//Button5.performClick();
			}else if(position.x < 384){
				if(position_flag != 6){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button6.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(6);
				
				
				//Button6.performClick();
			}else if(position.x < 512){
				if(position_flag != 13){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									plus_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(13);
				
				
				//plus_Button.performClick();
			}else{
				if(position_flag != 17){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									CE_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(17);
				
				
				//CE_Button.performClick();
			}
		}else if(position.y > 480 - 1024/3){
			if (position.x < 128){
				if(position_flag != 1){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button1.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(1);
				
				
				//Button1.performClick();
			}else if(position.x < 256){
				if(position_flag != 2){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button2.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(2);

				
				//Button2.performClick();
			}else if(position.x < 384){
				if(position_flag != 3){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button3.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(3);
				
				
				//Button3.performClick();
			}else if(position.x < 512){
				if(position_flag != 12){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									minus_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(12);
				
				
				//minus_Button.performClick();
			}else{
				if(position_flag != 16){
					clearColor_cal();
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									C_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
								}
		                    });   
		                }                     
		            }.start();
				}
				p_count(16);
				
				//C_Button.performClick();
			}
		}
		if (position_count == 6){
			position_count = 0;
			switch(position_flag){
				case 0:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button0.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button0.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					//Button0.performClick();
					break;
				case 1:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button1.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button1.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					//Button1.performClick();
					break;
				case 2:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button2.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button2.performClick();
								}
		                    });   
		                }                     
		            }.start();         
					//Button2.performClick();
					break;
				case 3:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button3.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button3.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//Button3.performClick();
					break;
				case 4:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button4.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));	
									Button4.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//Button4.performClick();
					break;
				case 5:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button5.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button5.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//Button5.performClick();
					break;
				case 6:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button6.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button6.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//Button6.performClick();
					break;
				case 7:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button7.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button7.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//Button7.performClick();
					break;
				case 8:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button8.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button8.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//Button8.performClick();
					break;
				case 9:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//Button9.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									Button9.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//Button9.performClick();
					break;
				case 10:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//dot_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									dot_Button.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//dot_Button.performClick();
					break;
				case 11:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//neg_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									neg_Button.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//neg_Button.performClick();
					break;
				case 12:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//minus_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									minus_Button.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//minus_Button.performClick();
					break;
				case 13:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//plus_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									plus_Button.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//plus_Button.performClick();
					break;
				case 14:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//mul_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									mul_Button.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//mul_Button.performClick();
					break;
				case 15:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//div_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									div_Button.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//div_Button.performClick();
					break;
				case 16:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//C_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									C_Button.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//C_Button.performClick();
					break;
				case 17:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//CE_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									CE_Button.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//CE_Button.performClick();
					break;
				case 18:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//equ_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									equ_Button.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//equ_Button.performClick();
					break;
				case 19:
					new Thread(){  
		                public void run(){
		                    handler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//esc_Button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
									esc_Button.performClick();
								}
		                    });   
		                }                     
		            }.start(); 
					//esc_Button.performClick();
					break;
			}	
		}
	}
    
    public void p_count(int num){//记录position_count和position_flag
    	if (position_flag == num){
			position_count++;
		}else{
			position_count = 1;
			position_flag = num;
		}
    }
    
    public Point CaculateEyecenterAppro(CvCameraViewFrame inputFrame){
    	//double min_val = 0;
        //double max_val = 0;
        Point max_loc = new Point();
        Point min_loc = new Point();
        Point eyecenter = new Point();
        MatOfRect eyeRect = new MatOfRect();
        Rect reye = new Rect();
        frame = inputFrame.rgba();
        tmpframe = inputFrame.gray();
        if(eyecenter_acc.x==-1000){
        	eyecascade.detectMultiScale(frame,eyeRect,1.1,2,0,new Size(90,90),new Size());
        	if(!eyeRect.empty())  reye = eyeRect.toList().get(0);
        	
        		for (Rect rect : eyeRect.toArray()){
        			if (rect.x > reye.x) reye = rect;
        		}
        }
        
        if(!eyeRect.empty() && eyecenter_acc.x==-1000){
        	Mat eyearea = tmpframe.submat(new Range(reye.y,reye.y+reye.height), 
        			new Range(reye.x,reye.x+reye.width)).clone();
        	//Mat eyearea_gray = new Mat();
        	//Imgproc.cvtColor(eyearea, eyearea_gray, Imgproc.COLOR_RGB2GRAY);
        	Mat eyearea_th = new Mat();
        	Imgproc.threshold(eyearea, eyearea_th, g1, 255, Imgproc.THRESH_BINARY);
        	Mat result_1 = new Mat();
        	Imgproc.matchTemplate(eyearea_th, exteye, result_1,Imgproc.TM_CCOEFF);
        	max_loc = Core.minMaxLoc(result_1).maxLoc;
        	//min_loc = Core.minMaxLoc(result_1).minLoc;
        	eyecenter.x=reye.x+max_loc.x+exteye.cols()*0.5;
            eyecenter.y=reye.y+max_loc.y+exteye.rows()*0.5;
            //Core.rectangle(frame,reye.br(),reye.tl(),new Scalar(0,255,0,0));   
        }
        else if(eyeroi.x>80&&eyeroi.x<560&&eyeroi.y>80&&eyeroi.y<400){
        	Mat RoiGray = tmpframe.submat(new Range((int)eyeroi.y-80,(int)eyeroi.y+80),
        			new Range((int)eyeroi.x-80,(int)eyeroi.x+80)).clone();
        	Imgproc.threshold(RoiGray, RoiGray, g1, 255, Imgproc.THRESH_BINARY);
        	Mat result_2 = new Mat();
        	Imgproc.matchTemplate(RoiGray, exteye, result_2, Imgproc.TM_CCOEFF);
        	max_loc = Core.minMaxLoc(result_2).maxLoc;
        	eyecenter.x=eyeroi.x-80+max_loc.x+exteye.cols()*0.5;
            eyecenter.y=eyeroi.y-80+max_loc.y+exteye.rows()*0.5;
            //Core.rectangle(frame, reye.br(), reye.tl(), new Scalar(0,255,0,0));
        }
        else{
        	Mat result_3 = new Mat();
        	Imgproc.matchTemplate(tmpframe, exteye99, result_3, Imgproc.TM_CCOEFF);
        	max_loc = Core.minMaxLoc(result_3).maxLoc;
        	min_loc = Core.minMaxLoc(result_3).minLoc;
        	eyecenter.x=max_loc.x+exteye99.cols()*0.5;
            eyecenter.y=max_loc.y+exteye99.rows()*0.5;
        }
       // assert(eyecenter.x != 0 && eyecenter.y != 0);
        eyecenter_app = eyecenter;
        /////////////////////change dst_gray to rgb;
        dst_gray = tmpframe.submat(new Range((int)eyecenter_app.y-40,(int)eyecenter_app.y+40),
        		new Range((int)eyecenter_app.x-45,(int)eyecenter_app.x+45)).clone(); 
        dst_rgb =  frame.submat(new Range((int)eyecenter_app.y-40,(int)eyecenter_app.y+40),
        		new Range((int)eyecenter_app.x-45,(int)eyecenter_app.x+45)).clone();  
        Log.e(TAG, "appropriate area:  x  " + Double.toString(eyecenter.x) + "  y  " + Double.toString(eyecenter.y));
		return eyecenter;
    }
    
    public Point caculate_eye_center(double mult){
    	if(mult <= 0)mult = 1;
    	RotatedRect boxpupil;
    	RotatedRect boxpupil_1 = new RotatedRect();
    	boxpupil_1.center.x = 1000;
    	boxpupil_1.center.y = 1000;
    	////////////////////////////////////
    	Mat dst = new Mat();
    	Imgproc.medianBlur(dst_gray, dst, 3);
    	Imgproc.resize(dst, dst, new Size(0,0),mult, mult, Imgproc.INTER_LINEAR);
    	Imgproc.threshold(dst, dst, g1, 255, Imgproc.THRESH_BINARY);
    	////////////////////////////////////
    	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    	MatOfPoint contourstarget = new MatOfPoint();
    	MatOfInt4 hierarchy = new MatOfInt4();
    	//hierarchy.convertTo(hierarchy, CvType.CV_32SC4);
		Imgproc.findContours(dst, contours, hierarchy, Imgproc.RETR_CCOMP , 
				Imgproc.CHAIN_APPROX_SIMPLE);
		if(hierarchy.empty()){
			Log.e(TAG, "pupil area:  not found ");
			return new Point(-1000,-1000);
		}
		//int Pcontour;
		int idx1 = 0;
		for(int idx = 0; idx >= 0;idx = hierarchy.toList().get(4*idx))
        {
                idx1 = hierarchy.toList().get(idx*4+2);
                while(idx1 >= 0)
                {
						double area = Math.abs(Imgproc.contourArea(contours.get(idx1)));
                        if( area>100 && area<3600 && contours.get(idx1).toList().size() > 6)//ROI区域中心的边缘为瞳孔
                        {
                                boxpupil = Imgproc.fitEllipse(new MatOfPoint2f(contours.get(idx1).toArray()));
                                double D = Math.abs(boxpupil.center.x/mult-eyecenter_app.x) + 
                                		Math.abs(boxpupil.center.y/mult-eyecenter_app.y);
                                double D1 = Math.abs(boxpupil_1.center.x/mult-eyecenter_app.x) + 
                                		Math.abs(boxpupil_1.center.y/mult-eyecenter_app.y);
                                if(D < D1)
                                {
                                        boxpupil_1=boxpupil;
                                        contourstarget = new MatOfPoint();
                                        contourstarget = contours.get(idx1);
                                        //Pcontour = idx1;
                                }
                        }
                        idx1 = hierarchy.toList().get(idx1*4);
                        //Log.i(TAG, "idx1  success!");
                }
        }
		if(contourstarget.empty())
        {
			Log.e(TAG, "pupil area:  not found ");
            return new Point(-1000, -1000);
        }
		eyecontours = new ArrayList<Point>();
		MatOfInt hull = new MatOfInt();
		Imgproc.convexHull(contourstarget, hull);
		if(hull.toList().size() < 6)
        {
			Log.e(TAG, "pupil area:  not found ");
            return new Point(-1000,-1000);
        }
		for (int j = 0; j < hull.toList().size(); j ++){
			eyecontours.add(contourstarget.toList().get(hull.toList().get(j)));
		}
		MatOfPoint2f obj = new MatOfPoint2f();
		obj.fromList(eyecontours);
		box = Imgproc.fitEllipse(obj); 
		dst_rgb = new Mat();
		Imgproc.cvtColor(dst_gray, dst_rgb,Imgproc.COLOR_GRAY2RGB);
		//Core.ellipse(dst_rgb, box, new Scalar(255,0,0,0));
		
		box.center.x /= mult;
        box.center.y /= mult;

        eyecenter_acc.x = box.center.x;
        eyecenter_acc.y = box.center.y;
        
        
        eyeroi.x = box.center.x+eyecenter_app.x-45;
        eyeroi.y = box.center.y+eyecenter_app.y-40;
        
        box.center.x = eyeroi.x;
        box.center.y = eyeroi.y;
        
        Log.e(TAG, "pupil area:  x  " + Double.toString(box.center.x) + "  y  " + Double.toString(box.center.y));
        return eyecenter_acc;
    }
    
    public Point caculate_spot_center(int num, double mult){
    	Mat dst = new Mat();
    	Imgproc.resize(dst_gray, dst, new Size(0,0), mult, mult, Imgproc.INTER_LINEAR);
    	Imgproc.threshold(dst, dst, g, 255, Imgproc.THRESH_BINARY);
    	Imgproc.medianBlur(dst, dst, 3);
    	/////////////////////////////////////////
    	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    	//MatOfPoint contourstarget = new MatOfPoint();
    	MatOfInt4 hierarchy = new MatOfInt4();
    	Imgproc.findContours(dst, contours, hierarchy, Imgproc.RETR_CCOMP , 
				Imgproc.CHAIN_APPROX_SIMPLE);
    	if (hierarchy.empty()){
    		Log.e(TAG, "spot area:  not found ");
    		return new Point(-1000,-1000);
    	}
    	
    	List<RotatedRect> spotscenter_tmp = new ArrayList<RotatedRect>();
    	List<Integer> D = new ArrayList<Integer>();
    	
    	for(int idx = 0;idx >= 0;idx = (int) hierarchy.toList().get(idx*4))
        {
                double area1 = Math.abs(Imgproc.contourArea(contours.get(idx)));
                if(area1 > 1 && area1 < 1000 && contours.get(idx).toList().size() > 6)
                {
                        RotatedRect box =Imgproc.fitEllipse(new MatOfPoint2f(contours.get(idx).toArray()));
                        spotscenter_tmp.add(box);
                        int Ds = (int) (Math.abs(box.center.x-eyecenter_acc.x) + 
                        		Math.abs(box.center.y-eyecenter_acc.y));
                        D.add(Ds);
                }
        }
    	int count = spotscenter_tmp.size();
    	if(count < num){
    		Log.e(TAG, "spot area:  not found ");
    		return new Point(-1000,-1000); 
    	}
    	spotscenter_tmp = InserSort(spotscenter_tmp,D,count);
    	
    	for(Object idx : spotscenter_tmp.toArray()){
    		((RotatedRect)idx).center.x /= mult;
    		((RotatedRect)idx).center.y /= mult;
    	}
    	
    	spotscenter = spotscenter_tmp;
    	
    	Collections.sort(spotscenter_tmp, new Comparator<RotatedRect>() {
    		@Override  
    		public int compare(RotatedRect lhs, RotatedRect rhs) {  
    		return (int) (rhs.center.x - lhs.center.x);  
    		}  

    	});
    	Log.e(TAG, "spot area:  x  " + Double.toString(spotscenter.get(0).center.x + eyecenter_app.x - 45) + "  y  " + Double.toString(spotscenter.get(0).center.y + eyecenter_app.y - 40));
		return new Point(spotscenter.get(0).center.x + eyecenter_app.x - 45
				, spotscenter.get(0).center.y + eyecenter_app.y - 40);
    	
    }
    
    public static List<RotatedRect> InserSort(List<RotatedRect> boxes , List<Integer> D , int count){
    	List<RotatedRect> result_boxes = boxes;
    	List<Integer> result_D = D;
    	if(count == 1) return boxes;
    	for (int j = 1; j < count; j ++){
    		int i = j - 1;
    		int k = result_D.get(j);
    		RotatedRect kbox = result_boxes.get(j);
    		while(i >= 0 && result_D.get(i) > k){
    			result_D.set(i+1, result_D.get(i));
    			result_boxes.set(i+1, result_boxes.get(i));
    			i --;
    		}
    		result_D.set(i+1, k);
    		result_boxes.set(i+1, kbox);
    	}
    	//for(int j = 1; j <= count - 1; j ++){
    	//	int i = j - 1;
    	//	int k = D.get(j);
    	//	kbox = boxes.get(j);
    	//	while(i >= 0 && D.get(i) > k){
    	//		result_D.put(i+1, 0, D.get(i));
    	//		result_boxes.add(i+1, boxes.get(i));
    	//		i--;
    	//	}
    	//	result_D.put(i+1, 0, k);
    	//	result_boxes.add(i+1, kbox);
    	//}
		return result_boxes;
    }

    public void calculate_pcr(int num){
    	/////////////////////////////////////////////////////
 		if ( spotscenter.size() <= num) num = 1;
 		switch (num){
 		case 2:
 			pcrvector = new Point(eyecenter_acc.x - (spotscenter.get(0).center.x + spotscenter.get(1).center.x)/2,
 					eyecenter_acc.y - (spotscenter.get(0).center.y + spotscenter.get(1).center.y)/2);
 			break;
 		default:
 			if(spotscenter.size() == 0){
                    pcrvector = new Point(0,0);
            }
            else{
                    pcrvector = new Point((eyecenter_acc.x - spotscenter.get(0).center.x) * 2,
                    		(eyecenter_acc.y - spotscenter.get(0).center.y) * 2);
            }
 			Log.e(TAG, "pcr :  x  " + Double.toString(pcrvector.x) + "  y  " + Double.toString(pcrvector.y));
            break;
 		}
 		//////////////////////////////////////////////////////
 		//new Thread(new Runnable(){
 		//	@Override 
 		//	public void run(){
 		//		handler.post(new Runnable(){
 		//			@Override 
 		//			public void run(){
 		//				if(spotscenter.size() == 0){
 		//                    pcrvector = new Point(0,0);
 		//				}
 		//				else{
 		//                    pcrvector = new Point(eyecenter_acc.x - spotscenter.get(0).center.x,
 		//                    		eyecenter_acc.y - spotscenter.get(0).center.x);
 		//				}
 		//			}
 		//		});
 		//	}
 		//}).start();
 	}

    public Mat calibrationprocess(Mat output){
    	//calib_mat = new ArrayList<Point>(); 
    	svd_x_coefficient_right = new Mat();
    	svd_y_coefficient_right = new Mat();
    	Mat Imgscreen = output;
    	//int calib_index  = 0;
    	//for (int index = 0; index < SS; index ++){
    		//double sum_x = 0;
    		//double sum_y = 0;
    		//int calib_count = 0;
    		
    		//for(int index_1 = 0; index_1 < calibration_mat.size()/SS; index_1 ++){
    			//RefreshFrame(calibration_mat.get(index));
    			//frame = calibration_mat.get(index);
    			//Log.e(TAG, "calib_index:" + Integer.toString(calib_index));
    			//Point eye = CaculateEyecenterAppro(calibration_mat.get(calib_index));
    			//Log.e(TAG, "eye area:  x  " + Double.toString(eye.x) + "  y  " + Double.toString(eye.y));
    			
    			//CaculateEyecenterAppro(frame);
    			//caculate_eye_center(1); 
    			//Log.e(TAG, "pupil area:  x  " + Double.toString(box.center.x) + "  y  " + Double.toString(box.center.y));
    			
        		//Point s = caculate_spot_center(1,1);
        		//Log.e(TAG, "spot area:  x  " + Double.toString(s.x) + "  y  " + Double.toString(s.y));
        		
        		//calculate_pcr(1);
        		//Log.e(TAG, "pcr:  x  " + Double.toString(pcrvector.x) + "  y  " + Double.toString(pcrvector.y));
        		//
        		//calib_index ++;
        		//int a ;
                //a = (int) pcrvector.x;
                //a = (int) pcrvector.y;
        		//if (eyecenter_acc.x != -1000){
        		//	if ( Math.abs(pcrvector.x) < 120 && Math.abs(pcrvector.y) < 120 ){
        		//		sum_x = pcrvector.x + sum_x;
        		//		sum_y = pcrvector.y + sum_y;
        		//		calib_count ++;
        		//	}
        		//}
    		//}
    		//if (calib_count != 0){
    		//	calib_mat.add(new Point(sum_x / calib_count, sum_y / calib_count));
    		//}
    		//else calib_mat.add(new Point(0,0));
    	//}
    	//input_x = new double[SS*4];
    	//input_y = new double[SS*4];
    	input_x_mat = new Mat(SS,4,CvType.CV_64FC1);
    	input_y_mat = new Mat(SS,4,CvType.CV_64FC1);
    	double[] calib_screenx = {80,Width-80,Width/2,80,Width-80};
        double[] calib_screeny = {80,80,Height/2,Height-80,Height-80};
        Mat calib_screen_x_mat = new Mat(SS,1,CvType.CV_64FC1);
        Mat calib_screen_y_mat = new Mat(SS,1,CvType.CV_64FC1);
        /////////////////////////////////////////////////////初始化参数
        Init_svd_SSx4(calib_mat);
        for (int k = 0; k < SS; k ++){
        	calib_screen_x_mat.put(k, 0, calib_screenx[k]);
        	calib_screen_y_mat.put(k, 0, calib_screeny[k]);
        }
        /////////////////////////////////////////////////////计算系数
        Core.solve(input_x_mat, calib_screen_x_mat, svd_x_coefficient_right, Core.DECOMP_SVD);
        Core.solve(input_y_mat, calib_screen_y_mat, svd_y_coefficient_right, Core.DECOMP_SVD);
        /////////////////////////////////////////////////////
        for (int i = 0; i < SS; i ++){
        	//踏马的怎么反过来了=。=
        	int x = (int) (svd_x_coefficient_right.get(0, 0)[0]
        			+ svd_x_coefficient_right.get(1, 0)[0] * calib_mat.get(i).x
        			+ svd_x_coefficient_right.get(2, 0)[0] * calib_mat.get(i).y
        		    + svd_x_coefficient_right.get(3, 0)[0] * calib_mat.get(i).x * calib_mat.get(i).x
        		    );
        	int y = (int) (svd_y_coefficient_right.get(0, 0)[0]
        			+ svd_y_coefficient_right.get(1, 0)[0] * calib_mat.get(i).x
        			+ svd_y_coefficient_right.get(2, 0)[0] * calib_mat.get(i).y
        		    + svd_y_coefficient_right.get(3, 0)[0] * calib_mat.get(i).y * calib_mat.get(i).y
        		    );
        	Core.circle(Imgscreen, new Point(calibration_screenx[i+1],calibration_screeny[i+1])
        	, 20, new Scalar(0,255,255,0), -1, 8, 0);
        	Core.circle(Imgscreen, new Point(x,y), 20, new Scalar(0,0,255,0), -1, 8, 0);
        	Log.e(TAG, "Point" + Integer.toString(i) + ":  x  " + Integer.toString(x) + "  y  " + Integer.toString(y));
        }
        //track.setCheckable(true);
        return Imgscreen;
    }
    
    public void Init_svd_SSx4(List<Point> calib){
    	for (int j = 0; j < 4; j ++){
    		switch(j){
    		case 0: 
    			for (int i = 0; i < SS; i ++){
    				//input_x[i*4+j] = 1;
    				input_x_mat.put(i, j, 1);
    				//input_y[i*4+j] = 1;
    				input_y_mat.put(i, j, 1);
    			}
    			break;
    		case 1: 
    			for (int i = 0; i < SS; i ++){
    				//input_x[i*4+j] = WW * Math.pow(calib.get(i).x, 1) ;
    				input_x_mat.put(i, j, WW * Math.pow(calib.get(i).x, 1));
    				//input_y[i*4+j] = WW * Math.pow(calib.get(i).x, 1);
    				input_y_mat.put(i, j, WW * Math.pow(calib.get(i).x, 1));
    			}
    			break;
    		case 2:
    			for (int i = 0; i < SS; i ++){
    				//input_x[i*4+j] = WW * Math.pow(calib.get(i).y, 1) ;
    				input_x_mat.put(i, j, WW * Math.pow(calib.get(i).y, 1));
    				//input_y[i*4+j] = WW * Math.pow(calib.get(i).y, 1);
    				input_y_mat.put(i, j, WW * Math.pow(calib.get(i).y, 1));
    			}
    			break;
    		case 3:
    			for (int i = 0; i < SS; i ++){
    				//input_x[i*4+j] = WW * Math.pow(calib.get(i).x, 2) ;
    				input_x_mat.put(i, j, WW * Math.pow(calib.get(i).x, 2));
    				//input_y[i*4+j] = WW * Math.pow(calib.get(i).y, 2);
    				input_y_mat.put(i, j, WW * Math.pow(calib.get(i).y, 2));
    			}
    			break;
    		default:break;
    		}
    	}
    }
 
    public Point gazetrack(CvCameraViewFrame inputFrame){
    	//end_time = Core.getTickCount();
    	//if (time_mark != 0){
    	//	 time = (end_time - start_time)/Core.getTickFrequency()/1000;
    	//}
    	//start_time = end_time;
    	//time_mark ++;
    	Point eye = CaculateEyecenterAppro(inputFrame);
    	Log.e(TAG, "eye area:  x  " + Double.toString(eye.x) + "  y  " + Double.toString(eye.y));
    	
        caculate_eye_center(1);
        Log.e(TAG, "pupil area:  x  " + Double.toString(box.center.x) + "  y  " + Double.toString(box.center.y));
        
        Point s = caculate_spot_center(1,1);
        Log.e(TAG, "spot area:  x  " + Double.toString(s.x) + "  y  " + Double.toString(s.y));
        
        calculate_pcr(1);
        Log.e(TAG, "pcr:  x  " + Double.toString(pcrvector.x) + "  y  " + Double.toString(pcrvector.y));
        
        Point position = new Point(0,0);
        if (Math.abs(pcrvector.x) < 30 && Math.abs(pcrvector.y) < 30){
        	position = new Point((int) (svd_x_coefficient_right.get(0, 0)[0]
        			+ svd_x_coefficient_right.get(1, 0)[0] * pcrvector.x
        			+ svd_x_coefficient_right.get(2, 0)[0] * pcrvector.y
        		    + svd_x_coefficient_right.get(3, 0)[0] * pcrvector.x * pcrvector.x
        		    )
        		    , 			 (int) (svd_y_coefficient_right.get(0, 0)[0]
                			+ svd_y_coefficient_right.get(1, 0)[0] * pcrvector.x
                			+ svd_y_coefficient_right.get(2, 0)[0] * pcrvector.y
                		    + svd_y_coefficient_right.get(3, 0)[0] * pcrvector.y * pcrvector.y
                		    )
                		    );
        	//position = GuassSmooth(position);
        	//double x = position.x;
        	//double y = position.y;
        }
        //int x = (int) (svd_x_coefficient_right.get(0, 0)[0]
    	//		+ svd_x_coefficient_right.get(1, 0)[0] * calib_mat.get(1).x
    	//		+ svd_x_coefficient_right.get(2, 0)[0] * calib_mat.get(1).y
    	//	    + svd_x_coefficient_right.get(3, 0)[0] * calib_mat.get(1).x * calib_mat.get(1).x
    	//	    );
    	//int y = (int) (svd_y_coefficient_right.get(0, 0)[0]
    	//		+ svd_y_coefficient_right.get(1, 0)[0] * calib_mat.get(1).x
    	//		+ svd_y_coefficient_right.get(2, 0)[0] * calib_mat.get(1).y
    	//	    + svd_y_coefficient_right.get(3, 0)[0] * calib_mat.get(1).y * calib_mat.get(1).y
    	//	    );
    	//new Point(x,y);
        Log.e(TAG, "Target Point:  x  " + Double.toString(position.x) + "  y  " + Double.toString(position.y));
        return position;
    }
    
    public Point GuassSmooth(Point position){
    	
    	point_count ++;
    	//////////////AddNewPoint(newPoint)
    	//if (point_count >= windowSize){
    	//	for (int cp = 0; cp < (windowSize - 1); cp ++){
    	//		GT.set(cp, GT.get(cp+1));
    	//	}	
    	//	GT.set(windowSize - 1, position);
    	//}
    	//else if(point_count == 1) GT.add(position);
    	//else GT.set(windowSize - 1, position);
    	//////////////////////////AddNewPoint(newPoint)
    	
    	if (point_count > 1){
    		/////////////////GetMaxDistanceOnWindow(GTPoint)
    		//int sumx = 0;
    		//int sumy = 0;
    		//double max_dist = 0;
    		//for (int i = 0; i < point_count; i ++){
    		//	sumx += GT.get(i).x;
    		//	sumy += GT.get(i).y;
    		//}
    		//Point centroid = new Point(sumx/point_count,sumy/point_count);
    		//int max_distance  = 80;
    		//double dist = 0;
    		//for(int j = 0; j < point_count; j++){
    		//	dist = Math.sqrt(Math.pow(GT.get(j).x - centroid.x, 2) 
    		//			+ Math.pow(GT.get(j).y - centroid.y, 2));
    		//	if (dist > max_dist) max_dist = dist;
    		//}
            /////////////////GetMaxDistanceOnWindow(GTPoint)
    		
    		if (getMaxDistance(GT) < max_dispersion){
    			////////////CalculateVelocity();
    			//Point new_point = GT.get(point_count - 1);
    			//Point old_point = GT.get(point_count - 2);
    			//dist_pixel = Math.sqrt(Math.pow(new_point.x - old_point.x,2) 
    			//		+ Math.pow(new_point.y - old_point.y,2));
    			//dist2mm = dist_pixel * Width_p / Width;
    			//dist_Degrees = Math.atan(dist2mm/10/dist_to_screen)*180/Math.PI;
    			//angularVelocity = dist_Degrees/time;
    			//
    			///////////////AddNewVelocity(angularVelocity);
    			//if (point_count == windowSize){
    			//	for (int i = 0; i < (windowSize - 1); i++){
    			//		Velocity.set(i, Velocity.get(i+1));
    			//	}
    			//	Velocity.add(angularVelocity);
    			//}
    			//else Velocity.set(point_count-1,angularVelocity);
    			////////////////AddNewVelocity(angularVelocity);
    			/////CalculateVelocity();
    			
    			calculateVelocity();
    			if(Velocity.get(point_count - 1) > max_AngularSpeed){
    				///////////////noFixation(Point newPoint);
    				//for (int cp =0; cp <10; cp++)
    				//{
    				//	GT.set(cp, new Point(0,0));
    				//}
    				//point_count = 0;
    				//point_count ++;
    				//   PointCount++;
    				//GT.set(point_count - 1, position);
    				///////////////noFixation(Point newPoint);
    				noFixation(position);
    			}
    		}
    		else{
    			///////////////noFixation(Point newPoint);
      			//for (int cp =0; cp <10; cp++)
      			//{
      			//	GT.set(cp, new Point(0,0));
      			//}
      			//point_count = 0;
      			//point_count ++;
      			//   PointCount++;
      			//GT.set(point_count - 1, position);
      			///////////////noFixation(Point newPoint);
    			noFixation(position);
    		}
    		
    	}
    	else {//point_count = 1;
    		for (int cp = 0; cp < 29 ; cp++)
			{
				GT.add(new Point(0,0));
				Velocity.add((double) 0);
				
			}
			point_count = 0;
			point_count ++;
			GT.set(point_count - 1, position);
    	}
    	///////////////////////////////Gauss smooth
    	double std_dev = smooth_level/5;
    	Point sum = new Point(0,0);
    	double sum_weight = 0;
    	double weight;
    	double mean = 0;
    	for (int i = 0; i < point_count; i++){
    		weight = get_GaussWeight(point_count - i - 1, mean, std_dev);
    		sum.x = GT.get(i).x * weight;
    		sum.y = GT.get(i).y * weight;
    		sum_weight = sum_weight + weight;
    	}
    	return new Point(sum.x/sum_weight, sum.y/sum_weight);
    }
    
    public void add_new_point(Point newPoint){
    	point_count ++;
    	if (point_count > windowSize){
    		for (int i = 0; i <(windowSize - 1); i ++){
    			GT.set(i, GT.get(i+1));
    		}
    		GT.set(windowSize - 1, newPoint);
    	}
    	else GT.set(point_count - 1, newPoint);
    }
    
    public double getMaxDistance(List<Point> GT){
    	int sumx = 0;
    	int sumy = 0;
    	for (int i = 0; i < point_count; i ++){
    		sumx = (int) (sumx + GT.get(i).x);
    		sumy = (int) (sumy + GT.get(i).y);
    	}
    	Point centroid = new Point(sumx/point_count,sumy/point_count);
    	
    	double maxdist = 0;
    	double dist = 0;
    	for (int i = 0; i < point_count; i++){
    		dist = Math.sqrt(Math.pow(GT.get(i).x - centroid.x, 2)
    				+ Math.pow(GT.get(i).y - centroid.y, 2));
    		if (dist > maxdist) maxdist = dist;
    		
    	}
    	return maxdist;
    }
    
    public void calculateVelocity(){
    	Point newPoint = GT.get(point_count - 1);
    	Point oldPoint = GT.get(point_count - 2);
    	
    	dist_pixel = Math.sqrt(Math.pow(newPoint.x - oldPoint.x, 2)
    			+ Math.pow(newPoint.y - oldPoint.y, 2));
    	dist2mm = dist_pixel * Width_p / Width;
    	
    	dist_Degrees = Math.atan(dist2mm/10/dist_to_screen)*180/Math.PI;
		angularVelocity = dist_Degrees/time;
		add_new_velocity(angularVelocity);
    }
    
    public void add_new_velocity(double angularVelocity){
    	if (point_count > windowSize){
    		for (int i = 1; i < windowSize - 1; i ++){
    			Velocity.set(i, Velocity.get(i+1));
    		}
    		Velocity.set(windowSize - 1, angularVelocity);
    	}
    	else Velocity.set(point_count - 2, angularVelocity);
    }
    
    public void noFixation(Point newPoint){
    	for (int i = 0; i < 10; i++){
    		GT.set(i, new Point(0,0));
    	}
    	point_count = 0;
    	add_new_point(newPoint);
    }

    public double get_GaussWeight(double count, double mean, double std_dev){
    	double scale = 1/(Math.sqrt(2*Math.PI)*std_dev)*Math.exp(-Math.pow(0 - mean, 2)/(2*Math.pow(std_dev, 2)));
    	return (1/(Math.sqrt(2*Math.PI)*std_dev*scale)*Math.exp(-Math.pow(-count - mean, 2)/(2*Math.pow(std_dev, 2))));
    }

    public Point gazetrack_1(){
    	double sum_x = 0;
		double sum_y = 0;
		int calib_count = 0;
		Point pcr;
    	for (int i = 0; i < 5; i ++){
    		CaculateEyecenterAppro(GazeTrack_mat.get(i));
    		caculate_eye_center(1); 
    		caculate_spot_center(1,1);
    		calculate_pcr(1);
    		
    		if (eyecenter_acc.x != -1000){
    			if ( Math.abs(pcrvector.x) < 100 && Math.abs(pcrvector.y) < 100 ){
    				sum_x = pcrvector.x + sum_x;
    				sum_y = pcrvector.y + sum_y;
    				calib_count ++;
    			}
    		}
    	}
    	if (calib_count != 0){
    		pcr = new Point(sum_x / calib_count, sum_y / calib_count);
		}
		else pcr = new Point(0,0);
    	Point position = new Point(100,100);
        if (Math.abs(pcr.x) < 100 && Math.abs(pcr.y) < 100){
        	position = new Point((int) (svd_x_coefficient_right.get(0, 0)[0]
        			+ svd_x_coefficient_right.get(1, 0)[0] * pcr.x
        			+ svd_x_coefficient_right.get(2, 0)[0] * pcr.y
        		    + svd_x_coefficient_right.get(3, 0)[0] * pcr.x * pcr.x
        		    )
        		    , 			 (int) (svd_y_coefficient_right.get(0, 0)[0]
                			+ svd_y_coefficient_right.get(1, 0)[0] * pcr.x
                			+ svd_y_coefficient_right.get(2, 0)[0] * pcr.y
                		    + svd_y_coefficient_right.get(3, 0)[0] * pcr.y * pcr.y
                		    )
                		    );
        }
        return position;
        //return GuassSmooth(position);
    }
    
    public void press_track_button(){
    	Track_flag = !Track_flag; 
    	eye_flag = false;
    	calib_flag = false;
    	
    	gaze_index = 0;
        GazeTrack_mat = new ArrayList<CvCameraViewFrame>();
        gaze_mat = new ArrayList<Point>();
    	gaze_index = 0;
        point_count = 0;
        time_mark = 0;
    	GT = new ArrayList<Point>();
    	Velocity = new ArrayList<Double>();
    }
    
    public void press_eyeregion_button(){
    	eye_flag = !eye_flag;
    	Track_flag = true;
    	calib_flag = false;
    	box = new RotatedRect();
    }
    
    public void press_calibration_button(){
    	calib_flag = !calib_flag;
    	eye_flag = false;
    	Track_flag = true;
    	
    	calibration_r1 = 20;
    	calibration_r2 = 20;
    	calibration_index0 = 1;
    	calibration_index1 = 0;
    	calibration_index2 = 0;   
    	calibration_mat = new ArrayList<CvCameraViewFrame>(); 
    }
    
    double sum_x = 0;
	double sum_y = 0;
	int calib_count = 0;
    public Mat Init_calib_show(Mat output,CvCameraViewFrame inputFrame){
    	if (calibration_index1 < 15){
			if (calibration_index2 == 0){
				Core.circle(output, new Point(calibration_x, calibration_y)
				, (int) calibration_r1, new Scalar(255,255,255),-1,8,0);
				Core.circle(output, new Point(calibration_x, calibration_y)
				, (int) calibration_r2, new Scalar(0,0,0),2,8,0);
				calibration_index2 = 1;
			}
			else if(calibration_index2 == 1){
				Core.circle(output,new Point(calibration_x, calibration_y)
				,(int) (calibration_r1 - 10),new Scalar(40,40,40),-1,8,0);
				calibration_index2 = 0;	
				
			}
			if (calibration_index1 < 10)
			{
				calibration_x+=(calibration_screenx[calibration_index0]-calibration_screenx[calibration_index0 - 1])/10;
				calibration_y+=(calibration_screeny[calibration_index0]-calibration_screeny[calibration_index0 - 1])/10;
				calibration_r1+=(34-calibration_r2)/34;
				calibration_index2 = 1;
				calibration_index1 ++;
			}
			else if (calibration_index1 < 14){
				calibration_x = calibration_screenx[calibration_index0];
    			calibration_y = calibration_screeny[calibration_index0];
    			calibration_r1 = 30;
    			calibration_index1 ++;
    			//calibration_mat.add(inputFrame);
    			Point eye = CaculateEyecenterAppro(inputFrame);
    			Log.e(TAG, "eye area:  x  " + Double.toString(eye.x) + "  y  " + Double.toString(eye.y));
    			
    			//CaculateEyecenterAppro(frame);
    			caculate_eye_center(1); 
    			Log.e(TAG, "pupil area:  x  " + Double.toString(box.center.x) + "  y  " + Double.toString(box.center.y));
    			
        		Point s = caculate_spot_center(1,1);
        		Log.e(TAG, "spot area:  x  " + Double.toString(s.x) + "  y  " + Double.toString(s.y));
        		
        		calculate_pcr(1);
        		Log.e(TAG, "pcr:  x  " + Double.toString(pcrvector.x) + "  y  " + Double.toString(pcrvector.y));
        		if (eyecenter_acc.x != -1000){
        			if ( Math.abs(pcrvector.x) < 120 && Math.abs(pcrvector.y) < 120 ){
        				sum_x = pcrvector.x + sum_x;
        				sum_y = pcrvector.y + sum_y;
        				calib_count ++;
        			}
        		}
        		
			}
			else{
				calibration_index1 = 0;
				calibration_index0 = calibration_index0 + 1;
				calibration_r1 = 20;
	        	calibration_r2 = 20;
	        	//calibration_mat.add(inputFrame);
	        	Point eye = CaculateEyecenterAppro(inputFrame);
    			Log.e(TAG, "eye area:  x  " + Double.toString(eye.x) + "  y  " + Double.toString(eye.y));
    			
    			//CaculateEyecenterAppro(frame);
    			caculate_eye_center(1); 
    			Log.e(TAG, "pupil area:  x  " + Double.toString(box.center.x) + "  y  " + Double.toString(box.center.y));
    			
        		Point s = caculate_spot_center(1,1);
        		Log.e(TAG, "spot area:  x  " + Double.toString(s.x) + "  y  " + Double.toString(s.y));
        		
        		calculate_pcr(1);
        		Log.e(TAG, "pcr:  x  " + Double.toString(pcrvector.x) + "  y  " + Double.toString(pcrvector.y));
        		if (eyecenter_acc.x != -1000){
        			if ( Math.abs(pcrvector.x) < 120 && Math.abs(pcrvector.y) < 120 ){
        				sum_x = pcrvector.x + sum_x;
        				sum_y = pcrvector.y + sum_y;
        				calib_count ++;
        			}
        		}
        		if (calib_count != 0){
        			calib_mat.add(new Point(sum_x / calib_count, sum_y / calib_count));
        		}
        		else calib_mat.add(new Point(0,0));
        		sum_x = 0;
        		sum_y = 0;
        		calib_count = 0;
			}
		}
		return output;
    }
    
    protected void dialog_exit(){
    	dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit")
		.setMessage("Are you sure to end this app?")
		.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
			@Override  
	         public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件  
				dialog.dismiss();
	        	Log.i(TAG, "exit");
	        	finish();
	         }  
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {//添加返回按钮 
			@Override  
	         public void onClick(DialogInterface dialog, int which) {//响应事件  
				//press_calibration_button();
				dialog.dismiss();
	         	Log.i(TAG, "stay");
	         }  
	     });
        dialog.create().show();
    }
    
    protected void dialog_calib2track(){
    	dialog =  new AlertDialog.Builder(this);
    	dialog.setTitle("Calibration finished")
    	.setMessage("Press 'Yes' to start the tracking process;Press 'No' to restart the calibration process.")
    	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				press_track_button();
				Log.i(TAG, "Tracking");
			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				eye_flag = false;
		    	Track_flag = true;
		    	
		    	calibration_r1 = 20;
		    	calibration_r2 = 20;
		    	calibration_index0 = 1;
		    	calibration_index1 = 0;
		    	calibration_index2 = 0;   
		    	calibration_mat = new ArrayList<CvCameraViewFrame>(); 
				Log.i(TAG, "calibration");	
			}
		});
    	dialog.create().show();
    	
    	
    	

    }
    
    
/////////////////////////////////////////////////////////////////////////////////////////////////    
	public static MediaPlayer getMediaPlayer(){
		if (mediaPlayer == null) {
			synchronized (Tutorial1Activity.class) {
				if (mediaPlayer == null) {
					mediaPlayer = new MediaPlayer();
				}
			}
		}
		return mediaPlayer;
	}
    //////////////////////////////////////////////////////////////////////////////////////////////
}