package com.example.pedarkharj_edit3.classes;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.pages.fragments.ContactsFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
    CircleImageView profImv;
    AppCompatImageView checkedImg;
    ImageView imageView;
    TextView nameTv, resultTxt, resultTxtGreen ;
    RelativeLayout relativeLayout;
    CardView cardView; //EventMng
    //diff dong
    Button plusBtn, minusBtn;
    TextView dongEtxt;
    EditText dongEtxtAmount;//mode_02 amount
    TextView dateTv, priceTitleTv;  // EventDetailed- ExpenseLists
    private LinearLayout bkGrndLayout; //Event recView



    ViewHolder(View itemView) {
        super(itemView);
//            itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        imageView = itemView.findViewById(R.id.imageview_event);
        profImv = itemView.findViewById(R.id.prof_pic);
        nameTv = itemView.findViewById(R.id.partic_name);
        resultTxt = itemView.findViewById(R.id.result_txt);
        resultTxtGreen = itemView.findViewById(R.id.result_txt_green); //expenseMode2
//            baseLayout = itemView.findViewById(R.id.base_layout);
        bkGrndLayout = itemView.findViewById(R.id.image_event_ll);

        //
        checkedImg = itemView.findViewById(R.id.sub_img);
        //dong
        plusBtn = itemView.findViewById(R.id.plus_btn);
        minusBtn = itemView.findViewById(R.id.minus_btn);
        dongEtxt = itemView.findViewById(R.id.dong_Etxt2);
        dongEtxtAmount =  itemView.findViewById(R.id.dong_Etxt_amount); // dong mode_02 only
        //
        cardView = itemView.findViewById(R.id.details_card_layout); //EventMng
        // EventDetailed- ExpenseLists
        dateTv = itemView.findViewById(R.id.tv_date);
        priceTitleTv = itemView.findViewById(R.id.tv_price_title);

        relativeLayout = itemView.findViewById(R.id.fu);

        if (relativeLayout!=null){
            relativeLayout.setOnCreateContextMenuListener(this);
        }
    }


    // --------------------    floating context menu    --------------------//
    /*
     * selected contact is initialized later in Contacts Section
     */
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
//            MenuInflater inflater = mActivity.getMenuInflater();
//            inflater.inflate(R.menu.menu_context_floating, menu);
        menu.setHeaderTitle(ContactsFragment.pressedContact.getName());
        menu.add(this.getAdapterPosition(), 100, 0, "ویرایش");
        menu.add(this.getAdapterPosition(), 101, 1, "حذف");
    }


    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
