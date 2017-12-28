package com.example.naveed.contentprovider;

import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MTAG";
    RecyclerView recyclerView;
    ArrayList<Contact> list;
    ContactsAdapter adapter;
    Button btnMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.phonerecycler);
        btnMatch = findViewById(R.id.btnMatch);
        list = readContacts();
        adapter = new ContactsAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        onMatchButton();

    }

    private void onMatchButton() {
        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<List<Contacts>> callList = ServiceSingleton.getInstance().contactMatch(list);
                callList.enqueue(new Callback<List<Contacts>>() {
                    @Override
                    public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse: ");
                            list.clear();
                            for (Contacts contact : response.body()) {
                                list.add(contact);
                                Log.d(TAG, "onResponse: number: " + contact.getNumber() + " \n");
                            }
                            startActivity(new Intent(MainActivity.this, MatchContact.class));
                        } else if (response.code() != 200) {
                            Log.d(TAG, "onResponse:else code " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Contacts>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    }
                });

            }
        });

    }

    public ArrayList<Contact> readContacts() {
        ArrayList<Contact> contactList = new ArrayList<Contact>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor contactsCursor = getContentResolver().query(uri, null, null,
                null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");


        if (contactsCursor.moveToFirst()) {
            do {
                long contctId = contactsCursor.getLong(contactsCursor
                        .getColumnIndex("_ID"));
                Uri dataUri = ContactsContract.Data.CONTENT_URI;

                Cursor dataCursor = getContentResolver().query(dataUri, null,
                        ContactsContract.Data.CONTACT_ID + " = " + contctId,
                        null, null);


                String displayName = "";
                String homePhone = "";
                String mobilePhone = "";
                String workPhone = "";
                String contactNumbers = "";



                if (dataCursor.moveToFirst()) {
                    displayName = dataCursor
                            .getString(dataCursor
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    do {

                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {

                            switch (dataCursor.getInt(dataCursor
                                    .getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    homePhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactNumbers = homePhone;
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    workPhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactNumbers = workPhone;
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    mobilePhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    contactNumbers = mobilePhone;
                                    break;
                            }
                        }


                    } while (dataCursor.moveToNext());

                    contactList.add(new Contact(
                            displayName + "", contactNumbers + ""));
                }

            } while (contactsCursor.moveToNext());
        }
        return contactList;
    }
}
