package com.dum.dodam.Univ.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.Univ.Adapter.UnivSearchAdapter;
import com.dum.dodam.Univ.UnivWebView;
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

    // search mode
    private ImageView major;
    public boolean majorMode;
    private ArrayList<UnivFrame> list; // univ
    private ArrayList<UnivFrame> list2; // major

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
        et_input = view.findViewById(R.id.edit_search);
        et_input.setFocusable(true);

        // major search 변경
        majorMode = false;
        major = view.findViewById(R.id.iv_major);
        major.setClickable(true);
        major.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(majorMode){
                    et_input.setTypeface(null, Typeface.NORMAL);
                    major.setImageResource(R.drawable.ic_mortarboard);
                    et_input.setBackgroundColor(Color.WHITE);
                    majorMode = false;
                } else {
                    major.setImageResource(R.drawable.ic_mortarboard_selected);
                    et_input.setTypeface(null, Typeface.BOLD);
                    et_input.setBackground(getResources().getDrawable(R.drawable.edge3));
                    majorMode = true;
                }
                et_input.setText("");
                list2.clear();
                list.clear();
                adapter.notifyDataSetChanged();
            }
        });


        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        final int width = dm.widthPixels; //디바이스 화면 너비
        final int height = dm.heightPixels; //디바이스 화면 높이

        // listener 설정
        list = new ArrayList<UnivFrame>();
        list2 = new ArrayList<UnivFrame>();
        adapter = new UnivSearchAdapter(getContext(), list, et_input);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final UnivFrame collage = list.get(i);
                addHistory(collage);
                InputMethodManager imm = (InputMethodManager) ((MainActivity) getActivity()).getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_input.getWindowToken(), 0);

                UnivSearchDialog dialog = new UnivSearchDialog(getContext(), collage, new UnivSearchDialog.myOnClickListener() {
                    @Override
                    public void onYoutubeClick() {
                        Log.d("RHC", "col" + collage.youtube);
                        if (collage.youtube == null) {
                            Toast.makeText(getContext(), "관련 정보가 없어 검색페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                            String youtubeUrl = "https://www.youtube.com/results?search_query=" + collage.univName;
                            ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(youtubeUrl));
                        } else {
                            ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(collage.youtube));
                        }
                    }

                    @Override
                    public void onEduPageClick() {
                        Log.d("RHC", "col" + collage.admission);
                        if (collage.admission == null) {
                            Toast.makeText(getContext(), "입학처 정보가 없어 홈페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                            ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(collage.homePage));
                        } else {
                            ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(collage.eduHomePage));
                        }
                    }
                });
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);

                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
                wm.copyFrom(dialog.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
                wm.width = (int) (width * 0.9);  //화면 너비의 절반
                wm.height = height / 2;  //화면 높이의 절반

                dialog.show();

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
                    if (majorMode){
                        search2(text);
                    } else {
                        search(text);
                    }
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
                    if (response.body().checkError(getContext()) != 0) {
                        return;
                    }
                    Log.d(TAG, "response" + response.raw());
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

    private void search2(String query) {
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
                    if(response.body().checkError(getContext())!=0){
                        return;
                    }
                    Log.d(TAG, "response" + response.raw());
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

    public void addHistory(UnivFrame univ) {
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
