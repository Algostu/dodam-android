package com.dum.dodam.Scheduler;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;
import com.dum.dodam.Scheduler.dataframe.SchoolTimeTable;
import com.dum.dodam.Scheduler.dataframe.Todo;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class SchedulerPager extends Fragment implements
        TimeTableAdapter.OnListItemSelectedInterface,
        TodoListAdapter.OnListItemSelectedInterface{
    private static final String TAG = "RHC";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public BottomAppBar bottomAppbar;
    private RecyclerView todoRecyclerView;
    private RecyclerView.Adapter todoAdapter;
    private RecyclerView.LayoutManager todoLayoutManager;
    SlidingUpPanelLayout slidingPaneLayout;
    public Button btn;

    public ArrayList<Todo> todoArrayList;

    public ImageView ic_write;
    public ImageView ic_calender;
    public ImageView ic_trashcan;


    private ArrayList<String> list = new ArrayList<>();

    private ArrayList<SchoolTimeTable.TimeTable> result;

    private ArrayList<String> monday = new ArrayList<>();
    private ArrayList<String> tuesday = new ArrayList<>();
    private ArrayList<String> wednesday = new ArrayList<>();
    private ArrayList<String> thursday = new ArrayList<>();
    private ArrayList<String> friday = new ArrayList<>();
    private ArrayList<String> time_table = new ArrayList<>();

    int date;
    int day_of_week;

    public SchedulerPager(int date, int day_of_week) {
        this.date = date;
        this.day_of_week = day_of_week;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduler_pager, container, false);
        view.setClickable(true);

        slidingPaneLayout = view.findViewById(R.id.slidingWindow);
        slidingPaneLayout.setPanelHeight(0);
        btn = view.findViewById(R.id.black_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingPaneLayout.setPanelHeight(0);
            }
        });

        ic_write = view.findViewById(R.id.ic_write);
        ic_calender = view.findViewById(R.id.ic_calender);
        ic_trashcan = view.findViewById(R.id.ic_trashcan);


        ic_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingPaneLayout.setPanelHeight(700);
            }
        });

        ic_trashcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Todo todo : todoArrayList){
                    todo.visible ^= true;
                }
                todoAdapter.notifyDataSetChanged();
            }
        });


        todoArrayList = new ArrayList<>();
        todoArrayList.add(new Todo("잠자기", false));
        todoArrayList.add(new Todo("진짜 잠자기", false));
        todoArrayList.add(new Todo("ㄹㅇ로 자고 싶다", false));
        todoArrayList.add(new Todo("진짜 자고 싶다고", false));
        todoArrayList.add(new Todo("꿀잠 예약", false));

        todoAdapter = new TodoListAdapter(getContext(), todoArrayList, this);
        adapter = new TimeTableAdapter(getContext(), list, this);

        String filename = "TimeTable";
        File file = new File(requireContext().getFilesDir() + "/" + filename);
        if (file.exists()) {
            readTimeTable();
        } else {
            getTimeTable();
        }

        todoRecyclerView = (RecyclerView) view.findViewById(R.id.rv_todo_list);
        todoRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
        todoRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        todoRecyclerView.setLayoutManager(layoutManager);
        todoRecyclerView.setAdapter(todoAdapter);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_time_table);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void getTimeTable() {
        Calendar cal = Calendar.getInstance();

        String AY = String.valueOf(cal.get(Calendar.YEAR));
        String SEM;
        if (cal.get(Calendar.MONTH) < 7) {
            SEM = "1";
        } else {
            SEM = "2";
        }

        String ATPT_OFCDC_SC_CODE = "J10";
        String SD_SCHUL_CODE = "7530612";
        String GRADE = "2";
        String CLASS_NM = "4";

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                //서버 url설정
                .baseUrl("https://open.neis.go.kr/")
                //데이터 파싱 설정
                .addConverterFactory(GsonConverterFactory.create(gson))
                //객체정보 반환
                .build();

        RetrofitService2 service = retrofit.create(RetrofitService2.class);

        Call<SchoolTimeTable> call = service.getTimeTable("4db607519a0e40b2910efcdf0070a215", "json", 1, 50, ATPT_OFCDC_SC_CODE, SD_SCHUL_CODE, AY, SEM, GRADE, CLASS_NM);
        call.enqueue(new Callback<SchoolTimeTable>() {
            @Override
            public void onResponse(Call<SchoolTimeTable> call, Response<SchoolTimeTable> response) {
                if (response.isSuccessful()) {
                    result = response.body().hisTimetable.get(1).row;
                    try {
                        makeTimeTable();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.d(TAG, "onResponse: Fail " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SchoolTimeTable> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public interface RetrofitService2 {
        @GET("/hub/hisTimetable")
        Call<SchoolTimeTable> getTimeTable(@Query("KEY") String KEY,
                                           @Query("Type") String Type,
                                           @Query("pIndex") int pIndex,
                                           @Query("pSize") int pSize,
                                           @Query("ATPT_OFCDC_SC_CODE") String ATPT_OFCDC_SC_CODE,
                                           @Query("SD_SCHUL_CODE") String SD_SCHUL_CODE,
                                           @Query("AY") String AY,
                                           @Query("SEM") String SEM,
                                           @Query("GRADE") String GRADE,
                                           @Query("CLASS_NM") String CLASS_NM);
    }

    public void makeTimeTable() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        Date date;

        boolean monday_flag = true;
        boolean tuesday_flag = true;
        boolean wednesday_flag = true;
        boolean thursday_flag = true;
        boolean friday_flag = true;

        int monday_before_period = 0;
        int tuesday_before_period = 0;
        int wednesday_before_period = 0;
        int thursday_before_period = 0;
        int friday_before_period = 0;

        int DOF;

        for (SchoolTimeTable.TimeTable table : result) {
            date = dateFormat.parse(table.ALL_TI_YMD);
            calendar.setTime(date);
            DOF = calendar.get(Calendar.DAY_OF_WEEK);

            if (DOF == 2 && monday_flag) {
                if (monday_before_period > Integer.parseInt(table.PERIO)) {
                    monday_flag = false;
                    continue;
                }
                monday.add(table.ITRT_CNTNT);
                monday_before_period = Integer.parseInt(table.PERIO);
            } else if (DOF == 3 && tuesday_flag) {
                if (tuesday_before_period > Integer.parseInt(table.PERIO)) {
                    tuesday_flag = false;
                    continue;
                }
                tuesday.add(table.ITRT_CNTNT);
                tuesday_before_period = Integer.parseInt(table.PERIO);
            } else if (DOF == 4 && wednesday_flag) {
                if (wednesday_before_period > Integer.parseInt(table.PERIO)) {
                    wednesday_flag = false;
                    continue;
                }
                wednesday.add(table.ITRT_CNTNT);
                wednesday_before_period = Integer.parseInt(table.PERIO);
            } else if (DOF == 5 && thursday_flag) {
                if (thursday_before_period > Integer.parseInt(table.PERIO)) {
                    thursday_flag = false;
                    continue;
                }
                thursday.add(table.ITRT_CNTNT);
                thursday_before_period = Integer.parseInt(table.PERIO);
            } else if (DOF == 6 && friday_flag) {
                if (friday_before_period > Integer.parseInt(table.PERIO)) {
                    friday_flag = false;
                    continue;
                }
                friday.add(table.ITRT_CNTNT);
                friday_before_period = Integer.parseInt(table.PERIO);
            }
        }
        saveTimeTable();
    }

    public void saveTimeTable() {
        String listString;

        if (getContext() == null) return;
        File file = new File(getContext().getFilesDir(), "TimeTable");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("  휴일  " + "\n");
            bufferedWriter.write("  휴일  " + "\n");
            listString = TextUtils.join(", ", monday);
            bufferedWriter.write(listString + "\n");
            listString = TextUtils.join(", ", tuesday);
            bufferedWriter.write(listString + "\n");
            listString = TextUtils.join(", ", wednesday);
            bufferedWriter.write(listString + "\n");
            listString = TextUtils.join(", ", thursday);
            bufferedWriter.write(listString + "\n");
            listString = TextUtils.join(", ", friday);
            bufferedWriter.write(listString + "\n");

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        readTimeTable();
    }

    public void readTimeTable() {
        String filename = "TimeTable";
        File file = new File(requireContext().getFilesDir() + "/" + filename);
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    time_table.add(line);
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        list.clear();
        List<String> obj = Arrays.asList(time_table.get(day_of_week).split(","));
        list.addAll(obj);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(View v, int position) {

    }
}
