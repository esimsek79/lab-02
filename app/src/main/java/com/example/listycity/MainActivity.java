package com.example.listycity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // ui components - references to elements defined in activity_main.xml
    ListView cityList;              // scrollable list that displays all cities
    ArrayAdapter<String> cityAdapter; // bridge between dataList and cityList (connects data to UI)
    ArrayList<String> dataList;     //  actual list of city names

    // button references - connected to buttons in the XML layout
    Button addCityButton;           // "ADD CITY" button at the top
    Button deleteCityButton;        // "DELETE CITY" button at the top
    Button confirmButton;           // "CONFIRM" button at the bottom (initially hidden)

    EditText cityInput;             // text input field at bottom (initially hidden)

    int selectedPosition = -1;      // tracks which city is selected for deletion
    // -1 means "nothing selected" (like None)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // onCreate runs when the app starts (like __init__ in python)
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); // loads the XML layout file

        // handles system UI padding (status bar, navigation bar, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // connects java variables to XML elements using their IDs
        //  findViewById is like: element = document.getElementById("city_list")
        cityList = findViewById(R.id.city_list);
        addCityButton = findViewById(R.id.add_city_button);
        deleteCityButton = findViewById(R.id.delete_city_button);
        confirmButton = findViewById(R.id.confirm_button);
        cityInput = findViewById(R.id.city_input);

        // initialize the data - starting cities to display
        String[] cities = {"Edmonton", "Calgary", "Montréal"};

        // create an ArrayList (dynamic array) and add the starting cities
        dataList = new ArrayList<>();
        dataList.addAll(Arrays.asList(cities)); // convert array to list and add all items

        // create the adapter - connects the data (dataList) to the UI (cityList)
        // R.layout.content is the template for how each city should look
        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
        cityList.setAdapter(cityAdapter); // attach the adapter to the ListView

        // hide the input controls initially - only appear when "ADD CITY" is pressed
        cityInput.setVisibility(View.GONE);
        confirmButton.setVisibility(View.GONE);

        // EVENT LISTENER: what happens when "ADD CITY" button is clicked
        // setOnClickListener is like: button.onclick = function() { ... } in JavaScript
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the input field and confirm button at the bottom
                cityInput.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.VISIBLE);
                cityInput.setText("");          // Clear any previous text
                cityInput.requestFocus();       // Put cursor in the input field
            }
        });

        // EVENT LISTENER: what happens when "CONFIRM" button is clicked
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the text from input field, convert to String, and remove extra spaces
                String newCity = cityInput.getText().toString().trim();

                // only add the city if it's not empty (like: if new_city: in Python)
                if (!newCity.isEmpty()) {
                    dataList.add(newCity);              // add to the ArrayList
                    cityAdapter.notifyDataSetChanged(); // tell ListView to refresh and show new city

                    // hide the input controls again
                    cityInput.setVisibility(View.GONE);
                    confirmButton.setVisibility(View.GONE);
                    cityInput.setText("");              // clear the input field
                }
            }
        });

        // EVENT LISTENER: what happens when "DELETE CITY" button is clicked
        deleteCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only delete if a city is selected (selectedPosition is not -1)
                if (selectedPosition != -1) {
                    dataList.remove(selectedPosition);  // remove city at that index
                    cityAdapter.notifyDataSetChanged(); // refresh the ListView
                    selectedPosition = -1;              // reset selection (nothing selected)
                }
            }
        });

        // EVENT LISTENER: what happens when a city in the list is tapped
        //  how we know WHICH city to delete
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position; // remember which city was tapped (store its index)
            }
        });
    }
}