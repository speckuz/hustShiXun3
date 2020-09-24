package com.feiyueve.snsdemo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.feiyueve.snsdemo.dao.PersonalInf;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class UpdateUserActivity extends AppCompatActivity {
    private MyApplication myApplication;


    private Calendar calendar;

    private EditText editTextUserPetName;
    private TextView textViewSign;
    private TextView textViewBirthday;
    private TextView textViewAddress;
    private EditText editTextSignature;
    private EditText editTextInterest;
    private EditText editTextProfession;
    private Spinner spinner;
    private Button buttonSaveUserInf;

    int year;
    int month;
    int day;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateuser);
        init();
        myApplication = (MyApplication)this.getApplication();

        calendar= Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;// 注意这里获取到的月份比现实中少一
        day = calendar.get(Calendar.DAY_OF_MONTH);// 注意参数：获取的是当前月的具体哪一天

        if(myApplication.getPersonalInf().getBirthday()!=null) {
            System.out.println(myApplication.getPersonalInf().getBirthday());
            String birthday[] = myApplication.getPersonalInf().getBirthday().split("-");
            year = Integer.parseInt(birthday[0]);
            month = Integer.parseInt(birthday[1]);
            day = Integer.parseInt(birthday[2]);

            System.out.println(year+month+day);
        }

        spinner.setPrompt("请选择您的学历:");
        ArrayList eduList = new ArrayList<CharSequence>();
        eduList.add("男");
        eduList.add("女");
        ArrayAdapter<CharSequence> eduAdapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,eduList);
        eduAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(eduAdapter);
        if(myApplication.getPersonalInf().getGender()!=null)
            spinner.setSelection(eduList.indexOf(myApplication.getPersonalInf().getGender()));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //这个方法里可以对点击事件进行处理
                //i指的是点击的位置,通过i可以取到相应的数据源
                String info=adapterView.getItemAtPosition(i).toString();//获取i所在的文本
                if(info.equals("女")){
                    myApplication.getPersonalInf().setGender("true");
                }else{
                    myApplication.getPersonalInf().setGender("false");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        textViewBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePicker_Dialog().show();
            }
        });

        buttonSaveUserInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextUserPetName.getText().toString().isEmpty())
                    Toast.makeText(getApplication(), "昵称不能为空", Toast.LENGTH_SHORT).show();
                else {
                    myApplication.getPersonalInf().setSignature(editTextSignature.getText().toString());
                    myApplication.getPersonalInf().setNickname(editTextUserPetName.getText().toString());
                    JSONObject userJSON = myApplication.getPersonalInf().personalInfToJson();
                    PersonalInf.updatePersonalInf(userJSON, myApplication.getLoginData().getUserKey());

                    Intent intent = new Intent();
                    intent.setClass(UpdateUserActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        textViewAddress.setText(myApplication.getPersonalInf().getLocation());
        editTextUserPetName.setText(myApplication.getPersonalInf().getNickname());
        editTextSignature.setText(myApplication.getPersonalInf().getSignature());
        if(myApplication.getPersonalInf().getBirthday()!=null) {
            textViewBirthday.setText(myApplication.getPersonalInf().getBirthday());
            textViewSign.setText(getSign());
        }

        textViewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UpdateUserActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {
        textViewAddress = (TextView)this.findViewById(R.id.textViewAddress);
        textViewSign = (TextView) this.findViewById(R.id.textViewSign);
        textViewBirthday = (TextView)this.findViewById(R.id.textViewBirthDay);
        editTextSignature = (EditText)this.findViewById(R.id.editTextSignature);
        editTextInterest = (EditText)this.findViewById(R.id.editTextInterest);
        editTextUserPetName = (EditText)this.findViewById(R.id.editTextUserPetName);
        editTextProfession = (EditText)this.findViewById(R.id.editTextProfession);
        buttonSaveUserInf = (Button)this.findViewById(R.id.buttonSaveUserInf);
        spinner = (Spinner)this.findViewById(R.id.spinnerGender);
    }

    private DatePickerDialog getDatePicker_Dialog() {
        DatePickerDialog dpd = new DatePickerDialog(UpdateUserActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        myApplication.getLoginData().setBirthYear(year);
                        myApplication.getLoginData().setBirthMonth(monthOfYear + 1);
                        myApplication.getLoginData().setBirthDay(dayOfMonth);
                        textViewBirthday.setText(myApplication.getLoginData().getBirthday());
                        myApplication.getPersonalInf().setBirthday(myApplication.getLoginData().getBirthday());
                        textViewSign.setText(getSign());
                    }
                }, year, month - 1, day);
        return dpd;
    }

    private String getSign() {
        if(myApplication.getPersonalInf().getBirthday()!=null) {
            String birthday[] = myApplication.getPersonalInf().getBirthday().split("-");
            year = Integer.parseInt(birthday[0]);
            month = Integer.parseInt(birthday[1]);
            day = Integer.parseInt(birthday[2]);
        }
        String sign = "";
        switch (month) {
            case 1:
                sign = day < 21 ? "摩羯座" : "水瓶座";
                break;
            case 2:
                sign = day < 20 ? "水瓶座" : "双鱼座";
                break;
            case 3:
                sign = day < 21 ? "双鱼座" : "白羊座";
                break;
            case 4:
                sign = day < 21 ? "白羊座" : "金牛座";
                break;
            case 5:
                sign = day < 22 ? "金牛座" : "双子座";
                break;
            case 6:
                sign = day < 22 ? "双子座" : "巨蟹座";
                break;
            case 7:
                sign = day < 23 ? "巨蟹座" : "狮子座";
                break;
            case 8:
                sign = day < 24 ? "狮子座" : "处女座";
                break;
            case 9:
                sign = day < 24 ? "处女座" : "天秤座";
                break;
            case 10:
                sign = day < 24 ? "天秤座" : "天蝎座";
                break;
            case 11:
                sign = day < 23 ? "天蝎座" : "射手座";
                break;
            case 12:
                sign = day < 22 ? "射手座" : "摩羯座";
                break;
        }
        return sign;
    }


}
