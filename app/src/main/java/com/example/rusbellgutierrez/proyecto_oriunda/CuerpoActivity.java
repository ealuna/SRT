package com.example.rusbellgutierrez.proyecto_oriunda;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v4.app.DialogFragment;


public class CuerpoActivity extends AppCompatActivity {

    //elementos para crear un drawer layout
    ListView lista_drawer;
    NavigationView nav;
    DrawerLayout drawer;
    //toolbar
    Toolbar toolbar;
    //instanciamos el alertdialog
    //AlertDialog alerta;
    Dialogo_Alerta alerta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuerpo);

        //lista_drawer =(ListView)findViewById(R.id.lista_drawer);
        drawer =(DrawerLayout)findViewById(R.id.drawer_layout);
        nav =(NavigationView)findViewById(R.id.nav);
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //color al title
        toolbar.setTitleTextColor(Color.WHITE);
        //soporte para toolbar
        setSupportActionBar(toolbar);
        //muestra el nombre de la apliación
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //habilita el boton de la apliacion
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //accion del boton en toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"presiono",Toast.LENGTH_SHORT).show();
                //revisar llamado
                new Dialogo_Alerta().show(getSupportFragmentManager(), "SimpleDialog");
            }
        });

        //redirige a los fragmente, lógica para toda la funcionalidad
        nav.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        switch (menuItem.getItemId()) {
                            case R.id.menu_seccion_1:
                                fragment = new Fragment1();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_seccion_2:
                                fragment = new Fragment2();
                                fragmentTransaction = true;
                                break;
                            case R.id.menu_opcion_1:
                                Log.i("NavigationView", "Pulsada opción 1");
                                break;
                            case R.id.menu_opcion_2:
                                Log.i("NavigationView", "Pulsada opción 2");
                                break;
                        }

                        if(fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_frame, fragment)
                                    .commit();

                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        drawer.closeDrawers();

                        return true;
                    }
                });

    }


}