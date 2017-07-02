package com.project.gaurs.tadqa.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.project.gaurs.tadqa.CurrentOrder;
import com.project.gaurs.tadqa.Pojo.OrderElements;
import com.project.gaurs.tadqa.Pojo.PreviousData;
import com.project.gaurs.tadqa.R;
import com.project.gaurs.tadqa.Unnecessary.DatabaseEntry;
import com.project.gaurs.tadqa.Unnecessary.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gaurs on 6/15/2017.
 */

public class Adapter_Order extends RecyclerView.Adapter<Adapter_Order.ViewHolder> {

    Adapter_List adapter_list;
    private ArrayList<PreviousData>data;
    private List<OrderElements>foodElements;
    Context context;
    boolean isPressed = false;
    DatabaseEntry databaseEntry;

    public Adapter_Order(List<OrderElements> foodElements, Context context) {
        this.foodElements = foodElements;
        this.context = context;
    }

    @Override
    public Adapter_Order.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order, parent, false);
        return new Adapter_Order.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Adapter_Order.ViewHolder holder, final int position) {

        databaseEntry = new DatabaseEntry(context);
        SharedPreferences pricetotal;
        pricetotal = context.getSharedPreferences("PRICE_TOTAL", Context.MODE_PRIVATE);
        String text = pricetotal.getString("total", null);
        holder.name.setText(foodElements.get(position).getName());
        holder.id.setText(foodElements.get(position).getTransactionID());
        holder.address.setText(foodElements.get(position).getAddress());
        holder.ordertotal.setText("Rs. "+text);
        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPressed) {
                holder.down.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                holder.listView.setVisibility(View.GONE);
                holder.orderdetails.setVisibility(View.GONE);
            }else{
                holder.down.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                holder.listView.setVisibility(View.VISIBLE);
                holder.orderdetails.setVisibility(View.VISIBLE);
            }
                isPressed = !isPressed;
            }
        });
        data = new ArrayList<PreviousData>();
        data= databaseEntry.getDataForList();
        adapter_list = new Adapter_List(data, context);
        holder.listView.setAdapter(adapter_list);
        Utility.setListViewHeightBasedOnChildren(holder.listView);
        holder.status.setText("Order Accepted.");

        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch(i){
                    case 0:
                        holder.status.setText("Order Accepted.");
                        break;
                    case 1:
                        holder.status.setText("Order Dispatched.");
                        break;
                    case 2:
                        holder.status.setText("Order Delivered.");
                        //databaseEntry.deleteAll();
                        databaseEntry.addToPreviousOrder();
                        databaseEntry.deleteTable();
                        foodElements.clear();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CurrentOrder.checkData(foodElements);

                            }
                        }, 5000);
                        break;
                    default:
                        holder.status.setText("Order Accepted.");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        holder.seekBar.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.seekBar.setProgress(1);
            }
        }, 7000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.seekBar.setProgress(2);

            }
        }, 14000);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("mm");
                SharedPreferences sharedPreferences = context.getSharedPreferences("TIME", Context.MODE_PRIVATE);
                String time = sharedPreferences.getString("time","");
                long t = Long.parseLong(time.trim());
                Date date = new Date(t);
                String mininitial = sdf.format(date);
                int mini = Integer.parseInt(mininitial);
                long yourmilliseconds = System.currentTimeMillis();
                Date resultdate = new Date(yourmilliseconds);
                String minpost = sdf.format(resultdate);
                int minpos = Integer.parseInt(minpost);
                int mintime = minpos - mini;
                System.out.println(mintime);
                if((mintime<20&&mintime>0) || (mintime<0&&mintime>-40)){
                    android.support.v7.app.AlertDialog alertbox = new android.support.v7.app.AlertDialog.Builder(context)
                            .setMessage("Sure! You want to delete your order??")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                    databaseEntry.deleteTable();
                                    foodElements.clear();
                                    notifyItemRangeRemoved(0, foodElements.size());
                                    CurrentOrder.checkData(foodElements);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            })
                            .show();
                }else{
                        android.support.v7.app.AlertDialog alertbox = new android.support.v7.app.AlertDialog.Builder(context)
                            .setMessage("Sorry!\nOrder cannot be deleted as you exceeds the order deletion time!")
                            .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            })
                            .show();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return 1;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, address, id, ordertotal, orderdetails, status;
        ImageButton down, delete;
        ListView listView;
        CardView cardview;
        SeekBar seekBar;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.namegettext);
            id = (TextView)itemView.findViewById(R.id.idgettext);
            address = (TextView)itemView.findViewById(R.id.shipaddget);
            ordertotal = (TextView)itemView.findViewById(R.id.pricetotalp);
            down = (ImageButton)itemView.findViewById(R.id.imageButton5);
            listView = (ListView)itemView.findViewById(R.id.listviewcard);
            orderdetails =(TextView)itemView.findViewById(R.id.textView42);
            cardview = (CardView)itemView.findViewById(R.id.cardorder);
            seekBar = (SeekBar)itemView.findViewById(R.id.seekBar);
            status = (TextView)itemView.findViewById(R.id.status);
            delete = (ImageButton)itemView.findViewById(R.id.deleteorder);
        }
    }
}