package com.example.pedarkharj_edit3.classes.web_db_pref;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pedarkharj_edit3.classes.Contact;
import com.example.pedarkharj_edit3.classes.Event;
import com.example.pedarkharj_edit3.classes.Expense;
import com.example.pedarkharj_edit3.classes.Participant;
import com.example.pedarkharj_edit3.classes.PersianDate;
import com.example.pedarkharj_edit3.classes.Routines;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private PersianDate persianDate;
    /******************************       Constants      *******************************/
    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "pedarKharjManager";

    // Table Names
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_EVENTS = "events";
    private static final String TABLE_PARTICES = "participants";
    private static final String TABLE_EXPENSES = "expenses";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // CONTACT Table - column names
    private static final String KEY_CONTACT_NAME = "contact_name";
    private static final String KEY_BMP_STR = "contact_img";

    // EVENTS Table - column names
    private static final String KEY_EVENT_NAME = "event_name";
    private static final String KEY_EVENT_BMP = "event_img";

    // PARTICES Table - column names
    private static final String KEY_CONTACT_ID = "contact_id";
    private static final String KEY_EVENT_ID = "event_id";
    private static final String KEY_PARTICE_NAME = "partice_name";
    private static final String KEY_PARTICE_EXPENSE = "partice_expense";
    private static final String KEY_PARTICE_DEBT = "partice_debt";

    // EXPENSES Table - column names
    private static final String KEY_EXPENSE_ID = "expense_id";
    private static final String KEY_BUYER_ID = "buyer_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EXPENSE_TITLE = "expense_title";
    private static final String KEY_EXPENSE_PRICE = "expense_price";
    private static final String KEY_EXPENSE_DEBT = "expense_debt";

    // Table Create Statements
    // Contact table create statement
    private static final String CREATE_TABLE_CONTACT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CONTACT_NAME+ " TEXT,"
            + KEY_BMP_STR + " TEXT,"
            + KEY_CREATED_AT+ " DATETIME" + ")";

    // Event table create statement
    private static final String CREATE_TABLE_EVENT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EVENTS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_EVENT_NAME + " TEXT,"
            + KEY_EVENT_BMP + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";


    // Participants table create statement
    private static final String CREATE_TABLE_PARTICES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PARTICES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_EVENT_NAME + " TEXT,"
            + KEY_EVENT_ID + " INTEGER,"
            + KEY_PARTICE_NAME + " TEXT,"
            + KEY_CONTACT_ID + " INTEGER,"
            + KEY_PARTICE_EXPENSE + " INTEGER,"
            + KEY_PARTICE_DEBT + " INTEGER,"
            + KEY_CREATED_AT+ " DATETIME" + ")";


    // Expense table create statement
    private static final String CREATE_TABLE_EXPENSES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EXPENSES
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_EXPENSE_ID + " INTEGER,"
            + KEY_EVENT_ID + " INTEGER,"
            + KEY_BUYER_ID + " INTEGER,"
            + KEY_USER_ID + " INTEGER,"
            + KEY_EXPENSE_TITLE+ " TEXT,"
            + KEY_EXPENSE_PRICE+ " INTEGER,"
            + KEY_EXPENSE_DEBT+ " INTEGER,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    /*******************************          Methods          ********************************/

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        persianDate = new PersianDate();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_CONTACT);
        db.execSQL(CREATE_TABLE_EVENT);
        db.execSQL(CREATE_TABLE_PARTICES);
        db.execSQL(CREATE_TABLE_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);

        // create new tables
        onCreate(db);
    }


    // ------------------------ "Contacts" table methods ----------------//
    /**
     * Creating contacts
     */
    public void createContacts(Contact[] contacts) {
        for(Contact contact : contacts){
            createContact(contact);
        }
    }
    // single contact
    public long createContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_CONTACT_NAME, contact.getName());
            values.put(KEY_BMP_STR,  contact.getBitmapStr());
            values.put(KEY_CREATED_AT, persianDate.getFullDateNTime());

            // insert row
            long contact_id = db.insert(TABLE_CONTACTS, null, values);
            contact.setId((int) contact_id);

            return contact_id;
    }


    /**
     * getting single contact
     */
    public Contact getContactById(long contact_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE "
                + KEY_ID + " = " + contact_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();


        Contact contact = new Contact();
        contact.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        contact.setName((c.getString(c.getColumnIndex(KEY_CONTACT_NAME))));
        contact.setBitmapStr(c.getString(c.getColumnIndex(KEY_BMP_STR)) ); // bitmapStr could even be null ! that's ok.
        contact.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return contact;
    }

    /**
     * getting all contacts
     * */
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                contact.setName((c.getString(c.getColumnIndex(KEY_CONTACT_NAME))));
                contact.setBitmapStr(c.getString(c.getColumnIndex(KEY_BMP_STR)) ); //bitmapStr could even be null !
                contact.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to to_do list
                contacts.add(contact);
            } while (c.moveToNext());
        }

        return contacts;
    }

    /**
     * getting contacts count
     */
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /**
     * Updating a contact
     */
    public void updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_NAME, contact.getName());
        values.put(KEY_BMP_STR, contact.getBitmapStr());
        values.put(KEY_CREATED_AT, contact.getCreated_at());
        // updating TABLE_CONTACTS table row
        db.update(TABLE_CONTACTS, values, KEY_ID + " = ?", new String[] { String.valueOf(contact.getId()) });


        /**  Now update participant     */
        List<Participant> participants = getAllParticesByContactId(contact.getId());
        ContentValues values0 = new ContentValues();

        //todo: Update -> do sth so we wouldn't need a loop anymore
        for (Participant participant0 : participants){
            values0.put(KEY_PARTICE_NAME, contact.getName());
            values0.put(KEY_PARTICE_EXPENSE, participant0.getExpense());
            values0.put(KEY_PARTICE_DEBT, participant0.getDebt());
            values0.put(KEY_EVENT_ID, participant0.getEvent().getId());
            values0.put(KEY_CONTACT_ID, contact.getId());
            // updating TABLE_PARTICES table row
            db.update(TABLE_PARTICES, values0, KEY_CONTACT_ID + " = ?", new String[] { String.valueOf(contact.getId()) });
        }

    }


    /**
     * Deleting a contact
     */
    public void deleteContact(long contact_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact_id) });
    }
    public void deleteContact(Contact contact) {
        long contact_id = contact.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact_id) });
    }

    // ------------------------ "Event" table methods ----------------//

    /**
     * Creating event
     */
    public long createEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, event.getEventName());
        values.put(KEY_EVENT_BMP, event.getBitmapStr());
        values.put(KEY_CREATED_AT, persianDate.getFullDateNTime() );

        // insert row
        long event_id = db.insert(TABLE_EVENTS, null, values);
        event.setId((int) event_id);

        return event_id;
    }

    /**
     * Creating new event with already existed Contacts
     */
    public long createNewEventWithPartices(Event event, Contact[] contacts) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, event.getEventName());
        values.put(KEY_EVENT_BMP, event.getBitmapStr());
        values.put(KEY_CREATED_AT, persianDate.getFullDateNTime());

        // insert row
        long event_id = db.insert(TABLE_EVENTS, null, values);
        event.setId((int) event_id);

        // insert contacts
        for (Contact contact : contacts) {
            createEventNewPartice(event, contact);
        }

        return event_id;
    }


    /**
     *  Creating a new Participant in an existing Event
     */
    public Participant createEventNewPartice(Event event, Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        Participant participant = new Participant(contact.getName());

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, event.getEventName());
        values.put(KEY_EVENT_ID, event.getId());
        values.put(KEY_PARTICE_NAME, participant.getName());
        values.put(KEY_CONTACT_ID, contact.getId()); //
        values.put(KEY_PARTICE_EXPENSE, participant.getExpense());
        values.put(KEY_PARTICE_DEBT , participant.getDebt());

        long id = db.insert(TABLE_PARTICES, null, values);

        return this.getParticeById(id);
    }

    /**
     * getting all events
     * */
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<Event>();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                event.setEventName(c.getString(c.getColumnIndex(KEY_EVENT_NAME)));
                event.setBitmapStr(c.getString(c.getColumnIndex(KEY_EVENT_BMP)));
                event.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to events list
                events.add(event);
            } while (c.moveToNext());
        }
        return events;
    }

    /**
     * getting event by name
     * */
    public Event getEventByName(String eventName) {

        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS + " WHERE " + KEY_EVENT_NAME + " = " + eventName;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            c.moveToFirst();
        }

        Event event = new Event();
        event.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        event.setEventName((c.getString(c.getColumnIndex(KEY_EVENT_NAME))));
        event.setBitmapStr(c.getString(c.getColumnIndex(KEY_EVENT_BMP)));
        event.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        Log.e("Event Maker ", event.getEventName() + "\n"+ event.getId());

        return event;
    }

    /**
     * getting event by id
     * */
    public Event getEventById(long eventId) {

        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS + " WHERE " + KEY_ID + " = " + eventId;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Event event = new Event();
        event.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        event.setEventName((c.getString(c.getColumnIndex(KEY_EVENT_NAME))));
        event.setBitmapStr(c.getString(c.getColumnIndex(KEY_EVENT_BMP)));
        event.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        Log.e("Event Maker ", event.getEventName() + "\n"+ event.getId());

        return event;
    }

    /**
     * Updating an event
     */
    public void updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, event.getEventName());
        // updating TABLTABLE_EVENT_PARTICES table row (event name ONLY)
        db.update(TABLE_PARTICES, values, KEY_EVENT_ID + " = ?", new String[] { String.valueOf(event.getId()) });

        values.put(KEY_EVENT_BMP, event.getBitmapStr());
        // updating TABLE_EVENTS table row (event name & Bmp)
        db.update(TABLE_EVENTS, values, KEY_ID + " = ?", new String[] { String.valueOf(event.getId()) });
    }


    /**
     * Deleting an event
     */
    public void deleteEvent(Event event, boolean should_delete_all_event_partices) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting event
        // check if Partices under this event should also be deleted
        if (should_delete_all_event_partices) {
            // get all contacts under this event
            List<Participant> allEventPartices = getAllParticeUnderEvent(event);

            // delete all contacts
            for (Participant participant : allEventPartices) {
                // delete a contact
                deletePartic(participant);
            }
        }

        // now delete the event
        db.delete(TABLE_EVENTS, KEY_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
    }


    // ------------------------ "partices" table methods ----------------//

    /**
     * adding a single new participant to an existing Event
     * if the partice already have an event in it
     */
    public void createParticipantUnderEvent(Participant participant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, participant.getEvent().getEventName());
        values.put(KEY_EVENT_ID, participant.getEvent().getId());
