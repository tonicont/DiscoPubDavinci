package com.tonicont.davinci;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Created by tonicont on 27/03/14.
 */
public class Fiestas2Activity extends ListActivity implements View.OnClickListener{
    private Vector<String> datos;
    private ProgressDialog pDialog;
    private ArrayList<Fiesta> fiestas;
    private ArrayList<Bitmap> imagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fiestas2);
        ImageButton btn_atras = (ImageButton)findViewById(R.id.btn_atras);
        btn_atras.setOnClickListener(this);
        datos = new Vector<String>();
        fiestas = new ArrayList<Fiesta>();
        imagenes = new ArrayList<Bitmap>();
        new DownloadFiestas().execute();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_atras:{
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
            }
        }
    }


    public class MiAdaptador extends BaseAdapter {
        private final Activity actividad;
        private final Vector<String> lista;

        public MiAdaptador(Activity actividad, Vector<String> lista) {
            super();
            this.actividad = actividad;
            this.lista = lista;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = actividad.getLayoutInflater();
            View view = inflater.inflate(R.layout.elemento_lista, null,true);
            TextView textView =(TextView)view.findViewById(R.id.titulo);
            TextView txt_fecha = (TextView)view.findViewById(R.id.lbl_fecha);
            textView.setText(lista.elementAt(position));
            SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
            txt_fecha.setText(formateador.format(fiestas.get(position).getFecha()));
            ImageView imageView=(ImageView)view.findViewById(R.id.icono);
            imageView.setImageBitmap(imagenes.get(position));

            return view;
        }

        public int getCount() {
            return lista.size();
        }

        public Object getItem(int arg0) {
            return lista.elementAt(arg0);
        }

        public long getItemId(int position) {
            return position;
        }
    }
    private void cargarDatos(){
        for(int i=0;i<fiestas.size();i++){
            datos.add(fiestas.get(i).getTitulo());
            URL imageUrl = null;
            HttpURLConnection conn = null;

            try {

                imageUrl = new URL("http://davinci.hostinazo.com/images/" + fiestas.get(i).getTitulo() + ".jpg");
                conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                Bitmap imagen = BitmapFactory.decodeStream(conn.getInputStream());
                imagenes.add(imagen);

            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }

    private class DownloadFiestas extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Fiestas2Activity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Fiesta f = new Fiesta();
            fiestas = f.getFiestas();
            cargarDatos();
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
                    setListAdapter(new MiAdaptador(Fiestas2Activity.this,datos));
                }
            });
        }
    }

    @Override protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Intent i = new Intent(this,FiestaActivity.class);
        i.putExtra("titulo",fiestas.get(position).getTitulo());
        i.putExtra("descripcion",fiestas.get(position).getDescripcion());
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        Date fecha = fiestas.get(position).getFecha();
        i.putExtra("fecha",formateador.format(fecha));
        startActivity(i);
    }
}
