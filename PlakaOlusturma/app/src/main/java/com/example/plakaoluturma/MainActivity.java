package com.example.plakaoluturma;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ListView listViewNumbers, listViewCities;
    private Button btnGenerate;
    private ArrayList<String> numbersList = new ArrayList<>();
    private ArrayList<String> citiesList = new ArrayList<>();

    private final String[] cities = {
            "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin", "Aydın", "Balıkesir",
            "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli",
            "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari",
            "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir",
            "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir",
            "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat",
            "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman",
            "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye",
            "Düzce"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewNumbers = findViewById(R.id.listViewNumbers);
        listViewCities = findViewById(R.id.listViewCities);
        btnGenerate = findViewById(R.id.btnGenerate);

        // ListView Adapter'larını oluştur
        ArrayAdapter<String> numbersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numbersList);
        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, citiesList);

        listViewNumbers.setAdapter(numbersAdapter);
        listViewCities.setAdapter(citiesAdapter);

        btnGenerate.setOnClickListener(v -> {
            generateRandomPlates();
            numbersAdapter.notifyDataSetChanged();
            citiesAdapter.notifyDataSetChanged();
        });

        // ListView item tıklama olayını ekleyelim
        listViewCities.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCity = citiesList.get(position);
            int plateNumber = Integer.parseInt(numbersList.get(position));

            // Yeni aktiviteye veri gönder
            Intent intent = new Intent(MainActivity.this, CityDetailActivity.class);
            intent.putExtra("cityName", selectedCity);
            intent.putExtra("plateNumber", plateNumber);
            startActivity(intent);
        });
    }

    private void generateRandomPlates() {
        numbersList.clear();
        citiesList.clear();

        ArrayList<Integer> plateNumbers = new ArrayList<>();
        for (int i = 1; i <= 81; i++) {
            plateNumbers.add(i);
        }

        Collections.shuffle(plateNumbers, new Random());

        for (int i = 0; i < 10; i++) { // Rastgele 10 şehir seç
            numbersList.add(String.valueOf(plateNumbers.get(i)));
            citiesList.add(cities[plateNumbers.get(i) - 1]); // Şehir dizisinden doğru eşleştirme yap
        }
    }
}