package com.example.heavon.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.heavon.dao.ShowDao;
import com.example.heavon.interfaceClasses.HttpResponse;
import com.example.heavon.myapplication.LoginActivity;
import com.example.heavon.myapplication.R;
import com.example.heavon.views.TypeShowContentView;
import com.example.heavon.vo.Show;
import com.example.heavon.vo.ShowFilter;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TypeShowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TypeShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TypeShowFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "type";
    private static final String ARG_PAGE = "page";

    // TODO: Rename and change types of parameters
    private String mType = "电视剧";
    private int mPage = 1;

    private RequestQueue mQueue;

    private OnFragmentInteractionListener mListener;
    private TextView mTypeTitle;
    private Button mTypeMoreButton;
//    private HorizontalScrollView mShowListScrollView;
    private LinearLayout mShowListView;
    private TextView mShowNone;


    public TypeShowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type the type of the h.
     * @param page Parameter 2.
     * @return A new instance of fragment TypeShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TypeShowFragment newInstance(String type, int page) {
        TypeShowFragment fragment = new TypeShowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_type_show, container, false);
        Log.e("showfragment", getContext().toString());
        mQueue = Volley.newRequestQueue(getContext());

        mTypeTitle = (TextView) view.findViewById(R.id.type_title);
        if(mType != null && !mType.isEmpty()){
            mTypeTitle.setText(mType);
        }

        mTypeMoreButton = (Button) view.findViewById(R.id.bt_type_more);
        mTypeMoreButton.setTag(mType);
        mTypeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**----------wait to modify-----------**/
                Toast.makeText(getContext(), "type more"+view.getTag(), Toast.LENGTH_SHORT).show();
                /**----------wait to modify-----------**/
            }
        });

//        mShowListScrollView = (HorizontalScrollView) view.findViewById(R.id.show_list_scroll);
        mShowListView = (LinearLayout) view.findViewById(R.id.show_list);

        mShowNone = (TextView) view.findViewById(R.id.show_none);
        initShowList();

        return view;
    }

    //初始化节目列表
    public void initShowList(){
        ShowDao dao = new ShowDao();
        ShowFilter filter = new ShowFilter("localization", mType);
        Log.e("localization", mType);
        filter.addFilter("perpage", "4");
        filter.addFilter("page", String.valueOf(mPage));
        dao.initShowsByFilter(filter, mQueue, new HttpResponse<Map<String, Object>>() {
            @Override
            public void getHttpResponse(Map<String, Object> result) {
                if((Boolean)result.get("error")){
                    //error
                    Toast.makeText(getContext(), (String)result.get("msg"), Toast.LENGTH_SHORT).show();
                }else{
                    List<Show> showList = (List<Show>) result.get("showList");
                    if(showList == null || showList.isEmpty()){
                        Log.e("typeshowfragment", "showlist is null" );
                        return;
                    }
                    mShowListView.removeView(mShowNone);
                    for(Show show : showList){
                        TypeShowContentView showView = new TypeShowContentView(getContext());
                        showView.initShow(show);
                        mShowListView.addView(showView);
                    }
                }
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
