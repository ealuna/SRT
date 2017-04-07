package com.example.rusbellgutierrez.SRT.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rusbellgutierrez.SRT.Adapter.RecyclerAdapter;
import com.example.rusbellgutierrez.SRT.Clases.Clase_FeedItem;
import com.example.rusbellgutierrez.SRT.Interfaces.OnFragmentListener;
import com.example.rusbellgutierrez.SRT.R;
import com.example.rusbellgutierrez.SRT.SQL.SQL_Helper;
import com.example.rusbellgutierrez.SRT.SQL.SQL_Sentencias;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Created by Russbell on 31/03/2017.
 */

public class Fragment_detBusq extends Fragment implements OnFragmentListener,SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private ArrayList<Clase_FeedItem> feed=new ArrayList<Clase_FeedItem>();
    private Cursor cursor;
    private SQL_Helper helper;
    private SQLiteDatabase db;
    private RecyclerAdapter recyclerAdapter;

    private ProgressBar pgb;

    SQL_Sentencias sql=new SQL_Sentencias();
    private Handler handler = new Handler();

    public Fragment_detBusq(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    OnFragmentListener mCallback=null;

    public static Fragment_detBusq newInstance(Bundle arguments){
        Fragment_detBusq f = new Fragment_detBusq();
        if(arguments != null){
            f.setArguments(arguments);
            Log.d("RESULTADO", "Tenemos data: "+f);
        }else {
            Log.d("RESULTADO", "No tenemos data");}
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //para poblar un recyclerview se emplea viewgroup en lugar de view
        ViewGroup viewGroup=(ViewGroup)inflater.inflate(R.layout.fragment_busqueda,container,false);
        recyclerView=(RecyclerView)viewGroup.findViewById(R.id.recycler_view);
        pgb=(ProgressBar) viewGroup.findViewById(R.id.pg_bar);

        setHasOptionsMenu(true);

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                loadDatabase();
            }
        });

        return viewGroup;
    }

    private class asincronoLoad extends AsyncTask<Void,Integer,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //progressBar.setVisibility(View.VISIBLE);
                    loadDatabase();

                }
            });

            return true;
        }

        protected void onPreExecute() {
            pgb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                pgb.setVisibility(View.GONE);
            }
        }
    }

    public void loadDatabase(){
        /*helper=new SQL_Helper(getActivity());
        db=helper.getReadableDatabase();
        cursor=db.rawQuery("select a.codbarra, a.idarticulo, a.nombre, c.almacen, c.cantidad, c.estado from carga c, articulo a where a.idarticulo=c.idarticulo and c.estado <> '0' and c.estado <> 'Completo'",null);

        if (cursor!=null){
            if (cursor.moveToFirst()){
                do{
                    Clase_FeedItem feedItem=new Clase_FeedItem();
                    feedItem.setCodbar(cursor.getString(0));
                    feedItem.setCodprod(cursor.getString(1));
                    feedItem.setNomprod(cursor.getString(2));
                    feedItem.setAlmprod(cursor.getString(3));
                    feedItem.setCanprod(cursor.getString(4));
                    feedItem.setEstado(cursor.getString(5));

                    feed.add(feedItem);
                }while (cursor.moveToNext());
            }
        }*/
        pgb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {

                boolean estado=sql.detalleRecycler(helper,getActivity(),feed);
                //SI "estado" ES TRUE, EJECUTA EL HANDLER
                //if (feed.size()==207)
                if (estado){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            pgb.setVisibility(View.GONE);
                            int count=0;
                            if (recyclerAdapter!=null)
                                count=recyclerAdapter.getItemCount();
                            Log.i("CANTIDAD DE ROWS","ES "+count);

                        }
                    });
                }

            }
        }).start();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerAdapter=new RecyclerAdapter(getActivity(),feed);
        //llamamos al metodo set anteriormente declarado
        recyclerAdapter.setOnFragmentListener(new OnFragmentListener() {
            //sobreescribimos el metodo para pasar bundle, el fragment destino lo recibe durante onResume
            @Override
            public void onFragmentListener(Bundle parameters) {
                mCallback.onSetTitle("Productos");

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment_Producto fp=new Fragment_Producto();
                //Log.i("BUNDLE"," DATA ANTES "+parameters.g+", "+parameters.getString("codprod")+", "+parameters.getString("nomprod")+", "+parameters.getString("almprod")+", "+parameters.getString("canprod"));
                fp.setArguments(parameters);

                ft.replace(R.id.content_frame, fp);
                ft.addToBackStack(null);
                ft.commit();

                Log.i("BUNDLE"," DATA "+parameters.getString("codbarra")+", "+parameters.getString("idarticulo")+", "+parameters.getString("nombre")+", "+parameters.getString("almacen")+", "+parameters.getString("cantidad"));
            }

            @Override
            public void onSetTitle(String title) {

            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            //se ata el fragment al activity cuerpo
            mCallback = (OnFragmentListener) context;
        }catch(Exception e){
            Log.e("ExampleFragment", "El Activity debe implementar la interfaz OnFragmentListener");
        }
    }

    @Override
    public void onFragmentListener(Bundle parameters) {
        /*mCallback.onSetTitle("Productos");

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment_Producto fp=new Fragment_Producto();
        fp.setArguments(parameters);
        ft.replace(R.id.content_frame, fp);
        ft.addToBackStack(null);
        ft.commit();*/

    }

    @Override
    public void onSetTitle(String title) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        //super.onCreateOptionsMenu(menu,inflater);

        /*MenuItem searchitem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchitem);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        TextView searchText = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        searchText.setTextColor(Color.parseColor("#000000"));
        searchText.setHintTextColor(Color.parseColor("#000000"));
        searchText.setBackgroundColor(Color.WHITE);
        searchText.setHint("Escriba...");
        searchView.setOnQueryTextListener(this);*/


    //}

        //comienza la implementacion del searchview
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        TextView searchText = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        searchText.setTextColor(Color.parseColor("#000000"));
        searchText.setHintTextColor(Color.parseColor("#000000"));
        searchText.setBackgroundColor(Color.WHITE);
        searchText.setHint("Escriba...");

        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        recyclerAdapter.setFilter(feed);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Clase_FeedItem> feedList = filter(feed, newText);
        //if (feedList.size() > 0) {
            recyclerAdapter.setFilter(feedList);
            return true;
        /*} else {
            Toast.makeText(getActivity(), "No se encontró", Toast.LENGTH_SHORT).show();
            return false;
        }*/


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<Clase_FeedItem> filter(List<Clase_FeedItem> models, String query) {
        query = query.toLowerCase();
        //this.recyclerAdapter.searchText=query;

        final List<Clase_FeedItem> feedList = new ArrayList<>();
        for (Clase_FeedItem model : models) {
            final String codbarra = model.getCodbar().toLowerCase();
            final String codprod = model.getCodprod().toLowerCase();
            final String nomprod = model.getNomprod().toLowerCase();
            final String almprod = model.getAlmprod().toLowerCase();
            final String estado = model.getEstado().toLowerCase();

            if (codbarra.contains(query)) {
                feedList.add(model);
            }else if (codprod.contains(query)){
                feedList.add(model);
            }else if (nomprod.contains(query)){
                feedList.add(model);
            }else if (almprod.contains(query)){
                feedList.add(model);
            }else if (estado.contains(query)){
                feedList.add(model);
            }
        }
        /*recyclerAdapter= new RecyclerAdapter(getActivity(),feedList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//revisar getActivity o getContext
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
        recyclerAdapter.setOnFragmentListener(new OnFragmentListener() {
            //sobreescribimos el metodo para pasar bundle, el fragment destino lo recibe durante onResume
            @Override
            public void onFragmentListener(Bundle parameters) {
                mCallback.onSetTitle("Productos");

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment_Producto fp=new Fragment_Producto();
                //Log.i("BUNDLE"," DATA ANTES "+parameters.g+", "+parameters.getString("codprod")+", "+parameters.getString("nomprod")+", "+parameters.getString("almprod")+", "+parameters.getString("canprod"));
                fp.setArguments(parameters);

                ft.replace(R.id.content_frame, fp);
                ft.addToBackStack(null);
                ft.commit();

                Log.i("BUNDLE"," DATA "+parameters.getString("codbarra")+", "+parameters.getString("idarticulo")+", "+parameters.getString("nombre")+", "+parameters.getString("almacen")+", "+parameters.getString("cantidad"));
            }

            @Override
            public void onSetTitle(String title) {

            }
        });*/

        return feedList;
    }
}