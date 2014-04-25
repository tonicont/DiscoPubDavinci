package com.tonicont.davinci;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tonicont on 27/03/14.
 */
public class FiestaActivity extends Activity {
    private ImageButton btn_atras;
    private TextView lbl_fecha;
    private TextView lbl_titulo;
    private TextView lbl_descripcion;
    private ImageView img_foto;
    private ProgressDialog pDialog;
    private Bitmap imagen;
    private String titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fiesta);

        btn_atras = (ImageButton)findViewById(R.id.btn_atras);
        lbl_fecha = (TextView)findViewById(R.id.lbl_fecha);
        lbl_descripcion = (TextView)findViewById(R.id.lbl_descripcion);
        lbl_titulo = (TextView)findViewById(R.id.lbl_titulo);
        img_foto = (ImageView)findViewById(R.id.img_foto);

        Bundle extras = getIntent().getExtras();
        titulo = extras.getString("titulo");
        lbl_fecha.setText(extras.getString("fecha"));
        lbl_descripcion.setText(extras.getString("descripcion"));
        lbl_titulo.setText(titulo);
        new DownloadFoto().execute();

        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FiestaActivity.this,Fiestas2Activity.class);
                startActivity(i);
            }
        });
    }

    private class DownloadFoto extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FiestaActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            URL imageUrl = null;
            HttpURLConnection conn = null;

            try {

                imageUrl = new URL("http://davinci.hostinazo.com/images/" + titulo + ".jpg");
                conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                imagen = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (IOException e) {

                e.printStackTrace();

            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url)
        {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    img_foto.setImageBitmap(imagen);
                }
            });
        }
    }

}
