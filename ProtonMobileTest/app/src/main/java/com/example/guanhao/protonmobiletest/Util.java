package com.example.guanhao.protonmobiletest;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by guanhao on 2018/2/7.
 */

public class Util {


    static final List<Task> UNFINISHED_TASKS = Collections.synchronizedList(new ArrayList());
    static final Set<String> UNFINISHED_SET = new HashSet<>();

    static final List<Task> FINISHED_TASKS = new ArrayList<>();
    static final Set<String> FINISHED_SET = new HashSet<>();





}
