package com.lintend.forum.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lintend.forum.DataModule;
import com.lintend.forum.R;
import com.lintend.forum.SessionManager;
import com.lintend.forum.adapter.MyPostAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyPostActivity extends Fragment {
    RecyclerView recyclerView;
    ListView listView;
    List<DataModule> mydata = new ArrayList<>();


    SessionManager sm;
    RequestQueue requestQueue;
    String url = "http://popularkoju.com.np/id1277129_lintendforum/mypost_display.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mypost_tab_layout,null);
          return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= view.findViewById(R.id.post_tab_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // session manager
        SessionManager sm = new SessionManager(getActivity());
        HashMap<String, String> mp = sm.getUserDetails();
        final String email = mp.get(SessionManager.KEY_EMAIL);


        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array1 = new JSONArray(response)   ;

                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject obj1 = array1.getJSONObject(i);

                            DataModule m = new DataModule();
                            m.setName(obj1.getString("name"));
                            m.setQuestion(obj1.getString("question"));
                            m.setTime(obj1.getString("date_time"));
                            mydata.add(m);
                    }

            //        Toast.makeText(getActivity(), "Data Refreshed", Toast.LENGTH_LONG).show();
                    recyclerView.setAdapter(new MyPostAdapter(getActivity(), mydata));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", email);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}