//        values.put(KEY_EVENT_ID, participant.getId());
        values.put(KEY_PARTICE_NAME, participant.getName());
        values.put(KEY_CONTACT_ID, participant.getContact().getId()); //
        values.put(KEY_PARTICE_EXPENSE, participant.getExpense());
        values.put(KEY_PARTICE_DEBT , participant.getDebt());
        values.put(KEY_CREATED_AT, persianDate.getFullDateNTime());


        long id = db.insert(TABLE_PARTICES, null, values);
    }
    /**
     * adding a single new participant to an existing Event
     */
    public void createParticipantUnderEvent(Participant participant, Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        participant.setEvent(event);

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, participant.getEvent().getEventName());
        values.put(KEY_EVENT_ID, event.getId());
//        values.put(KEY_EVENT_ID, participant.getId());
        values.put(KEY_PARTICE_NAME, participant.getName());
        values.put(KEY_CONTACT_ID, participant.getContact().getId()); //
        values.put(KEY_PARTICE_EXPENSE, participant.getExpense());
        values.put(KEY_PARTICE_DEBT , participant.getDebt());
        values.put(KEY_CREATED_AT, persianDate.getFullDateNTime());


        long id = db.insert(TABLE_PARTICES, null, values);
    }

    /**
     * creating a new contact THEN a new participant
     * adding a participant to main table
     */
    public Participant createContact_ParticipantUnderEvent(String particeName, Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        Contact contact0  = new Contact(particeName);
        createContacts(new Contact[] {contact0});

        Participant participant = new Participant(particeName);
        participant.setContact(contact0);
        // add partice to main table
        createParticipantUnderEvent(participant, event);

        return participant;
    }

    /**
     *   adding new participants to an existing Event
     */
    public List<Participant> createAllParticesUnderEvent(List<Participant> participants, Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Participant participant : participants){
            participant.setEvent(event);
            createParticipantUnderEvent(participant);
        }

        return participants;
    }

    /**
     * getting single Partice
     */
    public Participant getParticeById(long particeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTICES + " WHERE " + KEY_ID + " = " + particeId;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Participant participant = new Participant();
        participant.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        participant.setName(c.getString(c.getColumnIndex(KEY_PARTICE_NAME)));
        participant.setEvent( this.getEventById(c.getInt(c.getColumnIndex(KEY_EVENT_ID)) ));
        participant.setContact( this.getContactById(c.getInt(c.getColumnIndex(KEY_CONTACT_ID))) );
        participant.setExpense((c.getInt(c.getColumnIndex(KEY_PARTICE_EXPENSE))));
        participant.setDebt(c.getInt(c.getColumnIndex(KEY_PARTICE_DEBT)));
        participant.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return participant;
    }

    /**
     * getting partices by contact_id
     * */
    public List<Participant> getAllParticesByContactId(long contactId) {
        List<Participant> participants = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PARTICES + " WHERE " + KEY_CONTACT_ID + " = " + contactId;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);


        if (c.moveToFirst()) {
            do {
                Participant participant = new Participant();
                participant.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                participant.setName(c.getString(c.getColumnIndex(KEY_PARTICE_NAME)));
                participant.setEvent( this.getEventById(c.getInt(c.getColumnIndex(KEY_EVENT_ID)) ));
                participant.setContact( this.getContactById(c.getInt(c.getColumnIndex(KEY_CONTACT_ID))) );
                participant.setExpense((c.getInt(c.getColumnIndex(KEY_PARTICE_EXPENSE))));
                participant.setDebt(c.getInt(c.getColumnIndex(KEY_PARTICE_DEBT)));
                participant.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


                // adding to participants list
                participants.add(participant);
            } while (c.moveToNext());
        }

        return participants;
    }

    /**
     * getting Events under a specific Contact
     * */
    public List<Event> getAllEventsUnderContact(Contact contact) {
        List<Event> events = new ArrayList<>();

        String selectQuery = "SELECT  * FROM "
                + TABLE_PARTICES +  " event_partice, "
                + TABLE_EVENTS + " events"
                + " WHERE event_partice. " + KEY_CONTACT_ID + " = '" + contact.getId() + "' "
                + "And events." + KEY_ID + " = " + "event_partice." + KEY_EVENT_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {
                Event event = new Event();
                event.setId(c.getInt(c.getColumnIndex(KEY_EVENT_ID)));
                event.setEventName((c.getString(c.getColumnIndex(KEY_EVENT_NAME))));
                event.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                events.add(event);
            }while (c.moveToNext());
        }

        Log.e("EventMaker ", "Events Under contact  "+ contact.getName() + "\n");
        for (Event event0 : events){
            Log.e("EventMaker ", "Event "+ event0.getId() + ": "+ event0.getEventName()+"\n");
        }


        return events;
    }

    /**
     * getting participants under a specific Event
     * */
    public List<Participant> getAllParticeUnderEvent(Event event) {
        List<Participant> participants = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTICES + " WHERE " + KEY_EVENT_ID + " = " + event.getId();

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {
                Participant participant = new Participant();
                participant.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                participant.setName(c.getString(c.getColumnIndex(KEY_PARTICE_NAME)));
                participant.setEvent( this.getEventById(c.getInt(c.getColumnIndex(KEY_EVENT_ID)) ));
                participant.setContact( this.getContactById(c.getInt(c.getColumnIndex(KEY_CONTACT_ID))) );
                participant.setExpense((c.getInt(c.getColumnIndex(KEY_PARTICE_EXPENSE))));
                participant.setDebt(c.getInt(c.getColumnIndex(KEY_PARTICE_DEBT)));
                participant.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));


                // adding to participants list
                participants.add(participant);
            } while (c.moveToNext());
        }


        return participants;
    }
    // overload by name
    public List<Participant> getAllParticeUnderEvent(int eventId) {
        List<Participant> participants = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_PARTICES + " WHERE " + KEY_EVENT_ID + " = " + eventId;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {
                Participant participant = new Participant();
                participant.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                participant.setName(c.getString(c.getColumnIndex(KEY_PARTICE_NAME)));
                participant.setEvent( this.getEventById(eventId) );
                participant.setContact( this.getContactById(c.getInt(c.getColumnIndex(KEY_CONTACT_ID))) );
                participant.setExpense((c.getInt(c.getColumnIndex(KEY_PARTICE_EXPENSE))));
                participant.setDebt(c.getInt(c.getColumnIndex(KEY_PARTICE_DEBT)));
                participant.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to participants list
                participants.add(participant);
            } while (c.moveToNext());
        }


        return participants;
    }





    /**
     * Deleting a participant
     */
    public void deletePartic(Participant participant) {
        SQLiteDatabase db = this.getWritableDatabase();

        // now delete the Partic
        db.delete(TABLE_PARTICES, KEY_ID + " = ?",
                new String[] { String.valueOf(participant.getId()) });
    }

    /**
     * Deleting all participants Under Event
     */
    public void deleteAllParticeUnderEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "DELETE  * FROM " + TABLE_PARTICES + " WHERE " + KEY_EVENT_NAME + " = " + Routines.EVENT_TEMP_NAME;

        Log.e(LOG, selectQuery);

    }

    /**
     * updating a participant
     */
    public long updatePartice(Participant participant){
        SQLiteDatabase db = this.getWritableDatabase();

        Event event = participant.getEvent();
        //update contact name
        Contact contact = participant.getContact();
        this.updateContact(contact);

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, event.getEventName());
        values.put(KEY_EVENT_ID, event.getId());
        values.put(KEY_PARTICE_NAME, participant.getName());
        values.put(KEY_CONTACT_ID, participant.getContact().getId());
        values.put(KEY_PARTICE_EXPENSE, participant.getExpense());
        values.put(KEY_PARTICE_DEBT , participant.getDebt());
        values.put(KEY_CREATED_AT, persianDate.getFullDateNTime());


        return db.update(TABLE_PARTICES, values, KEY_ID + " = ?", new String[] { String.valueOf( participant.getId()) });
    }


    // ------------------------ "expenses" table methods -----------------------//
    /**
     * expense with equal debts
     */
    public void addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        Participant buyer = expense.getBuyer();
        List<Participant> userPartics = expense.getUserPartics();
        List<Integer> userIds = new ArrayList<>();
        for (Participant user : userPartics){
            userIds.add(user.getId());
        }

        boolean isBuyerIn = userIds.contains(buyer.getId());
        if (!isBuyerIn) userPartics.add(buyer);
        Log.e("fuck020", isBuyerIn + "");

        int i =0;
        List<Integer> expenseDebts = expense.getExpenseDebts();
        //get users as an array
        int userId;
        for (Participant user : userPartics){
            userId = user.getId();
            Log.e("ExpenseIds", user.getId() + "");

            // send an Expense
            ContentValues values = new ContentValues();
            values.put(KEY_EXPENSE_ID,  expense.getExpenseId());
            values.put(KEY_EVENT_ID, expense.getEvent().getId());
            values.put(KEY_BUYER_ID, expense.getBuyer().getId());
            values.put(KEY_USER_ID, userId);
            values.put(KEY_EXPENSE_TITLE, expense.getExpenseTitle());
            if (user.getId() == buyer.getId()) {
                values.put(KEY_EXPENSE_PRICE, expense.getExpensePrice());
            } else      values.put(KEY_EXPENSE_PRICE, 0);
            if (userId == buyer.getId() && !isBuyerIn){
                values.put(KEY_EXPENSE_DEBT, 0); //we need the buyer in to get the expense price
            } else values.put(KEY_EXPENSE_DEBT, expenseDebts.get(i));
            values.put(KEY_CREATED_AT, persianDate.getFullDateNTime());
            // insert row
            db.insert(TABLE_EXPENSES, null, values);

            //Add Debt to event_participant table
            ContentValues values2 = new ContentValues();
            if (userId == buyer.getId() && !isBuyerIn){
                values2.put(KEY_PARTICE_DEBT, user.getDebt());
            } else  values2.put(KEY_PARTICE_DEBT, user.getDebt() + expenseDebts.get(i));
            db.update(TABLE_PARTICES, values2,  KEY_ID + " = ?", new String[] {String.valueOf(userId) });
            i++;
        }

        // add Expense  to event_participant table
        ContentValues values1 = new ContentValues();
        values1.put(KEY_PARTICE_EXPENSE, buyer.getExpense() + expense.getExpensePrice());
        db.update(TABLE_PARTICES, values1,  KEY_ID + " = ?", new String[] {String.valueOf(buyer.getId()) });
    }

    /**
     * get the Last ExpenseId for saving a new expense
     */
    public int getLastExpenseId() {
        SQLiteDatabase db = this.getReadableDatabase();
        int lastId = 0;

        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES + " ORDER BY " +
                KEY_EXPENSE_ID + " DESC LIMIT 1" ;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToNext()){
            lastId =  c.getInt(c.getColumnIndex(KEY_EXPENSE_ID));
        }
        return lastId;
    }


    /**
     * getting all expenses of a partic (in a specific event)
     */
    public List<Expense> getAllExpensesOfEvent(Event event) {
        List<Expense> expenseList = new ArrayList<>();
        List<Integer> expenseIds = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES + " WHERE " + KEY_EVENT_ID + " = " + event.getId();
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {
                //we get them each time, cuz we won't know if some ids have been deleted by then.
                int newExpenseId = c.getInt(c.getColumnIndex(KEY_EXPENSE_ID));
                if (!expenseIds.contains(newExpenseId))
                    expenseIds.add(newExpenseId);
            } while (c.moveToNext());
        }

        Log.e("fuck021", "********************************************************* ");

        // getting each expense
        for (int expenseId : expenseIds) {
            expenseList.add( getExpenseByExpenseId(expenseId) );
        }
        return expenseList;
    }

    /**
     * getting s single expense BY the buyer partic (in a specific event)
     */
    public Expense getExpenseByExpenseId(int expenseId0) {
        Expense expense = new Expense();
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES + " WHERE " + KEY_EXPENSE_ID + " = " + expenseId0;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            int expenseId = c.getInt(c.getColumnIndex(KEY_EXPENSE_ID));
            int price;

            do {
                //todo: fuck ur code! that's bullshit, yet it works for now
                price = c.getInt(c.getColumnIndex(KEY_EXPENSE_PRICE));
            }while (price <= 0 && c.moveToNext());

                    expense.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                    expense.setExpenseId(expenseId);
                    expense.setEvent( this.getEventById((c.getInt(c.getColumnIndex(KEY_EVENT_ID))) ));
                    expense.setBuyer( this.getParticeById( c.getInt(c.getColumnIndex(KEY_BUYER_ID)) ));
                    expense.setUserPartics(this.getAllUsersByExpense(expenseId));
                    expense.setExpenseTitle(c.getString(c.getColumnIndex(KEY_EXPENSE_TITLE)));
                    expense.setExpensePrice(price);
                    expense.setExpenseDebts(this.getAllDebtsByExpense(expenseId));
                    expense.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
//            Log.e("fuck021", ": ");
//            Log.e("fuck021", ": ");
//
//            Log.e("fuck021", "expenseId0: "+ expenseId0);
//
//            Log.e("fuck021", "id: "+c.getInt(c.getColumnIndex(KEY_ID)) );
//            Log.e("fuck021", "setExpenseId: "+expenseId );
//            Log.e("fuck021", "EventId "+c.getInt(c.getColumnIndex(KEY_EVENT_ID)) );
//            Log.e("fuck021", "UserId "+c.getInt(c.getColumnIndex(KEY_USER_ID)) );
//            Log.e("fuck021", "BuyerId "+ c.getInt(c.getColumnIndex(KEY_BUYER_ID)) );
//            price = c.getInt(c.getColumnIndex(KEY_EXPENSE_PRICE));
//            Log.e("fuck021",  price+" price > 0 ? : "+ (price > 0));
//            Log.e("fuck021", "Debt: "+ c.getInt(c.getColumnIndex(KEY_EXPENSE_DEBT)) );
//                }

        }

        return expense;
    }

    /**
     * getting everyone's debt as a list (in an Expense)
     */
    private List<Integer> getAllDebtsByExpense(int expenseId) {
        List<Integer> expenseDebts = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES + " WHERE " + KEY_EXPENSE_ID + " = " + expenseId;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {
                expenseDebts.add( c.getInt(c.getColumnIndex(KEY_EXPENSE_DEBT)) );
            }while (c.moveToNext());
        }
        return expenseDebts;
    }

    /**
     * getting all users of an expense
     */
    private List<Participant> getAllUsersByExpense(int expenseId) {
        List<Participant> users = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES + " WHERE " + KEY_EXPENSE_ID + " = " + expenseId;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {
                int userId = c.getInt(c.getColumnIndex(KEY_USER_ID));
                users.add(this.getParticeById(userId));
            } while (c.moveToNext());
        }
        return users;
    }

    /**
     * getting a participant's all debts in an event
     */
    public int getAllParticDebtsByParticeId(int particId) {
        int debt = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_PARTICES + " WHERE " + KEY_ID + " = " + particId;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            debt = c.getInt(c.getColumnIndex(KEY_PARTICE_DEBT));
        }
        return debt;
    }

    /**
     * getting a participant's all expenses in an event
     */
    public int getAllParticExpensesByParticeId(int particId) {
        int debt = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_PARTICES + " WHERE " + KEY_ID + " = " + particId;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            debt = c.getInt(c.getColumnIndex(KEY_PARTICE_EXPENSE));
        }
        return debt;
    }

    /**
     * getting an Event whole Expenses
     */
    public int getEventAllExpensesByEventId(int eventId) {
        int allExpenses = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_PARTICES + " WHERE " + KEY_EVENT_ID + " = " + eventId;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);


        if (c.moveToFirst()){
            do {
                allExpenses += c.getInt(c.getColumnIndex(KEY_PARTICE_EXPENSE));
            } while (c.moveToNext());
        }
        return allExpenses;
    }

    // ------------------------ other stuff ---------------------//
    /**
     *     closing database
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public void syncEvents() {
        //from SQLite to mySql
        this.getAllEvents();

    }
}
