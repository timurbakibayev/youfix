package com.gii.youfix;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gii.youfix.order.FirebaseContent;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private FirebaseContent.OrderItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = FirebaseContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
        }

        for (String photoID : mItem.photos)
            CameraAndPictures.getPicFromFirebase(photoID,rootView);

        //mItem
        ((TextView)rootView.findViewById(R.id.form_name1)).setText(mItem.ownerName);
        ((TextView)rootView.findViewById(R.id.form_auto1)).setText(mItem.autoBrand);
        ((TextView)rootView.findViewById(R.id.form_description1)).setText(mItem.details);
        ((TextView)rootView.findViewById(R.id.form_phone1)).setText(mItem.phone);
        ((TextView)rootView.findViewById(R.id.form_year1)).setText(mItem.year);
        ((TextView)rootView.findViewById(R.id.form_city1)).setText(mItem.city);

        ((Button)rootView.findViewById(R.id.button_make_offer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAnOffer(rootView.getContext(), (LinearLayout)rootView.findViewById(R.id.priceLL));
            }
        });
        return rootView;
    }

    private void makeAnOffer(final Context context, final LinearLayout priceLL) {

        final String phone = YouFix.sharedPref.getString("phone","");
        final String name = YouFix.sharedPref.getString("name","");

        if (phone.equals("") || name.equals("")) {
            (new AlertDialog.Builder(context))
            .setMessage("Вам необходимо заполнить профиль в главном меню")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    }).show();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final ScrollView filterScrollView = new ScrollView(context);

        final EditText priceEditText = new EditText(context);
        priceEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

        filterScrollView.addView(priceEditText);

        builder
                .setMessage("Предложите свою сумму за ремонт в тенге")
                .setView(filterScrollView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        LinearLayout offerLL = new LinearLayout(context);
                        offerLL.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        offerLL.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        offerLL.setBackgroundResource(R.drawable.dark_selector);
                        offerLL.setOrientation(LinearLayout.VERTICAL);
                        TextView anotherOffer = new TextView(context);
                        anotherOffer.setText(name + ":" + priceEditText.getText() + " тенге");
                        Button call = new Button(context);
                        call.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        call.setText(name + ":" + priceEditText.getText() + " тенге \n" + "Набрать номер:" + phone);
                        call.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String uri = "tel:" + phone.trim() ;
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse(uri));
                                startActivity(intent);
                            }
                        });
                        //offerLL.addView(anotherOffer);
                        offerLL.addView(call);
                        priceLL.addView(offerLL);
                    }
                }).show();
    }


}
