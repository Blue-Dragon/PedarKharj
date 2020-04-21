package com.example.pedarkharj_edit3.classes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.models.Contact;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Expense;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.pages.fragments.ContactsFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {

    private List<Participant> participants;
    private List<Event> events;
    private List<Expense> expenseList;
    private List<Contact> contactList;
    private Context mContext;
    private Activity mActivity;
    private Participant selectedPartic;
    private Expense mExpense;

    private short selectMode = 3;
    private int mLayout, maxCheckImg;
    private float defaultDongAmount;
    private int widthSplit = 0;
    private boolean amountModeDong;
    private boolean isExpenseMode2;
    public boolean isAmountModeDong() {
        return amountModeDong;
    }
    private boolean addEventParticeMode = false;

    private Drawable drawable; //EventMng
    private boolean isFloatingActionBar;


    //------------------------------      Constructors       ---------------------------------/
    public void setContext(Context mContext) {
        this.mContext = mContext;
    }
    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    //EventMng Activity
    public MyAdapter(Context mContext) {
        this.mContext = mContext;
        this.mLayout = R.layout.sample_event;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }
    public void setLayout(int layoutId) {
        this.mLayout = layoutId;
    }
    public void setForeground(Drawable drawable) {
        this.drawable = drawable;
    }
    public void setItemsInScreen(int itemsCount) {
        this.widthSplit = itemsCount ;
    }

    //Typical Activities
    public MyAdapter(Context mContext, int mLayout, List<Participant> participants) {
        this.mContext = mContext;
        this.participants = participants;
        this.mLayout = mLayout;
    }
    public void setMaxCheckImg(int maxCheckImg) {
        this.maxCheckImg = maxCheckImg;
    }
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
    public void setSelectMode(short selectMode){
        this.selectMode = selectMode;
    }

    public void setDefaultDongAmount(float defaultDongAmount) {
        this.defaultDongAmount = defaultDongAmount;
    }
    public void setAmountModeDong(boolean amountModeDong) {
        this.amountModeDong = amountModeDong;
    }

    public MyAdapter(AppCompatActivity mActivity, int mLayout, List<Participant> participants, boolean amountModeDong) {
        this.mActivity = mActivity;
        this.participants = participants;
        this.mLayout = mLayout;
        this.amountModeDong = amountModeDong; //if true, mode 2- amount
    }

    //Contacts
    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public void setIsFloatingActionBar(boolean isFloatingActionBar) {
        this.isFloatingActionBar = isFloatingActionBar;
    }

    //EventDetailActivity (ExpenseList)
    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }
    public void setExpenseMode2(boolean expenseMode2) {
        isExpenseMode2 = expenseMode2;
    }
    public void setSelectedPartic(Participant selectedPartic) {
        this.selectedPartic = selectedPartic;
    }
    //eachExpense
    public void setExpense(Expense mExpense) {
        this.mExpense = mExpense;
    }
    //addEventPartice
    public void setIsAddEventParticeMode(boolean addEventParticeMode) {
        this.addEventParticeMode = addEventParticeMode;
    }

    /**
     * it handles the contact delete
     */
    public void doDeleteStuff(Activity mActivity, Contact pressedContact) {
        DatabaseHelper db = new DatabaseHelper(mActivity);

        List<Event> particedEvents = db.getAllEventsUnderContact(pressedContact);
        if (particedEvents.size() > 0)
            Routines.showDialog(mActivity);
        else
            Routines.deleteContect(mActivity, pressedContact);

        db.closeDB();
    }


    //------------------------------      Interface Methods       ---------------------------------/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        //screen_based width
        if (widthSplit > 0){
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            int split = (parent.getMeasuredWidth() / widthSplit);
            layoutParams.width = split - split/12 ;
            layoutParams.height = 4*(layoutParams.width)/3;
//        int width = parent.getMeasuredWidth() / 3;
//        view.setMinimumWidth(width);
        }
        ViewHolder holder = new ViewHolder(view);
        if (mActivity != null)
            holder.setActivity(mActivity);
        if (mContext != null)
            holder.setContext(mContext);
        holder.setIsFloatingActionBar(isFloatingActionBar);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        /*
         * EventFragment
         */
        if (events != null){
            Log.i("positionCall", "Events recyclerView Call");
            Event event = events.get(position);
            DatabaseHelper db0 = new DatabaseHelper(mContext);
            int particNumber = db0.getAllParticeUnderEvent(event).size();

            if (event.getEventName() !=null && holder.nameTv != null)      holder.nameTv.setText(event.getEventName());
            /* pic */ if ( event.getBitmapStr() != null  && holder.imageView != null)
                holder.imageView.setImageBitmap(Routines.stringToBitmap(event.getBitmapStr()));

//            /* back  ground */ if ( event.getBitmapStr() != null  && holder.bkGrndLayout != null) {
//                Drawable drawable = new BitmapDrawable(mContext.getResources(), Routines.stringToBitmap(event.getBitmapStr()) );
//                holder.bkGrndLayout.setBackground(drawable);
//            }
            if (particNumber > 0 && holder.resultTxt != null)      holder.resultTxt.setText(particNumber + " عضو");
            else Log.e("E002",  particNumber + "" );

            if (drawable != null && holder.cardView != null) holder.cardView.setForeground(drawable); //onLongClick color changing

            db0.closeDB();
        }


        /*
         * EventDetailed- ExpenseLists
         */
        if (expenseList != null){
            Log.i("positionCall", "ExpenseList recyclerView Call");
            DatabaseHelper db0 = new DatabaseHelper(mContext);

            Expense expense = expenseList.get(position);
            Participant buyer = expense.getBuyer();

            if (buyer.getName() != null && holder.nameTv != null)
                holder.nameTv.setText(buyer.getName());
            if (buyer.getBitmapStr() != null && holder.profImv != null)
                holder.profImv.setImageBitmap(Routines.stringToBitmap(buyer.getBitmapStr()));

            if (holder.resultTxt != null)
                holder.resultTxt.setText(Routines.getRoundFloatString(expense.getExpensePrice()) );
            if (holder.dateTv != null)
                holder.dateTv.setText(expense.getCreated_at());
            if (holder.priceTitleTv != null){
                String expenseTitle = expense.getExpenseTitle().length()>0 ? expense.getExpenseTitle() : "بدون عنوان" ;
                holder.priceTitleTv.setText(expenseTitle);
            }

                //if ExpenseMode2 (each person expenses)
            if (isExpenseMode2 && selectedPartic != null){
                float debt = db0.getParticeDebt(expense.getExpenseId(), selectedPartic.getId());

                if (holder.resultTxt != null) //Red/ debt
                    holder.resultTxt.setText(Routines.getRoundFloatString(debt));
                if (holder.resultTxtGreen != null){ //Green/ expense
                    float expense0 = buyer.getId() == selectedPartic.getId() ? expense.getExpensePrice() : 0f;
                    holder.resultTxtGreen.setText(Routines.getRoundFloatString(expense0));
                }

            }

            db0.closeDB();
        }


        /*
         * Contacts
         */
        if (contactList != null ) {
            holder.setOnMyLongClickListener(position1 -> {
                ContactsFragment.pressedContact = contactList.get(position1); //this is for the ContextMenu (edit/delete)
            });



            Contact contact;
            Log.i("positionCall", "contacts recyclerView Call");
            contact = contactList.get(position);


            //get info later. no hurry bro !
            if (contact.getName() != null && holder.nameTv != null){
//                holder.nameTv.setText(contact.getName());
                ContactAndHolder container = new ContactAndHolder();
                container.contact = contact;
                container.holder = holder;
                LoadImgATask aTask = new LoadImgATask();
                aTask.execute(container);
            }


//            if (contact.getBitmapStr() != null && holder.profImv != null){
//                ContactAndHolder container = new ContactAndHolder();
//                container.contact = contact;
//                container.holder = holder;
//                LoadImgATask aTask = new LoadImgATask();
//                aTask.execute(container);
////                holder.profImv.setImageBitmap(Routines.stringToBitmap(contact.getBitmapStr()));
//            }

            if (drawable != null && holder.relativeLayout != null) holder.relativeLayout.setForeground(drawable); //onLongClick color changing

        }


        /*
         * others
         */
        if (participants != null ) {
            Participant participant;

                Log.i("positionCall", "Participants recyclerView Call");
                participant = participants.get(position);

            if (participant.getName() != null && holder.nameTv != null)
                holder.nameTv.setText(participant.getName());
            if ( holder.profImv != null){
                if (participant.getBitmapStr() != null)
                    holder.profImv.setImageBitmap(Routines.stringToBitmap(participant.getBitmapStr()));
                else{
                    Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.profile);
                    holder.profImv.setImageBitmap( Routines.convertBitmapThumbnail1x1(b));
                }
            }
            if (participant.getResult() != null && holder.resultTxt != null)
                holder.resultTxt.setText(participant.getResult());

            //EventDetailEachExpense Activity
            if (mExpense !=null && holder.resultTxt != null){
                DatabaseHelper db = new DatabaseHelper(mContext);
                float debt = db.getParticeDebt(mExpense.getExpenseId(), participant.getId());
                holder.resultTxt.setText(Routines.getRoundFloatString(debt));
            }

            //addEventPartice
            if (addEventParticeMode && Routines.particSelectedIds.size() > 0){
//                if (Routines.particSelectedIds.contains((int) participant.getContact().getId()))
                Log.d("Fuck", Routines.particSelectedIds.size() + "");
//                holder.particRv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.selected_green));
            }

            //addExpenseActivity_ amountMode
            if (defaultDongAmount > 0 && holder.dongEtxtAmount != null)
                holder.dongEtxtAmount.setText(Routines.getRoundFloatString(defaultDongAmount));

            /*
             * AddExpenseActivity_ selecting users
             */
            if (selectMode == Routines.UNSELECT_ALL){
                //unselectAll
                holder.checkedImg.setVisibility(View.INVISIBLE);
            } else if (selectMode == Routines.SELECT_ALL){
                //SelectAll
                holder.checkedImg.setVisibility(View.VISIBLE);
            } //else, keep going dude!
        }

    }



    @Override
    public int getItemCount() {
        if (participants != null)
            return participants.size();

        else if (events != null)
            return events.size();

        else if (expenseList != null)
            return expenseList.size();

        else if (contactList != null)
            return contactList.size();

        else return 0;
    }

    /**************************************      other Methods       **************************************/

    @Override
    public void onClick(View view) {

    }


    class ContactAndHolder {
        Contact contact;
        ViewHolder holder;
        Bitmap bitmap;
        String name;
    }

    class LoadImgATask extends AsyncTask<ContactAndHolder, Void, ContactAndHolder>{

        @Override
        protected ContactAndHolder doInBackground(ContactAndHolder... contactAndViews) {

            ContactAndHolder contactAndView = contactAndViews[0];
            String bitmapString = (contactAndView.contact.getBitmapStr());
            if (bitmapString != null && bitmapString.length() > 0){
                contactAndView.bitmap = Routines.stringToBitmap(bitmapString);
            }
            contactAndView.name = contactAndView.contact.getName();
            return contactAndView;
        }

        @Override
        protected void onPostExecute(ContactAndHolder contactAndView) {
            if (contactAndView !=null) {
                ViewHolder holder = contactAndView.holder;
                Contact contact = contactAndView.contact;
                Bitmap bitmap = contactAndView.bitmap;
                String name = contactAndView.name;

                holder.nameTv.setText(name);
                if (bitmap != null && holder.profImv != null){
                    holder.profImv.setImageBitmap(bitmap);
                }
            }
            super.onPostExecute(contactAndView);
        }
    }
}
