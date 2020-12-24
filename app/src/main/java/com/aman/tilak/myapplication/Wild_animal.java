package com.aman.tilak.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

//import com.haoc.cameraxfullcodedemo.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aman.tilak.myapplication.Database.LocationBook;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Wild_animal extends AppCompatActivity implements View.OnClickListener {

    ImageView img;
    TextView click, getlocation, setlocation;
    private static final int REQUEST_LOCATION = 1;
    Button send;
    EditText name, problem, authority_tel_no, authority_email_id, autority_name, animal_type;
    LocationManager lm;
    LocationListener ll;

    LocationBook lb;

    String latitude, longitude;

    FusedLocationProviderClient fusedLocationProviderClient;
    private String currentLocation;

    List<String> cache= new ArrayList<>();
    String newAddressLine="";

    BottomNavigationView bv;


    public void requestPermission() {
        String permissions[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CALL_PHONE};
        ActivityCompat.requestPermissions(Wild_animal.this, permissions, 200);
    }

    private static final int pic_id = 123;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wild_animal);

        requestPermission();

        lb= new LocationBook(Wild_animal.this);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        bv= (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bv.setOnNavigationItemSelectedListener(navListener);


        name = (EditText) findViewById(R.id.animal_name_wild_id);
        animal_type= (EditText) findViewById(R.id.animal_type_wild_id);
        problem = (EditText) findViewById(R.id.animal_problem_wild_id);
        getlocation = (TextView) findViewById(R.id.animal_wild_getlocation_id);
        setlocation = (TextView) findViewById(R.id.set_location_wild_id);
        authority_tel_no = (EditText) findViewById(R.id.authority_mobile_no_wild_id);
        authority_email_id = (EditText) findViewById(R.id.authority_email_id_wild_id);
        autority_name= (EditText) findViewById(R.id.authority_type_wild_id);

        autority_name.setText("Animal rescur authority");
        authority_tel_no.setText("+91 78969 07029");
        authority_email_id.setText("charlestudu123@gmail.com");

        send = (Button) findViewById(R.id.send_wild_id);
        click = (TextView) findViewById(R.id.authority_click_wild_animal_id);

        send.setOnClickListener(this);
        click.setOnClickListener(this);
        getlocation.setOnClickListener(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(Wild_animal.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(Wild_animal.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(Wild_animal.this,Dashboard.class));
                    finish();
                    break;
                case R.id.back:
                    startActivity(new Intent(Wild_animal.this, Dashboard.class));
                    finish();
                    break;
                case R.id.camera:
                    startActivity(new Intent(Wild_animal.this, MainActivity.class));
                    finish();
                    break;
                case R.id.domestic:
                    startActivity(new Intent(Wild_animal.this, Domestic_animal.class));
                    finish();
                    break;
                case R.id.wild:
                    startActivity(new Intent(Wild_animal.this, Wild_animal.class));
                    finish();
                    break;
            }
            return false;
        }
    };



    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder= new Geocoder(Wild_animal.this, Locale.getDefault());
                        List<Address> addressList= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                        SQLiteDatabase db = lb.getWritableDatabase();
                        String query = "select * from location";
                        Cursor c1 = db.rawQuery(query, null);
                        if (c1.getCount()<=0) {
                            lb.insertData(addressList.get(0).getLocality(),String.valueOf(addressList.get(0).getLatitude()),String.valueOf(addressList.get(0).getLongitude()),addressList.get(0).getAddressLine(0));
                        }
                        else if (c1.getCount() >= 1) {
                            lb.insertData(addressList.get(0).getLocality(),String.valueOf(addressList.get(0).getLatitude()),String.valueOf(addressList.get(0).getLongitude()),addressList.get(0).getAddressLine(0));
                            if (addressList.get(0).getLocality()==null) {
                                lb.delete(null,String.valueOf(addressList.get(0).getLatitude()),String.valueOf(addressList.get(0).getLongitude()));
                            }
                        }
                        // String latitude= String.valueOf(addressList.get(0).);

                        HashMap<String,ArrayList<String>> fetchValues= lb.getValues();
                    /*    for (Map.Entry<String,ArrayList<String>> e1: fetchValues.entrySet()) {
                            AlertDialog.Builder showLocations= new AlertDialog.Builder(Wild_animal.this);
                            showLocations.setTitle("Locations");
                            showLocations.setCancelable(false);
                            showLocations.setMessage(e1.getKey() + " " + e1.getValue());
                            showLocations.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog a1= showLocations.create();
                            a1.show();
                        }    */

                        String lat_lon=addressList.get(0).getLatitude()+""+addressList.get(0).getLongitude(), newLocality="";

                        for (Map.Entry<String,ArrayList<String>> e1: fetchValues.entrySet()) {
                            if (lat_lon.equals(e1.getKey())) {
                                for (int i=0;i<e1.getValue().size();i++) {
                                    if (e1.getValue().get(i)!=null) {
                                        newLocality= e1.getValue().get(i);
                                    }
                                    else if (e1.getValue().get(i)==null) {
                                        continue;
                                    }
                                }
                            }
                        }

                        System.out.println("newLocality is: " + newLocality);

                        currentLocation = addressList.get(0).getFeatureName() + "" + newLocality + "\n"+ addressList.get(0).getAddressLine(0);

                        // String latitude= String.valueOf(addressList.get(0).);

                        AlertDialog.Builder showAddress= new AlertDialog.Builder(Wild_animal.this);
                        setlocation.setText("Location: " + String.valueOf(addressList.get(0).getLatitude() + "\n" + addressList.get(0).getLongitude() + "\n" + newLocality + "\n" + addressList.get(0).getAddressLine(0)));
                        showAddress.setTitle("Location");
                        showAddress.setMessage("Latitude: "+addressList.get(0).getLatitude()+"\nLongitude: " + addressList.get(0).getLongitude() + "\nLocality: " + newLocality + "\nPlace: "+ addressList.get(0).getAddressLine(0));
                        AlertDialog a1= showAddress.create();
                        a1.show();
                     //  currentLocation = addressList.get(0).getFeatureName() + "" + addressList.get(0).getLocality()+ "\n"+ addressList.get(0).getAddressLine(0);
                       // setlocation.setText("Location: " + String.valueOf(addressList.get(0).getLatitude() + "\n" + addressList.get(0).getLongitude() + "\n" + addressList.get(0).getAddressLine(0)));
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(e);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, Menu.FIRST, Menu.NONE,"Dashboard");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                startActivity(new Intent(Wild_animal.this, Dashboard.class));
                //  finish();
                finishAffinity();
                System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.authority_click_wild_animal_id:
                startActivity(new Intent(Wild_animal.this, MainActivity.class));
                finish();
                break;
        /*    case R.id.animal_wild_getlocation_id:

                ll = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        try {

                            setlocation.setText("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());

                            AlertDialog.Builder locationDisplay = new AlertDialog.Builder(Wild_animal.this);
                            locationDisplay.setTitle("My Place");
                            Geocoder geocoder = new Geocoder(Wild_animal.this, Locale.getDefault());
                            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            locationDisplay.setMessage("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude() + "\nPlace: " + addressList.get(0).getAddressLine(0));
                            locationDisplay.setCancelable(false);
                            locationDisplay.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog a1 = locationDisplay.create();
                            a1.show();
                        } catch (Exception e){}
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                };
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, ll);
                break;      */
            case R.id.send_wild_id:
                AlertDialog.Builder optionsmenu= new AlertDialog.Builder(Wild_animal.this);
                optionsmenu.setTitle("Options");
                optionsmenu.setMessage("Choose appropriate actions \nfrom the list provided\n\n");
                optionsmenu.setCancelable(false);
                optionsmenu.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                final String telNo= authority_tel_no.getText().toString().trim();
                optionsmenu.setNeutralButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent call= new Intent();
                        call.setAction(Intent.ACTION_CALL);
                        call.setData(Uri.parse("tel:"+telNo));
                        startActivity(call);
                    }
                });

                optionsmenu.setNegativeButton("Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String fetchEmailid= authority_email_id.getText().toString().trim();
                        String subject= "Please urgent response is required!";
                        String to[]={fetchEmailid};
                        String cc[]={""};
                        String bcc[]= {""};
                        String subjects[]= {subject};
                        String message = name.getText().toString().trim() + " animal has been found!\n\nAnimal type is: " + animal_type.getText().toString().trim()+"\n\nProblem with the " + name.getText().toString().trim() + " is " + problem.getText().toString().trim() + "\n\n My current location is " + currentLocation;
                        String body=" " + message;
                        Intent email= new Intent();
                        email.setType("message/rfc822");
                        email.putExtra(Intent.EXTRA_EMAIL,to);
                        email.putExtra(Intent.EXTRA_CC,cc);
                        email.putExtra(Intent.EXTRA_BCC, bcc);
                        email.putExtra(Intent.EXTRA_SUBJECT,subject);
                        email.putExtra(Intent.EXTRA_TEXT, body);
                        startActivity(Intent.createChooser(email,"Urgent help mail"));
                    }
                });
                AlertDialog a1= optionsmenu.create();
                a1.show();
                break;
        }
    }

    public boolean hasNetwork() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        // Match the request 'pic id with requestCode
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {

            // BitMap is data structure of image file
            // which stor the image in memory
            Bitmap photo = (Bitmap) data.getExtras()
                    .get("data");

            // Set the image in imageview for display
            img.setImageBitmap(photo);

        }
    }

}