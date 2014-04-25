package com.tonicont.davinci;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDoneException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

public class GenteActivity extends ListActivity implements View.OnClickListener {
    private Vector<String> datos;
    private ProgressDialog pDialog;
    private ArrayList<Gente> personas;
    private ArrayList<Bitmap> imagenes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gente);
        ImageButton btn_atras = (ImageButton)findViewById(R.id.btn_atras);
        btn_atras.setOnClickListener(this);
        datos = new Vector<String>();
        personas = new ArrayList<Gente>();
        imagenes = new ArrayList<Bitmap>();
        new DownloadGente().execute();




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
        textView.setText(lista.elementAt(position));
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
        for(int i=0;i<personas.size();i++){
            datos.add(personas.get(i).getNombre() + " " + personas.get(i).getApellidos());
            URL imageUrl = null;
            HttpURLConnection conn = null;

            try {

                imageUrl = new URL("http://davinci.hostinazo.com/images/foto" + i + ".jpg");
                conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                Bitmap imagen = BitmapFactory.decodeStream(conn.getInputStream());
                imagenes.add(imagen);

            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }

    private class DownloadGente extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GenteActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            Gente g = new Gente();
            personas = g.getGente();
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
                    setListAdapter(new MiAdaptador(GenteActivity.this,datos));
                }
            });
        }
    }

    @Override protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Intent i = new Intent(this,PersonaActivity.class);
        i.putExtra("nombre",personas.get(position).getNombre() + " " + personas.get(position).getApellidos());
        i.putExtra("twitter",personas.get(position).getTiwtter());
        i.putExtra("descripcion",personas.get(position).getDescripcion());
        i.putExtra("posicion",position);
        startActivity(i);
    }
}
