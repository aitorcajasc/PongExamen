package com.riberadeltajo.pongexamen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout c=findViewById(R.id.c);
        c.setBackground(getDrawable(R.drawable.fondo));

        Button b=findViewById(R.id.jugar);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(), ActividadJuego.class);
                v.getContext().startActivity(i);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nombre) {
            Toast.makeText(this, "Aitor Cajas Calvo", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.salir) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}