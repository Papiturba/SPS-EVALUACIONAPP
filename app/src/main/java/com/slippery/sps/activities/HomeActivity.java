package com.slippery.sps.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.slippery.sps.R;
import com.slippery.sps.fragments.ChatsFragment;
import com.slippery.sps.fragments.FiltrosFragment;
import com.slippery.sps.fragments.HomeFragment;
import com.slippery.sps.fragments.PerfilFragment;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new HomeFragment());

    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.itemHome){
                        //FRAGMENT HOME
                        openFragment(new HomeFragment());
                    }
                    else if (item.getItemId() == R.id.itemChats){
                        //FRAGMENT CHATS
                        openFragment(new ChatsFragment());
                    }
                    else if (item.getItemId() == R.id.itemFiltros){
                        //FRAGMENT FILTROS
                        openFragment(new FiltrosFragment());
                    }
                    else if (item.getItemId() == R.id.itemPerfil){
                        //FRAGMENT PERFIL
                        openFragment(new PerfilFragment());
                    }
                    return true;
                }
            };

}


