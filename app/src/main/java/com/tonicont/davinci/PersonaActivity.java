package com.tonicont.davinci;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tonicont on 14/03/14.
 */
public class PersonaActivity extends Activity {
    private ImageButton btn_atras;
    private TextView lbl_nombre;
    private TextView lbl_twitter;
    private TextView lbl_descripcion;
    private ImageView img_foto;
    private ProgressDialog pDialog;
    private Bitmap imagen;
    private int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.persona);

        btn_atras = (ImageButton)findViewById(R.id.btn_atras);
        lbl_nombre = (TextView)findViewById(R.id.lbl_nombre);
        lbl_descripcion = (TextView)findViewById(R.id.lbl_descripcion);
        lbl_twitter = (TextView)findViewById(R.id.lbl_twitter);
        img_foto = (ImageView)findViewById(R.id.img_foto);

        Bundle extras = getIntent().getExtras();
        posicion = extras.getInt("posicion");
        lbl_nombre.setText(extras.getString("nombre"));
        lbl_descripcion.setText(extras.getString("descripcion"));
        lbl_twitter.setText(extras.getString("twitter"));
        new DownloadFoto().execute();

         btn_atras.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(PersonaActivity.this,MainActivity.class);
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
            pDialog = new ProgressDialog(PersonaActivity.this);
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

                imageUrl = new URL("http://davinci.hostinazo.com/images/foto" + posicion + ".jpg");
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
