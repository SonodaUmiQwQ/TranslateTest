package com.example.sonodaumi.translatetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = "";

        final EditText wordText = (EditText)findViewById(R.id.word);
        final TextView resultText = (TextView)findViewById(R.id.result);
        Button btnSearch = (Button)findViewById(R.id.search);

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(wordText.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "输入为空,请重新输入!", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String path = "http://dict-co.iciba.com/search.php?word=" +  wordText.getText().toString() + "&submit=查询";
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(path);
                            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                            httpURLConnection.setRequestMethod("GET");
                            int code = httpURLConnection.getResponseCode();
                            if(code == 200){
                                InputStream in = httpURLConnection.getInputStream();
                                InputStreamReader isr = new InputStreamReader(in,"utf-8");
                                BufferedReader reader = new BufferedReader(isr);
                                String line = null;
                                int i = 0;
                                while((line = reader.readLine()) != null) {
                                    i++;
                                    if(i == 22) {
                                        content += line;
                                    }
                                }
                                resultText.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        content = content.replaceAll("&nbsp;","\n");
                                        content = content.replaceAll("<br />","");
                                        resultText.setText(content);
                                        content = "";
                                    }
                                });
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        });
    }

}
