package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.Toast;

import com.example.chatapp.Adapter.FragmentAdapter;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if(item.getItemId() == R.id.logout){
            mAuth.signOut();
            finish();
        }else if(item.getItemId() == R.id.settings){
            Toast.makeText(this, "Setting is clicked", Toast.LENGTH_SHORT).show();
        }else if (item.getItemId() == R.id.group){
            Toast.makeText(this, "Group is clicked", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.profile){
            //Intent intent = new Intent(this, ProfileActivity.class);
            //startActivity(intent);
            Toast.makeText(this, "Profile is clicked", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}