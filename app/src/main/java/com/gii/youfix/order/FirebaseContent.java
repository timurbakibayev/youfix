package com.gii.youfix.order;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.gii.youfix.YouFix;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class FirebaseContent {

    public static final List<OrderItem> ITEMS = new ArrayList<OrderItem>();

    public static final Map<String, OrderItem> ITEM_MAP = new HashMap<String, OrderItem>();

    private static final int COUNT = 25;

    private static final String TAG = "FirebaseContent.java";

    public static RecyclerView listView = null;

    static {
        // Add some sample items.
        /*addItem(new OrderItem("Ford","Ford Mustang, 2016, замена ветрового стекла",
                "Необходимо заменить ветровое стекло на моем любимом Ford Mustang 2016 года выпуска."));
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }*/

        YouFix.ref.child("youfix/orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    OrderItem fbItem = postSnapshot.getValue(OrderItem.class);
                    if (ITEM_MAP.get(fbItem.id) == null) {
                        addItem(fbItem);
                        Log.e(TAG, "onDataChange: add new item: " + fbItem.content);
                    }
                }
                Collections.sort(ITEMS, new Comparator<OrderItem>() {
                    @Override
                    public int compare(OrderItem lhs, OrderItem rhs) {
                        if (rhs.date == null && lhs.date == null)
                            return 0;
                        if (rhs.date == null)
                            return 1;
                        if (lhs.date == null)
                            return -1;
                        if (lhs.date.after(rhs.date))
                            return 1;
                        return -1;
                    }
                });
                if (listView != null) {
                    listView.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Firebase", "The read failed 4: " + firebaseError.getMessage());
            }

        });


    }

    private static void addItem(OrderItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static OrderItem createDummyItem(int position) {
        return new OrderItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class OrderItem {
        public String id;
        public String content = "";
        public String details = "";
        public Date date;

        public String ownerId = "";
        public String ownerName = "";

        public  String city = "";
        public  String autoBrand = "";
        public  String year = "";
        public  String phone = "";
        public ArrayList<String> photos = new ArrayList<>();

        public String dateInText() {
            Calendar c = Calendar.getInstance();
            if (date != null)
                c.setTime(date);
            return "" + (c.get(Calendar.DAY_OF_MONTH)) + "." + (c.get(Calendar.MONTH) + 1) + "." + (c.get(Calendar.YEAR));
        }

        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public String getDetails() {
            return details;
        }

        public Date getDate() {
            return date;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public String getCity() {
            return city;
        }

        public String getAutoBrand() {
            return autoBrand;
        }

        public String getYear() {
            return year;
        }

        public String getPhone() {
            return phone;
        }

        public ArrayList<String> getPhotos() {
            return photos;
        }

        public OrderItem() {
            this.id = YouFix.generateNewId();
        }

        public OrderItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
            Calendar c = Calendar.getInstance();
            this.date = c.getTime();

        }

        @Override
        public String toString() {
            return content;
        }
    }
}
