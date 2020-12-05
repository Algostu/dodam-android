package com.dum.dodam.Univ.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.Univ.Adapter.UnivSearchAdapter;
import com.dum.dodam.Univ.Univ;
import com.dum.dodam.Univ.dataframe.UnivFrame;
import com.dum.dodam.Univ.dataframe.UnivResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class UnivSearch extends Fragment {
    // log tag
    public final String TAG = "UnivSearch";

    // toolbar
    private Toolbar toolbar;
    private ActionBar actionbar;

    private ArrayList<UnivFrame> list;

    private EditText et_input;
    private ListView listView;
    private UnivSearchAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.univ_search, container, false);

        // appbar 적용
        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionbar.setDisplayHomeAsUpEnabled(true);

        // layout 연결
        listView = view.findViewById(R.id.lv_search);
        et_input = view.findViewById(R.id.et_search);

        // listener 설정
        list = new ArrayList<UnivFrame>();
        adapter = new UnivSearchAdapter(getContext(), list, et_input);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UnivFrame collage = list.get(i);
                addHistory(collage);
                InputMethodManager imm = (InputMethodManager) ((MainActivity) getActivity()).getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);
                ((MainActivity) getActivity()).replaceFragmentFull(new Univ(collage));
                et_input.setText("");
                et_input.clearFocus();
            }
        });
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = et_input.getText().toString();
                if(charSequence.length() > 0){
                    search(text);
                } else {
                    showHistory();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        showHistory();
        return view;
    }

    private void search(String query) {
        list.clear();
        if (query.equals("")) {
            adapter.notifyDataSetChanged();
            return;
        }

        RetrofitAdapter rAdapter = new RetrofitAdapter();
        RetrofitService service = rAdapter.getInstance(getActivity());
        Call<UnivResponse> call = service.searchCollageName(query);

        call.enqueue(new retrofit2.Callback<UnivResponse>() {
            @Override
            public void onResponse(Call<UnivResponse> call, retrofit2.Response<UnivResponse> response) {
                if (response.isSuccessful()) {
                    UnivResponse result = response.body();
                    list.clear();
                    list.addAll(result.body);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<UnivResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void showHistory(){
        // 저장되어있는 알람 중 오래된것들 삭제
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        ArrayList<UnivFrame> univFrameArrayList;
        String json = sharedPref.getString("univSearchHistory", "");
        if (json.equals("") == true) {
            univFrameArrayList = new ArrayList<UnivFrame>();
            Log.d("alarm debug", "no data stored");
        } else {
            Log.d("alarm debug", "data are stored");
            Type listType = new TypeToken<ArrayList<UnivFrame>>() {
            }.getType();
            univFrameArrayList = gson.fromJson(json, listType);
        }
        list.clear();
        list.addAll(univFrameArrayList);
        adapter.notifyDataSetChanged();
    }

    public void addHistory(UnivFrame univ){
        // 저장되어있는 알람 중 오래된것들 삭제
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        ArrayList<UnivFrame> univFrameArrayList;
        String json = sharedPref.getString("univSearchHistory", "");
        if (json.equals("") == true) {
            univFrameArrayList = new ArrayList<UnivFrame>();
            Log.d("alarm debug", "no data stored");
        } else {
            Log.d("alarm debug", "data are stored");
            Type listType = new TypeToken<ArrayList<UnivFrame>>() {
            }.getType();
            univFrameArrayList = gson.fromJson(json, listType);
        }
        if (univFrameArrayList.contains(univ))
            return;
        univFrameArrayList.add(univ);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("univSearchHistory", gson.toJson(univFrameArrayList));
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //select back button
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}